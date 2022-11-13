package Model.Thread;

import Entity.Land;
import Model.LandModel;

import java.sql.SQLException;

public class insertNewLandThread extends LandModel implements Runnable{

    private final int playerID;
    private final String landName;
    private final Land land;
    private final boolean staffClaim;

    public insertNewLandThread(int playerID, String landName, Land land, boolean staffClaim) {
        this.playerID = playerID;
        this.landName = landName;
        this.land = land;
        this.staffClaim = staffClaim;
    }

    @Override
    public void run() {
        try {
            this.insertNewLand(playerID, landName, land, staffClaim);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
