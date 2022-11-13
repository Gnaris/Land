package Entity;

import SPLand.SPLand;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Land extends Area implements Runnable{

    private int landID;
    private String landName;
    private final Map<UUID, PlayerLand> playerList = new HashMap<>();
    private int schedulerID = 0;

    private boolean staffClaim = false;

    private boolean confirmed = false;
    private boolean interact = false;
    private boolean mobSpawn = true;
    private boolean hitMob = false;
    private boolean hitAnimal = false;
    private boolean crops = false;

    public Land(UUID uuid)
    {
        super(uuid);
    }

    public Land(String owner, Location firstLocation, Location secondLocation, SPLand plugin, int landID, String landName, boolean confirmed, boolean interact, boolean mobSpawn, boolean hitMob, boolean hitAnimal, boolean crops, boolean staffClaim) {
        super(owner, firstLocation, secondLocation, plugin);
        this.landID = landID;
        this.landName = landName;
        this.confirmed = confirmed;
        this.interact = interact;
        this.mobSpawn = mobSpawn;
        this.hitMob = hitMob;
        this.hitAnimal = hitAnimal;
        this.crops = crops;
        this.staffClaim = staffClaim;
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
            plugin.getLandList().remove(this);
            Objects.requireNonNull(Bukkit.getPlayer(this.owner)).sendMessage("§cVous n'avez pas pu confirmer votre création de terrain");
        }
    }

    public int getLandID() {
        return landID;
    }
    public void setLandID(int landID) {
        this.landID = landID;
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
    public String getLandName() {
        return landName;
    }
    public boolean isInteract() {
        return interact;
    }
    public void setInteract(boolean interact) {
        this.interact = interact;
    }
    public Map<UUID, PlayerLand> getPlayerList() {
        return playerList;
    }
    public void setLandName(String landName) {
        this.landName = landName;
    }
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    public boolean isStaffClaim() {
        return staffClaim;
    }
    public void setStaffClaim(boolean staffClaim) {
        this.staffClaim = staffClaim;
    }
    public boolean isMobSpawn() {
        return mobSpawn;
    }
    public void setMobSpawn(boolean mobSpawn) {
        this.mobSpawn = mobSpawn;
    }
    public boolean isHitMob() {
        return hitMob;
    }
    public void setHitMob(boolean hitMob) {
        this.hitMob = hitMob;
    }
    public boolean isHitAnimal() {
        return hitAnimal;
    }
    public void setHitAnimal(boolean hitAnimal) {
        this.hitAnimal = hitAnimal;
    }
    public boolean isCrops() {
        return crops;
    }
    public void setCrops(boolean crops) {
        this.crops = crops;
    }
}
