package com.acc.webaudits.repository;

import com.acc.webaudits.model.Crawler;
import com.acc.webaudits.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends JpaRepository<Crawler, String>
{


}
