package Entity;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationParser {

    private String world;
    private double x;
    private double y;
    private double z;

    public LocationParser(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation()
    {
        return new Location(Bukkit.getWorld(world), this.x, this.y, this.z);
    }
    public String locationToJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
