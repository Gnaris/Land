package Event.Controller;

import Controller.Controller;
import Entity.Land;
import SPGroupManager.SPGroupManager;
import SPLand.SPLand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandCreationController extends Controller {

    private final SPGroupManager groupManager = (SPGroupManager) Bukkit.getServer().getPluginManager().getPlugin("SP_GroupManager");

    public LandCreationController(Player player, SPLand plugin) {
        super(player, plugin);
    }

    public boolean canStartToClaim(Location location)
    {
        if(playerClaim.getNbLand() >= groupManager.getPlayerGroupList().get(player.getUniqueId()).getRank().getNbMaxLand())
        {
            player.sendMessage("§cVous ne pouvez pas en avoir plus de " + groupManager.getPlayerGroupList().get(player.getUniqueId()).getRank().getNbMaxLand() + " terrain(s)");
            return false;
        }
        if(plugin.getPlayerLandNotConfirmed(player).size() > 0)
        {
            player.sendMessage("§cVous avez déjà un terrain en cours ! Faites /land cancel pour l'annuler");
            return false;
        }
        if(plugin.getLandList().size() != 0 && plugin.getLandList().stream().anyMatch(land -> land.isInArea(location)))
        {
            player.sendMessage("§cVous ne pouvez pas claim une zone déjà claim");
            return false;
        }
        return true;
    }

    public boolean canClaimLand(Land newLand)
    {
        if((newLand.getMaxLocation().getX() - newLand.getMinLocation().getX()) <= 3 || (newLand.getMaxLocation().getZ() - newLand.getMinLocation().getZ()) <= 3)
        {
            player.sendMessage("§cVotre terrain est trop petit. Veuillez recommencez à 0");
            return false;
        }

        player.sendMessage("§aPour confirmer votre terrain faites /land {confirm} {nom de terrain}");
        player.sendMessage("§aPour annuler votre terrain faites /land {cancel}");
        return true;
    }
}
