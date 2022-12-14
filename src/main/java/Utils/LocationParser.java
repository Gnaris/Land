package Utils;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationParser {

    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public LocationParser(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location toLocation()
    {
        return new Location(Bukkit.getWorld(world), this.x, this.y, this.z);
    }
    public String ToJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
