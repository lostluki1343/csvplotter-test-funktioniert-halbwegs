package at.ac.htlstp.et.k5b.aiit_csv_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data      // generiert u.a. getId(), setId(), getFilename(), …
@Entity
public class Plot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private LocalDateTime uploadedAt;

    @OneToMany(mappedBy = "plot", cascade = CascadeType.ALL)
    private List<DataPoint> points;

    public Long getId() {
        return id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }


    public void setPoints(List<DataPoint> points) {
        this.points = points;
    }

    public String getFilename() {
        return filename;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public List<DataPoint> getPoints() {
        return points;
    }

}

