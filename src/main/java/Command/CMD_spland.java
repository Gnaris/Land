package Command;

import LandStore.LandItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_spland implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("claim"))
            {
                player.getInventory().addItem(LandItem.getLandItem());
                return true;
            }
        }

        player.sendMessage("§cMauvaise commande");
        return false;
    }
}
