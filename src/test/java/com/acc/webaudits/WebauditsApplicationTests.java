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

	String crawlerName = "bmw-india";

	@Before
	public void setup() {

		System.out.println("setup");
		apiManager.cleanDB();
	}

	@Test
	public void createCrawlerTest() throws Exception
	{
		//Create the crawler
		Crawler  c = new Crawler();
		c.setName(crawlerName);
		c.setUrl("https://www.bmw.de/de/footer/sitemap.html");
		apiManager.createCrawler(c);

		List<Crawler> crawlers = apiManager.getAllCrawlers();

        CrawlerInfo crawlerinfo =  apiManager.getCrawlerInfo(crawlerName);

		Scanner s = new Scanner();
		s.setName("scan-1");
		s.setDtmUrl("some-dtm-url");
		s.setCrawlerName(crawlerName);
		apiManager.createScanner(s);
	}



}
