package Event;

import Model.LandModel;
import SPLand.SPLand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class E_InitializePlayer implements Listener {

    private SPLand plugin;

    public E_InitializePlayer(SPLand plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) throws SQLException, ClassNotFoundException {
        if(plugin.getPlayerClaimList().get(e.getPlayer().getUniqueId()) != null) return;

        plugin.getPlayerClaimList().put(e.getPlayer().getUniqueId(), new LandModel().getPlayerClaim(e.getPlayer()));
    }
}
