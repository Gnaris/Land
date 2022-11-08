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
        if(SPLand.getInstance().getPlayerStore().getPlayerList().get(e.getPlayer().getUniqueId()) != null) return;

        LandModel landModel = new LandModel();
        SPLand.getInstance().getPlayerStore().getPlayerList().put(e.getPlayer().getUniqueId(), landModel.getPlayerClaim(e.getPlayer()));
    }
}
