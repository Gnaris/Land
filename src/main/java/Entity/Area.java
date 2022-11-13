package Entity;

import SPLand.SPLand;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class Area {

    protected UUID owner;
    protected Location firstLocation;
    protected Location secondLocation;

    protected Location thirdLocation;
    protected Location fourthLocation;
    protected Location minLocation;
    protected Location maxLocation;

    protected SPLand plugin;

    public Area(UUID owner) {
        this.owner = owner;
    }

    public Area(String owner, Location firstLocation, Location secondLocation, SPLand plugin)
    {
        this.owner = UUID.fromString(owner);
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.plugin = plugin;
        initializeMinMaxLocation();
        initializeThirdFourthLocation();
    }
    public boolean isInArea(Location location)
    {
        return (location.getX() >= minLocation.getX() && location.getX() <= maxLocation.getX() && location.getZ() >= minLocation.getZ() && location.getZ() <= maxLocation.getZ());
    }
    public void showArea()
    {
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), firstLocation.getX(), firstLocation.getY(), firstLocation.getZ()), Material.OCHRE_FROGLIGHT.createBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), secondLocation.getX(), secondLocation.getY(), secondLocation.getZ()), Material.OCHRE_FROGLIGHT.createBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), thirdLocation.getX(), thirdLocation.getY(), thirdLocation.getZ()), Material.VERDANT_FROGLIGHT.createBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), fourthLocation.getX(), fourthLocation.getY(), fourthLocation.getZ()), Material.PEARLESCENT_FROGLIGHT.createBlockData());
    }
    public void hideArea()
    {
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), firstLocation.getX(), firstLocation.getY(), firstLocation.getZ()), firstLocation.getBlock().getBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), secondLocation.getX(), secondLocation.getY(), secondLocation.getZ()), secondLocation.getBlock().getBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), thirdLocation.getX(), thirdLocation.getY(), thirdLocation.getZ()), thirdLocation.getBlock().getBlockData());
        Bukkit.getPlayer(owner).sendBlockChange(new Location(firstLocation.getWorld(), fourthLocation.getX(), fourthLocation.getY(), fourthLocation.getZ()), fourthLocation.getBlock().getBlockData());
    }
    protected void initializeMinMaxLocation()
    {
        minLocation = new Location(firstLocation.getWorld(),
                (int)Math.min(firstLocation.getX(), secondLocation.getX()),
                (int)Math.min(firstLocation.getY(), secondLocation.getY()),
                (int)Math.min(firstLocation.getZ(), secondLocation.getZ())
        );
        maxLocation = new Location(firstLocation.getWorld(),
                (int)Math.max(firstLocation.getX(), secondLocation.getX()),
                (int)Math.max(firstLocation.getY(), secondLocation.getY()),
                (int)Math.max(firstLocation.getZ(), secondLocation.getZ())
        );
    }
    public void initializeThirdFourthLocation()
    {
        thirdLocation = new Location(firstLocation.getWorld(), secondLocation.getX(), secondLocation.getY(), firstLocation.getZ());
        fourthLocation = new Location(firstLocation.getWorld(), firstLocation.getX(), secondLocation.getY(), secondLocation.getZ());
    }

    public int getAirOfClaim()
    {
        return (int) (((maxLocation.getX() - minLocation.getX()) + 1) * ((maxLocation.getZ() - minLocation.getZ()) + 1));
    }

    public UUID getOwner() {
        return owner;
    }
    public Location getFirstLocation() {
        return firstLocation;
    }
    public Location getSecondLocation() {
        return secondLocation;
    }
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
        if(secondLocation != null) initializeMinMaxLocation();
    }
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
        initializeThirdFourthLocation();
        initializeMinMaxLocation();
    }
    public Location getMinLocation() {
        return minLocation;
    }
    public Location getMaxLocation() {
        return maxLocation;
    }
}
