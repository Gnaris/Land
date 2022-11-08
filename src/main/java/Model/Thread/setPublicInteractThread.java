package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class setPublicInteractThread extends LandModel implements Runnable {

    private int landID;
    private boolean response;

    public setPublicInteractThread(int landID, boolean response) {
        this.landID = landID;
        this.response = response;
    }

    @Override
    public void run() {
        try {
            this.setPublicInteractLand(this.landID, this.response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
