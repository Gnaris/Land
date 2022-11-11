package LandStore;

import Entity.PlayerClaim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerClaimStore {

    private final Map<UUID, PlayerClaim> playerClaimList = new HashMap<>();

    public Map<UUID, PlayerClaim> getPlayerClaimList() {
        return playerClaimList;
    }
}
