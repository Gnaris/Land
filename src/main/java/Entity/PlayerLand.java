package Entity;

import java.util.UUID;

public class PlayerLand {

    private final int landID;
    private final UUID player;

    public PlayerLand(int landID, String player) {
        this.landID = landID;
        this.player = UUID.fromString(player);
    }

    public int getLandID() {
        return landID;
    }
    public UUID getPlayer() {
        return player;
    }
}
