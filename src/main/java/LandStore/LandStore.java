package LandStore;

import Entity.Land;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LandStore {

    private final List<Land> landList = new ArrayList<>();

    public List<Land> getLandList() {
        return landList;
    }

    public List<Land> getPlayerLandList(Player player)
    {
        return landList.stream().filter(land -> land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())).collect(Collectors.toList());
    }

    public List<Land> getPlayerLandNotConfirmed(Player player)
    {
        return landList.stream().filter(land -> !land.isConfirmed() && land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())).collect(Collectors.toList());
    }

    public Land getPlayerLandByName(Player player, String name)
    {
        List<Land> PlayerLand = landList.stream().filter(land -> land.getName() != null && land.getName().equalsIgnoreCase(name) && land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())).collect(Collectors.toList());
        if(PlayerLand.size() > 0)
        {
            return PlayerLand.get(0);
        }
        return null;
    }
}
