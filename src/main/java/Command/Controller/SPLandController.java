package Command.Controller;

import Controller.Controller;
import Entity.Land;
import Model.Thread.*;
import SPLand.SPLand;
import org.bukkit.entity.Player;

public class SPLandController extends Controller implements LandParameter{

    public SPLandController(Player player, SPLand plugin) {
        super(player, plugin);
    }

    public boolean canConfirmStaffClaim(String name)
    {
        if(!this.claimInProgress()) return false;

        Land staffLand = plugin.getPlayerLandNotConfirmed(player).get(0);

        if(plugin.getLandList().stream().anyMatch(land -> land.isConfirmed() && (staffLand.isInArea(land.getFirstLocation()) || staffLand.isInArea(land.getSecondLocation()))))
        {
            player.sendMessage("§cIl semblerait que quelqu'un a déjà claim une partie de votre terrain. Veuillez recommencez à 0");
            plugin.getPlayerLandNotConfirmed(player).get(0).hideArea();
            plugin.getLandList().remove(plugin.getPlayerLandNotConfirmed(player).get(0));
            return false;
        }
        if(plugin.getStaffLandList().stream().anyMatch(land -> land.getLandName() != null && land.getLandName().equalsIgnoreCase(name)))
        {
            player.sendMessage("§cLe nom de cette claim est déjà utilisé");
            return false;
        }

        new Thread(new insertNewLandThread(plugin.getPlayerClaimList().get(player.getUniqueId()).getPlayerID(), name, staffLand, true)).start();
        return true;
    }

    public boolean canDeleteStaffClaim(String name)
    {
        if(!existingStaffLand(name)) return false;

        new Thread(new deleteLandThread(playerClaim.getPlayerID(), name, true)).start();
        return true;
    }

    @Override
    public boolean canSetInteract(String name, String value)
    {
        if(!this.canHandleStaffLandParameter(name, value)) return false;

        Land staffLand = plugin.getStaffLandByName(name);
        boolean response = staffLand.isInteract();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setInteractThread(response, playerClaim.getPlayerID(), name, true)).start();
        return true;
    }

    @Override
    public boolean canSetMobSpawn(String name, String value)
    {
        if(!this.canHandleStaffLandParameter(name, value)) return false;

        Land staffLand = plugin.getStaffLandByName(name);

        boolean response = staffLand.isMobSpawn();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setMobSpawnThread(response, playerClaim.getPlayerID(), name, true)).start();
        return true;
    }

    @Override
    public boolean canSetHitMob(String name, String value) {
        if(!this.canHandleStaffLandParameter(name, value)) return false;

        Land staffLand = plugin.getStaffLandByName(name);

        boolean response = staffLand.isHitMob();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setHitMobThread(response, playerClaim.getPlayerID(), name, true)).start();
        return true;
    }

    @Override
    public boolean canSetHitAnimal(String name, String value) {
        if(!this.canHandleStaffLandParameter(name, value)) return false;

        Land staffLand = plugin.getStaffLandByName(name);

        boolean response = staffLand.isHitAnimal();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setHitAnimalThread(response, playerClaim.getPlayerID(), name, true)).start();
        return true;
    }

    @Override
    public boolean canSetCrops(String name, String value) {
        if(!this.canHandleStaffLandParameter(name, value)) return false;

        Land staffLand = plugin.getStaffLandByName(name);

        boolean response = staffLand.isCrops();
        if(value.equalsIgnoreCase("on")) response = true;
        if(value.equalsIgnoreCase("off")) response = false;

        new Thread(new setCropsThread(response, playerClaim.getPlayerID(), name, true)).start();
        return true;
    }
}
