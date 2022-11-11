package SPLand;

import Command.CMD_land;
import Command.CMD_spland;
import Event.E_InitializePlayer;
import Event.E_LandCreation;
import Event.E_LandManagement;
import LandStore.LandStore;
import LandStore.PlayerClaimStore;
import Model.LandModel;
import SPGroupManager.SPGroupManager;
import SPWallet.SPWallet;
import WalletStore.WalletStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sperias.gnaris.SPDatabase.SPDatabase;
import sperias.group.GroupStore.GroupStore;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public final class SPLand extends JavaPlugin {

    private final SPWallet spWallet = (SPWallet) getServer().getPluginManager().getPlugin("SP_Wallet");
    private final SPDatabase spDatabase = (SPDatabase) getServer().getPluginManager().getPlugin("SP_Database");
    private final SPGroupManager spGroupManager = (SPGroupManager) getServer().getPluginManager().getPlugin("SP_GroupManager");

    private static SPLand INSTANCE;

    private final LandStore landStore = new LandStore();
    private final PlayerClaimStore playerClaimStore = new PlayerClaimStore();

    @Override
    public void onEnable() {

        INSTANCE = this;

        Objects.requireNonNull(getCommand("land")).setExecutor(new CMD_land());
        Objects.requireNonNull(getCommand("spland")).setExecutor(new CMD_spland());

        getServer().getPluginManager().registerEvents(new E_LandCreation(), this);
        getServer().getPluginManager().registerEvents(new E_LandManagement(), this);
        getServer().getPluginManager().registerEvents(new E_InitializePlayer(), this);

        LandModel landModel = new LandModel();
        try {
            landStore.getLandList().addAll(landModel.getAllLand());
            landModel.getAllPlayerLand().forEach(playerLand ->
                    landStore.getLandList().stream().filter(land -> land.getId() == playerLand.getLandID())
                            .forEach(land -> land.getPlayerList().put(playerLand.getPlayer(), playerLand))
            );
            Bukkit.getOnlinePlayers().forEach(player -> {
                try {
                    playerClaimStore.getPlayerClaimList().put(player.getUniqueId(), landModel.getPlayerClaim(player));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SPLand getInstance() {
        return INSTANCE;
    }
    public LandStore getLandStore()
    {
        return landStore;
    }
    public PlayerClaimStore getPlayerClaimStore() {
        return playerClaimStore;
    }
    public static WalletStore getWalletStore()
    {
        assert getInstance().spWallet != null;
        return  getInstance().spWallet.getWalletStore();
    }
    public static Connection getDatabase() throws SQLException, ClassNotFoundException {
        assert getInstance().spDatabase != null;
        return getInstance().spDatabase.getSPDatabase().getDatabase();
    }
    public static GroupStore getGroupStore()
    {
        return getInstance().spGroupManager.getGroupStore();
    }
}
