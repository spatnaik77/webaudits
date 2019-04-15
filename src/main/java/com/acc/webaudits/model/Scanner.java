package com.acc.webaudits.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "scanner")
public class Scanner {

    @Id
    private String name;

    //The input to scanner is either a crawler name or a list of URLs separated by semicolon
    // When the input is the crawler name, it gets the url list from the CrawlerDetail for the given crawler

    private String crawlerName;

    @Column(columnDefinition = "TEXT")
    private String urlList;

    private String dtmUrl;

    @NotBlank
    private String status;

    private int totalURLCount;

    private int successCount;

    private int failureCount;

    private long timeTaken;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }

    public String getUrlList() {
        return urlList;
    }

    public void setUrlList(String urlList) {
        this.urlList = urlList;
    }

    public String getDtmUrl() {
        return dtmUrl;
    }

    public void setDtmUrl(String dtmUrl) {
        this.dtmUrl = dtmUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalURLCount() {
        return totalURLCount;
    }

    public void setTotalURLCount(int totalURLCount) {
        this.totalURLCount = totalURLCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }
}
