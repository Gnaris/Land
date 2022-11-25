package Command.Controller;

import Controller.Controller;
import Entity.Land;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class SPLandController extends Controller {

    public SPLandController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    public boolean canCreateLand(String landName)
    {
        if(this.hasLandProgress())
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous possédez déjà un terrain en cours de progression");
            return false;
        }
        if(plugin.getSafeLands().containsKey(landName))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Le nom de ce terrain est déjà utilisé");
            return false;
        }
        return true;
    }

    public boolean canDeleteLand(String landName)
    {
        if(!this.hasSafeLand(landName)) return false;
        landModel.deleteSafeLand(landName);
        return true;
    }

    public boolean canConfirmLand()
    {
        if(!this.hasLandProgress()) return false;
        Land land = plugin.getLandProgress().get(player.getUniqueId());
        if(land.getPosition1() == null || land.getPosition2() == null)
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas sauvegarder tout les positions de la ville");
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
}
