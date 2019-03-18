package com.acc.webaudits.repository;

import com.acc.webaudits.model.Crawler;
import com.acc.webaudits.model.CrawlerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlerDetailRepository extends JpaRepository<CrawlerDetail, Long>
{
    List<CrawlerDetail> findByCrawlerName(String crawlerName );

}
