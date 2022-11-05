package Model;

import Entity.Land;
import Entity.PlayerClaim;
import SPLand.SPLand;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LandModel {

    public void insertNewLand(UUID uuid, Land land) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = SPLand.getDatabase().prepareStatement("INSERT INTO player_land VALUES (NULL, (SELECT id FROM player WHERE uuid = ?) , ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, uuid.toString());
        stmt.setString(2, land.getName());
        stmt.setString(3, land.getWorld().getName());
        stmt.setString(4, land.locationToJson(land.getFirstLocation()));
        stmt.setString(5, land.locationToJson(land.getSecondLocation()));
        stmt.setBoolean(6, land.isConfirmed());
        stmt.setBoolean(7, land.canOpenSomething());
        stmt.executeUpdate();
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
}
