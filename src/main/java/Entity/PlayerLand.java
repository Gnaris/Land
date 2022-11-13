package Entity;

import java.util.UUID;

public class PlayerLand {

    private final int playerID;
    private final int landID;
    private final UUID player;

    public PlayerLand(int playerID, int landID, String player) {
        this.playerID = playerID;
        this.landID = landID;
        this.player = UUID.fromString(player);
    }

    public int getPlayerID() {
        return playerID;
    }
    public int getLandID() {
        return landID;
    }
    public UUID getPlayer() {
        return player;
    }
}
