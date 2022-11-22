package Command;

import Command.Controller.LandController;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import Entity.LandSecurity;
import Land.LandMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CMD_Land implements CommandExecutor {

    private LandMain plugin;

    public CMD_Land(LandMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            LandController landController = new LandController(player, plugin);

            if(args.length == 2)
            {
                String landName = args[1];
                if(args[0].equalsIgnoreCase("showarea"))
                {
                    if(!landController.hasLand(landName)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).showArea();

                    player.sendMessage("§aPour ne plus voir la zone, faites /land hidearea " + landName);
                    return true;
                }

                if(args[0].equalsIgnoreCase("hidearea"))
                {
                    if(!landController.hasLand(landName)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).hideArea();

                    player.sendMessage("§aC'est fait !");
                    return true;
                }

                if(args[0].equalsIgnoreCase("delete"))
                {
                    if(!landController.canDeleteCity(landName)) return false;

                    plugin.getLands().get(player.getUniqueId()).remove(landName);

                    player.sendMessage("§aLa ville " + landName + " a été supprimée");
                    return true;
                }

                if(args[0].equalsIgnoreCase("info"))
                {
                    if(!landController.hasLand(landName)) return false;

                    Land land = plugin.getLands().get(player.getUniqueId()).get(landName);

                    player.sendMessage("Nom : " + land.getRegionName());
                    player.sendMessage("");
                    if((land.getFirstLocation() != null && land.getSecondLocation() != null) || (land.getMinLocation() != null || land.getFirstLocation() != null))
                    {
                        player.sendMessage("Claim : ");
                        player.sendMessage("Position A : X :" + land.getMinLocation().getX() + " Z : " + land.getMinLocation().getZ());
                        player.sendMessage("Position B : X :" + land.getMaxLocation().getX() + " Z : " + land.getMaxLocation().getZ());
                    }
                    player.sendMessage("");
                    player.sendMessage("Liste des membres dans votre villes :");
                    StringBuilder members = new StringBuilder();
                    if(land.getMembers().size() > 0)
                    {
                        land.getMembers().stream()
                                .filter(member -> Bukkit.getPlayer(member) != null)
                                .forEach(member -> members.append(Bukkit.getPlayer(member).getName()).append(" "));
                        player.sendMessage(members.toString());
                    }
                    else
                    {
                        player.sendMessage("Tu es tout seul :(");
                    }
                    return true;
                }

                if(args[0].equalsIgnoreCase("setfirstposition"))
                {
                    if(!landController.canSetFirstLocationOnLand(landName, player.getLocation())) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setFirstLocation(player.getLocation());

                    player.sendMessage("§aPremière positon sauvegardée, /land setsecondposition " + landName);

                    return true;
                }
                if(args[0].equalsIgnoreCase("setlastposition"))
                {
                    if(!landController.canSetSecondLocationOnCity(landName, player.getLocation())) return false;

                    Land newLand =  plugin.getLands().get(player.getUniqueId()).get(landName);
                    newLand.setSecondLocation(player.getLocation());
                    newLand.buildLandLocation();

                    player.sendMessage("§aPrix de votre terrain : " + newLand.getArea() * plugin.getConfig().getInt("price") + "\n" +
                            "Pour confirmer, faites /land confirm " + landName);
                    return true;
                }
                if(args[0].equalsIgnoreCase("confirm"))
                {
                    if(!landController.canConfirmCity(landName)) return false;

                    EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
                    Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
                    playerEconomy.remove(plugin.getLands().get(player.getUniqueId()).get(landName).getArea() * plugin.getConfig().getInt("price"));

                    player.sendMessage("§aLa région de votre ville a bien été sauvegardée");
                    return true;
                }

                if(args[0].equalsIgnoreCase("create"))
                {
                    if(!landController.canCreateCity(landName)) return false;

                    plugin.getLands().computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
                    plugin.getLands().get(player.getUniqueId()).put(landName, new Land(player.getUniqueId(), landName, false));

                    player.sendMessage("§aFélicitation le terrain " + landName + " a bien été crée ! \n" +
                            " Vous avez jusqu'au prochain redemarrage pour completer votre terrain ou il sera supprimé");
                    return true;
                }
            }


            if(args.length == 3)
            {
                String landName = args[2];
                if(args[0].equalsIgnoreCase("invite"))
                {
                    Player target = Bukkit.getPlayer(args[1]);
                    if(!landController.canInviteMember(target, landName)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().add(target.getUniqueId());

                    player.sendMessage("§aVous avez inviter " + target.getName() + " dans votre ville");
                    target.sendMessage("§a" + player.getName() + " vous a invité dans sa ville");
                    return true;
                }

                if(args[0].equalsIgnoreCase("remove"))
                {
                    Player target = Bukkit.getPlayer(args[1]);
                    if(!landController.canRemovePlayer(target, landName)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().remove(target.getUniqueId());

                    player.sendMessage("§aCe joueur a été supprimé du terrain");
                    return true;
                }

                if(args[0].equalsIgnoreCase("setinteract"))
                {
                    String value = args[1];
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.INTERACT, value)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setInteract(value.equalsIgnoreCase("on"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("setmobspawn"))
                {
                    String value = args[1];
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.MONSTER_SPAWN, value)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setMonsterSpawn(value.equalsIgnoreCase("on"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitmob"))
                {
                    String value = args[1];
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.HIT_MONSTER, value)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setHitMonster(value.equalsIgnoreCase("on"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitanimal"))
                {
                    String value = args[1];
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.HIT_ANIMAL, value)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setHitAnimal(value.equalsIgnoreCase("on"));
                    return true;
                }

                if(args[0].equalsIgnoreCase("setcrops"))
                {
                    String value = args[1];
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.CROPS, value)) return false;

                    plugin.getLands().get(player.getUniqueId()).get(landName).setCrops(value.equalsIgnoreCase("on"));
                    return true;
                }
            }

        player.sendMessage("§cCommande incorrect");
        return false;
    }
}
