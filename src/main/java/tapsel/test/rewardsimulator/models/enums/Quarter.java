package tapsel.test.rewardsimulator.models.enums;

public enum Quarter {
    QUARTER1,
    QUARTER2,
    QUARTER3,
    QUARTER4;

    public static Quarter getByNumber(int number) {
        if (number == 1) {
            return QUARTER1;
        } else if (number == 2) {
            return QUARTER2;
        } else if (number == 3) {
            return QUARTER3;
        } else if (number == 4) {
            return QUARTER4;
        }
        return null;
    }
}
