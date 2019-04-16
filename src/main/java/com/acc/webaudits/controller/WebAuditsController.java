package com.acc.webaudits.controller;


import com.acc.webaudits.ApiManager;
import com.acc.webaudits.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WebAuditsController
{
    @Autowired
    ApiManager apiManager;

    @GetMapping("/")
    public String healthCheck()
    {
        return "Hello from WebAudits server";
    }

    @PostMapping("/crawler")
    public Crawler createCrawlerSync(@RequestBody Crawler crawler)
    {
        return apiManager.createCrawler(crawler);
    }

    @GetMapping("/crawlers")
    public List<Crawler> getAllCrawlers()
    {
        return apiManager.getAllCrawlers();
    }

    @GetMapping("/crawlers/{name}")
    public Crawler getCrawlerByName(@PathVariable(value = "name") String name)
    {
        return apiManager.getCrawlerByName(name);
    }

    @GetMapping("/crawlers/{name}/details")
    public List<CrawlerDetail> getCrawlerDetails(@PathVariable(value = "name") String name)
    {
        return apiManager.getCrawlerDetails(name);
    }

    @DeleteMapping("/crawlers/{name}")
    public void deleteCrawler(@PathVariable(value = "name") String name)
    {
        apiManager.deleteCrawler(name);
    }

    @PostMapping("/scanner")
    public Scanner createScanner(@RequestBody Scanner scanner) throws Exception
    {
        return apiManager.createScanner(scanner);
    }

    @GetMapping("/scanners")
    public List<Scanner> getAllScanners()
    {
        return apiManager.getAllScanners();
    }

    @GetMapping("/scanners/{name}")
    public Scanner getScannerByName(@PathVariable(value = "name") String name)
    {
        return apiManager.getScannerByName(name);
    }

    @GetMapping("/scanners/{name}/details")
    public List<ScannerDetail> getScannerDetails(@PathVariable(value = "name") String name)
    {
        return apiManager.getScannerDetails(name);
    }

    @DeleteMapping("/scanners/{name}")
    public void deleteScanner(@PathVariable(value = "name") String name)
    {
        apiManager.deleteScanner(name);
    }


}
