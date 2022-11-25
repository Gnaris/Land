package Event.Controller;

import Controller.Controller;
import Entity.Land;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandClaimController extends Controller {

    public LandClaimController(Player player, LandMain plugin) {
        super(player, plugin);
    }

    protected boolean positionOnOtherLand(Location location)
    {
        if(plugin.getAllLand().stream().anyMatch(l -> l.isInRegion(location)))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Cette zone est déjà claim");
            return false;
        }
        return true;
    }

    public boolean canSetPosition1(Location position1)
    {
        return this.positionOnOtherLand(position1);
    }

    public boolean canSetPosition2(Land land, Location position2) {
        if(position2.getWorld() != land.getPosition1().getWorld())
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Les deux positions n'ont pas les mêmes monde");
            return false;
        }
        if(!positionOnOtherLand(position2)) return false;
        land.setPosition2(position2);
        land.setMinLocation();
        land.setMaxLocation();
        if(plugin.getAllLand().stream().anyMatch(l -> land.isInRegion(l.getMinLocation()) || land.isInRegion(l.getMaxLocation())))
        {
            player.sendMessage(ChatColor.of("#FF0000") + "Il y a déjà un claim dans votre zone");
            land.setPosition2(null);
            land.setMinLocation();
            land.setMaxLocation();
            return false;
        }
        return true;
    }
}
