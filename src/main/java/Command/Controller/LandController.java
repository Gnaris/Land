package Command.Controller;

import Controller.Controller;
import Entity.Land;
import Entity.Wallet;
import Model.Thread.*;
import SPLand.SPLand;
import org.bukkit.entity.Player;

public class LandController extends Controller implements LandParameter {

    public LandController(Player player, SPLand plugin) {
        super(player, plugin);
    }

    public boolean canConfirmLand(String name)
    {
        if(!this.claimInProgress()) return false;

        Land playerLand = plugin.getPlayerLandNotConfirmed(player).get(0);

        if(plugin.getLandList().stream().anyMatch(land -> land.isConfirmed() && (playerLand.isInArea(land.getFirstLocation()) || playerLand.isInArea(land.getSecondLocation()))))
        {
            player.sendMessage("§cIl semblerait que quelqu'un a déjà claim une partie de votre terrain. Veuillez recommencez à 0");
            plugin.getPlayerLandNotConfirmed(player).get(0).hideArea();
            plugin.getLandList().remove(plugin.getPlayerLandNotConfirmed(player).get(0));
            return false;
        }
        if(plugin.getPlayerLandList(player).stream().anyMatch(land -> land.getLandName() != null && land.getLandName().equalsIgnoreCase(name) && !land.isStaffClaim()))
        {
            player.sendMessage("§cLe nom de cette claim est déjà utilisé");
            return false;
        }
        if(playerClaim.getNbClaimBlock() - playerLand.getAirOfClaim() < 0)
        {
            player.sendMessage("§cVous n'avez pas assez de blocs de claim, faites /land buyclaimblock " + (playerLand.getAirOfClaim() - playerClaim.getNbClaimBlock()) + " pour en acheter");
            return false;
        }

        new Thread(new insertNewLandThread(plugin.getPlayerClaimList().get(player.getUniqueId()).getPlayerID(), name, playerLand, false)).start();
        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() - playerLand.getAirOfClaim()))).start();
        new Thread(new updatePlayerClaimedBlockThread(player.getUniqueId().toString(), (playerClaim.getNBClaimedBlock() + playerLand.getAirOfClaim()))).start();
        return true;
    }

    public boolean canBuyClaimBlock(Wallet playerWallet, String amount)
    {
        if(!isLongFormat(amount)) return false;
        long nbBlock = Long.parseLong(amount);
        long priceTotal = nbBlock * plugin.getConfig().getInt("claim_block_price");
        if((playerWallet.getBalance() - priceTotal) < 0)
        {
            player.sendMessage("§cVous n'avez pas assez d'argent. Il vous manque " + (priceTotal - playerWallet.getBalance()) + wallet.getConfig().getString("symbol"));
            return false;
        }

        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (nbBlock + playerClaim.getNbClaimBlock()))).start();
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

        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() - blockAmount))).start();
        return true;
    }

    public boolean canDeleteLand(String name)
    {
        if(!this.existingLand(name)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);

        new Thread(new updatePlayerClaimBlockThread(player.getUniqueId().toString(), (playerClaim.getNbClaimBlock() + playerLand.getAirOfClaim()))).start();
        new Thread(new updatePlayerClaimedBlockThread(player.getUniqueId().toString(), (playerClaim.getNBClaimedBlock() - playerLand.getAirOfClaim()))).start();
        new Thread(new deleteLandThread(playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

    public boolean canInvitePlayerOnLand(Player target, String name)
    {
        if(!this.checkTarget(target)) return false;
        if(!this.existingLand(name)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);
        if(playerLand.getPlayerList().get(target.getUniqueId()) != null)
        {
            player.sendMessage("§cCe joueur est déjà dans votre claim");
            return false;
        }

        new Thread(new invitePlayerOnLandThread(playerClaim.getPlayerID(), name, false, target.getUniqueId().toString())).start();
        return true;
    }

    public boolean canRemovePlayer(Player target, String name)
    {
        if(!this.checkTarget(target)) return false;
        if(!this.existingLand(name)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);
        if(playerLand.getPlayerList().get(target.getUniqueId()) == null)
        {
            player.sendMessage("§cCe joueur n'est pas dans votre terrain");
            return false;
        }

        new Thread(new removePlayerOnTheLandThread(playerClaim.getPlayerID(), name, false, target.getUniqueId().toString())).start();
        return true;
    }

    @Override
    public boolean canSetInteract(String name, String value)
    {
        if(!this.canHandleLandParameter(name, value)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);
        boolean response = playerLand.isInteract();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setInteractThread(response, playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

    @Override
    public boolean canSetMobSpawn(String name, String value)
    {
        if(!this.canHandleLandParameter(name, value)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);

        boolean response = playerLand.isMobSpawn();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setMobSpawnThread(response, playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

    @Override
    public boolean canSetHitMob(String name, String value) {
        if(!this.canHandleLandParameter(name, value)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);

        boolean response = playerLand.isHitMob();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setHitMobThread(response, playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

    @Override
    public boolean canSetHitAnimal(String name, String value) {
        if(!this.canHandleLandParameter(name, value)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);

        boolean response = playerLand.isHitAnimal();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setHitAnimalThread(response, playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

    @Override
    public boolean canSetCrops(String name, String value) {
        if(!this.canHandleLandParameter(name, value)) return false;

        Land playerLand = plugin.getPlayerLandByName(player, name);

        boolean response = playerLand.isCrops();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setCropsThread(response, playerClaim.getPlayerID(), name, false)).start();
        return true;
    }

}
