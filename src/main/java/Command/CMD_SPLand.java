package Command;

import Command.Controller.SPLandController;
import Entity.Land;
import Entity.LandSecurity;
import LandMain.LandMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_SPLand implements CommandExecutor{

    private final LandMain plugin;

    public CMD_SPLand(LandMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        SPLandController landController = new SPLandController(player, plugin);
        if(!landController.hasPermission("sperias.land.command.staff")) return false;

        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                player.sendMessage("§aVoici la liste des régions staff :");
                StringBuilder lands = new StringBuilder();
                plugin.getSafeLands().values().forEach(l -> lands.append(l.getRegionName()).append(" "));
                plugin.getLandProgress().values().stream().filter(Land::isSafeZone).forEach(l -> lands.append(l.getRegionName()).append(" : §c§lOFF").append(" "));
                player.sendMessage(lands.toString());
                return true;
            }
        }

        if(args.length == 2)
        {
            String landName = args[1];
            if(args[0].equalsIgnoreCase("setposition1") || args[0].equalsIgnoreCase("setpos1"))
            {
                if(!landController.canSetPosition1(player.getLocation())) return false;
                plugin.getSafeLands().get(landName).setPosition1(player.getLocation());
                player.sendMessage("§aPremière positon sauvegardée, /spland setpos2 " + landName );
                return true;
            }
            if(args[0].equalsIgnoreCase("setposition2") || args[0].equalsIgnoreCase("setpos2"))
            {
                if(!landController.canSetPosition2(player.getLocation())) return false;
                plugin.getLandProgress().get(player.getUniqueId()).setPosition2(player.getLocation());
                player.sendMessage("§aPour valider votre région, faites /spland confirm " + landName);
                return true;
            }
            if(args[0].equalsIgnoreCase("confirm"))
            {
                if(!landController.canConfirmLand(landName)) return false;
                plugin.getSafeLands().put(landName, plugin.getLandProgress().get(player.getUniqueId()));
                plugin.getLandProgress().remove(player.getUniqueId());
                player.sendMessage("§aLa région du " + landName + " a bien été enregistrée");
                return true;
            }
            if(args[0].equalsIgnoreCase("create"))
            {
                if(!landController.canCreateLand(landName)) return false;
                plugin.getLandProgress().put(player.getUniqueId(), new Land(player.getUniqueId(), landName, true));
                player.sendMessage("§aFélicitation le terrain " + landName + " a bien été crée ! \n" +
                        " Vous avez jusqu'au prochain redemarrage pour completer votre terrain ou il sera supprimé");
                return true;
            }

            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!landController.canDeleteLand(landName)) return false;
                plugin.getSafeLands().remove(landName);
                player.sendMessage(landName + " a été supprimé avec succès !");
                return true;
            }
        }

        if(args.length == 3)
        {
            String landName = args[1];
            String value = args[2];
            if(args[0].equalsIgnoreCase("setinteract"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.INTERACT, value)) return false;

                plugin.getSafeLands().get(landName).setInteract(value.equalsIgnoreCase("on"));
                return true;
            }

            if(args[0].equalsIgnoreCase("setmobspawn"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.MONSTER_SPAWN, value)) return false;

                plugin.getSafeLands().get(landName).setMonsterSpawn(value.equalsIgnoreCase("on"));
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitmob"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.HIT_MONSTER, value)) return false;

                plugin.getSafeLands().get(landName).setHitMonster(value.equalsIgnoreCase("on"));
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitanimal"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.HIT_ANIMAL, value)) return false;

                plugin.getSafeLands().get(landName).setHitAnimal(value.equalsIgnoreCase("on"));
                return true;
            }

            if(args[0].equalsIgnoreCase("setcrops"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.CROPS, value)) return false;

                plugin.getSafeLands().get(landName).setCrops(value.equalsIgnoreCase("on"));
                return true;
            }
        }

        player.sendMessage("§cMauvaise commande");
        return false;
    }
}
