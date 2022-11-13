package Command.Controller;

public interface LandParameter {

    boolean canSetInteract(String name, String value);

    boolean canSetMobSpawn(String name, String value);

    boolean canSetHitMob(String name, String value);

    boolean canSetHitAnimal(String name, String value);

    boolean canSetCrops(String name, String value);
}
