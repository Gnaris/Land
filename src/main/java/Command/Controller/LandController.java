package Command.Controller;

import Controller.Controller;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import Land.LandMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sperias.group.GroupManager.GroupManager;

public class LandController extends Controller{

    public LandController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    public boolean canListLand()
    {
        if(!plugin.getLands().containsKey(player.getUniqueId()))
        {
            if(!this.hasLandProgress())
            {
                player.sendMessage("§cVous n'avez pas de terrain");
                return false;
            }
        }
        return true;
    }

    public boolean canCreateLand(String landName)
    {
        if(this.hasLandProgress())
        {
            player.sendMessage("§cVous avez déjà un terrain en progression");
            return false;
        }
        if(plugin.getLands().size() > 0)
        {
            if(plugin.getLands().get(player.getUniqueId()).containsKey(landName))
            {
                player.sendMessage("§cVous possédez déjà un terrain sous ce nom");
                return false;
            }
            GroupManager group = (GroupManager) Bukkit.getServer().getPluginManager().getPlugin("GroupManager");
            if(plugin.getLands().get(player.getUniqueId()).size() > group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand())
            {
                player.sendMessage("§cVous ne pouvez pas avoir plus de " + group.getPlayerGroups().get(player.getUniqueId()).getRank().getNbLand());
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

    public boolean canConfirmLand(String landName)
    {
        if(!this.hasLandProgress()) return false;
        Land land = plugin.getLandProgress().get(player.getUniqueId());
        if(land.getMinLocation() == null || land.getMaxLocation() == null)
        {
            player.sendMessage("§aVous n'avez pas sauvegarder tout les positions");
            return false;
        }

        EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
        Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
        long price = land.getArea() * plugin.getConfig().getLong("price");

        if(playerEconomy.getMoney() - price < 0)
        {
            player.sendMessage("§cVous n'avez pas assez d'argent pour claim. Il vous manque " + (price - playerEconomy.getMoney()) + economyPlugin.getCurrency());
        }

        landModel.createLand(land);
        return true;
    }
}
