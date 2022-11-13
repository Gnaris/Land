package Command;

import Command.Controller.SPLandController;
import Entity.Land;
import LandStore.LandItem;
import SPLand.SPLand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_spland implements CommandExecutor{

    private SPLand plugin;

    public CMD_spland(SPLand plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        SPLandController landController = new SPLandController(player, plugin);
        if(!landController.havePermission("sperias.land.command.staff")) return false;

        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("claim"))
            {
                player.getInventory().addItem(LandItem.getLandItem());
                return true;
            }

            if(args[0].equalsIgnoreCase("list"))
            {
                player.sendMessage("§aVoici la liste des claims staff :");
                StringBuilder landList = new StringBuilder();
                plugin.getStaffLandList().forEach(land -> landList.append(ChatColor.of("#FFB927")).append(land.getLandName()).append(" "));
                player.sendMessage(landList.toString());
                return true;
            }
        }

        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("confirm"))
            {
                String landName = args[1];
                if(!landController.canConfirmStaffClaim(landName)) return false;

                Land staffLand = plugin.getPlayerLandNotConfirmed(player).get(0);
                staffLand.setLandName(landName);
                staffLand.setLandID(plugin.getMaxID() + 1);
                staffLand.setConfirmed(true);
                staffLand.setStaffClaim(true);
                staffLand.hideArea();
                Bukkit.getScheduler().cancelTask(staffLand.getSchedulerID());

                player.sendMessage("§aFélicitation, un nouveau terrain a été créee sous le nom de " + landName);
                return true;
            }

            if(args[0].equalsIgnoreCase("delete"))
            {
                String landName = args[1];
                if(!landController.canDeleteStaffClaim(landName)) return false;

                plugin.getLandList().remove(plugin.getStaffLandByName(landName));
                player.sendMessage(landName + " a été supprimé avec succès !");
                return true;
            }
        }

        if(args.length == 3)
        {
            if(args[0].equalsIgnoreCase("setinteract"))
            {
                String landName = args[1];
                String response = args[2];
                if(!landController.canSetInteract(landName, response)) return false;

                Land staffLand = plugin.getStaffLandByName(landName);
                if(response.equalsIgnoreCase("on"))
                {
                    staffLand.setInteract(true);
                    player.sendMessage("§aTout le monde pourra à présent intéragir avec vos blocs");
                }
                if(response.equalsIgnoreCase("off"))
                {
                    staffLand.setInteract(false);
                    player.sendMessage("§aPlus personne ne pourra à présent intéragir avec vos blocs");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setmobspawn"))
            {
                String landName = args[1];
                String response = args[2];
                if(!landController.canSetMobSpawn(landName, response)) return false;

                Land staffLand = plugin.getStaffLandByName(landName);
                if(response.equalsIgnoreCase("on"))
                {
                    staffLand.setMobSpawn(true);
                    player.sendMessage("§aLes monstres peuvent désormais apparaitre sur le terrain");
                }
                if(response.equalsIgnoreCase("off"))
                {
                    staffLand.setMobSpawn(false);
                    player.sendMessage("§aLes monstres n'apparaitront plus sur le terrain");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitmob"))
            {
                String landName = args[1];
                String response = args[2];
                if(!landController.canSetHitMob(landName, response)) return false;

                Land staffLand = plugin.getStaffLandByName(landName);
                if(response.equalsIgnoreCase("on"))
                {
                    staffLand.setHitMob(true);
                    player.sendMessage("§aTout le monde pourra à présent taper les monstres");
                }
                if(response.equalsIgnoreCase("off"))
                {
                    staffLand.setHitMob(false);
                    player.sendMessage("§aPlus personne pourra taper les monstres");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitanimal"))
            {
                String landName = args[1];
                String response = args[2];
                if(!landController.canSetHitAnimal(landName, response)) return false;

                Land staffLand = plugin.getStaffLandByName(landName);
                if(response.equalsIgnoreCase("on"))
                {
                    staffLand.setHitAnimal(true);
                    player.sendMessage("§aTout le monde pourra à présent taper les animaux");
                }
                if(response.equalsIgnoreCase("off"))
                {
                    staffLand.setHitAnimal(false);
                    player.sendMessage("§aPlus personne pourra taper les animaux");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setcrops"))
            {
                String landName = args[1];
                String response = args[2];
                if(!landController.canSetCrops(landName, response)) return false;

                Land staffLand = plugin.getStaffLandByName(landName);
                if(response.equalsIgnoreCase("on"))
                {
                    staffLand.setCrops(true);
                    player.sendMessage("§aTout le monde pourra à présent casser vos plantes");
                }
                if(response.equalsIgnoreCase("off"))
                {
                    staffLand.setCrops(false);
                    player.sendMessage("§aPlus personne pourra casser vos plantes");
                }
                return true;
            }
        }

        player.sendMessage("§cMauvaise commande");
        return false;
    }
}
