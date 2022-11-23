package Command;

import Command.Controller.LandController;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import Entity.LandSecurity;
import LandMain.LandMain;
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
            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("list"))
                {
                    if(!landController.canListLand()) return false;
                    StringBuilder lands = new StringBuilder();
                    plugin.getLands().get(player.getUniqueId()).values().forEach(land -> lands.append(land.getRegionName()).append(" "));
                    if(plugin.getLandProgress().get(player.getUniqueId()) != null)
                    {
                        lands.append(plugin.getLandProgress().get(player.getUniqueId()).getRegionName()).append(" : §c§lOFF").append(" ");
                    }
                    player.sendMessage("§cVoici la liste de vos terrains : " + lands.toString());
                    return true;
                }
            }

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
                    if(!landController.canDeleteLand(landName)) return false;
                    plugin.getLands().get(player.getUniqueId()).remove(landName);
                    player.sendMessage("§aLa région " + landName + " a été supprimée");
                    return true;
                }

                if(args[0].equalsIgnoreCase("pdelete"))
                {
                    if(!landController.hasLandProgress()) return false;
                    plugin.getLandProgress().remove(player.getUniqueId());
                    player.sendMessage("§aLa région " + landName + " a été supprimée");
                    return true;
                }

                if(args[0].equalsIgnoreCase("info"))
                {
                    if(!landController.hasLand(landName)) return false;
                    Land land = plugin.getLands().get(player.getUniqueId()).get(landName);
                    StringBuilder members = new StringBuilder();
                    if(land.getMembers().size() > 0)
                    {
                        land.getMembers().stream()
                                .filter(member -> Bukkit.getPlayer(member) != null)
                                .forEach(member -> members.append(Bukkit.getPlayer(member).getName()).append(" "));
                        player.sendMessage(members.toString());
                    }
                    player.sendMessage("Nom : " + land.getRegionName() + "\n" +
                            "Claim : \n" +
                            "Position 1 : X : " + land.getMinLocation().getX() + " Z : " + land.getMinLocation().getZ() + "\n" +
                            "Position 2 : X : " + land.getMaxLocation().getX() + " Z : " + land.getMaxLocation().getZ() + "\n" +
                            "Liste des membres dans votre villes :" + members.toString()
                    );
                    return true;
                }

                if(args[0].equalsIgnoreCase("setposition1") || args[0].equalsIgnoreCase("setpos1"))
                {
                    if(!landController.canSetPosition1(player.getLocation())) return false;
                    plugin.getLandProgress().get(player.getUniqueId()).setPosition1(player.getLocation());
                    player.sendMessage("§aPremière position sauvegardée, /land setpos2 " + landName);
                    return true;
                }
                if(args[0].equalsIgnoreCase("setposition2") || args[0].equalsIgnoreCase("setpos2"))
                {
                    if(!landController.canSetPosition2(player.getLocation())) return false;
                    plugin.getLandProgress().get(player.getUniqueId()).setPosition2(player.getLocation());
                    player.sendMessage("§aCoût de votre région : " + plugin.getLandProgress().get(player.getUniqueId()).getArea() * plugin.getConfig().getInt("price") + "\n" +
                                          "Pour confirmer, faites /land confirm " + landName);
                    return true;
                }
                if(args[0].equalsIgnoreCase("confirm"))
                {
                    if(!landController.canConfirmLand()) return false;
                    plugin.getLands().computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
                    plugin.getLands().get(player.getUniqueId()).put(landName, plugin.getLandProgress().get(player.getUniqueId()));
                    plugin.getLandProgress().remove(player.getUniqueId());
                    EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
                    Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
                    playerEconomy.remove(plugin.getLands().get(player.getUniqueId()).get(landName).getArea() * plugin.getConfig().getInt("price"));
                    player.sendMessage("§aLa région de votre ville a bien été sauvegardée");
                    return true;
                }

                if(args[0].equalsIgnoreCase("create"))
                {
                    if(!landController.canCreateLand(landName)) return false;
                    plugin.getLandProgress().put(player.getUniqueId(), new Land(player.getUniqueId(), landName, false));
                    player.sendMessage("§aFélicitation le terrain " + landName + " a bien été crée ! \n" +
                            " Vous avez jusqu'au prochain redemarrage pour completer votre région ou il sera supprimé");
                    return true;
                }

                if(args[0].equalsIgnoreCase("setspawn"))
                {
                    if(!landController.canSetSpawn(landName)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setSpawnLocation(player.getLocation());
                    player.sendMessage("§cUn nouveau spawn a été défini dans cette région");
                    return false;
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
