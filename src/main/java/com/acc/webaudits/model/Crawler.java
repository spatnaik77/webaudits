package com.acc.webaudits.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "crawler")
public class Crawler {

    @Id
    private String name;

    @NotBlank
    private String url;

    @NotBlank
    private String status;


    private int crawledURLCount;


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
}
