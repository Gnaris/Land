package Controller;

import LandStore.LandStore;
import Model.LandModel;
import SPLand.SPLand;
import org.bukkit.entity.Player;
import sperias.group.Controller.C_Group;

public class Controller{

    protected Player player;
    protected LandStore landStore = SPLand.getInstance().getLandStore();
    protected C_Group groupController;
    protected LandModel landModel = new LandModel();

    public Controller(Player player) {
        this.player = player;
        groupController = new C_Group(player);
    }
}
