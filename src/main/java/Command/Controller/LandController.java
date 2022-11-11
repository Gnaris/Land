package Command.Controller;

import Controller.Controller;
import Entity.Land;
import Entity.PlayerClaim;
import Entity.Wallet;
import Model.Thread.*;
import SPLand.SPLand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LandController extends Controller {

    public LandController(Player player) {
        super(player);
    }

    private boolean claimNotInProgress()
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

    private boolean isLongFormat(String amount)
    {
        try{
            Long.parseLong(amount);
        }catch (NumberFormatException e)
        {
            player.sendMessage("§c" + amount + " n'est pas un montant valide");
            return false;
        }

        return true;
    }

    public boolean canConfirmClaim(String name)
    {
        if(!this.claimNotInProgress()) return false;

        Land playerLand = landStore.getPlayerLandNotConfirmed(player).get(0);

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
        new Thread(new insertNewLandThread(player.getUniqueId(), playerLand)).start();
        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() - playerLand.getAirOfClaim()))).start();
        new Thread(new updatePlayerClaimedBlockThread(player.getUniqueId().toString(), (playerClaim.getNBClaimedBlock() + playerLand.getAirOfClaim()))).start();
        return true;
    }

    public boolean canCancelClaim()
    {
        if(!this.claimNotInProgress()) return false;
        player.sendMessage("§cVous avez annuler votre claim");
        return true;
    }

    public boolean canShowPlayerLandList()
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

    public boolean canBuyClaimBlock(Wallet playerWallet, String amount)
    {
        if(!isLongFormat(amount)) return false;
        long blockAmount = Long.parseLong(amount);
        if((playerWallet.getBalance() - ( blockAmount * PlayerClaim.getPrice())) < 0)
        {
            player.sendMessage("§cVous n'avez pas assez d'argent. Il vous manque " + ((blockAmount * PlayerClaim.getPrice()) - playerWallet.getBalance()) + "$");
            return false;
        }

        player.sendMessage("§aVous venez d'acheter " + blockAmount + " bloc(s) de claim pour " + (blockAmount * PlayerClaim.getPrice()) + "$");
        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (blockAmount + playerClaim.getNbClaimBlock()))).start();
        return true;
    }

    public boolean canSellClaimBlock(String amount)
    {
        if(!this.isLongFormat(amount)) return false;
        long blockAmount = Long.parseLong(amount);
        if((playerClaim.getNbClaimBlock() - blockAmount) < 0)
        {
            player.sendMessage("§cVous ne pouvez pas vendre plus de " + playerClaim.getNbClaimBlock() + " bloc(s)");
            return false;
        }

        player.sendMessage("§aVous venez de vendre " + blockAmount + " bloc(s) pour " + (blockAmount * PlayerClaim.getPrice()) + "$");
        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() - blockAmount))).start();
        return true;
    }

    public boolean canShowLandClaim(String name)
    {
        if(!this.existingLand(name)) return false;
        player.sendMessage("§aPour cacher les claims faites /land hide " + name);
        return true;
    }

    public boolean canHideLandClaim(String name)
    {
        if(!this.existingLand(name)) return false;

        player.sendMessage("§aVous avez cacher votre claim");
        return true;
    }

    public boolean canDeleteLand(String name)
    {
        if(!this.existingLand(name)) return false;

        Land playerLand = landStore.getPlayerLandByName(player, name);
        player.sendMessage("§aLe terrain " + name + " a été supprimé avec succès");
        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() + playerLand.getAirOfClaim()))).start();
        new Thread(new updatePlayerClaimedBlockThread(player.getUniqueId().toString(), (playerClaim.getNBClaimedBlock() - playerLand.getAirOfClaim()))).start();
        new Thread(new deleteLandThread(playerLand.getId())).start();
        return true;
    }

    public boolean canInvitePlayerOnClaim(Player target, String name)
    {
        if(!this.checkTarget(target)) return false;
        if(!this.existingLand(name)) return false;

        Land playerLand = landStore.getPlayerLandByName(player, name);
        if(playerLand.getPlayerList().get(target.getUniqueId()) != null)
        {
            player.sendMessage("§cCe joueur est déjà dans votre claim");
            return false;
        }

        player.sendMessage("§aVous avez inviter " + target.getName() + " dans votre claim");
        target.sendMessage("§a" + player.getName() + " vous a invité dans son claim");

        new Thread(new invitePlayerOnLandThread(playerLand.getId(), target.getUniqueId().toString())).start();
        return true;
    }

    public boolean canSetPublicInteract(String name, String value)
    {
        if(!this.existingLand(name)) return false;

        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }


        Land playerLand = landStore.getPlayerLandByName(player, name);
        boolean response = playerLand.isPublicInteract();
        if(value.equalsIgnoreCase("on"))
        {
            player.sendMessage("§aTout le monde pourra à présent intéragir avec vos blocs");
            response = true;
        }
        if(value.equalsIgnoreCase("off"))
        {
            response = false;
            player.sendMessage("§aPlus personne ne pourra à présent intéragir avec vos blocs");
        }

        new Thread(new setPublicInteractThread(playerLand.getId(), response)).start();
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

        new Thread(new removePlayerOnTheLandThread(playerLand.getId(), target.getUniqueId().toString())).start();
        return true;
    }

    public boolean canShowLandInfo(String name)
    {
        if(!this.existingLand(name)) return false;
        Land playerLand = landStore.getPlayerLandByName(player, name);

        player.sendMessage("Land : " + playerLand.getName());
        player.sendMessage("");
        player.sendMessage("Claim : ");
        player.sendMessage("Position A : X :" + playerLand.getFirstLocation().getX() + " Z : " + playerLand.getFirstLocation().getZ());
        player.sendMessage("Position B : X :" + playerLand.getSecondLocation().getX() + " Z : " + playerLand.getSecondLocation().getZ());
        player.sendMessage("");
        player.sendMessage("Liste des joueurs dans votre claim :");
        StringBuilder playerList = new StringBuilder();
        if(playerLand.getPlayerList().size() > 0)
        {
            for(UUID uuid : playerLand.getPlayerList().keySet())
            {
                playerList.append(Bukkit.getPlayer(uuid).getName()).append(" ");
            }
            player.sendMessage(playerList.toString());
        }
        else
        {
            player.sendMessage("Tu es tout seul :(");
        }

        return true;
    }
}
