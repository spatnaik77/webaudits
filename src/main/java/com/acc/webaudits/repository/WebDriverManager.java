package com.acc.webaudits.repository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Repository;

@Repository
public class WebDriverManager
{
    private WebDriver webDriver;

    public WebDriverManager()
    {
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("-incognito");
            options.addArguments("start-maximized");
            options.addArguments("disable-infobars");
            options.addArguments("--headless");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            webDriver = new ChromeDriver(capabilities);
            System.out.println(webDriver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public WebDriver getWebDriver()
    {
        return this.webDriver;
    }

}
