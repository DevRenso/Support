package dev.deqrenso.supportplugin;

import dev.deqrenso.supportplugin.commands.SupportCommand;
import dev.deqrenso.supportplugin.managers.PartnerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupportPlugin extends JavaPlugin {

   @Getter public static SupportPlugin instance;
   @Setter public PartnerManager partnerManager;
   @Setter DataFile dataFile;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.setDataFile(new DataFile());
        DataFile.getConfig().saveDefault();
        this.setPartnerManager(new PartnerManager());
        Bukkit.getPluginManager().registerEvents(new PartnerManager(), this);
        this.getCommand("support").setExecutor(new SupportCommand());
    }

    public void onDisable() {
        instance = null;
    }
}
