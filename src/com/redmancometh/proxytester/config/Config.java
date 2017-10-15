package com.redmancometh.proxytester.config;

import java.util.HashSet;

import lombok.Data;

@Data
public class Config
{
    private HashSet<Proxy> goodProxies, badProxies, proxies;
    private int bots;
}
