package Controller;

import Entity.Land;
import Entity.LandSecurity;
import Model.LandModel;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Controller{

    protected Player player;
    protected LandMain plugin;
    protected LandModel landModel = new LandModel();

    public Controller(Player player, LandMain plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public boolean targetExist(Player target)
    {
        if(target == null)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce joueur n'existe pas");
            return false;
        }
        if(target == player)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "...");
            return false;
        }
        return true;
    }

    public boolean hasLand(String landName)
    {
        if(!plugin.getLands().containsKey(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas de terrain");
            return false;
        }
        if(plugin.getLands().containsKey(player.getUniqueId()))
        {
            if(!plugin.getLands().get(player.getUniqueId()).containsKey(landName))
            {
                player.sendMessage(ChatColor.of("#FF0000") + "Ce terrain n'existe pas");
                return false;
            }
        }
        return true;
    }

    public boolean hasSafeLand(String landName)
    {
        if(!plugin.getSafeLands().containsKey(landName))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean hasLandProgress()
    {
        if(!plugin.getLandProgress().containsKey(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean hasPermission(String permission)
    {
        if(!player.hasPermission(permission) && !player.isOp())
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas la permission");
            return false;
        }
        return true;
    }

    public boolean canHandleLandSecurity(String landName, LandSecurity landSecurity, String value)
    {
        if(!this.hasLand(landName)) return false;
        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage(ChatColor.of("#FF0000") + value + " n'est pas une valeur correct. Mettez ON ou OFF");
            return false;
        }
        landModel.setLandSecurity(player.getUniqueId(), landName, landSecurity, value.equalsIgnoreCase("on"));
        return true;
    }

    public boolean canHandleSafeLandSecurity(String landName, LandSecurity landSecurity, String value)
    {
        if(!this.hasSafeLand(landName)) return false;
        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage(ChatColor.of("#FF0000") + value + " n'est pas une valeur correct. Mettez ON ou OFF");
            return false;
        }

        landModel.setSafeLandSecurity(landName, landSecurity, value.equalsIgnoreCase("on"));
        return true;
    }
}
