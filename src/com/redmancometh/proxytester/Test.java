package com.redmancometh.proxytester;

import java.util.function.Consumer;

import com.redmancometh.proxytester.config.Proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Test
{
    private Proxy proxy;

    public void testProxy()
    {
        
    }
    
    public void testConnection(Consumer<Boolean> callback)
    {
        try
        {

        }
        catch (Exception e)
        {
            callback.accept(false);
        }
    }
}
