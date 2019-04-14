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

public class ScannerJob implements Callable<Boolean>{



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
	public Boolean call() throws Exception 	{
		try 
		{
			this.scanURls(urlList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			webDriver.close();
			webDriver.quit();
		}
		return true;
	}
	private void scanURls(List<String> lsturls )
	{
		try
		{
			for(int j=0;j<lsturls.size();j++)
			{
				String urlval = lsturls.get(j);
				if(urlval != null)
				{
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
								System.out.println(Thread.currentThread().getName() + "\t " +urlval + "\t " + "success");
								break;
							}
							else
							{
								ScannerDetail scannerDetail = new ScannerDetail();
								scannerDetail.setScannerName(scannerName);
								scannerDetail.setResult("failed");
								scannerDetail.setUrl(urlval);
								scannerDetailRepository.save(scannerDetail);
								System.out.println(Thread.currentThread().getName() + "\t " +urlval + "\t " + "failure");
								break;
							}
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					System.out.println(" url is null");
				}
			
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
