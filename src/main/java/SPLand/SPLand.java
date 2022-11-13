package SPLand;

import Command.CMD_land;
import Command.CMD_spland;
import Entity.Land;
import Entity.PlayerClaim;
import Event.E_InitializePlayer;
import Event.E_LandCreation;
import Event.E_LandManagement;
import Model.LandModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public final class SPLand extends JavaPlugin {

    private final List<Land> landList = new ArrayList<>();
    private final Map<UUID, PlayerClaim> playerClaimList = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Objects.requireNonNull(getCommand("land")).setExecutor(new CMD_land(this));
        Objects.requireNonNull(getCommand("spland")).setExecutor(new CMD_spland(this));

        getServer().getPluginManager().registerEvents(new E_LandCreation(this), this);
        getServer().getPluginManager().registerEvents(new E_LandManagement(this), this);
        getServer().getPluginManager().registerEvents(new E_InitializePlayer(this), this);

        LandModel landModel = new LandModel();
        try {
            landList.addAll(landModel.getAllLand(this));
            landModel.getAllPlayerLand().forEach(playerLand ->
                    landList.stream().filter(land -> land.getLandID() == playerLand.getLandID())
                            .forEach(land -> land.getPlayerList().put(playerLand.getPlayer(), playerLand))
            );
            Bukkit.getOnlinePlayers().forEach(player -> {
                try {
                    playerClaimList.put(player.getUniqueId(), landModel.getPlayerClaim(player));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<UUID, PlayerClaim> getPlayerClaimList() {
        return playerClaimList;
    }
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
        List<Land> PlayerLand = landList.stream().filter(land -> land.getLandName() != null && land.getLandName().equalsIgnoreCase(name) && land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString()) && !land.isStaffClaim()).collect(Collectors.toList());
        if(PlayerLand.size() > 0)
        {
            return PlayerLand.get(0);
        }
        return null;
    }
    public List<Land> getStaffLandList()
    {
        return landList.stream().filter(Land::isStaffClaim).collect(Collectors.toList());
    }
    public Land getStaffLandByName(String name)
    {
        List<Land> PlayerLand = landList.stream().filter(land -> land.getLandName() != null && land.getLandName().equalsIgnoreCase(name) && land.isStaffClaim()).collect(Collectors.toList());
        if(PlayerLand.size() > 0)
        {
            return PlayerLand.get(0);
        }
        return null;
    }

    public int getMaxID()
    {
        return landList.stream().max(Comparator.comparing(Land::getLandID)).get().getLandID();
    }
}
