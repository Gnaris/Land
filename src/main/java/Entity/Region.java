package Entity;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Region{

    protected UUID owner;
    protected String regionName;
    protected List<UUID> members = new ArrayList<>();
    protected Location minLocation;
    protected Location maxLocation;
    protected Location lastMinLocation;
    protected Location lastMaxLocation;

    public Region(UUID owner, String regionName) {
        this.owner = owner;
        this.regionName = regionName;
    }

    public Region(UUID owner, String regionName, Location minLocation, Location maxLocation)
    {
        this.owner = owner;
        this.regionName = regionName;
        this.minLocation = minLocation;
        this.maxLocation = maxLocation;
    }
    public boolean isInRegion(Location location)
    {
        return ((int)location.getX() >= minLocation.getX() && (int)location.getX() <= maxLocation.getX() && (int)location.getZ() >= minLocation.getZ() && (int)location.getZ() <= maxLocation.getZ());
    }
    public void showArea()
    {
        // TODO A REFAIRE
    }
    public void hideArea()
    {
        // TODO A REFAIRE
    }

    public long getArea()
    {
        if(minLocation != null && maxLocation != null)
        {
            return (long) (((maxLocation.getX() - minLocation.getX()) + 1) * ((maxLocation.getZ() - minLocation.getZ()) + 1));
        }
        return 0;
    }

    public UUID getOwner() {
        return owner;
    }
    public Location getMinLocation() {
        return minLocation;
    }
    public Location getMaxLocation() {
        return maxLocation;
    }

    public void setMinLocation(Location minLocation) {
        this.minLocation = minLocation;
    }

    public void setMaxLocation(Location maxLocation) {
        this.maxLocation = maxLocation;
    }

    public Location getLastMinLocation() {
        return lastMinLocation;
    }

    public Location getLastMaxLocation() {
        return lastMaxLocation;
    }
    public String getRegionName() {
        return regionName;
    }

    public List<UUID> getMembers() {
        return members;
    }
}
