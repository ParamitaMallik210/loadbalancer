package org.example.utils;


//it will have the list if the backendhosts
//gives one host at A TIME

import java.util.ArrayList;
import java.util.List;

public class BackendServers {
    private static List<String> servers=new ArrayList<>();
    private static int count =0;
    static {
        servers.add("IP1");
        servers.add("IP2");

    }
    public static String getHost(){

        String Host=servers.get(count%servers.size());
        count++;
        return Host;
    }

}
