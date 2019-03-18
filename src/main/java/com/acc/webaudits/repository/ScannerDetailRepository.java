package com.acc.webaudits.repository;

import com.acc.webaudits.model.CrawlerDetail;
import com.acc.webaudits.model.Scanner;
import com.acc.webaudits.model.ScannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerDetailRepository extends JpaRepository<ScannerDetail, Long>
{
    ScannerDetail findByScannerName(String scannerName );

}
