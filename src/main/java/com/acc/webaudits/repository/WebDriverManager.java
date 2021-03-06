package com.acc.webaudits.repository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Repository;

@Repository
public class WebDriverManager
{
    public static WebDriver createWebDriver()
    {
        WebDriver webDriver = null;
        try
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("-incognito");
            options.addArguments("start-maximized");
            options.addArguments("disable-infobars");
            options.addArguments("--headless");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            webDriver = new ChromeDriver(options);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return webDriver;
    }
}
