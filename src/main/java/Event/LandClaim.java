package Event;

import Economy.EconomyPlugin;
import Entity.Land;
import Event.Controller.LandClaimController;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LandClaim implements Listener {

    private final LandMain plugin;

    public LandClaim(LandMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerClaimEvent(PlayerInteractEvent e)
    {
        if(e.useInteractedBlock().equals(Event.Result.DENY) && e.useItemInHand().equals(Event.Result.DENY))
        {
            e.setCancelled(true);
            return;
        }
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock() == null) return;
        if(e.getItem() == null) return;
        if(!e.getItem().hasItemMeta() && !e.getItem().getItemMeta().hasDisplayName()) return;
        if(!e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#85CE51") + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Bâton de claim")) return;
        if(!plugin.getLandProgress().containsKey(e.getPlayer().getUniqueId()))
        {
            e.getPlayer().sendMessage(ChatColor.of("#FF0000") + "Vous n'avez pas de claim en cours !");
            e.setCancelled(true);
            return;
        }

        LandClaimController landClaimController = new LandClaimController(e.getPlayer(), plugin);
        Land land = plugin.getLandProgress().get(e.getPlayer().getUniqueId());

        if(land.getPosition1() == null && landClaimController.canSetPosition1(e.getClickedBlock().getLocation()))
        {
            land.setPosition1(e.getClickedBlock().getLocation());
            e.getPlayer().sendMessage(ChatColor.of("#00FF00") + "Première position sauvegardée ! Veuillez maintenant selectionner la dernière position du claim");
            return;
        }
        if(land.getPosition1() != null && landClaimController.canSetPosition2(land, e.getClickedBlock().getLocation()))
        {
            EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
            e.getPlayer().sendMessage(ChatColor.of("#00FF00") + "Coût de votre région : " + ChatColor.of("#FFFF00") + (land.getArea() * plugin.getClaimPrice()) + economyPlugin.getCurrency() +  ChatColor.of("#00FF00") + " Pour confirmer, faites /land confirm " + land.getRegionName());
        }
        e.setCancelled(true);
    }
}
