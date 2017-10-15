package com.redmancometh.proxytester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.redmancometh.proxytester.config.Proxy;
import com.redmancometh.proxytester.proxies.TesterState;

import lombok.Data;

@Data
public class Tester
{
    private Proxy proxy;
    private HtmlUnitDriver driver;
    private TesterState state = TesterState.READY_FOR_POLLING;
    public ScheduledExecutorService poll = Executors.newSingleThreadScheduledExecutor();
    private int botNumber;

    public Tester(Proxy proxy)
    {
        this.proxy = proxy;
    }

    public void rearm()
    {
        ProxyTester.proxies().getNext((newProxy) ->
        {
            System.out.println("Rearming bot " + botNumber + " with: " + newProxy);
            this.proxy = newProxy;
            driver = new HtmlUnitDriver();
            driver.setProxy(newProxy.getAddress(), newProxy.getPort());
        });
    }

    public void init()
    {
        driver = new HtmlUnitDriver();
        driver.setProxy(proxy.getAddress(), proxy.getPort());
    }

    public void poll()
    {
        if (getState() == TesterState.COMPLETED_TEST)
        {
            System.out.println("REARMING " + this.getBotNumber());
            rearm();
            poll.submit(() -> test());
        }

    }

    public void test()
    {
        setState(TesterState.RUNNING_TEST);
        System.out.println("Bot: " + botNumber + ": NAVIGATING!");
        try
        {
            driver.navigate().to("http://minecraftservers.org/vote/111329");
            new WebDriverWait(driver, 10).until((
            driver) -> driver != null && driver.getTitle() != null && driver.findElements(By.id("vote")).size() > 0);
            System.out.println("MARKED GOOD!");
            this.getProxy().markGood();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Purging bad " + proxy);
            this.getProxy().purgeBadProxy();
        }
        File logFile = new File(this.botNumber + ".log");
        if (!logFile.exists()) try
        {
            logFile.createNewFile();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        try (FileWriter w = new FileWriter(logFile))
        {
            w.write(driver.getPageSource());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        setState(TesterState.COMPLETED_TEST);
        System.out.println(proxy + " has been checked!");
    }
}
