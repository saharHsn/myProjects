package tapsel.test.rewardsimulator.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import tapsel.test.rewardsimulator.models.TimePeriod;
import tapsel.test.rewardsimulator.models.enums.LevelType;
import tapsel.test.rewardsimulator.models.enums.Quarter;
import tapsel.test.rewardsimulator.utils.DateUtil;

import java.util.Date;

@Component
public class RewardService {
    private final ContractService contractService;

    public RewardService(ContractService contractService) {
        this.contractService = contractService;
    }

    public int getRewardsInTimePeriod(Long partnerId, TimePeriod timePeriod) {
        Date current = new Date();
        Integer year = timePeriod.getYear();
        int diff = DateUtil.getYear(current) - year;

        LevelType partnerLevel = this.contractService.getPartnerLevel(partnerId, new TimePeriod());

        int contractsSumQ = 0;
        int diffSumQ = 0;
        for (int j = 0; j < 8 - diff; j++) {
            contractsSumQ += this.contractService.getContractNumbers(partnerId, new TimePeriod(year - j, timePeriod.getQuarter()));
            diffSumQ += this.contractService.getRewardDifference(partnerId, new TimePeriod(year - j, timePeriod.getQuarter()));
        }
        int rewardQ = contractsSumQ * partnerLevel.getReward();
        int bonusQ = this.contractService.getBonus(partnerId, new TimePeriod(year, Quarter.QUARTER1));
        return rewardQ + diffSumQ + bonusQ;
    }

    public String getAllRewards(Long partnerId) {
        StringBuilder builder = new StringBuilder();
        Date current = new Date();
        builder.append("PartnerId : ").append(partnerId).append(" ").append("\n");
        LevelType partnerLevel = this.contractService.getPartnerLevel(partnerId, new TimePeriod());
        for (int i = 0; i < 8; i++) {
            int year = DateUtil.getYear(current) - i;
            int contractsSumQ1 = 0;
            int contractsSumQ2 = 0;
            int contractsSumQ3 = 0;
            int contractsSumQ4 = 0;

            int diffSumQ1 = 0;
            int diffSumQ2 = 0;
            int diffSumQ3 = 0;
            int diffSumQ4 = 0;

            for (int j = 0; j < 8; j++) {
                contractsSumQ1 += this.contractService.getContractNumbers(partnerId, new TimePeriod(year - j, Quarter.QUARTER1));
                contractsSumQ2 += this.contractService.getContractNumbers(partnerId, new TimePeriod(year - j, Quarter.QUARTER2));
                contractsSumQ3 += this.contractService.getContractNumbers(partnerId, new TimePeriod(year - j, Quarter.QUARTER3));
                contractsSumQ4 += this.contractService.getContractNumbers(partnerId, new TimePeriod(year - j, Quarter.QUARTER4));

                diffSumQ1 += this.contractService.getRewardDifference(partnerId, new TimePeriod(year - j, Quarter.QUARTER1));
                diffSumQ2 += this.contractService.getRewardDifference(partnerId, new TimePeriod(year - j, Quarter.QUARTER2));
                diffSumQ3 += this.contractService.getRewardDifference(partnerId, new TimePeriod(year - j, Quarter.QUARTER3));
                diffSumQ4 += this.contractService.getRewardDifference(partnerId, new TimePeriod(year - j, Quarter.QUARTER4));
            }
            int rewardQ1 = contractsSumQ1 * partnerLevel.getReward();
            int rewardQ2 = contractsSumQ2 * partnerLevel.getReward();
            int rewardQ3 = contractsSumQ3 * partnerLevel.getReward();
            int rewardQ4 = contractsSumQ4 * partnerLevel.getReward();

            int bonusQ1 = this.contractService.getBonus(partnerId, new TimePeriod(year, Quarter.QUARTER1));
            int bonusQ2 = this.contractService.getBonus(partnerId, new TimePeriod(year, Quarter.QUARTER2));
            int bonusQ3 = this.contractService.getBonus(partnerId, new TimePeriod(year, Quarter.QUARTER3));
            int bonusQ4 = this.contractService.getBonus(partnerId, new TimePeriod(year, Quarter.QUARTER4));


            builder.append(year).append(" ").append(Quarter.QUARTER1).append(" ").append(rewardQ1 + diffSumQ1 + bonusQ1).append("\n");
            builder.append(year).append(" ").append(Quarter.QUARTER2).append(" ").append(rewardQ2 + diffSumQ2 + bonusQ2).append("\n");
            builder.append(year).append(" ").append(Quarter.QUARTER3).append(" ").append(rewardQ3 + diffSumQ3 + bonusQ3).append("\n");
            builder.append(year).append(" ").append(Quarter.QUARTER4).append(" ").append(rewardQ4 + diffSumQ4 + bonusQ4).append("\n");
        }
        return builder.toString();
    }
}
