package Command.Controller;

import Controller.Controller;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import Land.LandMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sperias.group.GroupManager.GroupManager;

public class LandController extends Controller{

    public LandController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    public boolean canCreateCity(String landName)
    {
        if(plugin.getLands().size() > 0)
        {
            if(plugin.getLands().get(player.getUniqueId()).containsKey(landName))
            {
                player.sendMessage("§cLe nom de cette ville existe déjà");
                return false;
            }
            GroupManager group = (GroupManager) Bukkit.getServer().getPluginManager().getPlugin("SP_GroupManager");
            if(plugin.getLands().get(player.getUniqueId()).size() > group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand())
            {
                player.sendMessage("§cVous ne pouvez pas avour plus de " + group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand());
                return false;
            }
        }
        return true;
    }

    public boolean canDeleteCity(String landName)
    {
        if(!this.hasLand(landName)) return false;

        landModel.deleteLand(player.getUniqueId(), landName);
        return true;
    }

    public boolean canInviteMember(Player target, String landName)
    {
        if(!this.targetExist(target)) return false;
        if(!this.hasLand(landName)) return false;

        if(plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().contains(player.getUniqueId()))
        {
            player.sendMessage("§cCe joueur est déjà dans votre claim");
            return false;
        }

        landModel.addMember(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    public boolean canRemovePlayer(Player target, String landName)
    {
        if(!this.targetExist(target)) return false;
        if(!this.hasLand(landName)) return false;

        if(!plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().contains(player.getUniqueId()))
        {
            player.sendMessage("§cCe joueur n'est pas dans votre terrain");
            return false;
        }

        landModel.removeMember(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    public boolean canSetFirstLocationOnLand(String landName, Location firstLocation)
    {
        if(!this.hasLand(landName)) return false;

        return this.positionOnOtherLand(landName, firstLocation);
    }

    public boolean canSetSecondLocationOnCity(String landName, Location secondLocation) {
        if(!this.hasLand(landName)) return false;
        Land land = plugin.getLands().get(player.getUniqueId()).get(landName);
        if(land.getFirstLocation() == null)
        {
            player.sendMessage("§aPremière position inexistant");
            return false;
        }
        if(secondLocation.getWorld() != land.getFirstLocation().getWorld())
        {
            player.sendMessage("§cLes deux positions n'ont pas les mêmes monde");
            return false;
        }
        return positionOnOtherLand(landName, secondLocation);
    }

    public boolean canConfirmCity(String landName)
    {
        if(!this.hasLand(landName)) return false;
        Land land = plugin.getLands().get(player.getUniqueId()).get(landName);
        if(land.getFirstLocation() == null || land.getSecondLocation() == null)
        {
            player.sendMessage("§aVous n'avez pas sauvegarder tout les positions de la ville");
            return false;
        }
        land.buildLandLocation();
        if(!this.cityContainOtherCity(land))
        {
            if(land.getLastMinLocation() != null && land.getLastMaxLocation() != null)
            {
                land.setMinLocation(land.getLastMinLocation());
                land.setMaxLocation(land.getLastMaxLocation());
            }
            return false;
        }

        EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
        Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
        long price = land.getArea() * plugin.getConfig().getLong("price");
        if(playerEconomy.getMoney() - price < 0)
        {
            player.sendMessage("§cVous n'avez pas assez d'argent pour claim. Il vous manque " + (price - playerEconomy.getMoney()) + economyPlugin.getCurrency());
        }
        land.setFirstLocation(null);
        land.setSecondLocation(null);
        landModel.createLand(player.getUniqueId(), land.getRegionName(), land.getMinLocation(), land.getMaxLocation(), false);
        return true;
    }
}
