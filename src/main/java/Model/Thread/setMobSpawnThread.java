package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class setMobSpawnThread extends LandModel implements Runnable{

    private final boolean response;
    private final int playerID;
    private final String landName;
    private final boolean staffClaim;

    public setMobSpawnThread(boolean response, int playerID, String landName, boolean staffClaim) {
        this.response = response;
        this.playerID = playerID;
        this.landName = landName;
        this.staffClaim = staffClaim;
    }

    @Override
    public void run() {
        try {
            this.setMobSpawnLand(response, playerID, landName, staffClaim);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
