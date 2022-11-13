package Model.Thread;

import Entity.Land;
import Model.LandModel;

import java.sql.SQLException;

public class invitePlayerOnLandThread extends LandModel implements Runnable {

    private final int playerID;
    private final String landName;
    private final boolean staffClaim;
    private final String targetUUID;

    public invitePlayerOnLandThread(int playerID, String landName, boolean staffClaim, String targetUUID) {
        this.playerID = playerID;
        this.landName = landName;
        this.staffClaim = staffClaim;
        this.targetUUID = targetUUID;
    }

    @Override
    public void run() {
        try {
            this.invitePlayerOnLand(playerID, landName, staffClaim, targetUUID);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
