package Event;

import Event.Controller.LandManagementController;
import LandStore.LandStore;
import SPLand.SPLand;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class E_LandManagement implements Listener {

    private LandManagementController landController;

    @EventHandler
    public void onBreak(BlockBreakEvent e)
    {
        landController = new LandManagementController(e.getPlayer());
        if(!landController.canBreakPutBlock(e.getBlock().getLocation()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPut(BlockPlaceEvent e)
    {
        landController = new LandManagementController(e.getPlayer());
        if(!landController.canBreakPutBlock(e.getBlock().getLocation()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getClickedBlock() == null) return;
        landController = new LandManagementController(e.getPlayer());
        if(!landController.canInteractBloc(e.getClickedBlock().getLocation()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void killEntity(EntityDamageByEntityEvent e)
    {
        if(!(e.getEntity() instanceof Animals)) return;
        if(!(e.getDamager() instanceof Player)) return;
        landController = new LandManagementController((Player) e.getDamager());
        if(!landController.canKillAnimals(e.getEntity().getLocation()))
        {
            e.setCancelled(true);
        }
    }
}
