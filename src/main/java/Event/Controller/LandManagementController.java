package Event.Controller;

import Controller.Controller;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sperias.group.Controller.C_Group;

public class LandManagementController extends Controller {

    private C_Group groupController;

    public LandManagementController(Player player) {
        super(player);
    }

    public boolean canBreakPutBlock(Location blocLocation)
    {
        groupController = new C_Group(player);
        if(landStore.getLandList().stream().anyMatch(land ->
                !(land.getPlayerList().get(player.getUniqueId()) != null &&  land.getPlayerList().get(player.getUniqueId()).canBuild()) &&
                land.isInArea(blocLocation) &&
                !player.getPlayer().isOp() &&
                !groupController.hasPermission("sperias.land.build") &&
                land.getOwner() != player.getUniqueId()
        ))
        {
            player.sendMessage("§cVous ne pouvez pas faire ceci dans une zone claim");
            return true;
        }
        return false;
    }

    public boolean canInteractBloc(Location blocLocation)
    {
        groupController = new C_Group(player);
        if(landStore.getLandList().stream().anyMatch(land ->
                !(land.getPlayerList().get(player.getUniqueId()) != null &&  land.getPlayerList().get(player.getUniqueId()).canOpen()) &&
                land.isInArea(blocLocation) &&
                !player.getPlayer().isOp() &&
                !groupController.hasPermission("sperias.land.build") &&
                !land.canOpenSomething() &&
                land.getOwner() != player.getUniqueId()
        ))
        {
            player.sendMessage("§cVous ne pouvez pas intéragir ce bloc qui est dans une zone claim");
            return false;
        }

        return true;
    }

    public boolean canKillAnimals(Location animalLocation)
    {
        groupController = new C_Group(player);
        if(landStore.getLandList().stream().anyMatch(land ->
                !(land.getPlayerList().get(player.getUniqueId()) != null &&  land.getPlayerList().get(player.getUniqueId()).canKillAnimals()) &&
                land.isInArea(animalLocation) &&
                !player.isOp() &&
                !groupController.hasPermission("sperias.land.killanimal") &&
                land.getOwner() != player.getUniqueId()
        ))
        {
            player.sendMessage("Vous ne pouvez pas taper les animaux car ce n'est pas gentil");
            return false;
        }

        return true;
    }
}
