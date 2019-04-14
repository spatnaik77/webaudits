package com.acc.webaudits.model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TBD {
    public static void main(String[] args)
    {
        for(int c = 0; c < 3; c++)
        {
            long statrtTime = System.currentTimeMillis();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("-incognito");
            options.addArguments("start-maximized");
            options.addArguments("disable-infobars");
            options.addArguments("--headless");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            WebDriver webDriver = new ChromeDriver(options);
            webDriver.close();
            webDriver.quit();
            long endTime = System.currentTimeMillis();
            System.out.println(webDriver + "\t " + (endTime-statrtTime) + " Ms");

        }
    }
}
