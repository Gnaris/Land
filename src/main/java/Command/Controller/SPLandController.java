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
        if(plugin.getSafeLands().size() > 0)
        {
            if(plugin.getSafeLands().containsKey(landName))
            {
                player.sendMessage("§cLe nom de ce terrain est déjà utilisée");
                return false;
            }
        }
        return true;
    }

    public boolean canDeleteCity(String landName)
    {
        if(!this.hasSafeLand(landName)) return false;
        landModel.deleteSafeLand(landName);
        return true;
    }

    public boolean canSetFirstLocationOnCity(String landName, Location firstLocation)
    {
        if(!this.hasSafeLand(landName)) return false;
        return this.positionOnOtherLand(landName, firstLocation);
    }

    public boolean canSetSecondLocationOnCity(String landName, Location secondLocation)
    {
        if(!this.hasSafeLand(landName)) return false;
        Land land = plugin.getSafeLands().get(landName);
        if(land.getFirstLocation() == null)
        {
            player.sendMessage("§cPremière position inexistant");
            return false;
        }
        if(secondLocation.getWorld() != land.getFirstLocation().getWorld())
        {
            player.sendMessage("§cLes deux positions n'ont pas les mêmes monde");
            return false;
        }
        return this.positionOnOtherLand(landName, secondLocation);
    }

    public boolean canConfirmCity(String landName)
    {
        if(!this.hasSafeLand(landName)) return false;
        Land land = plugin.getSafeLands().get(landName);
        if(land.getFirstLocation() == null || land.getSecondLocation() == null)
        {
            player.sendMessage("§cVous n'avez pas sauvegarder tout les positions de la ville");
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

        land.setFirstLocation(null);
        land.setSecondLocation(null);
        landModel.createLand(null, land.getRegionName(), land.getMinLocation(), land.getMaxLocation(), true);
        return true;
    }
}
