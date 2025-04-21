package at.ac.htlstp.et.k5b.aiit_csv_server.controller;



import at.ac.htlstp.et.k5b.aiit_csv_server.model.Plot;
import at.ac.htlstp.et.k5b.aiit_csv_server.service.PlotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class PlotController {

    private final PlotService plotService;
    @Value("${plot.storage.path}")
    private String storagePath;

    public PlotController(PlotService plotService) {
        this.plotService = plotService;
    }

    @GetMapping("/")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file) throws Exception {
        plotService.handleCsv(file);
        return "redirect:/plots";
    }

    @GetMapping("/plots")
    public String listPlots(Model model) {
        List<Plot> all = plotService.listAll();
        model.addAttribute("plots", all);
        return "list";
    }

    @GetMapping("/plots/{id}/image")
    @ResponseBody
    public Resource serveImage(@PathVariable Long id) throws MalformedURLException {
        Path path = Paths.get(storagePath, id + ".png");
        return new UrlResource(path.toUri());
    }

    @GetMapping("/plots/{id}")
    public String viewPlot(@PathVariable Long id, Model model) {
        Plot plot = plotService.listAll()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow();
        model.addAttribute("plot", plot);
        return "view";
    }
}

