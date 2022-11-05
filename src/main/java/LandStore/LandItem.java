package LandStore;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LandItem {

    public static ItemStack getLandItem()
    {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eBâton de terrain");
            meta.setLore(Arrays.asList("", "§eBâton de terrain", "§aClique droit d'un point A à un point B en diagonale"));
            meta.addEnchant(Enchantment.DURABILITY, 5, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stick.setItemMeta(meta);
        return stick;
    }
}
