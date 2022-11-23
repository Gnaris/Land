package Entity;

public enum LandSecurity {

    INTERACT("canInteract"),
    MONSTER_SPAWN("monsterCanSpawn"),
    HIT_MONSTER("canHitMonster"),
    HIT_ANIMAL("canHitAnimal"),
    CROPS("canCrops");

    private final String name;

    private LandSecurity(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
