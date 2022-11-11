package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class deleteLandThread extends LandModel implements Runnable {

    private int landID;

    public deleteLandThread(int landID) {
        this.landID = landID;
    }

    @Override
    public void run() {
        try {
            this.deleteLand(this.landID);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
