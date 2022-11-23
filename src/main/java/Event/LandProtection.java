package Event;

import Entity.Land;
import LandMain.LandMain;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Animals;
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

public class LandProtection implements Listener {

    private final LandMain plugin;

    public LandProtection(LandMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getAllLand().stream().noneMatch(c -> c.isInRegion(e.getBlock().getLocation()))) return;
        Land land = plugin.getAllLand().stream().filter(c -> c.isInRegion(e.getBlock().getLocation())).findFirst().orElse(null);
        if(land == null) return;
        if(land.getOwner().equals(e.getPlayer().getUniqueId()) && !land.isSafeZone()) return;
        if(land.getMembers().contains(e.getPlayer().getUniqueId())) return;
        if(e.getBlock().getBlockData() instanceof Ageable && land.canCrops()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPut(BlockPlaceEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getAllLand().stream().noneMatch(c -> c.isInRegion(e.getBlock().getLocation()))) return;
        Land land = plugin.getAllLand().stream().filter(c -> c.isInRegion(e.getBlock().getLocation())).findFirst().orElse(null);
        if(land == null) return;
        if(land.getOwner().equals(e.getPlayer().getUniqueId()) && !land.isSafeZone()) return;
        if(land.getMembers().contains(e.getPlayer().getUniqueId())) return;
        if(e.getBlock().getBlockData() instanceof Ageable && land.canCrops()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getPlayer().hasPermission("sperias.land.claim.modify") || e.getPlayer().isOp()) return;
        if(plugin.getAllLand().stream().noneMatch(c -> c.isInRegion(e.getClickedBlock().getLocation()))) return;
        Land land = plugin.getAllLand().stream().filter(l -> l.isInRegion(e.getClickedBlock().getLocation())).findFirst().orElse(null);
        if(land == null) return;
        if(land.getOwner().equals(e.getPlayer().getUniqueId()) && !land.isSafeZone()) return;
        if(land.getMembers().contains(e.getPlayer().getUniqueId())) return;
        if(e.getClickedBlock().getBlockData() instanceof Ageable && land.canCrops()) return;
        if(land.canInteract()) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void killEntity(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player && (e.getDamager().hasPermission("sperias.land.claim.modify") || e.getDamager().isOp())) return;
        if(plugin.getAllLand().stream().noneMatch(land -> land.isInRegion(e.getEntity().getLocation()))) return;
        Land land = plugin.getAllLand().stream().filter(l -> l.isInRegion(e.getEntity().getLocation())).collect(Collectors.toList()).stream().findFirst().orElse(null);
        if (land == null) return;
        if(e.getDamager() instanceof Player && land.getMembers().contains(e.getEntity().getUniqueId())) return;
        if(e.getDamager() instanceof Monster && e.getEntity() instanceof Player && land.canHitMonster()) return;
        if(e.getEntity() instanceof Monster && land.canHitMonster()) return;
        if(e.getEntity() instanceof Animals && land.canHitAnimal()) return;

        e.setCancelled(true);
        e.getDamager().sendMessage("§cVous n'avez pas la permission");
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void spawningEntity(EntitySpawnEvent e)
    {
        if(!(e.getEntity() instanceof Monster)) return;
        if(plugin.getAllLand().stream().noneMatch(land -> land.isInRegion(e.getEntity().getLocation()))) return;
        Land land = plugin.getAllLand().stream().filter(l -> l.isInRegion(e.getEntity().getLocation())).findFirst().orElse(null);
        if(land == null) return;
        if(land.monsterCanSpawn()) return;
        e.setCancelled(true);
    }
}
