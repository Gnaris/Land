package Land;

import Command.CMD_Land;
import Command.CMD_SPLand;
import Entity.Land;
import Event.LandProtection;
import Model.LandModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public final class LandMain extends JavaPlugin {

    private Map<UUID, Land> landProgress = new HashMap<>();
    private Map<UUID, Map<String, Land>> lands = new HashMap<>();
    private Map<String, Land> safeLands = new HashMap<>();

    @Override
    public void onEnable() {
        // ! VERIFIER SI UN CLAIM EXISTE DEJA DANS LA ZONE
        // ! METTRE UN SPAWN POUR CHAQUE CLAIM
        saveDefaultConfig();

        getCommand("land").setExecutor(new CMD_Land(this));
        getCommand("spland").setExecutor(new CMD_SPLand(this));

        getServer().getPluginManager().registerEvents(new LandProtection(this), this);

        LandModel landModel = new LandModel();
        try
        {
            lands = landModel.getAllLands();
            safeLands = landModel.getAllSafeLand();
            landModel.getAllLandMembers().forEach(member ->
                    lands.get(member.getOwnerLand()).get(member.getLandName()).getMembers().add(member.getUUID())
            );
            landModel.getAllSafeLandMembers().forEach(member -> safeLands.get(member.getLandName()).getMembers().add(member.getUUID()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<UUID, Land> getLandProgress() {
        return landProgress;
    }
    public Map<UUID, Map<String, Land>> getLands() {
        return lands;
    }

    public Map<String, Land> getSafeLands() {
        return safeLands;
    }

    public List<Land> getAllLand()
    {
        List<Land> landList = new ArrayList<>();
        if(lands.size() > 0)
        {
            for(UUID owner : lands.keySet())
            {
                landList.addAll(lands.get(owner).values());
            }
        }
        if(safeLands.size() > 0)
        {
            landList.addAll(safeLands.values());
        }
        return landList.stream().filter(land -> land.getMinLocation() != null && land.getMaxLocation() != null).collect(Collectors.toList());
    }
}
