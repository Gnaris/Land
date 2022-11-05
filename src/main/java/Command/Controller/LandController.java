package Command.Controller;

import Controller.Controller;
import Entity.Land;
import Entity.PlayerClaim;
import Entity.Wallet;
import SPLand.SPLand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class LandController extends Controller {

    public LandController(Player player) {
        super(player);
    }

    private boolean haveClaimInProgress()
    {
        if(landStore.getPlayerLandNotConfirmed(player).size() == 0)
        {
            player.sendMessage("§cTu n'as pas de claim en cours !");
            return false;
        }
        return true;
    }

    private boolean checkTarget(Player target)
    {
        if(target == null)
        {
            player.sendMessage("§cCe joueur n'existe pas");
            return false;
        }
        if(target == player)
        {
            player.sendMessage("§c...");
            return false;
        }

        return true;
    }

    private boolean existingLand(String name)
    {
        Land playerLand = SPLand.getInstance().getLandStore().getPlayerLandByName(player, name);
        if(playerLand == null)
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean canConfirmClaim(String name)
    {
        if(!this.haveClaimInProgress()) return false;

        Land playerLand = landStore.getPlayerLandNotConfirmed(player).get(0);
        PlayerClaim playerClaim = SPLand.getInstance().getPlayerStore().getPlayerList().get(player.getUniqueId());

        if(playerClaim.getNbClaimBlock() - playerLand.getAirOfClaim() < 0)
        {
            player.sendMessage("§cVous n'avez pas assez de blocs de claim, faites /land buyclaimblock " + (playerLand.getAirOfClaim() - playerClaim.getNbClaimBlock()) + " pour en acheter");
            return false;
        }
        if(landStore.getPlayerLandList(player).stream().anyMatch(land -> land.getName() != null && land.getName().equalsIgnoreCase(name)))
        {
            player.sendMessage("§cLe nom de cette claim est déjà utilisé");
            return false;
        }
        player.sendMessage("§aFélicitation, votre terrain a été créee sous le nom de " + name);
        return true;
    }

    public boolean canCancelClaim()
    {
        if(!this.haveClaimInProgress()) return false;
        player.sendMessage("§cVous avez annuler votre claim");
        return true;
    }

    public boolean canShowPlayerList()
    {
        if(landStore.getPlayerLandList(player).size() == 0)
        {
            this.player.sendMessage("§cVous n'avez pas terrain");
            return false;
        }

        player.sendMessage("§aVoici la liste de terrain que vous avez :");
        StringBuilder claimName = new StringBuilder();
        landStore.getPlayerLandList(player).forEach(claim -> claimName.append(ChatColor.of("#FFB927")).append(claim.getName()).append(" "));
        player.sendMessage(claimName.toString());
        return true;
    }

    public boolean canBuyClaimBlock(Wallet PlayerWallet, int BlockAmount)
    {
        if((PlayerWallet.getMoney() - (BlockAmount * PlayerClaim.getPrice())) < 0)
        {
            player.sendMessage("§cVous n'avez pas assez d'argent. Il vous manque " + ((BlockAmount * PlayerClaim.getPrice()) - PlayerWallet.getMoney()) + "$");
            return false;
        }

        player.sendMessage("§aVous venez d'acheter " + BlockAmount + " bloc(s) de claim pour " + (BlockAmount * PlayerClaim.getPrice()) + "$");
        return true;
    }

    public boolean canSellClaimBlock(int BlockAmount)
    {
        PlayerClaim playerClaim = SPLand.getInstance().getPlayerStore().getPlayerList().get(player.getUniqueId());
        if((playerClaim.getNbClaimBlock() - BlockAmount) < 0)
        {
            player.sendMessage("§cVous ne pouvez pas vendre plus de " + playerClaim.getNbClaimBlock() + " bloc(s)");
            return false;
        }

        player.sendMessage("§aVous venez de vendre " + BlockAmount + " bloc(s) pour " + (BlockAmount * PlayerClaim.getPrice()) + "$");
        return true;
    }

    public boolean canShowLandClaim(String name)
    {
        Land playerLand = landStore.getPlayerLandByName(player, name);
        if(playerLand == null)
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }

        player.sendMessage("§aPour cacher les claims faites /land hide " + name);
        return true;
    }

    public boolean canHideLandClaim(String name)
    {
        if(!this.existingLand(name)) return false;

        player.sendMessage("§aVous avez cacher votre claim");
        return true;
    }

    public boolean canDeleteClaim(String name)
    {
        if(!existingLand(name)) return false;

        player.sendMessage("§aLe terrain " + name + " a été supprimé avec succès");
        return true;
    }

    public boolean canInvitePlayerOnClaim(Player target, String name)
    {
        if(!checkTarget(target)) return false;
        if(!existingLand(name)) return false;

        Land playerLand = landStore.getPlayerLandByName(player, name);
        if(playerLand.getPlayerList().get(target.getUniqueId()) != null)
        {
            player.sendMessage("§cCe joueur est déjà dans votre claim");
            return false;
        }

        player.sendMessage("§aVous avez inviter " + target.getName() + " dans votre claim");
        target.sendMessage("§a" + player.getName() + " vous a invité dans son claim");
        return true;
    }

    public boolean canSetOpen(String name, String value)
    {
        if(!existingLand(name)) return false;

        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }

        if(value.equalsIgnoreCase("on"))
        {
            player.sendMessage("§aTout le monde pourra à présent intéragir avec vos blocs");
        }
        if(value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§aPlus personne ne pourra à présent intéragir avec vos blocs");
        }
        return true;
    }

    public boolean canRemovePlayer(Player target, String name)
    {
        if(!checkTarget(target)) return false;
        Land playerLand = landStore.getPlayerLandByName(player, name);
        if(playerLand == null)
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        if(playerLand.getPlayerList().get(target.getUniqueId()) == null)
        {
            player.sendMessage("§cCe joueur n'est pas dans le terrain");
            return false;
        }

        player.sendMessage("§aCe joueur a été supprimé du terrain");
        return true;
    }
}
