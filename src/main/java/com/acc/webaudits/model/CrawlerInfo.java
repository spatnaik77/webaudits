package com.acc.webaudits.model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class CrawlerInfo {

    private String name;
    private String url;
    private String status;
    private int crawledURLCount;
    private List<String> crawledURLList;

    public CrawlerInfo()
    {
        crawledURLList= new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCrawledURLCount() {
        return crawledURLCount;
    }

    public void setCrawledURLCount(int crawledURLCount) {
        this.crawledURLCount = crawledURLCount;
    }

    public List<String> getCrawledURLList() {
        return crawledURLList;
    }

    public void addCrawleredURL(String url)
    {
        crawledURLList.add(url);
    }

}
