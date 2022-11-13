package Controller;

import Entity.Land;
import Entity.PlayerClaim;
import SPLand.SPLand;
import SPWallet.SPWallet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Controller{

    protected Player player;
    protected PlayerClaim playerClaim;
    protected SPLand plugin;
    protected final SPWallet wallet = (SPWallet) Bukkit.getServer().getPluginManager().getPlugin("SP_Wallet");

    public Controller(Player player, SPLand plugin) {
        this.player = player;
        this.plugin = plugin;
        playerClaim = plugin.getPlayerClaimList().get(player.getUniqueId());
    }

    public boolean isLongFormat(String amount)
    {
        try{
            Long.parseLong(amount);
        }catch (NumberFormatException e)
        {
            player.sendMessage("§c" + amount + " n'est pas un montant valide");
            return false;
        }
        if(Long.parseLong(amount) < 0)
        {
            player.sendMessage("§cLe montant ne pas être négatif");
            return false;
        }

        return true;
    }

    public boolean claimInProgress()
    {
        if(plugin.getPlayerLandNotConfirmed(player).size() == 0)
        {
            player.sendMessage("§cTu n'as pas de claim en cours !");
            return false;
        }
        return true;
    }

    public boolean checkTarget(Player target)
    {
        if(target == null)
        {
            player.sendMessage("§cCe joueur n'existe pas");
            return false;
        }
        if(target == player)
        {
            player.sendMessage("§c...");
            return false;
        }

        return true;
    }

    public boolean existingLand(String name)
    {
        Land playerLand = plugin.getPlayerLandByName(player, name);
        if(playerLand == null)
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean existingStaffLand(String name)
    {
        Land staffLand = plugin.getStaffLandByName(name);
        if(staffLand == null)
        {
            player.sendMessage("§cCe terrain n'existe pas");
            return false;
        }
        return true;
    }

    public boolean havePermission(String permission)
    {
        if(!player.hasPermission(permission) && !player.isOp())
        {
            player.sendMessage("§cVous n'avez pas la permission");
            return false;
        }
        return true;
    }

    protected boolean canHandleLandParameter(String name, String value)
    {
        if(!this.existingLand(name)) return false;

        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }

        return true;
    }

    protected boolean canHandleStaffLandParameter(String name, String value)
    {
        if(!this.existingStaffLand(name)) return false;

        if(!value.equalsIgnoreCase("on") && !value.equalsIgnoreCase("off"))
        {
            player.sendMessage("§c" + value + " n'est pas une valeur correct. (§aON §r| §cOFF)");
            return false;
        }

        return true;
    }
}
