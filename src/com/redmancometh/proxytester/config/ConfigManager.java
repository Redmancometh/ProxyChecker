package com.redmancometh.proxytester.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ConfigManager
{

    private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).registerTypeHierarchyAdapter(Proxy.class, new ProxyAdapter()).setPrettyPrinting().create();

    private Config config;

    public void init()
    {
        initConfig();
    }

    private void initConfig()
    {
        try (FileReader reader = new FileReader("proxycfg" + File.separator + "proxies.json"))
        {
            Config conf = gson.fromJson(reader, Config.class);
            this.config = conf;
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public Config getConfig()
    {
        return config;
    }

    public void setConfig(Config config)
    {
        this.config = config;
    }

    public void writeConfig()
    {
        try (FileWriter w = new FileWriter("proxycfg" + File.separator + "proxies.json"))
        {
            System.out.println("Writing");
            gson.toJson(config, w);
            System.out.println("WRITTEN");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static class ProxyAdapter extends TypeAdapter<Proxy>
    {

        @Override
        public Proxy read(JsonReader reader) throws IOException
        {
            String[] rawString = reader.nextString().split(":");
            String address = rawString[0];
            int port = Integer.parseInt(rawString[1]);
            Proxy proxy = new Proxy(address, port);
            proxy.setAddress(address);
            proxy.setPort(port);
            return proxy;
        }

        @Override
        public void write(JsonWriter arg0, Proxy arg1) throws IOException
        {
            arg0.value(arg1.getAddress() + ":" + arg1.getPort());
        }
    }

}
