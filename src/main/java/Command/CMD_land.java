package Command;

import Command.Controller.LandController;
import Entity.Land;
import Entity.PlayerClaim;
import Entity.PlayerLand;
import Entity.Wallet;
import LandStore.LandStore;
import LandStore.PlayerStore;
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
            PlayerStore playerStore = SPLand.getInstance().getPlayerStore();
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
            }


            if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("confirm"))
                {
                    if(!landController.canConfirmClaim(args[1])) return false;

                    Land playerLand = landStore.getPlayerLandNotConfirmed(player).get(0);
                    playerLand.confirmLand(args[1]);
                    Bukkit.getScheduler().cancelTask(playerLand.getSchedulerID());

                    PlayerClaim playerClaim = playerStore.getPlayerList().get(player.getUniqueId());
                    playerClaim.removeClaimBlock(playerLand.getAirOfClaim());
                    playerClaim.addClaimedBlock(playerLand.getAirOfClaim());

                    return true;
                }

                if(args[0].equalsIgnoreCase("buyclaimblock"))
                {
                    int amount;
                    try{
                        amount = Integer.parseInt(args[1]);
                    }catch (NumberFormatException e)
                    {
                        player.sendMessage("§c" + args[1] + " n'est pas un montant valide");
                        return false;
                    }
                    PlayerClaim playerClaim = playerStore.getPlayerList().get(player.getUniqueId());
                    if(!landController.canBuyClaimBlock(playerWallet, amount)) return false;
                    playerWallet.remove(amount * PlayerClaim.getPrice());
                    playerClaim.addClaimBlock(amount);
                    return true;
                }

                if(args[0].equalsIgnoreCase("sellclaimblock"))
                {
                    int amount;
                    try{
                        amount = Integer.parseInt(args[1]);
                    }catch (NumberFormatException e)
                    {
                        player.sendMessage("§c" + args[1] + " n'est pas un montant valide");
                        return false;
                    }
                    PlayerClaim playerClaim = playerStore.getPlayerList().get(player.getUniqueId());
                    if(!landController.canSellClaimBlock(amount)) return false;
                    playerWallet.add(amount * PlayerClaim.getPrice());
                    playerClaim.removeClaimBlock(amount);
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
                    if(!landController.canDeleteClaim(args[1])) return false;
                    Land PlayerLand = landStore.getPlayerLandByName(player, args[1]);
                    landStore.getLandList().remove(PlayerLand);
                    PlayerClaim playerClaim = playerStore.getPlayerList().get(player.getUniqueId());
                    playerClaim.removeClaimedBlock(PlayerLand.getAirOfClaim());
                    playerClaim.addClaimBlock(PlayerLand.getAirOfClaim());
                    return true;
                }
            }

            if(args.length == 3)
            {
                if(args[0].equalsIgnoreCase("invite"))
                {
                    if(!landController.canInvitePlayerOnClaim(Bukkit.getPlayer(args[1]), args[2])) return false;
                    Land playerLand = SPLand.getInstance().getLandStore().getPlayerLandByName(player, args[2]);
                    playerLand.getPlayerList().put(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId(), new PlayerLand(playerLand.getId(), Bukkit.getPlayer(args[1]).getUniqueId().toString(), false,  false, false));
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
