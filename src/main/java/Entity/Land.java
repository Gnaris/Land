package Entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Land extends Region
{
    private boolean isCity;
    private final boolean isSafeZone;
    private final LandSecurity canInteract = LandSecurity.INTERACT;
    private final LandSecurity monsterCanSpawn = LandSecurity.MONSTER_SPAWN;
    private final LandSecurity canHitMonster = LandSecurity.HIT_MONSTER;
    private final LandSecurity canHitAnimal = LandSecurity.HIT_ANIMAL;
    private final LandSecurity canCrops = LandSecurity.HIT_ANIMAL;
    private Location firstLocation;
    private Location secondLocation;

    public Land(UUID owner, String landName, boolean isSafeZone) {
        super(owner, landName);
        this.isSafeZone = isSafeZone;
    }

    public Land(UUID owner, String landName, Location minLocation, Location maxLocation, boolean isSafeZone, boolean isCity, boolean canInteract, boolean monsterCanSpawn, boolean canHitMonster, boolean canHitAnimal, boolean canCrops) {
        super(owner, landName, minLocation, maxLocation);
        this.isCity = isCity;
        this.isSafeZone = isSafeZone;
        this.canInteract.setValue(canInteract);
        this.monsterCanSpawn.setValue(monsterCanSpawn);
        this.canHitMonster.setValue(canHitMonster);
        this.canHitAnimal.setValue(canHitAnimal);
        this.canCrops.setValue(canCrops);
    }

    public boolean isCity() {
        return isCity;
    }

    public boolean isSafeZone() {
        return isSafeZone;
    }

    public boolean canInteract() {
        return canInteract.getValue();
    }

    public boolean monsterCanSpawn() {
        return monsterCanSpawn.getValue();
    }

    public boolean canHitMonster() {
        return canHitMonster.getValue();
    }

    public boolean canHitAnimal() {
        return canHitAnimal.getValue();
    }

    public boolean canCrops() {
        return canCrops.getValue();
    }

    public void setInteract(boolean value) {
        this.canInteract.setValue(value);
        Bukkit.getPlayer(this.owner).sendMessage("§cChangement d'état d'interaction avec les blocs en " + value);
    }

    public void setMonsterSpawn(boolean value) {
        this.monsterCanSpawn.setValue(value);
        Bukkit.getPlayer(this.owner).sendMessage("§cChangement d'état d'apparaition des monstres en " + value);
    }

    public void setHitMonster(boolean value) {
        this.canHitMonster.setValue(value);
        Bukkit.getPlayer(this.owner).sendMessage("§cChangement d'état d'hostilité contre les monstres en " + value);
    }

    public void setHitAnimal(boolean value) {
        this.canHitAnimal.setValue(value);
        Bukkit.getPlayer(this.owner).sendMessage("§cChangement d'état d'hostilité contre les animaux en " + value);
    }

    public void setCrops(boolean value) {
        this.canCrops.setValue(value);
        Bukkit.getPlayer(this.owner).sendMessage("§cChangement d'état sur la récolte en " + value);
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public void buildLandLocation()
    {
        lastMinLocation = this.minLocation;
        lastMaxLocation = this.maxLocation;
        this.minLocation = new Location(firstLocation.getWorld(),
                (int) Math.min(this.firstLocation.getX(), this.secondLocation.getX()),
                (int) Math.min(this.firstLocation.getY(), this.secondLocation.getY()),
                (int) Math.min(this.firstLocation.getZ(), this.secondLocation.getZ())
        );
        this.maxLocation = new Location(firstLocation.getWorld(),
                (int) Math.max(this.firstLocation.getX(), this.secondLocation.getX()),
                (int) Math.max(this.firstLocation.getY(), this.secondLocation.getY()),
                (int) Math.max(this.firstLocation.getZ(), this.secondLocation.getZ())
        );
    }
}
