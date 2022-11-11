package Command;

import Command.Controller.LandController;
import Entity.Land;
import Entity.PlayerClaim;
import Entity.PlayerLand;
import Entity.Wallet;
import LandStore.LandStore;
import LandStore.PlayerClaimStore;
import SPLand.SPLand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CMD_land implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            LandStore landStore = SPLand.getInstance().getLandStore();
            PlayerClaim playerClaim = SPLand.getInstance().getPlayerClaimStore().getPlayerClaimList().get(player.getUniqueId());
            Wallet playerWallet = SPLand.getWalletStore().getWalletList().get(player.getUniqueId());
            LandController landController = new LandController(player);


            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("cancel"))
                {
                    if(!landController.canCancelClaim()) return false;

                    Land playerLand = landStore.getPlayerLandNotConfirmed(player).get(0);
                    playerLand.cancelLand();
                    Bukkit.getScheduler().cancelTask(playerLand.getSchedulerID());

                    return true;
                }

                if(args[0].equalsIgnoreCase("list"))
                {
                    return landController.canShowPlayerLandList();
                }

                if(args[0].equalsIgnoreCase("test"))
                {
                    player.sendMessage(player.hasPermission("sperias.wallet.command.add") + " BLABLA");
                    return true;
                }
            }

            if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("confirm"))
                {
                    if(!landController.canConfirmClaim(args[1])) return false;

                    Land playerLand = landStore.getPlayerLandNotConfirmed(player).get(0);
                    playerLand.confirmLand(args[1]);
                    Bukkit.getScheduler().cancelTask(playerLand.getSchedulerID());

                    playerClaim.removeClaimBlock(playerLand.getAirOfClaim());
                    playerClaim.addClaimedBlock(playerLand.getAirOfClaim());
                    playerClaim.setNbLand(playerClaim.getNbLand() + 1);

                    return true;
                }

                if(args[0].equalsIgnoreCase("buyclaimblock"))
                {
                    if(!landController.canBuyClaimBlock(playerWallet, args[1])) return false;
                    long blockAmount = Long.parseLong(args[1]);
                    playerWallet.remove(blockAmount * PlayerClaim.getPrice());
                    playerClaim.addClaimBlock(blockAmount);
                    return true;
                }

                if(args[0].equalsIgnoreCase("sellclaimblock"))
                {
                    if(!landController.canSellClaimBlock(args[1])) return false;
                    long blockAmount = Long.parseLong(args[1]);
                    playerWallet.add(blockAmount * PlayerClaim.getPrice());
                    playerClaim.removeClaimBlock(blockAmount);
                    return true;
                }

                if(args[0].equalsIgnoreCase("show"))
                {
                    if(!landController.canShowLandClaim(args[1])) return false;
                    Land PlayerLand = landStore.getPlayerLandByName(player, args[1]);
                    PlayerLand.showArea();
                    return true;
                }

                if(args[0].equalsIgnoreCase("hide"))
                {
                    if(!landController.canHideLandClaim(args[1])) return false;
                    Land PlayerLand = landStore.getPlayerLandByName(player, args[1]);
                    PlayerLand.hideArea();
                    return true;
                }

                if(args[0].equalsIgnoreCase("delete"))
                {
                    if(!landController.canDeleteLand(args[1])) return false;
                    Land PlayerLand = landStore.getPlayerLandByName(player, args[1]);
                    landStore.getLandList().remove(PlayerLand);
                    playerClaim.removeClaimedBlock(PlayerLand.getAirOfClaim());
                    playerClaim.addClaimBlock(PlayerLand.getAirOfClaim());
                    playerClaim.setNbLand(playerClaim.getNbLand() - 1);
                    return true;
                }

                if(args[0].equalsIgnoreCase("info"))
                {
                    return !landController.canShowLandInfo(args[1]);
                }
            }

            if(args.length == 3)
            {
                if(args[0].equalsIgnoreCase("invite"))
                {
                    if(!landController.canInvitePlayerOnClaim(Bukkit.getPlayer(args[1]), args[2])) return false;
                    Land playerLand = SPLand.getInstance().getLandStore().getPlayerLandByName(player, args[2]);
                    playerLand.getPlayerList().put(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId(), new PlayerLand(playerLand.getId(), Bukkit.getPlayer(args[1]).getUniqueId().toString()));
                    return true;
                }

                if(args[0].equalsIgnoreCase("remove"))
                {
                    if(!landController.canRemovePlayer(Bukkit.getPlayer(args[1]), args[2])) return false;
                    Land playerLand = landStore.getPlayerLandByName(player, args[2]);
                    playerLand.getPlayerList().remove(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId());
                    return true;
                }

                if(args[0].equalsIgnoreCase("setpublicinteract"))
                {
                    if(!landController.canSetPublicInteract(args[1], args[2])) return false;
                    Land playerLand = landStore.getPlayerLandByName(player, args[1]);
                    if(args[2].equalsIgnoreCase("on"))
                    {
                        playerLand.setPublicInteract(true);
                    }
                    if(args[2].equalsIgnoreCase("off"))
                    {
                        playerLand.setPublicInteract(false);
                    }
                    return true;
                }
            }

        player.sendMessage("§cCommande incorrect");
        return false;
    }
}
