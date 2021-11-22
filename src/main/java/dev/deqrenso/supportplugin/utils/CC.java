package dev.deqrenso.supportplugin.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class CC {

    public static String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<String> translate(List<String> msg){
        for(int i =0;i<msg.size();i++){
            msg.set(i, translate(msg.get(i)));
        }
        return msg;
    }
}
