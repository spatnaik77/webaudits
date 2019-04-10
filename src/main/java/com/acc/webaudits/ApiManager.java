package com.acc.webaudits;

import com.acc.webaudits.model.*;
import com.acc.webaudits.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;

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

    @Autowired
    WebDriverManager webDriverManager;

    private final String URL_XPATH = "//a[contains(@class,'ds2-icon--arrow-big-r-grey-2')]";

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
        WebDriver webDriver = webDriverManager.getWebDriver();
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
    }
    public List<Crawler> getAllCrawlers()
    {
        return crawlerRepository.findAll();
    }
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
