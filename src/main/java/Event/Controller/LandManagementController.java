package Event.Controller;

import Controller.Controller;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandManagementController extends Controller {


    public LandManagementController(Player player) {
        super(player);
    }

    public boolean canBreakPutBlock(Location blocLocation)
    {
        if(landStore.getLandList().stream().anyMatch(land ->
                land.getPlayerList().get(player.getUniqueId()) == null &&
                land.isInArea(blocLocation) &&
                !player.isOp() &&
                !player.hasPermission("sperias.land.modify") &&
                !land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())
        ))
        {
            player.sendMessage("§cVous ne pouvez pas faire ceci dans une zone claim");
            return false;
        }
        return true;
    }

    public boolean canInteractBloc(Location blocLocation)
    {
        if(landStore.getLandList().stream().anyMatch(land ->
                land.getPlayerList().get(player.getUniqueId()) == null &&
                land.isInArea(blocLocation) &&
                !player.isOp() &&
                !land.isPublicInteract() &&
                !player.hasPermission("sperias.land.modify") &&
                !land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())
        ))
        {
            player.sendMessage("§cVous ne pouvez pas intéragir ce bloc qui est dans une zone claim");
            return false;
        }

        return true;
    }

    public boolean canKillAnimals(Location animalLocation)
    {
        if(landStore.getLandList().stream().anyMatch(land ->
                land.getPlayerList().get(player.getUniqueId()) == null &&
                land.isInArea(animalLocation) &&
                !player.isOp() &&
                !player.hasPermission("sperias.land.modify") &&
                !land.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())
        ))
        {
            player.sendMessage("Vous ne pouvez pas taper les animaux car ce n'est pas gentil");
            return false;
        }

        return true;
    }
}
