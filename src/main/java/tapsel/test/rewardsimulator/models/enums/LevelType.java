package tapsel.test.rewardsimulator.models.enums;

import lombok.Getter;

@Getter
public enum LevelType {
    ANT(0, 5),
    BEE(10, 7),
    CAT(50, 9),
    DOG(200, 12),
    ELEPHANT(1000, 19);

    final int minContractNumber;
    final int reward;

    LevelType(int minContractNumber, int reward) {
        this.minContractNumber = minContractNumber;
        this.reward = reward;
    }
}
