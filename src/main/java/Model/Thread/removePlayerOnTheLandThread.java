package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class removePlayerOnTheLandThread extends LandModel implements Runnable {

    private final int landID;
    private final String uuid;

    public removePlayerOnTheLandThread(int landID, String uuid) {
        this.landID = landID;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            this.removePlayerOnLand(landID, uuid);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
