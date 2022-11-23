package Model;

import Entity.Land;
import Entity.LandSecurity;
import Utils.LocationParser;
import Entity.Member;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;
import sperias.gnaris.SPDatabase.SPDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LandModel {

    private final SPDatabase database = (SPDatabase) Bukkit.getServer().getPluginManager().getPlugin("SP_Database");


    public Map<UUID, Map<String, Land>> getAllLands() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT * FROM land WHERE isSafezone = false");
        ResultSet result = stmt.executeQuery();
        List<Land> lands = new ArrayList<>();
        Map<UUID, Map<String, Land>> playerLands = new HashMap<>();
        Gson  gson = new Gson();
        while (result.next())
        {
            lands.add(new Land(
                    UUID.fromString(result.getString("owner")),
                    result.getString("landName"),
                    gson.fromJson(result.getString("minLocation"), LocationParser.class).toLocation(),
                    gson.fromJson(result.getString("maxLocation"), LocationParser.class).toLocation(),
                    result.getBoolean("isSafeZone"),
                    result.getBoolean("isCity"),
                    result.getBoolean("canInteract"),
                    result.getBoolean("monsterCanSpawn"),
                    result.getBoolean("canHitMonster"),
                    result.getBoolean("canHitAnimal"),
                    result.getBoolean("canCrops")
            ));
            playerLands.put(UUID.fromString(result.getString("owner")), new HashMap<>());
        }
        lands.forEach(land -> playerLands.get(land.getOwner()).put(land.getRegionName(), land));
        return playerLands;
    }

    public Map<String, Land> getAllSafeLand() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT * FROM land WHERE isSafeZone = true");
        ResultSet result = stmt.executeQuery();
        Map<String, Land> lands = new HashMap<>();
        Gson  gson = new Gson();
        while (result.next())
        {
            lands.put(result.getString("landName"), new Land(
                    null,
                    result.getString("landName"),
                    gson.fromJson(result.getString("minLocation"), LocationParser.class).toLocation(),
                    gson.fromJson(result.getString("maxLocation"), LocationParser.class).toLocation(),
                    result.getBoolean("isSafeZone"),
                    result.getBoolean("isCity"),
                    result.getBoolean("canInteract"),
                    result.getBoolean("monsterCanSpawn"),
                    result.getBoolean("canHitMonster"),
                    result.getBoolean("canHitAnimal"),
                    result.getBoolean("canCrops")
            ));
        }
        return lands;
    }

    public void createLand(Land land)
    {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("INSERT INTO land (owner, landName, minLocation, maxLocation, isSafeZone) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, land.getOwner().toString());
                stmt.setString(2, land.getRegionName());
                stmt.setString(3, new LocationParser(land.getMinLocation()).ToJson());
                stmt.setString(4, new LocationParser(land.getMaxLocation()).ToJson());
                stmt.setBoolean(5, land.isSafeZone());
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void deleteLand(UUID uuid, String landName) {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("DELETE FROM land WHERE owner = ? && landName = ?");
                stmt.setString(1, uuid.toString());
                stmt.setString(2, landName);
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void deleteSafeLand(String landName) {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("DELETE FROM land WHERE landName = ? AND isSafeZone = true");
                stmt.setString(1, landName);
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public List<Member> getAllLandMembers() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT c.landName, c.owner, m.member FROM land_member m JOIN land c ON c.id = m.landID WHERE c.isSafeZone = false");
        ResultSet result = stmt.executeQuery();
        List<Member> landMembers = new ArrayList<>();
        while(result.next())
        {
            landMembers.add(new Member(
                    result.getString("landName"),
                    UUID.fromString(result.getString("owner")),
                    UUID.fromString(result.getString("member"))
            ));
        }
        return landMembers;
    }

    public List<Member> getAllSafeLandMembers() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT c.landName, c.owner, m.member FROM land_member m JOIN land c ON c.id = m.landID WHERE c.isSafeZone = true");
        ResultSet result = stmt.executeQuery();
        List<Member> landMembers = new ArrayList<>();
        while(result.next())
        {
            landMembers.add(new Member(
                    result.getString("landName"),
                    UUID.fromString(result.getString("owner")),
                    UUID.fromString(result.getString("member"))
            ));
        }
        return landMembers;
    }

    public void addMember(UUID owner, UUID member)
    {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("INSERT INTO land_member (owner, member) VALUES (?, ?)");
                stmt.setString(1, owner.toString());
                stmt.setString(2, member.toString());
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public void removeMember(UUID owner, UUID member)
    {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("DELETE FROM land_member WHERE owner = ? AND member = ?");
                stmt.setString(1, owner.toString());
                stmt.setString(2, member.toString());
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setLandSecurity(UUID owner, LandSecurity landSecurity, boolean value){
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE land SET " + landSecurity.getName() +" = ? WHERE owner = ? AND isSafeZone = false");
                stmt.setBoolean(1, value);
                stmt.setString(2, owner.toString());
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setSafeLandSecurity(String landName, LandSecurity landSecurity, boolean value){
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE land SET " + landSecurity.getName() +" = ? WHERE landName = ? AND isSafeZone = true");
                stmt.setBoolean(1, value);
                stmt.setString(2, landName);
                stmt.executeUpdate();
            }catch (SQLException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setSpawnLand(UUID uuid, String landName, Location location)
    {
        new Thread(() -> {
            try {
                PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE land SET spawnLocation = ? WHERE landName = ? AND owner = ?");
                stmt.setString(1, new LocationParser(location).ToJson());
                stmt.setString(2, landName);
                stmt.setString(3, uuid.toString());
                stmt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
