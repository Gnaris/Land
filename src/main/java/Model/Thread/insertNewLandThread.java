package Model.Thread;

import Entity.Land;
import Model.LandModel;

import java.sql.SQLException;
import java.util.UUID;

public class insertNewLandThread extends LandModel implements Runnable{

    private UUID uuid;
    private Land land;

    public insertNewLandThread(UUID uuid, Land land) {
        this.uuid = uuid;
        this.land = land;
    }

    @Override
    public void run() {
        try {
            this.insertNewLand(this.uuid, this.land);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
