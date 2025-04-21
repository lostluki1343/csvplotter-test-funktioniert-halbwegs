package at.ac.htlstp.et.k5b.aiit_csv_server.model;

import jakarta.persistence.*;



@Entity
public class DataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double x;
    private double y;

    @ManyToOne
    @JoinColumn(name = "plot_id")
    private Plot plot;

    // Standard‑Konstruktor
    public DataPoint() {}

    // Getter und Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public Plot getPlot() {
        return plot;
    }
    public void setPlot(Plot plot) {
        this.plot = plot;
    }
}
