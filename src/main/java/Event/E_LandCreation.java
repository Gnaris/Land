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

public class E_LandCreation implements Listener {

    private Land land;
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
        if(!landController.canClaimLandOnThisPosition(e.getClickedBlock().getLocation())) return;
        if(!landController.isInLandCreation()) return;

        land = land == null ? new Land(e.getPlayer().getUniqueId()) : land;
        if(land.getFirstLocation() == null)
        {
            land.setFirstLocation(e.getClickedBlock().getLocation());
            land.setSchedulerID(Bukkit.getScheduler().runTaskLater(SPLand.getInstance(), land, 20 * 60 * 5).getTaskId());
            e.getPlayer().sendMessage("§eVous avez 5 minutes pour compléter votre terrain");
        }
        else
        {
            land.setSecondLocation(e.getClickedBlock().getLocation());
            if(!landController.canClaimLand(land)) return;
            landStore.getLandList().add(land);
            land.showArea();
            land = null;
        }

        Bukkit.getScheduler().runTaskLater(SPLand.getInstance(), () -> e.getPlayer().sendBlockChange(e.getClickedBlock().getLocation(), Material.OCHRE_FROGLIGHT.createBlockData()), 0);
    }
}
