package com.acc.webaudits;

import com.acc.webaudits.model.Crawler;
import com.acc.webaudits.model.CrawlerDetail;
import com.acc.webaudits.model.Scanner;
import com.acc.webaudits.model.ScannerDetail;
import com.acc.webaudits.repository.CrawlerDetailRepository;
import com.acc.webaudits.repository.CrawlerRepository;
import com.acc.webaudits.repository.ScannerDetailRepository;
import com.acc.webaudits.repository.ScannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ApiManager {

    @Autowired
    CrawlerRepository crawlerRepository;

    @Autowired
    CrawlerDetailRepository crawlerDetailRepository;

    @Autowired
    ScannerRepository scannerRepository;

    @Autowired
    ScannerDetailRepository scannerDetailRepository;

    public void cleanDB()
    {
        scannerDetailRepository.deleteAll();

        scannerRepository.deleteAll();

        crawlerDetailRepository.deleteAll();

        crawlerRepository.deleteAll();
    }

    public void createCrawler(Crawler crawler)
    {
        crawler.setStatus("in-progress");
        crawlerRepository.save(crawler);

        String url = crawler.getUrl();
        //Crawl and get the list of urls

        List<String> urls = Arrays.asList("yahoo.com/a", "yahoo.com/b", "yahoo.com/c");
        for(String u : urls)
        {
            CrawlerDetail crawlerDetail = new CrawlerDetail();
            crawlerDetail.setCrawlerName(crawler.getName());
            crawlerDetail.setCrawled_url(u);
            crawlerDetailRepository.save(crawlerDetail);
        }
        //set the crawler status to complete
        Crawler c = crawlerRepository.findById(crawler.getName()).get();
        c.setStatus("complete");
        crawlerRepository.save(c);

    }

    public void createScanner(Scanner scanner)
    {
        scanner.setStatus("in-progress");
        scannerRepository.save(scanner);

        String crawlerName = scanner.getCrawlerName();
        List<String> urls = null;
        if(crawlerName != null)
        {
            List<CrawlerDetail> crawlerDetailList = crawlerDetailRepository.findByCrawlerName(crawlerName);
            urls = new ArrayList<>();
            for(CrawlerDetail crawlerDetail : crawlerDetailList)
            {
                urls.add(crawlerDetail.getCrawled_url());
            }
        }
        else
        {
            String urlListAsString = scanner.getUrlList();
            //Parse and get the url list. Not implemented

        }

        for(String url : urls)
        {
            ScannerDetail sd = new ScannerDetail();
            sd.setScannerName(scanner.getName());
            sd.setUrl(url);
            sd.setResult("success");
            scannerDetailRepository.save(sd);
        }
        //set the scanner status to complete
        Scanner s = scannerRepository.findById(scanner.getName()).get();
        s.setStatus("complete");
        scannerRepository.save(s);
    }



}
