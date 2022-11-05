package Entity;

import SPLand.SPLand;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Land extends Area implements Runnable{

    private String name;
    private final Map<UUID, PlayerLand> playerList = new HashMap<>();
    private int schedulerID = 0;
    private boolean confirmed = false;
    private boolean openSomething = false;

    public Land(UUID uuid)
    {
        super(uuid);
    }

    @Override
    public void run() {
        if(firstLocation == null || secondLocation == null)
        {
            hideArea();
            firstLocation = null;
            secondLocation = null;
            Objects.requireNonNull(Bukkit.getPlayer(this.owner)).sendMessage("§cVous n'avez pas selectionner tout les points");
        }
        if(secondLocation != null && !confirmed)
        {
            hideArea();
            SPLand.getInstance().getLandStore().getLandList().remove(this);
            Objects.requireNonNull(Bukkit.getPlayer(this.owner)).sendMessage("§cVous n'avez pas pu confirmer votre création de terrain");
        }
    }

    public void confirmLand(String name)
    {
        confirmed = true;
        this.name = name;
    }

    public void cancelLand()
    {
        hideArea();
        SPLand.getInstance().getLandStore().getLandList().remove(this);
    }
    public int getAirOfClaim()
    {
        return (int) (((maxLocation.getX() - minLocation.getX()) + 1) * ((maxLocation.getZ() - minLocation.getZ()) + 1));
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    public void setSchedulerID(int schedulerID) {
        this.schedulerID = schedulerID;
    }
    public int getSchedulerID() {
        return schedulerID;
    }
    public String getName() {
        return name;
    }
    public boolean canOpenSomething() {
        return openSomething;
    }
    public void setOpenSomething(boolean openSomething) {
        this.openSomething = openSomething;
    }
    public Map<UUID, PlayerLand> getPlayerList() {
        return playerList;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
