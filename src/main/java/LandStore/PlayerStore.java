package LandStore;

import Entity.PlayerClaim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStore {

    private final Map<UUID, PlayerClaim> playerList = new HashMap<>();

    public Map<UUID, PlayerClaim> getPlayerList() {
        return playerList;
    }
}
