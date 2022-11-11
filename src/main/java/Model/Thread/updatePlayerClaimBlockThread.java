package Model.Thread;

import Model.LandModel;

import java.sql.SQLException;

public class updatePlayerClaimBlockThread extends LandModel implements Runnable {

    private String uuid;
    private long claimBlock;

    public updatePlayerClaimBlockThread(String uuid, long claimBlock) {
        this.uuid = uuid;
        this.claimBlock = claimBlock;
    }

    @Override
    public void run() {
        try {
            this.updatePlayerClaimBlock(this.uuid, this.claimBlock);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
