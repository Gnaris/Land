package Controller;

import Entity.PlayerClaim;
import LandStore.LandStore;
import LandStore.PlayerClaimStore;
import Model.LandModel;
import SPLand.SPLand;
import org.bukkit.entity.Player;

public class Controller{

    protected Player player;
    protected PlayerClaim playerClaim;
    protected LandStore landStore = SPLand.getInstance().getLandStore();
    protected PlayerClaimStore playerStore = SPLand.getInstance().getPlayerClaimStore();
    protected LandModel landModel = new LandModel();

    public Controller(Player player) {
        this.player = player;
        playerClaim = playerStore.getPlayerClaimList().get(player.getUniqueId());
    }
}
