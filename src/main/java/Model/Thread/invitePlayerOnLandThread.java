package Model.Thread;

import Entity.Land;
import Model.LandModel;

import java.sql.SQLException;

public class invitePlayerOnLandThread extends LandModel implements Runnable {

    private final int landID;
    private final String uuid;

    public invitePlayerOnLandThread(int landID, String uuid) {
        this.landID = landID;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            this.invitePlayerOnLand(this.landID, this.uuid);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
