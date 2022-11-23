package Controller;

import Entity.Land;
import Entity.LandSecurity;
import Model.LandModel;
import LandMain.LandMain;
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
            player.sendMessage("§cCe joueur n'existe pas");
            return false;
        }
        if(target == player)
        {
            player.sendMessage("§c...");
            return false;
        }
        return true;
    }

    public boolean hasLand(String landName)
    {
        if(plugin.getLands().containsKey(player.getUniqueId()))
        {
            if(!plugin.getLands().get(player.getUniqueId()).containsKey(landName))
            {
                player.sendMessage("Ce terrain n'existe pas");
                return false;
            }
        }
        return true;
    }

    public boolean hasSafeLand(String landName)
    {
        if(!plugin.getSafeLands().containsKey(landName))
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean hasLandProgress()
    {
        if(!plugin.getLandProgress().containsKey(player.getUniqueId()))
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean hasPermission(String permission)
    {
        if(!player.hasPermission(permission) && !player.isOp())
        {
            player.sendMessage("§cVous n'avez pas la permission");
            return false;
        }
        return true;
    }

    public boolean canHandleLandSecurity(String landName, LandSecurity landSecurity, String value)
    {
        if(!this.hasLand(landName)) return false;
        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }
        landModel.setLandSecurity(player.getUniqueId(), landSecurity, value.equalsIgnoreCase("on"));
        return true;
    }

    public boolean canHandleSafeLandSecurity(String landName, LandSecurity landSecurity, String value)
    {
        if(!this.hasSafeLand(landName)) return false;
        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }

        landModel.setSafeLandSecurity(landName, landSecurity, value.equalsIgnoreCase("on"));
        return true;
    }

    protected boolean positionOnOtherLand(Location location)
    {
        if(plugin.getAllLand().stream().anyMatch(l -> l.isInRegion(location)))
        {
            player.sendMessage("§cCette zone est déjà claim");
            return false;
        }
        return true;
    }

    public boolean canSetPosition1(Location position1)
    {
        if(!this.hasLandProgress()) return false;
        return this.positionOnOtherLand(position1);
    }

    public boolean canSetPosition2(Location position2) {
        if(!this.hasLandProgress()) return false;
        Land land = plugin.getLandProgress().get(player.getUniqueId());
        if(land.getPosition1() == null)
        {
            player.sendMessage("§aPremière position inexistant");
            return false;
        }
        if(position2.getWorld() != land.getPosition1().getWorld())
        {
            player.sendMessage("§cLes deux positions n'ont pas les mêmes monde");
            return false;
        }
        if(!positionOnOtherLand(position2)) return false;

        land.buildLandLocation();
        if(plugin.getAllLand().stream().anyMatch(l -> land.isInRegion(l.getMinLocation()) || land.isInRegion(l.getMaxLocation())))
        {
            player.sendMessage("§cIl y a déjà un claim dans votre zone");
            return false;
        }
        return true;
    }
}
