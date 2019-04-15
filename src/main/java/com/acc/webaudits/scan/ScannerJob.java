package com.acc.webaudits.scan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.acc.webaudits.model.ScannerDetail;
import com.acc.webaudits.repository.ScannerDetailRepository;
import com.acc.webaudits.repository.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

public class ScannerJob implements Callable<ScannerJobResult>{



	WebDriver webDriver;
	private List<String> urlList;

	private String scannerName;
	private ScannerDetailRepository scannerDetailRepository;

	public ScannerJob(String scannerName, List<String> lsturls, ScannerDetailRepository scannerDetailRepository)
	{
		this.scannerName = scannerName;
		this.urlList = lsturls;
		this.scannerDetailRepository = scannerDetailRepository;

		this.webDriver = WebDriverManager.createWebDriver();

	}
	@Override
	public ScannerJobResult call() throws Exception 	{
		ScannerJobResult scannerJobResult = null;
		try 
		{
			scannerJobResult = this.scanURls(urlList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			webDriver.close();
			webDriver.quit();
		}
		return scannerJobResult;
	}
	private ScannerJobResult scanURls(List<String> lsturls )
	{
		int successCount = 0;
		int failureCount = 0;
		ScannerJobResult scannerJobResult = new ScannerJobResult();
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
			System.out.println("ScannerJob done. " + "successCount: " + successCount + " FailureCount: " + failureCount +
					" Time taken: " + (endTime-startTime));
			scannerJobResult.setSuccessCount(successCount);
			scannerJobResult.setFailureCount(failureCount);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return scannerJobResult;
	}
}
