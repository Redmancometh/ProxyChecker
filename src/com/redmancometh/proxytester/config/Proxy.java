package com.redmancometh.proxytester.config;

import com.redmancometh.proxytester.ProxyTester;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Proxy
{
    private String address;
    private int port;

    public void purgeBadProxy()
    {
        ProxyTester.cfg().getProxies().remove(this);
        ProxyTester.cfg().getBadProxies().add(this);
        ProxyTester.confmon().writeConfig();
    }

    public void markGood()
    {
        ProxyTester.cfg().getProxies().remove(this);
        ProxyTester.cfg().getGoodProxies().add(this);
        ProxyTester.confmon().writeConfig();
    }

}
