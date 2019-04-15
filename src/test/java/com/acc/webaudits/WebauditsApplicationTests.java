package com.acc.webaudits;

import com.acc.webaudits.model.Crawler;
import com.acc.webaudits.model.CrawlerInfo;
import com.acc.webaudits.model.Note;
import com.acc.webaudits.model.Scanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebauditsApplicationTests {

	@Autowired
	ApiManager apiManager;

	String crawl_url = "https://www.bmw.de/de/footer/sitemap.html";

	String crawlerBaseName = "crawler-";
	String scannerBaseName = "scan-";

	@Before
	public void setup() {

		System.out.println("setup");
		apiManager.cleanDB();
	}

	@Test
	public void Test1() throws Exception
	{
		//Create the crawler
		Crawler  c = new Crawler();
		String crawlerName = crawlerBaseName + System.nanoTime();
		c.setName(crawlerName);
		c.setUrl(crawl_url);
		apiManager.createCrawler(c);

		List<Crawler> crawlers = apiManager.getAllCrawlers();

        CrawlerInfo crawlerinfo =  apiManager.getCrawlerInfo(crawlerName);

		Scanner s = new Scanner();
		String scannerName = scannerBaseName + System.nanoTime();
		s.setName(scannerName);
		s.setDtmUrl("some-dtm-url");
		String urls = "https://www.bmw.de/de/footer/footer-section/cookie-policy.html;https://www.bmw.de/de/footer/footer-section/cookie-policy.html;https://www.bmw.de/de/footer/footer-section/cookie-policy.html;https://www.bmw.de/de/footer/footer-section/cookie-policy.html;https://www.bmw.de/de/footer/footer-section/cookie-policy.html";
		s.setUrlList(urls);
		apiManager.createScanner(s);

		System.out.println("Done...");
	}
	@Test
	public void Test2() throws Exception
	{
		//Create the crawler
		Crawler  c = new Crawler();
		String crawlerName = crawlerBaseName + System.nanoTime();
		c.setName(crawlerName);
		c.setUrl(crawl_url);
		apiManager.createCrawler(c);

		List<Crawler> crawlers = apiManager.getAllCrawlers();

		CrawlerInfo crawlerinfo =  apiManager.getCrawlerInfo(crawlerName);

		Scanner s = new Scanner();
		String scannerName = scannerBaseName + System.nanoTime();
		s.setName(scannerName);
		s.setDtmUrl("some-dtm-url");
		s.setCrawlerName(crawlerName);
		apiManager.createScanner(s);

		System.out.println("Done...");
	}



}
