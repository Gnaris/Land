package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class deleteLandThread extends LandModel implements Runnable {

    private final int playerID;
    private final String landName;
    private final boolean staffClaim;

    public deleteLandThread(int playerID, String landName, boolean staffClaim) {
        this.playerID = playerID;
        this.landName = landName;
        this.staffClaim = staffClaim;
    }

    @Override
    public void run() {
        try {
            this.deleteLand(playerID, landName, staffClaim);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
