package at.ac.htlstp.et.k5b.aiit_csv_server.repo;


import at.ac.htlstp.et.k5b.aiit_csv_server.model.DataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataPointRepo extends JpaRepository<DataPoint, Long> { }
