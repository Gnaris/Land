package Controller;

import Entity.Land;
import Entity.LandSecurity;
import Model.LandModel;
import Land.LandMain;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

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
                player.sendMessage("§cVous ne possédez pas de ville au nom de " + landName);
                return false;
            }
        }
        return true;
    }

    public boolean hasSafeLand(String landName)
    {
        if(!plugin.getSafeLands().containsKey(landName))
        {
            player.sendMessage("§cCette ville n'existe pas");
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

    protected boolean positionOnOtherLand(String landName, Location location)
    {
        Land onLand = null;
        if(plugin.getAllLand().stream().anyMatch(l -> l.isInRegion(location)))
        {
            onLand = plugin.getAllLand().stream().filter(l -> l.isInRegion(location)).collect(Collectors.toList()).get(0);
        }
        if(onLand != null)
        {
            if(onLand.isSafeZone() && !onLand.getRegionName().equalsIgnoreCase(landName))
            {
                player.sendMessage("§cVous ne pouvez pas claim les zones protégées");
                return false;
            }
            if(!onLand.isSafeZone() && (!onLand.getOwner().equals(player.getUniqueId()) || !onLand.getRegionName().equalsIgnoreCase(landName)))
            {
                player.sendMessage("§cVous n'êtes pas le propriétaire de cette zone ou cette zone est déjà claim par quelqu'un");
                return false;
            }
        }
        return true;
    }

    protected boolean cityContainOtherCity(Land land)
    {
        Land onLand = plugin.getAllLand().stream()
                .filter(l ->
                        (land.isInRegion(l.getMinLocation()) || land.isInRegion(l.getMaxLocation())) &&
                        ((!land.getRegionName().equalsIgnoreCase(l.getRegionName()) && l.getOwner().equals(player.getUniqueId())) ||
                        !l.getOwner().equals(player.getUniqueId()) ||
                        l.isSafeZone())
                )
                .findFirst().orElse(null);
        if(onLand != null)
        {
            if(onLand.isSafeZone() && !onLand.getRegionName().equalsIgnoreCase(land.getRegionName()))
            {
                player.sendMessage("§cVous ne pouvez pas claim les zones protégées " + onLand.getRegionName());
                return false;
            }
            if(!onLand.isSafeZone() && !onLand.getRegionName().equalsIgnoreCase(land.getRegionName()) && onLand.getOwner().equals(player.getUniqueId()))
            {
                player.sendMessage("§cVous ne pouvez par surclaim une de vos terrain");
                return false;
            }
            if(!onLand.isSafeZone() && !onLand.getRegionName().equalsIgnoreCase(land.getRegionName()) && !onLand.getOwner().equals(player.getUniqueId()))
            {
                player.sendMessage("§cVous ne pouvez par surclaim un terrain");
                return false;
            }
        }
        return true;
    }
}
