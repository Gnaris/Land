package Model;

import Entity.Land;
import Entity.LocationParser;
import Entity.PlayerClaim;
import Entity.PlayerLand;
import SPLand.SPLand;
import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LandModel {

    public void insertNewLand(UUID uuid, Land land) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("INSERT INTO player_land VALUES (NULL, (SELECT id FROM player WHERE uuid = ?) , ?, ?, ?, ?, DEFAULT, DEFAULT)");
        stmt.setString(1, uuid.toString());
        stmt.setString(2, land.getName());
        stmt.setString(3, land.getWorld().getName());
        stmt.setString(4, land.locationToJson(land.getFirstLocation()));
        stmt.setString(5, land.locationToJson(land.getSecondLocation()));
        stmt.executeUpdate();
    }

    public List<Land> getAllLand() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("SELECT pl.id, p.uuid, pl.landName, pl.world, pl.firstLocation, pl.secondLocation, pl.confirmed, pl.publicInteract FROM player_land pl JOIN player p ON p.id = pl.playerID");
        ResultSet result = stmt.executeQuery();
        List<Land> landList = new ArrayList<>();
        Gson  gson = new Gson();
        if(result != null)
        {
            while (result.next())
            {
                landList.add(new Land(
                        result.getInt("id"),
                        result.getString("uuid"),
                        result.getString("landName"),
                        result.getString("world"),
                        gson.fromJson(result.getString("firstLocation"), LocationParser.class).toLocation(result.getString("world")),
                        gson.fromJson(result.getString("secondLocation"), LocationParser.class).toLocation(result.getString("world")),
                        result.getBoolean("confirmed"),
                        result.getBoolean("publicInteract")
                ));
            }
        }
        return landList;
    }

    public List<PlayerLand> getAllPlayerLand() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("SELECT p.uuid, plo.landID , plo.canBuild, plo.canInteract, plo.canKillAnimals FROM player_land_option plo JOIN player p ON p.id = plo.playerID");
        ResultSet result = stmt.executeQuery();
        List<PlayerLand> playerList = new ArrayList<>();
        while(result.next())
        {
            playerList.add(new PlayerLand(
                    result.getInt("landID"),
                    result.getString("uuid"),
                    result.getBoolean("canBuild"),
                    result.getBoolean("canInteract"),
                    result.getBoolean("canKillAnimals")
            ));
        }
        return playerList;
    }

    public PlayerClaim getPlayerClaim(Player player) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("SELECT nbClaimBlock, nbClaimedBlock FROM player_claimblock WHERE playerID = (SELECT id FROM player WHERE uuid = ?)");
        stmt.setString(1, player.getUniqueId().toString());
        ResultSet result = stmt.executeQuery();
        while(result.next())
        {
            return new PlayerClaim(player, result.getInt("nbClaimBlock"), result.getInt("nbClaimedBlock"));
        }
        return null;
    }

    public void invitePlayerOnLand(int landID, String uuid) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("INSERT INTO player_land_option VALUES (NULL, ?, (SELECT id FROM player WHERE uuid = ?), DEFAULT, DEFAULT, DEFAULT)");
        stmt.setInt(1, landID);
        stmt.setString(2, uuid);
        stmt.executeUpdate();
    }

    public void removePlayerOnLand(int landID, String uuid) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("DELETE FROM player_land_option WHERE landID = ? AND playerID = (SELECT id FROM player WHERE uuid = ?)");
        stmt.setInt(1, landID);
        stmt.setString(2, uuid);
        stmt.executeUpdate();
    }

    public void setPublicInteractLand(int landID, boolean response) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("UPDATE player_land SET publicInteract = ? WHERE id = ?");
        stmt.setBoolean(1, response);
        stmt.setInt(2, landID);
        stmt.executeUpdate();
    }
}
