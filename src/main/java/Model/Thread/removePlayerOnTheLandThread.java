package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class removePlayerOnTheLandThread extends LandModel implements Runnable {

    private final int playerID;
    private final String landName;
    private final boolean staffClaim;
    private final String targetUUID;

    public removePlayerOnTheLandThread(int playerID, String landName, boolean staffClaim, String targetUUID) {
        this.playerID = playerID;
        this.landName = landName;
        this.staffClaim = staffClaim;
        this.targetUUID = targetUUID;
    }

    @Override
    public void run() {
        try {
            this.removePlayerOnLand(playerID, landName, staffClaim, targetUUID);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
