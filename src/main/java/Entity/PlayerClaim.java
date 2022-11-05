package Entity;

import org.bukkit.entity.Player;

public class PlayerClaim {

    private int id;
    private final Player player;
    private int NbClaimBlock;
    private int NBClaimedBlock;

    private static int price = 25;

    public PlayerClaim(Player player, int nbClaimBlock, int NBClaimedBlock) {
        this.player = player;
        NbClaimBlock = nbClaimBlock;
        this.NBClaimedBlock = NBClaimedBlock;
    }

    public static int getPrice() {
        return price;
    }

    public Player getPlayer() {
        return player;
    }
    public int getNbClaimBlock() {
        return NbClaimBlock;
    }
    public void addClaimBlock(int amount)
    {
        NbClaimBlock += amount;
    }
    public void addClaimedBlock(int amount)
    {
        NBClaimedBlock += amount;
    }
    public void removeClaimBlock(int amount)
    {
        NbClaimBlock -= amount;
    }
    public void removeClaimedBlock(int amount)
    {
        NBClaimedBlock -= amount;
    }
}
