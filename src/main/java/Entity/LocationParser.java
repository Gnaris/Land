package Entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationParser {

    private double x;
    private double y;
    private double z;

    public LocationParser(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation(String world)
    {
        return new Location(Bukkit.getWorld(world), this.x, this.y, this.z);
    }
}
