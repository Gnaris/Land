package Event;

import SPLand.SPLand;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class E_InitializePlayer implements Listener {

    public void onJoin(PlayerJoinEvent e)
    {
        if(SPLand.getInstance().getPlayerStore().getPlayerList().get(e.getPlayer().getUniqueId()) != null) return;

    }
}
