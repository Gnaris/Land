package Command;

import Command.Controller.LandController;
import Economy.EconomyPlugin;
import Entity.Economy;
import Entity.Land;
import Entity.LandSecurity;
import LandMain.LandMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
                    if(landController.canListLand())
                    {
                        StringBuilder lands = new StringBuilder();
                        plugin.getLands().get(player.getUniqueId()).values().forEach(land -> lands.append(land.getRegionName()).append(" "));
                        if(plugin.getLandProgress().get(player.getUniqueId()) != null)
                        {
                            lands.append(plugin.getLandProgress().get(player.getUniqueId()).getRegionName()).append(" : §c§lOFF").append(" ");
                        }
                        player.sendMessage(ChatColor.of("#00FF00") + "Voici la liste de vos terrains : " + lands);
                        return true;
                    }

                    return false;
                }

                if(args[0].equalsIgnoreCase("pdelete"))
                {
                    if(landController.hasLandProgress())
                    {
                        plugin.getLandProgress().remove(player.getUniqueId());
                        player.sendMessage(ChatColor.of("#00FF00") + "Cette région a été supprimée");
                        return true;
                    }

                    return false;
                }

                if(args[0].equalsIgnoreCase("confirm"))
                {
                    if(!landController.canConfirmLand()) return false;
                    plugin.getLands().computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());

                    Land land = plugin.getLandProgress().get(player.getUniqueId());
                    plugin.getLands().get(player.getUniqueId()).put(land.getRegionName(), land);
                    plugin.getLandProgress().remove(player.getUniqueId());

                    EconomyPlugin economyPlugin = (EconomyPlugin) Bukkit.getServer().getPluginManager().getPlugin("Economy");
                    Economy playerEconomy = economyPlugin.getEconomies().get(player.getUniqueId());
                    playerEconomy.remove(land.getArea() * plugin.getClaimPrice());

                    player.sendMessage(ChatColor.of("#00FF00") + "La région " + land.getRegionName() + " a bien été sauvegardée");
                    return true;
                }
            }

            if(args.length == 2)
            {
                String landName = args[1];
                if(args[0].equalsIgnoreCase("showarea"))
                {
                    if(landController.hasLand(landName))
                    {
                        plugin.getLands().get(player.getUniqueId()).get(landName).showArea();
                        player.sendMessage(ChatColor.of("#00FF00") + "Pour ne plus voir la zone, faites /land hidearea " + landName);
                        return true;
                    }

                    return false;
                }

                if(args[0].equalsIgnoreCase("hidearea"))
                {
                    if(landController.hasLand(landName))
                    {
                        plugin.getLands().get(player.getUniqueId()).get(landName).hideArea();
                        player.sendMessage(ChatColor.of("#00FF00") + "C'est fait !");
                        return true;
                    }

                    return false;
                }

                if(args[0].equalsIgnoreCase("delete"))
                {
                    if(landController.canDeleteLand(landName))
                    {
                        plugin.getLands().get(player.getUniqueId()).remove(landName);
                        player.sendMessage(ChatColor.of("#00FF00") + "La région " + landName + " a été supprimée");
                        return true;
                    }

                    return false;
                }

                if(args[0].equalsIgnoreCase("info"))
                {
                    if(landController.hasLand(landName))
                    {
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
                                "Liste des membres dans votre villes :" + members
                        );
                        return true;
                    }

                    return false;
                }
                if(args[0].equalsIgnoreCase("create"))
                {
                    if(!landController.canCreateLand(landName)) return false;
                    plugin.getLandProgress().put(player.getUniqueId(), new Land(player.getUniqueId(), landName, false));
                    player.sendMessage(ChatColor.of("#00FF00") + "Félicitation le terrain " + landName + " a bien été crée ! Vous avez jusqu'au prochain redemarrage pour completer votre région ou il sera supprimé");
                    return true;
                }

                if(args[0].equalsIgnoreCase("setspawn"))
                {
                    if(!landController.canSetSpawn(landName)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setSpawnLocation(player.getLocation());
                    player.sendMessage(ChatColor.of("#00FF00") + "Un nouveau spawn a été défini dans cette région");
                    return true;
                }
                if(args[0].equalsIgnoreCase("deletespawn"))
                {
                    if(!landController.canDeleteSpawn(landName)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setSpawnLocation(null);
                    player.sendMessage(ChatColor.of("#00FF00") + "Le spawn a été détruit");
                    return true;
                }
                if(args[0].equalsIgnoreCase("spawn"))
                {
                    if(!landController.canGoSpawn(landName)) return false;
                    player.teleport(plugin.getLands().get(player.getUniqueId()).get(landName).getSpawnLocation());
                    player.sendMessage(ChatColor.of("#00FF00") + "Pouf !");
                    return true;
                }
            }


            if(args.length == 3)
            {
                String value = args[1];
                Player target = Bukkit.getPlayer(args[1]);
                String landName = args[2];
                if(args[0].equalsIgnoreCase("spawn"))
                {
                    if(!landController.canGoOtherSpawn(target, landName)) return false;
                    player.teleport(plugin.getLands().get(target.getUniqueId()).get(landName).getSpawnLocation());
                    player.sendMessage(ChatColor.of("#00FF00") + "Pouf !");
                    return true;
                }
                if(args[0].equalsIgnoreCase("invite"))
                {
                    if(landController.canInviteMember(target, landName))
                    {
                        plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().add(target.getUniqueId());
                        player.sendMessage(ChatColor.of("#00FF00") + "Vous avez inviter " + target.getName() + " dans votre ville");
                        target.sendMessage(ChatColor.of("#00FF00") + player.getName() + " vous a invité dans sa ville");
                        return true;
                    }
                }

                if(args[0].equalsIgnoreCase("remove"))
                {
                    if(landController.canRemovePlayer(target, landName))
                    {
                        plugin.getLands().get(player.getUniqueId()).get(landName).getMembers().remove(target.getUniqueId());
                        player.sendMessage(ChatColor.of("#00FF00") + "Ce joueur a été supprimé du terrain");
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("setinteract"))
                {
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.INTERACT, value)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setInteract(value.equalsIgnoreCase("on"));
                    player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'interaction avec les blocs en " + value);
                    return true;
                }

                if(args[0].equalsIgnoreCase("setmobspawn"))
                {
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.MONSTER_SPAWN, value)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setMonsterSpawn(value.equalsIgnoreCase("on"));
                    player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'apparaition des monstres en " + value);
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitmob"))
                {
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.HIT_MONSTER, value)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setHitMonster(value.equalsIgnoreCase("on"));
                    player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'hostilité contre les monstres en " + value);
                    return true;
                }

                if(args[0].equalsIgnoreCase("sethitanimal"))
                {
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.HIT_ANIMAL, value)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setHitAnimal(value.equalsIgnoreCase("on"));
                    player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état d'hostilité contre les animaux en " + value);
                    return true;
                }

                if(args[0].equalsIgnoreCase("setcrops"))
                {
                    if(!landController.canHandleLandSecurity(landName, LandSecurity.CROPS, value)) return false;
                    plugin.getLands().get(player.getUniqueId()).get(landName).setCrops(value.equalsIgnoreCase("on"));
                    player.sendMessage(ChatColor.of("#00FF00") + "Changement d'état sur la récolte en " + value);
                    return true;
                }
            }
        return false;
    }
}
