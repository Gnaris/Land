package Entity;

import java.util.UUID;

public class Member {

    private String landName;
    private UUID owner;
    private UUID member;

    public Member(String landName, UUID owner, UUID member) {
        this.landName = landName;
        this.owner = owner;
        this.member = member;
    }

    public String getLandName() {
        return landName;
    }
    public UUID getOwnerLand() {
        return owner;
    }
    public UUID getUUID() {
        return member;
    }

}
