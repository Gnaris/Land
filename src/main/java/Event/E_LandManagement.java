package Event;

import Entity.Land;
import SPLand.SPLand;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.stream.Collectors;

public class E_LandManagement implements Listener {

    private final SPLand plugin;

    public E_LandManagement(SPLand plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getLandList().stream().noneMatch(land -> land.isInArea(e.getBlock().getLocation()))) return;
        Land playerLand = plugin.getLandList().stream().filter(land -> land.isInArea(e.getBlock().getLocation())).collect(Collectors.toList()).get(0);
        if(playerLand.getOwner().toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString()) && !playerLand.isStaffClaim()) return;
        if(playerLand.getPlayerList().get(e.getPlayer().getUniqueId()) != null) return;
        if(e.getBlock().getBlockData() instanceof Ageable && playerLand.isCrops()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPut(BlockPlaceEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getLandList().stream().noneMatch(land -> land.isInArea(e.getBlock().getLocation()))) return;
        Land playerLand = plugin.getLandList().stream().filter(land -> land.isInArea(e.getBlock().getLocation())).collect(Collectors.toList()).get(0);
        if(playerLand.getOwner().toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString()) && !playerLand.isStaffClaim()) return;
        if(playerLand.getPlayerList().get(e.getPlayer().getUniqueId()) != null) return;
        if(e.getBlock().getBlockData() instanceof Ageable && playerLand.isCrops()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getLandList().stream().noneMatch(land -> land.isInArea(e.getClickedBlock().getLocation()))) return;
        Land playerLand = plugin.getLandList().stream().filter(land -> land.isInArea(e.getClickedBlock().getLocation())).collect(Collectors.toList()).get(0);
        if(playerLand.getOwner().toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString()) && !playerLand.isStaffClaim()) return;
        if(playerLand.getPlayerList().get(e.getPlayer().getUniqueId()) != null) return;
        if(e.getClickedBlock().getBlockData() instanceof Ageable && playerLand.isCrops()) return;
        if(playerLand.isInteract()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void killEntity(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player) if(e.getDamager().hasPermission("sperias.land.claim.modify") || e.getDamager().isOp()) return;
        if(plugin.getLandList().stream().noneMatch(land -> land.isInArea(e.getEntity().getLocation()))) return;
        Land playerLand = plugin.getLandList().stream().filter(land -> land.isInArea(e.getEntity().getLocation())).collect(Collectors.toList()).get(0);
        if(e.getDamager() instanceof Player && playerLand.getPlayerList().get(e.getDamager().getUniqueId()) != null) return;
        if(e.getEntity() instanceof Monster && playerLand.isHitMob()) return;
        if(e.getEntity() instanceof Animals && playerLand.isHitAnimal()) return;
        e.setCancelled(true);
        e.getDamager().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void spawningEntity(EntitySpawnEvent e)
    {
        if(!(e.getEntity() instanceof Monster)) return;
        if(plugin.getLandList().stream().noneMatch(land -> land.isInArea(e.getEntity().getLocation()))) return;
        Land playerLand = plugin.getLandList().stream().filter(land -> land.isInArea(e.getEntity().getLocation())).collect(Collectors.toList()).get(0);
        if(playerLand.isMobSpawn()) return;
        e.setCancelled(true);
    }
}
