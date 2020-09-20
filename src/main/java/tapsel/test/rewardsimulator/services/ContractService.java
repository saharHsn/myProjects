package tapsel.test.rewardsimulator.services;

import com.sun.istack.NotNull;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tapsel.test.rewardsimulator.models.Contract;
import tapsel.test.rewardsimulator.models.Partner;
import tapsel.test.rewardsimulator.models.TimePeriod;
import tapsel.test.rewardsimulator.models.enums.ContractType;
import tapsel.test.rewardsimulator.models.enums.LevelType;
import tapsel.test.rewardsimulator.models.enums.Quarter;
import tapsel.test.rewardsimulator.repositories.ContractRepository;
import tapsel.test.rewardsimulator.utils.DateUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ContractService {
    private final ContractRepository repository;
    private final PartnerService partnerService;

    public ContractService(ContractRepository repository, PartnerService parentService) {
        this.repository = repository;
        this.partnerService = parentService;
    }

    @Transactional
    public Contract addContract(Contract contract) {
        Date startDate = contract.getStartDate();
        TimePeriod timePeriod = new TimePeriod(DateUtil.getYear(startDate), getQuarter(startDate));
        contract.setTimePeriod(timePeriod);
        this.repository.save(contract);
        this.partnerService.addContract(contract.getOwner(), contract);
        return contract;
    }

    public void expireContract(Contract contract) {
        contract.setIsExpired(true);
        this.repository.save(contract);
        this.partnerService.updateLevel(contract.getOwner());
    }

    public int getBonus(Long partnerId, TimePeriod timePeriod) {
        int bonus = 0;
        List<Contract> contracts = geAllContractsInTimePeriod(partnerId, timePeriod);
        for (Contract contract : contracts) {
            if (checkIfContractIsExpired(contract)) {
                if (contract.getType().equals(ContractType.RABBIT)) {
                    bonus += 50;
                }
            }
        }
        return bonus;
    }

    public int getRewardDifference(Long partnerId, TimePeriod timePeriod) {
        int reward = 0;
        List<Contract> allChildContracts = getAllChildContracts(partnerId, timePeriod);
        for (Contract childContract : allChildContracts) {
            if (!checkIfContractIsExpired(childContract)) {
                //System.out.println("in contract : " + ++i);
                reward += calculateLevelDifference(partnerId, childContract, timePeriod);
                //System.out.println("reward : " + reward);
            }
        }
        return reward;
    }

    public int calculateLevelDifference(Long rootParentId, Contract childContract, TimePeriod timePeriod) {
        Partner child = childContract.getOwner();
        Partner childParent = child.getParent();

        while (true) {
            if (childParent.getId().equals(rootParentId)) {
                int partnerLevelReward = getTotalContractNumbers(childParent.getId(), timePeriod);
                int childLevelReward = getTotalContractNumbers(child.getId(), childContract.getTimePeriod());
                return partnerLevelReward - childLevelReward;
            } else {
                child = childParent;
                childParent = childParent.getParent();
            }
        }
    }

    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000)
    public void findExpiredContracts() {
        Iterable<Contract> all = this.repository.findAll();
        for (Contract contract : all) {
            if (!contract.getIsExpired()) {
                if (checkIfContractIsExpired(contract)) {
                    expireContract(contract);
                }
            }
        }
    }

    public List<Contract> getAllChildContracts(Long partnerId, TimePeriod timePeriod) {
        return getAllChildContracts(partnerId, timePeriod, new ArrayList<>());
    }

    public int getTotalContractNumbers(Long partnerId, TimePeriod timePeriod) {
        return sumContracts(partnerId, 0, timePeriod);
    }

    public int getContractNumbers(Long partnerId, TimePeriod timePeriod) {
        List<Contract> allContracts = this.repository.filterContractsByTimePeriod(partnerId, timePeriod.getQuarter(), timePeriod.getYear());
        List<Contract> enabledContracts = getEnabledContracts(allContracts);
        return enabledContracts.size();
    }

    public LevelType getPartnerLevel(Long partnerId, TimePeriod timePeriod) {
        int allContractsSize = sumContracts(partnerId, 0, timePeriod);
        return this.partnerService.getLevelType(allContractsSize);
    }

    public Integer sumContracts(Long partnerId, int totalContractSize, @NotNull TimePeriod timePeriod) {
        List<Contract> contracts = this.repository.filterContractsByTimePeriod(partnerId, timePeriod.getQuarter(), timePeriod.getYear());
        totalContractSize += getEnabledContracts(contracts).size();
        List<Long> children = this.partnerService.getChildren(partnerId);
        if (!CollectionUtils.isEmpty(children)) {
            for (Long childId : children) {
                totalContractSize += sumContracts(childId, totalContractSize, timePeriod);
            }
        }
        return totalContractSize;
    }

    public List<Contract> getAllChildContracts(Long parentId, TimePeriod timePeriod, List<Contract> childContracts) {
        List<Long> children = this.partnerService.getChildren(parentId);
        for (Long childId : children) {
            List<Contract> contracts;
            contracts = filterContractsByTimePeriod(childId, timePeriod);
            List<Contract> enabledContracts = getEnabledContracts(contracts);
            if (!CollectionUtils.isEmpty(enabledContracts)) {
                childContracts.addAll(enabledContracts);
            }
            getAllChildContracts(childId, timePeriod, childContracts);
        }
        return childContracts;
    }

    public List<Contract> geAllContractsInTimePeriod(Long partnerId, TimePeriod timePeriod) {
        return getByOwnerAndTimePeriod(partnerId, timePeriod.getQuarter(), timePeriod.getYear());
    }

    public boolean checkIfContractIsExpired(Contract contract) {
        Date currentDate = new Date();
        Date startDate = contract.getStartDate();
        return (DateUtil.getYear(currentDate) - DateUtil.getYear(startDate)) <= 8;
    }

    public List<Contract> getByOwnerAndTimePeriod(Long partnerId, Quarter quarter, Integer year) {
        return this.repository.filterContractsByTimePeriod(partnerId, quarter, year);
    }

    public Iterable<Contract> getAll() {
        return repository.findAll();
    }

    ///////////////////////////////////////////////PRIVATE METHODS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    private List<Contract> filterContractsByTimePeriod(Long partnerId, TimePeriod timePeriod) {
        return this.repository.filterContractsByTimePeriod(partnerId, timePeriod.getQuarter(), timePeriod.getYear());
    }

    private Quarter getQuarter(Date date) {
        int month = DateUtil.getMonth(date);
        if (month >= 1 && month <= 3) {
            return Quarter.QUARTER1;
        } else if (month >= 4 && month <= 6) {
            return Quarter.QUARTER2;
        } else if (month >= 7 && month <= 9) {
            return Quarter.QUARTER3;
        } else if (month >= 10 && month <= 12) {
            return Quarter.QUARTER4;
        }
        return Quarter.QUARTER1;
    }

    private List<Contract> getEnabledContracts(List<Contract> allContracts) {
        List<Contract> enabledContracts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allContracts)) {
            for (Contract contract : allContracts) {
                if (!checkIfContractIsExpired(contract)) {
                    enabledContracts.add(contract);
                }
            }
        }
        return enabledContracts;
    }
}
