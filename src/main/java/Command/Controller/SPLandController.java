package Command.Controller;

import Controller.Controller;
import Entity.Land;
import Land.LandMain;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SPLandController extends Controller {

    public SPLandController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    public boolean canCreateLand(String landName)
    {
        if(this.hasLandProgress())
        {
            player.sendMessage("§cVous possédez déjà un terrain en cours de progression");
            return false;
        }
        if(plugin.getSafeLands().containsKey(landName))
        {
            player.sendMessage("§cLe nom de ce terrain est déjà utilisé");
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

    public boolean canConfirmLand(String landName)
    {
        if(!this.hasLandProgress()) return false;
        Land land = plugin.getLandProgress().get(player.getUniqueId());
        if(land.getPosition1() == null || land.getPosition2() == null)
        {
            player.sendMessage("§cVous n'avez pas sauvegarder tout les positions de la ville");
            return false;
        }

        land.buildLandLocation();
        landModel.createLand(land);
        return true;
    }
}
