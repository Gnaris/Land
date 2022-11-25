package Command;

import Command.Controller.SPLandController;
import Entity.Land;
import Entity.LandSecurity;
import Item.ClaimItem;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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
            if(args[0].equalsIgnoreCase("claim"))
            {
                player.getInventory().addItem(ClaimItem.getClaimItem());
                return true;
            }
            if(args[0].equalsIgnoreCase("list"))
            {
                player.sendMessage("§aVoici la liste des régions staff :");
                StringBuilder lands = new StringBuilder();
                plugin.getSafeLands().values().forEach(l -> lands.append(l.getRegionName()).append(" "));
                plugin.getLandProgress().values().stream().filter(Land::isSafeZone).forEach(l -> lands.append(l.getRegionName()).append(" : §c§lOFF").append(" "));
                player.sendMessage(lands.toString());
                return true;
            }
            if(args[0].equalsIgnoreCase("confirm"))
            {
                if(!landController.canConfirmLand()) return false;
                plugin.getSafeLands().put(plugin.getLandProgress().get(player.getUniqueId()).getRegionName(), plugin.getLandProgress().get(player.getUniqueId()));
                plugin.getLandProgress().remove(player.getUniqueId());
                player.sendMessage(ChatColor.of("#00FF00") + "La région du " + plugin.getLandProgress().get(player.getUniqueId()).getRegionName() + " a bien été sauvegardée");
                return true;
            }
        }

        if(args.length == 2)
        {
            String landName = args[1];
            if(args[0].equalsIgnoreCase("create"))
            {
                if(!landController.canCreateLand(landName)) return false;
                plugin.getLandProgress().put(player.getUniqueId(), new Land(player.getUniqueId(), landName, true));
                player.sendMessage(ChatColor.of("#00FF00") + "Félicitation le terrain " + landName + " a bien été crée ! Vous avez jusqu'au prochain redemarrage pour completer votre terrain ou il sera supprimé");
                return true;
            }

            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!landController.canDeleteLand(landName)) return false;
                plugin.getSafeLands().remove(landName);
                player.sendMessage(ChatColor.of("#00FF00") + landName + " a été supprimé avec succès !");
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
                player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'interaction avec les blocs en " + value);
                return true;
            }

            if(args[0].equalsIgnoreCase("setmobspawn"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.MONSTER_SPAWN, value)) return false;
                plugin.getSafeLands().get(landName).setMonsterSpawn(value.equalsIgnoreCase("on"));
                player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'apparaition des monstres en " + value);
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitmob"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.HIT_MONSTER, value)) return false;
                plugin.getSafeLands().get(landName).setHitMonster(value.equalsIgnoreCase("on"));
                player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'hostilité contre les monstres en " + value);
                return true;
            }

            if(args[0].equalsIgnoreCase("sethitanimal"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.HIT_ANIMAL, value)) return false;
                plugin.getSafeLands().get(landName).setHitAnimal(value.equalsIgnoreCase("on"));
                player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'hostilité contre les animaux en " + value);
                return true;
            }

            if(args[0].equalsIgnoreCase("setcrops"))
            {
                if(!landController.canHandleSafeLandSecurity(landName, LandSecurity.CROPS, value)) return false;
                plugin.getSafeLands().get(landName).setCrops(value.equalsIgnoreCase("on"));
                player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état sur la récolte en " + value);
                return true;
            }
        }

        player.sendMessage("§cMauvaise commande");
        return false;
    }
}
