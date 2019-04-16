package com.acc.webaudits.scan;

import com.acc.webaudits.model.CrawlerDetail;
import com.acc.webaudits.model.Scanner;
import com.acc.webaudits.repository.CrawlerDetailRepository;
import com.acc.webaudits.repository.ScannerDetailRepository;
import com.acc.webaudits.repository.ScannerRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ScannerJob implements Runnable{

    private Scanner scanner;
    private CrawlerDetailRepository crawlerDetailRepository;
    private ScannerDetailRepository scannerDetailRepository;
    private ExecutorService executor;
    private ScannerRepository scannerRepository;


    public ScannerJob(Scanner scanner, ScannerRepository scannerRepository, ScannerDetailRepository scannerDetailRepository, CrawlerDetailRepository crawlerDetailRepository, ExecutorService executor)
    {
        this.scanner = scanner;
        this.scannerRepository = scannerRepository;
        this.scannerDetailRepository = scannerDetailRepository;
        this.crawlerDetailRepository = crawlerDetailRepository;
        this.executor = executor;
    }
    public void run()
    {
        long startTime = System.currentTimeMillis();
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
            String[] tokens = urlListAsString.split(";");
            urlList = Arrays.asList(tokens);
        }

        List<Future<ScannerBatchJobResult>> results = new ArrayList<Future<ScannerBatchJobResult>>();
        int batchSize = 100;
        for (int start = 0; start < urlList.size(); start += batchSize)
        {
            int end = Math.min(start + batchSize, urlList.size());
            List<String> sublist = urlList.subList(start, end);
            ScannerBatchJob scannerBatchJob = new ScannerBatchJob(scanner.getName(), sublist, scannerDetailRepository);
            Future<ScannerBatchJobResult> scannerJobResultFuture = executor.submit(scannerBatchJob);
            results.add(scannerJobResultFuture);
            System.out.println("ScannerBatchJob created...");
        }
        int totalURLCount = urlList.size();
        int totalSuccessCount = 0;
        int totalFailureCount = 0;
        for(Future<ScannerBatchJobResult> f : results)
        {
            ScannerBatchJobResult scannerBatchJobResult = null;
            try
            {
                scannerBatchJobResult = f.get();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            totalSuccessCount = totalSuccessCount + scannerBatchJobResult.getSuccessCount();
            totalFailureCount = totalFailureCount + scannerBatchJobResult.getFailureCount();
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime-startTime);
        System.out.println("-----------DONE-----------------");
        System.out.println("totalURLCount : " + totalURLCount + " ,totalSuccessCount : " + totalSuccessCount +
                " ,totalFailureCount : " + totalFailureCount + "  ,Time taken : " + timeTaken + " Ms");
        System.out.println("-----------DONE-----------------");

        //set the scanner status to complete
        //saveScannerState(scanner.getName(), "complete", totalURLCount, totalSuccessCount, totalFailureCount, timeTaken);
        Scanner s = scannerRepository.findById(scanner.getName()).get();
        s.setTotalURLCount(totalURLCount);
        s.setSuccessCount(totalSuccessCount);
        s.setFailureCount(totalFailureCount);
        s.setTimeTaken(timeTaken);
        s.setStatus("complete");
        scannerRepository.save(s);
    }
}
