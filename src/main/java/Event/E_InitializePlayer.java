package Event;

import Model.LandModel;
import SPLand.SPLand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class E_InitializePlayer implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) throws SQLException, ClassNotFoundException {
        if(SPLand.getInstance().getPlayerClaimStore().getPlayerClaimList().get(e.getPlayer().getUniqueId()) != null) return;

        SPLand.getInstance().getPlayerClaimStore().getPlayerClaimList().put(e.getPlayer().getUniqueId(), new LandModel().getPlayerClaim(e.getPlayer()));
    }
}
