package Entity;

import SPLand.SPLand;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Area {

    protected int id;
    protected UUID owner;
    protected Player ownerPlayer;
    protected World world;
    protected Location firstLocation;
    protected Location secondLocation;
    protected Location thirdLocation;
    protected Location fourthLocation;

    protected Location minLocation;
    protected Location maxLocation;

    public Area(UUID owner) {
        this.owner = owner;
        this.ownerPlayer = Bukkit.getPlayer(owner);
        this.world = ownerPlayer.getWorld();
    }

    public Area(int id, String owner, String world, Location firstLocation, Location secondLocation)
    {
        this.id = id;
        this.owner = UUID.fromString(owner);
        this.world = Bukkit.getWorld(world);
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        SerializeMinMaxLocation();
        initializeThirdFourthLocation();
    }
    public boolean isInArea(Location location)
    {
        return (location.getX() >= minLocation.getX() && location.getX() <= maxLocation.getX() && location.getZ() >= minLocation.getZ() && location.getZ() <= maxLocation.getZ());
    }
    public void showArea()
    {
        ownerPlayer = ownerPlayer == null ? Bukkit.getPlayer(owner) : ownerPlayer;
        Bukkit.getScheduler().runTaskLater(SPLand.getInstance(), ()->{
            ownerPlayer.sendBlockChange(new Location(world, firstLocation.getX(), firstLocation.getY(), firstLocation.getZ()), Material.OCHRE_FROGLIGHT.createBlockData());
            ownerPlayer.sendBlockChange(new Location(world, secondLocation.getX(), secondLocation.getY(), secondLocation.getZ()), Material.OCHRE_FROGLIGHT.createBlockData());
            ownerPlayer.sendBlockChange(new Location(world, thirdLocation.getX(), thirdLocation.getY(), thirdLocation.getZ()), Material.VERDANT_FROGLIGHT.createBlockData());
            ownerPlayer.sendBlockChange(new Location(world, fourthLocation.getX(), fourthLocation.getY(), fourthLocation.getZ()), Material.PEARLESCENT_FROGLIGHT.createBlockData());
        }, 0);
    }
    public void hideArea()
    {
        ownerPlayer = ownerPlayer == null ? Bukkit.getPlayer(owner) : ownerPlayer;
        ownerPlayer.sendBlockChange(new Location(world, firstLocation.getX(), firstLocation.getY(), firstLocation.getZ()), firstLocation.getBlock().getBlockData());
        ownerPlayer.sendBlockChange(new Location(world, secondLocation.getX(), secondLocation.getY(), secondLocation.getZ()), secondLocation.getBlock().getBlockData());
        ownerPlayer.sendBlockChange(new Location(world, thirdLocation.getX(), thirdLocation.getY(), thirdLocation.getZ()), thirdLocation.getBlock().getBlockData());
        ownerPlayer.sendBlockChange(new Location(world, fourthLocation.getX(), fourthLocation.getY(), fourthLocation.getZ()), fourthLocation.getBlock().getBlockData());
    }
    protected void SerializeMinMaxLocation()
    {
        minLocation = new Location(this.world,
                (int)Math.min(firstLocation.getX(), secondLocation.getX()),
                (int)Math.min(firstLocation.getY(), secondLocation.getY()),
                (int)Math.min(firstLocation.getZ(), secondLocation.getZ())
        );
        maxLocation = new Location(this.world,
                (int)Math.max(firstLocation.getX(), secondLocation.getX()),
                (int)Math.max(firstLocation.getY(), secondLocation.getY()),
                (int)Math.max(firstLocation.getZ(), secondLocation.getZ())
        );
    }
    public void initializeThirdFourthLocation()
    {
        thirdLocation = new Location(world, secondLocation.getX(), secondLocation.getY(), firstLocation.getZ());
        fourthLocation = new Location(world, firstLocation.getX(), secondLocation.getY(), secondLocation.getZ());
    }

    public String locationToJson(Location location)
    {
        LocationParser locationParser = new LocationParser(location.getX(), location.getY(), location.getZ());
        Gson gson = new Gson();
        return gson.toJson(locationParser);
    }

    public int getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    public Location getFirstLocation() {
        return firstLocation;
    }
    public Location getSecondLocation() {
        return secondLocation;
    }
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
        if(secondLocation != null) SerializeMinMaxLocation();
    }
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
        initializeThirdFourthLocation();
        SerializeMinMaxLocation();
    }
    public Location getMinLocation() {
        return minLocation;
    }
    public Location getMaxLocation() {
        return maxLocation;
    }
    public World getWorld() {
        return world;
    }
}
