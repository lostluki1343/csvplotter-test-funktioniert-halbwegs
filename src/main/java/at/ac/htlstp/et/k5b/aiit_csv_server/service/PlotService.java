package at.ac.htlstp.et.k5b.aiit_csv_server.service;


import at.ac.htlstp.et.k5b.aiit_csv_server.model.DataPoint;
import at.ac.htlstp.et.k5b.aiit_csv_server.model.Plot;
import at.ac.htlstp.et.k5b.aiit_csv_server.repo.PlotRepo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlotService {
    private final PlotRepo plotRepo;
    @Value("${plot.storage.path}")
    private String storagePath;

    public PlotService(PlotRepo plotRepo) {
        this.plotRepo = plotRepo;
    }

    @Transactional
    public Plot handleCsv(MultipartFile file) throws IOException {
        // 1. Plot-Entity anlegen
        Plot plot = new Plot();
        plot.setFilename(file.getOriginalFilename());
        plot.setUploadedAt(LocalDateTime.now());
        plot = plotRepo.save(plot);

        // 2. CSV parsen
        Reader in = new InputStreamReader(file.getInputStream());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(in);
        List<DataPoint> points = new ArrayList<>();
        XYSeries series = new XYSeries(plot.getFilename());
        for (CSVRecord rec : records) {
            double x = Double.parseDouble(rec.get(0));
            double y = Double.parseDouble(rec.get(1));
            DataPoint dp = new DataPoint();
            dp.setX(x); dp.setY(y);
            dp.setPlot(plot);
            points.add(dp);
            series.add(x, y);
        }
        plot.setPoints(points);
        plotRepo.save(plot);

        // 3. Chart erstellen
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Plot: " + plot.getFilename(),
                "X", "Y",
                dataset
        );
        Files.createDirectories(Paths.get(storagePath));
        File imgFile = Paths.get(storagePath, plot.getId() + ".png").toFile();
        try (OutputStream os = new FileOutputStream(imgFile)) {
            ChartUtils.writeChartAsPNG(os, chart, 800, 600);
        }

        return plot;
    }

    public List<Plot> listAll() {
        return plotRepo.findAll();
    }
}

