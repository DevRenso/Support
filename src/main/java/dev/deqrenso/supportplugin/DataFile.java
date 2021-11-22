package dev.deqrenso.supportplugin;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import java.io.*;

public class DataFile extends YamlConfiguration
{
    private static DataFile config;
    private final Plugin plugin;
    private final File configFile;

    public static DataFile getConfig() {
        if (DataFile.config == null) {
            DataFile.config = new DataFile();
        }
        return DataFile.config;
    }
    
    public void saveDefault() {
        this.plugin.saveResource("data.yml", false);
    }

    
    public void saveAll() {
        this.save();
        this.reload();
    }
    
    private Plugin main() {
        return SupportPlugin.getInstance();
    }
    
    public void save() {
        try {
            super.save(this.configFile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void reload() {
        try {
            super.load(this.configFile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public DataFile() {
        this.plugin = this.main();
        this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        this.saveDefault();
        this.reload();
    }
}
