package dev.deqrenso.supportplugin.commands;

import com.google.common.collect.Lists;
import dev.deqrenso.supportplugin.DataFile;
import dev.deqrenso.supportplugin.SupportPlugin;
import dev.deqrenso.supportplugin.utils.CC;
import dev.deqrenso.supportplugin.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SupportCommand implements CommandExecutor {

    private List<Integer> reservedSlot;

    private void openInventory(Player player){
        Inventory inv = Bukkit.createInventory(null,
                SupportPlugin.getInstance().getConfig().getInt("HOTBARS")*9,
                CC.translate(SupportPlugin.getInstance().getConfig().getString("INVENTORY-NAME")));
        ConfigurationSection partnersSection = SupportPlugin.getInstance().getConfig().getConfigurationSection("PARTNERS");
        ItemStack fillItem = new ItemBuilder(Material.valueOf(SupportPlugin.getInstance().getConfig().getString("FILL-ITEM.MATERIAL")))
                .data(SupportPlugin.getInstance().getConfig().getInt("FILL-ITEM.DATA"))
                .name(CC.translate("&7 "))
                .lore(CC.translate("&7 "))
                .build();

        partnersSection.getKeys(false).forEach(partner -> {
            if (SupportPlugin.getInstance().getConfig().getBoolean("PARTNERS." + partner + ".ENABLE")){
                reservedSlot.add(SupportPlugin.getInstance().getConfig().getInt("PARTNERS." + partner + ".SLOT"));
                List<String> lore = SupportPlugin.getInstance().getConfig().getStringList("PARTNERS."+partner+".LORE");
                for(int i =0;i<lore.size();i++){
                    lore.set(i, lore.get(i).replace("%displayName%", SupportPlugin.getInstance().getConfig().getString("PARTNERS." + partner + ".DISPLAYNAME"))
                    .replace("%votes%", String.valueOf(SupportPlugin.getInstance().getConfig().getInt("PARTNERS." + partner + ".VOTES"))));
                }
            ItemStack partnerSkull = new ItemBuilder(Material.SKULL_ITEM)
                    .data(3)
                    .name(CC.translate(SupportPlugin.getInstance().getConfig().getString("PARTNERS." + partner + ".DISPLAYNAME").replace("%votes%", String.valueOf(SupportPlugin.getInstance().getConfig().getInt("PARTNERS." + partner + ".VOTES")))))
                    .lore(CC.translate(lore))
                    .owner(SupportPlugin.getInstance().getConfig().getString("PARTNERS." + partner + ".SKULL-NAME"))
                    .build();
            inv.setItem(SupportPlugin.getInstance().getConfig().getInt("PARTNERS." + partner + ".SLOT"), partnerSkull);
        }

        });
        for(int i =0;i<SupportPlugin.getInstance().getConfig().getInt("HOTBARS")*9;i++){
            if(!reservedSlot.contains(i)) inv.setItem(i, fillItem);
        }
        player.openInventory(inv);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player)sender;
        if(!p.hasPermission("support.inventory")) return false;
        if(args.length == 0){
            this.openInventory(p);
            return false;
        }
        if(args[0].equalsIgnoreCase("reload")){
            if(!p.hasPermission("support.op")){
                this.openInventory(p);
                return false;
            }else{
                SupportPlugin.getInstance().reloadConfig();
                DataFile.getConfig().reload();
                p.sendMessage(CC.translate("&aAll files reloaded!"));
            }
        }
        if(args[0].equalsIgnoreCase("data")){
            if(!p.hasPermission("support.op")){
                this.openInventory(p);
                return false;
            }else{
            if(args.length < 2){
                this.openInventory(p);
                return false;
            }
            if(SupportPlugin.getInstance().getConfig().contains("PARTNERS."+args[1].toUpperCase())){
                p.sendMessage(CC.translate("&aPartner &7"+args[1]+" &ahas &7"+SupportPlugin.getInstance().getConfig().getInt("PARTNERS."+args[1]+".VOTES")+" &avotes."));
                return true;
            }
            }
        }
        return false;
    }
}
