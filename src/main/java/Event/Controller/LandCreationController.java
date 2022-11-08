package Event.Controller;

import Controller.Controller;
import Entity.Land;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandCreationController extends Controller {

    public LandCreationController(Player player) {
        super(player);
    }

    public boolean isInLandCreation()
    {
        if(landStore.getPlayerLandNotConfirmed(player).size() > 0)
        {
            player.sendMessage("§cVous avez déjà un Land en cours ! Faites /land cancel pour l'annuler");
            return false;
        }
        return true;
    }

    public boolean canClaimLandOnThisPosition(Location location)
    {
        if(landStore.getLandList().size() != 0 && landStore.getLandList().stream().anyMatch(land -> land.isInArea(location)))
        {
            player.sendMessage("§cVous ne pouvez pas claim une zone déjà claim");
            return false;
        }
        if(landStore.getLandList().stream().anyMatch(land -> land.getMinLocation().distance(location) < 25 || land.getMaxLocation().distance(location) < 25))
        {
            player.sendMessage("§cVous êtes trop proche d'un terrain, veuillez vous éloigner");
            return false;
        }
        return true;
    }

    public boolean canClaimLand(Land newLand)
    {
        if((newLand.getMaxLocation().getX() - newLand.getMinLocation().getX()) <= 5 || (newLand.getMaxLocation().getZ() - newLand.getMinLocation().getZ()) <= 5)
        {
            player.sendMessage("§cVotre terrain est trop petit");
            return false;
        }

        player.sendMessage("§aPour confirmer votre terrain faites /land {confirm} {nom de terrain}");
        player.sendMessage("§aPour annuler votre terrain faites /land {cancel}");
        return true;
    }
}
