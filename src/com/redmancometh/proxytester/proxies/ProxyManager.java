package com.redmancometh.proxytester.proxies;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.redmancometh.proxytester.ProxyTester;
import com.redmancometh.proxytester.config.Config;
import com.redmancometh.proxytester.config.Proxy;

import lombok.Data;

@Data
public class ProxyManager
{
    private List<Proxy> beingChecked = new CopyOnWriteArrayList();
    private AtomicInteger proxyNo = new AtomicInteger(0);

    public boolean beingChecked(Proxy proxy)
    {
        return beingChecked.contains(proxy);
    }

    public void printNumber()
    {
        System.out.println("Finished: " + proxyNo.get());
    }

    public void getNext(Consumer<Proxy> callback)
    {
        Config config = ProxyTester.cfg();
        Proxy proxy = null;
        Iterator<Proxy> iter = config.getProxies().iterator();
        while (iter.hasNext())
        {
            proxy = iter.next();
            if (getBeingChecked().contains(proxy))
                continue;
            else
            {
                beingChecked.add(proxy);
                proxyNo.getAndIncrement();
                callback.accept(proxy);
                return;
            }
        }
    }
}
