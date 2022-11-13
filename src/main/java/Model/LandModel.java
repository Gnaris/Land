package Model;

import Entity.Land;
import Entity.LocationParser;
import Entity.PlayerClaim;
import Entity.PlayerLand;
import SPLand.SPLand;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sperias.gnaris.SPDatabase.SPDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LandModel {

    private final SPDatabase database = (SPDatabase) Bukkit.getServer().getPluginManager().getPlugin("SP_Database");


    public void insertNewLand(int playerID, String landName, Land land, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("INSERT INTO player_land VALUES (NULL, ?, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, ?)");
        stmt.setInt(1, playerID);
        stmt.setString(2, landName);
        stmt.setString(3, new LocationParser(land.getFirstLocation().getWorld().getName(), land.getFirstLocation().getX(), land.getFirstLocation().getY(), land.getFirstLocation().getZ()).locationToJson());
        stmt.setString(4, new LocationParser(land.getSecondLocation().getWorld().getName(), land.getSecondLocation().getX(), land.getSecondLocation().getY(), land.getSecondLocation().getZ()).locationToJson());
        stmt.setBoolean(5, staffClaim);
        stmt.executeUpdate();
    }

    public List<Land> getAllLand(SPLand plugin) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT pl.id, p.uuid, pl.landName, pl.firstLocation, pl.secondLocation, pl.confirmed, pl.interact, pl.mobSpawn, pl.hitMob, pl.hitAnimal, pl.crops, pl.staffClaim FROM player_land pl JOIN player p ON p.id = pl.playerID");
        ResultSet result = stmt.executeQuery();
        List<Land> landList = new ArrayList<>();
        Gson  gson = new Gson();
        if(result != null)
        {
            while (result.next())
            {
                landList.add(new Land(
                        result.getString("uuid"),
                        gson.fromJson(result.getString("firstLocation"), LocationParser.class).toLocation(),
                        gson.fromJson(result.getString("secondLocation"), LocationParser.class).toLocation(),
                        plugin,
                        result.getInt("id"),
                        result.getString("landName"),
                        result.getBoolean("confirmed"),
                        result.getBoolean("interact"),
                        result.getBoolean("mobSpawn"),
                        result.getBoolean("hitMob"),
                        result.getBoolean("hitAnimal"),
                        result.getBoolean("crops"),
                        result.getBoolean("staffClaim")
                ));
            }
        }
        return landList;
    }

    public List<PlayerLand> getAllPlayerLand() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT p.id, p.uuid, plo.landID FROM player_land_option plo JOIN player p ON p.id = plo.playerID");
        ResultSet result = stmt.executeQuery();
        List<PlayerLand> playerList = new ArrayList<>();
        while(result.next())
        {
            playerList.add(new PlayerLand(
                    result.getInt("id"),
                    result.getInt("landID"),
                    result.getString("uuid")
            ));
        }
        return playerList;
    }

    public PlayerClaim getPlayerClaim(Player player) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("SELECT id, nbClaimBlock, nbClaimedBlock, nbLand FROM player WHERE uuid = ?");
        stmt.setString(1, player.getUniqueId().toString());
        ResultSet result = stmt.executeQuery();
        while(result.next())
        {
            return new PlayerClaim(
                    player,
                    result.getInt("id"),
                    result.getLong("nbClaimBlock"),
                    result.getLong("nbClaimedBlock"),
                    result.getInt("nbLand"));
        }
        return null;
    }

    public void invitePlayerOnLand(int playerID, String landName, boolean staffClaim, String targetUUID) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("INSERT INTO player_land_option VALUES (NULL, (SELECT id FROM player_land WHERE playerID = ? AND landName = ? AND staffClaim = ?), (SELECT id FROM player WHERE uuid = ?))");
        stmt.setInt(1, playerID);
        stmt.setString(2, landName);
        stmt.setBoolean(3, staffClaim);
        stmt.setString(4, targetUUID);
        stmt.executeUpdate();
    }

    public void removePlayerOnLand(int playerID, String landName, boolean staffClaim, String targetUUID) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("DELETE FROM player_land_option WHERE landID = (SELECT id FROM player_land WHERE playerID = ? AND landName = ? AND staffClaim = ?) AND playerID = (SELECT id FROM player WHERE uuid = ?)");
        stmt.setInt(1, playerID);
        stmt.setString(2, landName);
        stmt.setBoolean(3, staffClaim);
        stmt.setString(4, targetUUID);
        stmt.executeUpdate();
    }

    public void updatePlayerClaimBlock(String uuid, long claimBlock) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player SET nbClaimBlock = ? WHERE uuid = ?");
        stmt.setLong(1, claimBlock);
        stmt.setString(2, uuid);
        stmt.executeUpdate();
    }

    public void updatePlayerClaimedBlock(String uuid, long claimedBlock) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player SET nbClaimedBlock = ? WHERE uuid = ?");
        stmt.setLong(1, claimedBlock);
        stmt.setString(2, uuid);
        stmt.executeUpdate();
    }

    public void deleteLand(int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("DELETE FROM player_land WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setInt(1, playerID);
        stmt.setString(2, landName);
        stmt.setBoolean(3, staffClaim);
        stmt.executeUpdate();
    }

    public void setInteractLand(boolean response, int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player_land SET interact = ? WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, playerID);
        stmt.setString(3, landName);
        stmt.setBoolean(4, staffClaim);
        stmt.executeUpdate();
    }

    public void setMobSpawnLand(boolean response, int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player_land SET mobSpawn = ? WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, playerID);
        stmt.setString(3, landName);
        stmt.setBoolean(4, staffClaim);
        stmt.executeUpdate();
    }

    public void setHitMobLand(boolean response, int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player_land SET hitMob = ? WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, playerID);
        stmt.setString(3, landName);
        stmt.setBoolean(4, staffClaim);
        stmt.executeUpdate();
    }

    public void setHitAnimalLand(boolean response, int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player_land SET hitAnimal = ? WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, playerID);
        stmt.setString(3, landName);
        stmt.setBoolean(4, staffClaim);
        stmt.executeUpdate();
    }

    public void setCropsLand(boolean response, int playerID, String landName, boolean staffClaim) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE player_land SET crops = ? WHERE playerID = ? AND landName = ? AND staffClaim = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, playerID);
        stmt.setString(3, landName);
        stmt.setBoolean(4, staffClaim);
        stmt.executeUpdate();
    }
}
