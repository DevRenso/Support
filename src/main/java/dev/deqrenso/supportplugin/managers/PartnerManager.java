package dev.deqrenso.supportplugin.managers;

import dev.deqrenso.supportplugin.DataFile;
import dev.deqrenso.supportplugin.SupportPlugin;
import dev.deqrenso.supportplugin.utils.CC;
import dev.deqrenso.supportplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class PartnerManager implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null || e.getInventory() != e.getClickedInventory())return;
        if(e.getInventory().getTitle().equals(CC.translate(SupportPlugin.getInstance().getConfig().getString("INVENTORY-NAME")))){
            Player player = (Player) e.getWhoClicked();;
            e.setCancelled(true);
            SupportPlugin.getInstance().getConfig().getConfigurationSection("PARTNERS").getKeys(false).forEach(partner ->{
                if(e.getSlot() == SupportPlugin.getInstance().getConfig().getInt("PARTNERS."+partner+".SLOT")){
                    if(DataFile.getConfig().contains("DATA."+player.getUniqueId())) {
                        player.sendMessage(CC.translate(SupportPlugin.getInstance().getConfig().getString("MESSAGES.ALREADY-VOTE")));
                        return;
                    }
                    List<String> commands = SupportPlugin.getInstance().getConfig().getStringList("PARTNERS."+partner+".COMMANDS");
                    commands.forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName())));
                    player.sendMessage(CC.translate(SupportPlugin.getInstance().getConfig().getString("MESSAGES.GIVE-PARTNER-REWARDS").replace("%displayName%", SupportPlugin.getInstance().getConfig().getString("PARTNERS."+partner+".DISPLAYNAME"))));
                    player.closeInventory();
                    DataFile.getConfig().set("DATA."+player.getUniqueId(), true);
                    SupportPlugin.getInstance().getConfig().set("PARTNERS."+partner+".VOTES", SupportPlugin.getInstance().getConfig().getInt("PARTNERS."+partner+".VOTES")+1);
                    DataFile.getConfig().save();
                    SupportPlugin.getInstance().saveConfig();
                    return;
                }
            });
        }
    }

}
