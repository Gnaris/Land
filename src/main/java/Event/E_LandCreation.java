package Event;

import Entity.Land;
import Event.Controller.LandCreationController;
import LandStore.LandStore;
import SPLand.SPLand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class E_LandCreation implements Listener {

    private Map<UUID, Land> land = new HashMap<>();
    private final LandStore landStore = SPLand.getInstance().getLandStore();

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getItem() == null || e.getClickedBlock() == null) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getItem().getType() != Material.STICK) return;;
        if(!e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eBâton de terrain")) return;

        // Normal Claim Mode
        LandCreationController landController = new LandCreationController(e.getPlayer());
        if(!landController.canStartToClaim(e.getClickedBlock().getLocation())) return;

        if(land.get(e.getPlayer().getUniqueId()) == null)
        {
            land.put(e.getPlayer().getUniqueId(), new Land(e.getPlayer().getUniqueId()));
        }
        Land playerLand = land.get(e.getPlayer().getUniqueId());
        if(playerLand.getFirstLocation() == null)
        {
            playerLand.setFirstLocation(e.getClickedBlock().getLocation());
            playerLand.setSchedulerID(Bukkit.getScheduler().runTaskLater(SPLand.getInstance(), playerLand, 20 * 60 * 5).getTaskId());
            e.getPlayer().sendMessage("§eVous avez 5 minutes pour compléter votre terrain");
        }
        else
        {
            playerLand.setSecondLocation(e.getClickedBlock().getLocation());
            if(!landController.canClaimLand(playerLand)) return;
            landStore.getLandList().add(playerLand);
            playerLand.showArea();
            land.remove(e.getPlayer().getUniqueId());
        }

        Bukkit.getScheduler().runTaskLater(SPLand.getInstance(), () -> e.getPlayer().sendBlockChange(e.getClickedBlock().getLocation(), Material.OCHRE_FROGLIGHT.createBlockData()), 0);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        land.remove(e.getPlayer().getUniqueId());
    }
}
