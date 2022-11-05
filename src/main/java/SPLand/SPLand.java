package SPLand;

import Command.CMD_land;
import Command.CMD_spland;
import Entity.PlayerClaim;
import Event.E_LandCreation;
import Event.E_LandManagement;
import LandStore.LandStore;
import LandStore.PlayerStore;
import SPGroupManager.SPGroupManager;
import SPWallet.SPWallet;
import WalletStore.WalletStore;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import sperias.gnaris.SPDatabase.SPDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public final class SPLand extends JavaPlugin {

    private final SPWallet spWallet = (SPWallet) getServer().getPluginManager().getPlugin("SP_Wallet");
    private final SPGroupManager spGroupManager = (SPGroupManager) getServer().getPluginManager().getPlugin("SP_GroupManager");
    private final SPDatabase spDatabase = (SPDatabase) getServer().getPluginManager().getPlugin("SP_Database");

    private static SPLand INSTANCE;

    private final LandStore landStore = new LandStore();
    private final PlayerStore playerStore = new PlayerStore();

    @Override
    public void onEnable() {

        INSTANCE = this;

        Objects.requireNonNull(getCommand("land")).setExecutor(new CMD_land());
        Objects.requireNonNull(getCommand("spland")).setExecutor(new CMD_spland());

        getServer().getPluginManager().registerEvents(new E_LandCreation(), this);
        getServer().getPluginManager().registerEvents(new E_LandManagement(), this);

        Bukkit.getOnlinePlayers().forEach(player -> getPlayerStore().getPlayerList().put(player.getUniqueId(), new PlayerClaim(player, 80000, 0)));
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
    public PlayerStore getPlayerStore() {
        return playerStore;
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
}
