package Command;

import Command.Controller.LandController;
import Entity.Land;
import Entity.PlayerClaim;
import Entity.PlayerLand;
import Entity.Wallet;
import SPLand.SPLand;
import SPWallet.SPWallet;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CMD_land implements CommandExecutor {

    private SPLand plugin;
    private final SPWallet wallet = (SPWallet) Bukkit.getServer().getPluginManager().getPlugin("SP_Wallet");

    public CMD_land(SPLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            PlayerClaim playerClaim = plugin.getPlayerClaimList().get(player.getUniqueId());
            Wallet playerWallet = wallet.getWalletStore().get(player.getUniqueId());
            LandController landController = new LandController(player, plugin);


            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("cancel"))
                {
                    if(!landController.claimInProgress()) return false;

                    Land playerLand = plugin.getPlayerLandNotConfirmed(player).get(0);
                    playerLand.hideArea();
                    Bukkit.getScheduler().cancelTask(playerLand.getSchedulerID());

                    plugin.getLandList().remove(playerLand);

                    player.sendMessage("§cVous avez annuler votre claim");
                    return true;
                }

                if(args[0].equalsIgnoreCase("list"))
                {
                    player.sendMessage("§aVoici la liste des terrains que vous possédez :");
                    StringBuilder claimName = new StringBuilder();
                    plugin.getPlayerLandList(player).stream().filter(land -> !land.isStaffClaim()).forEach(land -> claimName.append(ChatColor.of("#FFB927")).append(land.getLandName()).append(" "));
                    player.sendMessage(claimName.toString());

                    return true;
                }
            }

            if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("confirm"))
                {
                    String landName = args[1];
                    if(!landController.canConfirmLand(landName)) return false;

                    Land playerLand = plugin.getPlayerLandNotConfirmed(player).get(0);
                    playerLand.setLandName(landName);
                    playerLand.setConfirmed(true);
                    playerLand.setLandID(plugin.getMaxID() + 1);
                    playerLand.hideArea();
                    Bukkit.getScheduler().cancelTask(playerLand.getSchedulerID());

                    playerClaim.removeClaimBlock(playerLand.getAirOfClaim());
                    playerClaim.addClaimedBlock(playerLand.getAirOfClaim());
                    playerClaim.setNbLand(playerClaim.getNbLand() + 1);

                    player.sendMessage("§aFélicitation, votre terrain a été créee sous le nom de " + landName);
                    return true;
                }

                if(args[0].equalsIgnoreCase("buyclaimblock"))
                {
                    if(!landController.canBuyClaimBlock(playerWallet, args[1])) return false;

                    long blockAmount = Long.parseLong(args[1]);
                    long totalPrice = blockAmount * plugin.getConfig().getInt("claim_block_price");
                    playerWallet.remove(totalPrice);
                    playerClaim.addClaimBlock(blockAmount);

                    player.sendMessage("§aVous venez d'acheter " + blockAmount + " bloc(s) de claim pour " + totalPrice + wallet.getConfig().getString("symbol"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("sellclaimblock"))
                {
                    if(!landController.canSellClaimBlock(args[1])) return false;

                    long blockAmount = Long.parseLong(args[1]);
                    long totalPrice = blockAmount * plugin.getConfig().getInt("claim_block_price");
                    playerWallet.add(totalPrice);
                    playerClaim.removeClaimBlock(blockAmount);

                    player.sendMessage("§aVous venez de vendre " + blockAmount + " bloc(s) pour " + totalPrice + wallet.getConfig().getString("symbol"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("show"))
                {
                    String landName = args[1];
                    if(!landController.existingLand(landName)) return false;

                    Land PlayerLand = plugin.getPlayerLandByName(player, args[1]);
                    PlayerLand.showArea();

                    player.sendMessage("§aPour cacher les claims faites /land hide " + landName);
                    return true;
                }

                if(args[0].equalsIgnoreCase("hide"))
                {
                    String landName = args[1];
                    if(!landController.existingLand(landName)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    playerLand.hideArea();

                    player.sendMessage("§aVous avez cacher votre claim");
                    return true;
                }

                if(args[0].equalsIgnoreCase("delete"))
                {
                    String landName = args[1];
                    if(!landController.canDeleteLand(landName)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);

                    plugin.getLandList().remove(playerLand);
                    playerClaim.removeClaimedBlock(playerLand.getAirOfClaim());
                    playerClaim.addClaimBlock(playerLand.getAirOfClaim());
                    playerClaim.setNbLand(playerClaim.getNbLand() - 1);

                    player.sendMessage("§aLe terrain " + landName + " a été supprimé avec succès");
                    return true;
                }

                if(args[0].equalsIgnoreCase("info"))
                {
                    String landName = args[1];
                    if(!landController.existingLand(landName)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);

                    player.sendMessage("Land : " + playerLand.getLandName());
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

            if(args.length == 3)
            {
                if(args[0].equalsIgnoreCase("invite"))
                {
                    Player target = Bukkit.getPlayer(args[1]);
                    String landName = args[2];
                    if(!landController.canInvitePlayerOnLand(target, landName)) return false;
                    PlayerClaim targetClaim = plugin.getPlayerClaimList().get(target.getUniqueId());
                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    playerLand.getPlayerList().put(target.getUniqueId(), new PlayerLand(targetClaim.getPlayerID(), playerLand.getLandID(), target.getUniqueId().toString()));

                    player.sendMessage("§aVous avez inviter " + target.getName() + " dans votre claim");
                    target.sendMessage("§a" + player.getName() + " vous a invité dans son claim");
                    return true;
                }

                if(args[0].equalsIgnoreCase("remove"))
                {
                    Player target = Bukkit.getPlayer(args[1]);
                    String landName = args[2];
                    if(!landController.canRemovePlayer(target, landName)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    playerLand.getPlayerList().remove(target.getUniqueId());

                    player.sendMessage("§aCe joueur a été supprimé du terrain");
                    return true;
                }

                if(args[0].equalsIgnoreCase("setinteract"))
                {
                    String landName = args[1];
                    String response = args[2];
                    if(!landController.canSetInteract(landName, response)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    if(response.equalsIgnoreCase("on"))
                    {
                        playerLand.setInteract(true);
                        player.sendMessage("§aTout le monde pourra à présent intéragir avec vos blocs");
                    }
                    if(response.equalsIgnoreCase("off"))
                    {
                        playerLand.setInteract(false);
                        player.sendMessage("§aPlus personne ne pourra à présent intéragir avec vos blocs");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("setmobspawn"))
                {
                    String landName = args[1];
                    String response = args[2];
                    if(!landController.canSetMobSpawn(landName, response)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    if(response.equalsIgnoreCase("on"))
                    {
                        playerLand.setMobSpawn(true);
                        player.sendMessage("§aLes monstres peuvent désormais apparaitre sur le terrain");
                    }
                    if(response.equalsIgnoreCase("off"))
                    {
                        playerLand.setMobSpawn(false);
                        player.sendMessage("§aLes monstres n'apparaitront plus sur le terrain");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitmob"))
                {
                    String landName = args[1];
                    String response = args[2];
                    if(!landController.canSetHitMob(landName, response)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    if(response.equalsIgnoreCase("on"))
                    {
                        playerLand.setHitMob(true);
                        player.sendMessage("§aTout le monde pourra à présent taper les monstres");
                    }
                    if(response.equalsIgnoreCase("off"))
                    {
                        playerLand.setHitMob(false);
                        player.sendMessage("§aPlus personne pourra taper les monstres");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitanimal"))
                {
                    String landName = args[1];
                    String response = args[2];
                    if(!landController.canSetHitAnimal(landName, response)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    if(response.equalsIgnoreCase("on"))
                    {
                        playerLand.setHitAnimal(true);
                        player.sendMessage("§aTout le monde pourra à présent taper les animaux");
                    }
                    if(response.equalsIgnoreCase("off"))
                    {
                        playerLand.setHitAnimal(false);
                        player.sendMessage("§aPlus personne pourra taper les animaux");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("setcrops"))
                {
                    String landName = args[1];
                    String response = args[2];
                    if(!landController.canSetCrops(landName, response)) return false;

                    Land playerLand = plugin.getPlayerLandByName(player, landName);
                    if(response.equalsIgnoreCase("on"))
                    {
                        playerLand.setCrops(true);
                        player.sendMessage("§aTout le monde pourra à présent casser vos plantes");
                    }
                    if(response.equalsIgnoreCase("off"))
                    {
                        playerLand.setCrops(false);
                        player.sendMessage("§aPlus personne pourra casser vos plantes");
                    }
                    return true;
                }
            }

        player.sendMessage("§cCommande incorrect");
        return false;
    }
}
