package Command;

import Command.Controller.SPLandController;
import Entity.Land;
import Entity.LandSecurity;
import Land.LandMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
        if(!landController.hasPermission("sperias.city.command.staff")) return false;

        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                player.sendMessage("§aVoici la liste des claims staff :");
                StringBuilder cities = new StringBuilder();
                for (String name : plugin.getSafeLands().keySet()) cities.append("§6" + name).append(" ");
                player.sendMessage(cities.toString());
                return true;
            }
        }

        if(args.length == 2)
        {
            String landName = args[1];
            if(args[0].equalsIgnoreCase("setfirstlocation"))
            {
                if(!landController.canSetFirstLocationOnCity(landName, player.getLocation())) return false;

                plugin.getSafeLands().get(landName).setFirstLocation(player.getLocation());

                player.sendMessage("§aPremière positon sauvegardée, /spcity setsecondlocation " + landName );

                return true;
            }
            if(args[0].equalsIgnoreCase("setsecondlocation"))
            {
                if(!landController.canSetSecondLocationOnCity(landName, player.getLocation())) return false;

                plugin.getSafeLands().get(landName).setSecondLocation(player.getLocation());

                player.sendMessage("§aPour valider votre ville, faites /spcity confirm " + landName);

                return true;
            }
            if(args[0].equalsIgnoreCase("confirm"))
            {
                if(!landController.canConfirmCity(landName)) return false;

                player.sendMessage("§aLa région de votre ville a bien été sauvegardée");
                return true;
            }
            if(args[0].equalsIgnoreCase("create"))
            {
                if(!landController.canCreateLand(landName)) return false;

                plugin.getSafeLands().put(landName, new Land(player.getUniqueId(), landName));

                player.sendMessage("§cFélicitation le terrain " + landName + " a bien été crée ! \n" +
                        " Vous avez jusqu'au prochain redemarrage pour completer votre terrain ou il sera supprimé");
                return true;
            }

            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!landController.canDeleteCity(landName)) return false;

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
