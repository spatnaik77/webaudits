package com.acc.webaudits;

import com.acc.webaudits.model.*;
import com.acc.webaudits.repository.*;
import com.acc.webaudits.scan.ScannerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    ExecutorService executor;

    private final String URL_XPATH = "//a[contains(@class,'ds2-icon--arrow-big-r-grey-2')]";

    public ApiManager()
    {
        executor = Executors.newFixedThreadPool(10);
    }
    @Transactional
    public void cleanDB()
    {
        scannerDetailRepository.deleteAll();
        scannerRepository.deleteAll();
        crawlerDetailRepository.deleteAll();
        crawlerRepository.deleteAll();
    }
    @Transactional
    public void createCrawler(Crawler crawler)
    {
        crawler.setStatus("in-progress");
        crawlerRepository.save(crawler);

        String url = crawler.getUrl();
        //Crawl and get the list of urls
        WebDriver webDriver = WebDriverManager.createWebDriver();
        webDriver.get(url);
        List<WebElement> webElements = webDriver.findElements(By.xpath(URL_XPATH));
        System.out.println("total links" + webElements.size());
        for(WebElement webElement : webElements)
        {
            CrawlerDetail crawlerDetail = new CrawlerDetail();
            crawlerDetail.setCrawlerName(crawler.getName());
            crawlerDetail.setCrawled_url(webElement.getAttribute("href"));
            crawlerDetailRepository.save(crawlerDetail);
        }
        //set the crawler status to complete
        Crawler c = crawlerRepository.findById(crawler.getName()).get();
        c.setCrawledURLCount(webElements.size());
        c.setStatus("complete");
        crawlerRepository.save(c);

        //close the web driver
        webDriver.close();
        webDriver.quit();
    }
    @Transactional
    public List<Crawler> getAllCrawlers()
    {
        return crawlerRepository.findAll();
    }

    @Transactional
    public CrawlerInfo getCrawlerInfo(String crawlerName)
    {
        Crawler crawler = crawlerRepository.getOne(crawlerName);
        List<CrawlerDetail> crawlerDetailList = crawlerDetailRepository.findByCrawlerName(crawlerName);

        CrawlerInfo crawlerInfo = new CrawlerInfo();
        crawlerInfo.setName(crawler.getName());
        crawlerInfo.setStatus(crawler.getStatus());
        crawlerInfo.setUrl(crawler.getUrl());
        crawlerInfo.setCrawledURLCount(crawler.getCrawledURLCount());
        for(CrawlerDetail crawlerDetail : crawlerDetailList)
        {
            crawlerInfo.addCrawleredURL(crawlerDetail.getCrawled_url());
        }
        return crawlerInfo;

    }
    //TODO Make it Async
    @Transactional
    public void createScanner(Scanner scanner) throws Exception
    {
        long startTime = System.currentTimeMillis();
        scanner.setStatus("in-progress");
        scannerRepository.save(scanner);

        String crawlerName = scanner.getCrawlerName();
        List<String> urlList = null;
        if(crawlerName != null)
        {
            List<CrawlerDetail> crawlerDetailList = crawlerDetailRepository.findByCrawlerName(crawlerName);
            urlList = new ArrayList<>();
            for(CrawlerDetail crawlerDetail : crawlerDetailList)
            {
                urlList.add(crawlerDetail.getCrawled_url());
            }
        }
        else
        {
            String urlListAsString = scanner.getUrlList();
            //Parse and get the url list. Not implemented

        }
        List<Future> results = new ArrayList<>();
        int batchSize = 100;
        for (int start = 0; start < urlList.size(); start += batchSize)
        {
            int end = Math.min(start + batchSize, urlList.size());
            List<String> sublist = urlList.subList(start, end);
            ScannerJob scannerJob = new ScannerJob(scanner.getName(), sublist, scannerDetailRepository);
            Future<Boolean> result = executor.submit(scannerJob);
            results.add(result);
            System.out.println("Scanner created...");
        }
        for(Future f : results)
        {
            f.get();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("done... Time taken: " + (endTime-startTime)/60000 + " Ms");

        //set the scanner status to complete
        Scanner s = scannerRepository.findById(scanner.getName()).get();
        s.setStatus("complete");
        scannerRepository.save(s);
    }
}
