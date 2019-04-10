package com.acc.webaudits.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "crawler_detail")
public class CrawlerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String crawlerName;

    private String crawled_url;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }

    public String getCrawled_url() {
        return crawled_url;
    }

    public void setCrawled_url(String crawled_url) {
        this.crawled_url = crawled_url;
    }
}
