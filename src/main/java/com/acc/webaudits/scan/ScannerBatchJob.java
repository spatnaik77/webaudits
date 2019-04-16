package com.acc.webaudits.scan;

import java.util.List;
import java.util.concurrent.Callable;

import com.acc.webaudits.model.ScannerDetail;
import com.acc.webaudits.repository.ScannerDetailRepository;
import com.acc.webaudits.repository.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScannerBatchJob implements Callable<ScannerBatchJobResult>{

	WebDriver webDriver;
	private List<String> urlList;

	private String scannerName;
	private ScannerDetailRepository scannerDetailRepository;

	public ScannerBatchJob(String scannerName, List<String> lsturls, ScannerDetailRepository scannerDetailRepository)
	{
		this.scannerName = scannerName;
		this.urlList = lsturls;
		this.scannerDetailRepository = scannerDetailRepository;

		this.webDriver = WebDriverManager.createWebDriver();
	}
	@Override
	public ScannerBatchJobResult call() throws Exception 	{
		ScannerBatchJobResult scannerBatchJobResult = null;
		try 
		{
			scannerBatchJobResult = this.scanURls(urlList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			webDriver.close();
			webDriver.quit();
		}
		return scannerBatchJobResult;
	}
	private ScannerBatchJobResult scanURls(List<String> lsturls )
	{
		int successCount = 0;
		int failureCount = 0;
		ScannerBatchJobResult scannerBatchJobResult = new ScannerBatchJobResult();
		long startTime = System.currentTimeMillis();
		try
		{
			for(int j=0;j<lsturls.size();j++)
			{
				String urlval = lsturls.get(j);
				if(urlval != null)
				{
					boolean result = false;
					try
					{
						webDriver.navigate().to(urlval);
						List<WebElement> liElement1 = webDriver.findElements(By.tagName("script"));
						for (WebElement dtmscript : liElement1)
						{
							if(dtmscript.getAttribute("src").contains("assets.adobedtm.com"))
							{
								ScannerDetail scannerDetail = new ScannerDetail();
								scannerDetail.setScannerName(scannerName);
								scannerDetail.setResult("success");
								scannerDetail.setUrl(urlval);
								scannerDetailRepository.save(scannerDetail);
								//System.out.println(Thread.currentThread().getName() + "\t " +urlval + "\t " + "success");
								result = true;
								break;
							}
							else
							{
								ScannerDetail scannerDetail = new ScannerDetail();
								scannerDetail.setScannerName(scannerName);
								scannerDetail.setResult("failed");
								scannerDetail.setUrl(urlval);
								scannerDetailRepository.save(scannerDetail);
								//System.out.println(Thread.currentThread().getName() + "\t " +urlval + "\t " + "failure");
							}
						}
						if(result)
						{
							successCount++;
						}
						else
						{
							failureCount++;
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					failureCount++;
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("ScannerBatchJob done. " + "successCount: " + successCount + " FailureCount: " + failureCount +
					" Time taken: " + (endTime-startTime));
			scannerBatchJobResult.setSuccessCount(successCount);
			scannerBatchJobResult.setFailureCount(failureCount);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return scannerBatchJobResult;
	}
}
