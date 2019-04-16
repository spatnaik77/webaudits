package com.acc.webaudits.scan;

import com.acc.webaudits.model.Crawler;
import com.acc.webaudits.model.CrawlerDetail;
import com.acc.webaudits.repository.CrawlerDetailRepository;
import com.acc.webaudits.repository.CrawlerRepository;
import com.acc.webaudits.repository.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.Callable;

public class CrawlerJob implements Runnable {

    private Crawler crawler;
    private String URL_XPATH;
    private CrawlerRepository crawlerRepository;
    private CrawlerDetailRepository crawlerDetailRepository;

    public CrawlerJob(Crawler crawler, String URL_XPATH, CrawlerRepository crawlerRepository,
                        CrawlerDetailRepository crawlerDetailRepository)
    {
        this.crawler = crawler;
        this.URL_XPATH = URL_XPATH;
        this.crawlerRepository = crawlerRepository;
        this.crawlerDetailRepository = crawlerDetailRepository;
    }

    @Override
    public void run()
    {
        //Crawl and get the list of urls
        WebDriver webDriver = WebDriverManager.createWebDriver();
        webDriver.get(crawler.getUrl());
        List<WebElement> webElements = webDriver.findElements(By.xpath(URL_XPATH));
        System.out.println("total links found : " + webElements.size());
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
        System.out.println(crawler.getName() + " updated with status complete");

        //close the web driver
        webDriver.close();
        webDriver.quit();
    }
}
