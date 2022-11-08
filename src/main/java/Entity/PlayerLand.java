package Entity;

import java.util.UUID;

public class PlayerLand {

    private final int landID;
    private final UUID player;
    private boolean build;
    private boolean interact;
    private boolean killAnimals;

    public PlayerLand(int landID, String player, boolean build, boolean interact, boolean killAnimals) {
        this.landID = landID;
        this.player = UUID.fromString(player);
        this.build = build;
        this.interact = interact;
        this.killAnimals = killAnimals;
    }

    public int getLandID() {
        return landID;
    }
    public UUID getPlayer() {
        return player;
    }
    public boolean canBuild()
    {
        return build;
    }
    public boolean canInteract()
    {
        return interact;
    }
    public boolean canKillAnimals()
    {
        return killAnimals;
    }
    public void setBuild(boolean build) {
        this.build = build;
    }
    public void setInteract(boolean interact) {
        this.interact = interact;
    }
    public void setKillAnimals(boolean killAnimals) {
        this.killAnimals = killAnimals;
    }
}
