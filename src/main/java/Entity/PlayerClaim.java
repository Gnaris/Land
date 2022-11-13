package Entity;

import org.bukkit.entity.Player;

public class PlayerClaim {

    private int playerID;
    private final Player player;
    private long NbClaimBlock;
    private long NBClaimedBlock;
    private int nbLand;

    public PlayerClaim(Player player, int playerID, long nbClaimBlock, long NBClaimedBlock, int nbLand) {
        this.player = player;
        this.playerID = playerID;
        this.NbClaimBlock = nbClaimBlock;
        this.NBClaimedBlock = NBClaimedBlock;
        this.nbLand = nbLand;
    }

    public int getPlayerID() {
        return playerID;
    }
    public Player getPlayer() {
        return player;
    }
    public long getNbClaimBlock() {
        return NbClaimBlock;
    }
    public long getNBClaimedBlock() {
        return NBClaimedBlock;
    }
    public void addClaimBlock(long amount)
    {
        NbClaimBlock += amount;
    }
    public void addClaimedBlock(long amount)
    {
        NBClaimedBlock += amount;
    }
    public void removeClaimBlock(long amount)
    {
        NbClaimBlock -= amount;
    }
    public void removeClaimedBlock(long amount)
    {
        NBClaimedBlock -= amount;
    }
    public int getNbLand() {
        return nbLand;
    }
    public void setNbLand(int nbLand) {
        this.nbLand = nbLand;
    }
}
