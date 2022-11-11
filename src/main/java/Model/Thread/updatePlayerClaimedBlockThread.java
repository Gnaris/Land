package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class updatePlayerClaimedBlockThread extends LandModel implements Runnable {

    private String uuid;
    private long claimedBlock;

    public updatePlayerClaimedBlockThread(String uuid, long claimedBlock) {
        this.uuid = uuid;
        this.claimedBlock = claimedBlock;
    }

    @Override
    public void run() {
        try {
            this.updatePlayerClaimedBlock(this.uuid, this.claimedBlock);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
