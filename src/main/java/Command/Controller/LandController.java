package Command.Controller;

import Controller.Controller;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sperias.group.GroupManager.GroupManager;

public class LandController extends Controller{

    public LandController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    public boolean canListLand()
    {
        if(!plugin.getLands().containsKey(player.getUniqueId()) && !plugin.getLandProgress().containsKey(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas de terrain");
            return false;
        }
        return true;
    }

    public boolean canCreateLand(String landName)
    {
        if(plugin.getLandProgress().containsKey(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous avez déjà un terrain en progression");
            return false;
        }
        if(plugin.getLands().containsKey(player.getUniqueId()))
        {
            if(plugin.getLands().get(player.getUniqueId()).containsKey(landName))
            {
                player.sendMessage(ChatColor.of("#FF0000") + "Vous possédez déjà un terrain sous ce nom");
                return false;
            }
            GroupManager group = (GroupManager) Bukkit.getServer().getPluginManager().getPlugin("GroupManager");
            if(plugin.getLands().get(player.getUniqueId()).size() > group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand())
            {
                player.sendMessage(ChatColor.of("#FF0000") + "Vous ne pouvez pas avoir plus de " + group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand());
                return false;
            }
        }
        return true;
    }

    public boolean canDeleteLand(String landName)
    {
        if(!this.hasLand(landName)) return false;
        landModel.deleteLand(player.getUniqueId(), landName);
        return true;
    }

    public boolean canInviteMember(Player target, String landName)
    {
        if(!this.targetExist(target)) return false;
        if(!this.hasLand(landName)) return false;

        if(plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().contains(target.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce joueur est déjà dans votre claim");
            return false;
        }

        landModel.addMember(player.getUniqueId(), landName, target.getUniqueId());
        return true;
    }

    public boolean canRemovePlayer(Player target, String landName)
    {
        if(!this.targetExist(target)) return false;
        if(!this.hasLand(landName)) return false;

        if(!plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().contains(target.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce joueur n'est pas dans votre terrain");
            return false;
        }

        landModel.removeMember(player.getUniqueId(), landName, target.getUniqueId());
        return true;
    }

    public boolean canConfirmLand()
    {
        if(!this.hasLandProgress()) return false;
        Land land = plugin.getLandProgress().get(player.getUniqueId());
        if(land.getMinLocation() == null || land.getMaxLocation() == null)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas sauvegarder tout les positions");
            return false;
        }

        EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
        Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
        long price = land.getArea() * plugin.getClaimPrice();

        if(playerEconomy.getMoney() - price < 0 && !land.isSafeZone())
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas assez d'argent pour claim. Il vous manque " + (price - playerEconomy.getMoney()) + economyPlugin.getCurrency());
            return false;
        }
        if(plugin.getAllLand().stream().anyMatch(l -> land.isInRegion(l.getMinLocation()) || land.isInRegion(l.getMaxLocation())))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Quelqu'un a déjà claim avant vous une partie de votre zone. Votre terrain a été automatiquement supprimé");
            plugin.getLandProgress().remove(player.getUniqueId());
            return false;
        }

        landModel.createLand(land);
        return true;
    }

    public boolean canSetSpawn(String landName)
    {
        if(!this.hasLand(landName)) return false;
        Land land = plugin.getLands().get(player.getUniqueId()).get(landName);
        if(!land.isInRegion(player.getLocation()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous ne pouvez pas positionner un spawn en dehors de la zone");
            return false;
        }
        landModel.setSpawnLand(land, player.getLocation());
        return true;
    }

    public boolean canDeleteSpawn(String landName)
    {
        if(!this.hasLand(landName)) return false;
        Land land = plugin.getLands().get(player.getUniqueId()).get(landName);
        if(land.getSpawnLocation() == null)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Cette région ne possède pas de spawn");
            return false;
        }
        landModel.deleteSpawnLand(land);
        return true;
    }

    public boolean canGoSpawn(String landName)
    {
        if(!this.hasLand(landName)) return false;
        if(plugin.getLands().get(player.getUniqueId()).get(landName).getSpawnLocation() == null)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Cette région ne possède pas de spawn");
            return false;
        }
        return true;
    }

    public boolean canGoOtherSpawn(Player owner, String landName)
    {
        if(!targetExist(owner)) return false;
        if(!plugin.getLands().containsKey(owner.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce joueur ne possède pas de région");
            return false;
        }
        if(!plugin.getLands().get(owner.getUniqueId()).containsKey(landName))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Ce joueur ne possède pas cette région");
            return false;
        }
        Land land = plugin.getLands().get(owner.getUniqueId()).get(landName);
        if(!land.getMembers().contains(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous ne pouvez pas vous téléporter dans cette région");
            return false;
        }
        return true;
    }
}
