package Item;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClaimItem {

    public static ItemStack getClaimItem()
    {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        stickMeta.setDisplayName(ChatColor.of("#85CE51") + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Bâton de claim");
        stick.setItemMeta(stickMeta);
        return stick;
    }
}
