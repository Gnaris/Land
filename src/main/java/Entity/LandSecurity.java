package Entity;

public enum LandSecurity {

    INTERACT("canInteract", false),
    MONSTER_SPAWN("monsterCanSpawn", true),
    HIT_MONSTER("canHitMonster", false),
    HIT_ANIMAL("canHitAnimal", false),
    CROPS("canCrops", false);

    private String name;
    private boolean value;

    private LandSecurity(String name, boolean value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
