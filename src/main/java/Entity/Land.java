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
    private Location position1;
    private Location position2;

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

    public void buildLandLocation()
    {
        lastMinLocation = this.minLocation;
        lastMaxLocation = this.maxLocation;
        this.minLocation = new Location(position1.getWorld(),
                (int) Math.min(this.position1.getX(), this.position2.getX()),
                (int) Math.min(this.position1.getY(), this.position2.getY()),
                (int) Math.min(this.position1.getZ(), this.position2.getZ())
        );
        this.maxLocation = new Location(position1.getWorld(),
                (int) Math.max(this.position1.getX(), this.position2.getX()),
                (int) Math.max(this.position1.getY(), this.position2.getY()),
                (int) Math.max(this.position1.getZ(), this.position2.getZ())
        );
    }
}
