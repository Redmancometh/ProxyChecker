package com.redmancometh.proxytester;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.redmancometh.proxytester.config.Config;
import com.redmancometh.proxytester.config.ConfigManager;
import com.redmancometh.proxytester.proxies.ProxyManager;

import lombok.Data;

@Data
public class ProxyTester
{
    public static ScheduledExecutorService pool = Executors.newScheduledThreadPool(16);
    public static ScheduledExecutorService poll = Executors.newScheduledThreadPool(1);
    private static ConfigManager configManager;
    private static ProxyManager proxyManager = new ProxyManager();
    private static List<Tester> testers = new CopyOnWriteArrayList();

    public static void main(String[] args)
    {
        configManager = new ConfigManager();
        configManager.init();
        Config config = configManager.getConfig();
        for (int x = 0; x < config.getBots(); x++)
        {
            AtomicInteger newX = new AtomicInteger(x);
            proxyManager.getNext((proxy) ->
            {
                Tester tester = new Tester(proxy);
                testers.add(tester);
                tester.init();
                tester.setBotNumber(newX.get());
                pool.submit(() -> tester.test());
            });
        }
        try
        {
            poll.scheduleAtFixedRate(() -> testers.forEach((tester) -> tester.poll()), 1000, 1000, TimeUnit.MILLISECONDS).get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    public static ProxyManager proxies()
    {
        return proxyManager;
    }

    public static ConfigManager confmon()
    {
        return configManager;
    }

    public static Config cfg()
    {
        return configManager.getConfig();
    }
}
