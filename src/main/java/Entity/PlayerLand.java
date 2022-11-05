package Entity;

import org.bukkit.entity.Player;

public class PlayerLand {

    private final Player player;
    private boolean build;
    private boolean open;
    private boolean killAnimals;

    public PlayerLand(Player player, boolean build, boolean open, boolean killAnimals) {
        this.player = player;
        this.build = build;
        this.open = open;
        this.killAnimals = killAnimals;
    }

    public Player getPlayer() {
        return player;
    }
    public boolean canBuild()
    {
        return build;
    }
    public boolean canOpen()
    {
        return open;
    }
    public boolean canKillAnimals()
    {
        return killAnimals;
    }
    public void setBuild(boolean build) {
        this.build = build;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }
    public void setKillAnimals(boolean killAnimals) {
        this.killAnimals = killAnimals;
    }
}
