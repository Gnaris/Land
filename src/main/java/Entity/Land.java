package Entity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Land extends Region
{
    private Location spawnLocation;
    private final boolean isSafeZone;
    private boolean canInteract = false;
    private boolean monsterCanSpawn = true;
    private boolean canHitMonster = false;
    private boolean canHitAnimal = false;
    private boolean canCrops = false;
    private Location position1;
    private Location position2;

    public Land(UUID owner, String landName, boolean isSafeZone) {
        super(owner, landName);
        this.isSafeZone = isSafeZone;
    }

    public Land(UUID owner, String landName, Location minLocation, Location maxLocation, Location spawnLocation, boolean isSafeZone, boolean canInteract, boolean monsterCanSpawn, boolean canHitMonster, boolean canHitAnimal, boolean canCrops) {
        super(owner, landName, minLocation, maxLocation);
        this.spawnLocation = spawnLocation;
        this.isSafeZone = isSafeZone;
        this.canInteract = canInteract;
        this.monsterCanSpawn = monsterCanSpawn;
        this.canHitMonster = canHitMonster;
        this.canHitAnimal = canHitAnimal;
        this.canCrops = canCrops;
    }

    public boolean isSafeZone() {
        return isSafeZone;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public boolean monsterCanSpawn() {
        return monsterCanSpawn;
    }

    public boolean canHitMonster() {
        return canHitMonster;
    }

    public boolean canHitAnimal() {
        return canHitAnimal;
    }

    public boolean canCrops() {
        return canCrops;
    }

    public void setInteract(boolean value) {
        this.canInteract = value;
    }

    public void setMonsterSpawn(boolean value) {
        this.monsterCanSpawn = value;
    }

    public void setHitMonster(boolean value) {
        this.canHitMonster = value;
    }

    public void setHitAnimal(boolean value) {
        this.canHitAnimal = value;
    }

    public void setCrops(boolean value) {
        this.canCrops = value;
    }

    public Location getPosition1() {
        return position1;
    }

    public void setPosition1(Location position1) {
        this.position1 = position1;
    }

    public Location getPosition2() {
        return position2;
    }

    public void setPosition2(Location position2) {
        this.position2 = position2;
    }

    public void setMinLocation() {
        if(position1 == null || position2 == null)
        {
            minLocation = null;
            return;
        }
        this.minLocation = new Location(position1.getWorld(),
                (int) Math.min(this.position1.getX(), this.position2.getX()),
                (int) Math.min(this.position1.getY(), this.position2.getY()),
                (int) Math.min(this.position1.getZ(), this.position2.getZ())
        );
    }

    public void setMaxLocation() {
        if(position1 == null || position2 == null)
        {
            maxLocation = null;
            return;
        }
        this.maxLocation = new Location(position1.getWorld(),
                (int) Math.max(this.position1.getX(), this.position2.getX()),
                (int) Math.max(this.position1.getY(), this.position2.getY()),
                (int) Math.max(this.position1.getZ(), this.position2.getZ())
        );
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
