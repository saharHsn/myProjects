package tapsel.test.rewardsimulator;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tapsel.test.rewardsimulator.models.Contract;
import tapsel.test.rewardsimulator.models.Partner;
import tapsel.test.rewardsimulator.models.TimePeriod;
import tapsel.test.rewardsimulator.models.enums.ContractType;
import tapsel.test.rewardsimulator.models.enums.LevelType;
import tapsel.test.rewardsimulator.models.enums.Quarter;
import tapsel.test.rewardsimulator.services.ContractService;
import tapsel.test.rewardsimulator.services.PartnerService;
import tapsel.test.rewardsimulator.services.RewardService;
import tapsel.test.rewardsimulator.services.SaleService;
import tapsel.test.rewardsimulator.utils.DateUtil;

import java.io.FileNotFoundException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
class RewardSimulatorApplicationTests {
    @Autowired
    ContractService contractService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    RewardService rewardService;
    @Autowired
    SaleService saleService;

    void initializeDB() {
        //Add Partners
        makeSinglePartner();
        makeMultiLevelPartners();
    }

    private void makeSinglePartner() {
        Date currentDate = new Date();
        //Single Partner
        Partner single = new Partner("Single");
        this.partnerService.save(single);
        Contract singleContract = new Contract();
        singleContract.setOwner(single);
        singleContract.setType(ContractType.TORTOISE);
        singleContract.setStartDate(DateUtil.increaseDate(currentDate, -7, DateUtil.DateType.YEAR));
        this.contractService.addContract(singleContract);
    }

    private void makeMultiLevelPartners() {
        Date currentDate = new Date();

        Partner root = new Partner("Root");
        this.partnerService.save(root);

        Contract rootCnt1 = new Contract();
        rootCnt1.setStartDate(currentDate);
        rootCnt1.setType(ContractType.TORTOISE);
        rootCnt1.setOwner(root);
        this.contractService.addContract(rootCnt1);

        Contract rootCnt2 = new Contract();
        rootCnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        rootCnt2.setType(ContractType.RABBIT);
        rootCnt2.setOwner(root);
        this.contractService.addContract(rootCnt2);

        Contract rootCnt3 = new Contract();
        rootCnt3.setStartDate(DateUtil.increaseDate(currentDate, -9, DateUtil.DateType.YEAR));
        rootCnt3.setType(ContractType.RABBIT);
        rootCnt3.setOwner(root);
        this.contractService.addContract(rootCnt3);

        Contract rootCnt4 = new Contract();
        rootCnt4.setStartDate(DateUtil.increaseDate(currentDate, -1, DateUtil.DateType.YEAR));
        rootCnt4.setType(ContractType.TORTOISE);
        rootCnt4.setOwner(root);
        this.contractService.addContract(rootCnt4);

        //Add children
        /////////////////////////////////////////////////////////////////////////////////
        Partner child1Root = new Partner("Child1Root").setParent(root);
        this.partnerService.save(child1Root);

        Contract child1RootCnt1 = new Contract();
        child1RootCnt1.setStartDate(currentDate);
        child1RootCnt1.setType(ContractType.TORTOISE);
        child1RootCnt1.setOwner(child1Root);
        this.contractService.addContract(child1RootCnt1);

        Contract child1RootCnt2 = new Contract();
        child1RootCnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child1RootCnt2.setType(ContractType.RABBIT);
        child1RootCnt2.setOwner(child1Root);
        this.contractService.addContract(child1RootCnt2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child2Root = new Partner("Child2Root").setParent(root);
        this.partnerService.save(child2Root);
        Contract child2RootCnt1 = new Contract();
        child2RootCnt1.setStartDate(currentDate);
        child2RootCnt1.setType(ContractType.TORTOISE);
        child2RootCnt1.setOwner(child2Root);
        this.contractService.addContract(child2RootCnt1);

        Contract child2RootCnt2 = new Contract();
        child2RootCnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child2RootCnt2.setType(ContractType.RABBIT);
        child2RootCnt2.setOwner(child2Root);
        this.contractService.addContract(child2RootCnt2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child3Root = new Partner("Child3Root").setParent(root);
        this.partnerService.save(child3Root);
        Contract child3RootCnt1 = new Contract();
        child3RootCnt1.setStartDate(currentDate);
        child3RootCnt1.setType(ContractType.TORTOISE);
        child3RootCnt1.setOwner(child3Root);
        this.contractService.addContract(child3RootCnt1);

        Contract child3RootCnt2 = new Contract();
        child3RootCnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child3RootCnt2.setType(ContractType.RABBIT);
        child3RootCnt2.setOwner(child3Root);
        this.contractService.addContract(child3RootCnt2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child4Root = new Partner("Child4Root").setParent(root);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child1Child1 = new Partner("Child1Child1").setParent(child1Root);
        this.partnerService.save(child1Child1);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child2Child1 = new Partner("Child2Child1").setParent(child1Root);
        this.partnerService.save(child2Child1);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child3Child1 = new Partner("Child3Child1").setParent(child1Root);
        this.partnerService.save(child3Child1);
        Contract child3Child1Cnt1 = new Contract();
        child3Child1Cnt1.setStartDate(currentDate);
        child3Child1Cnt1.setType(ContractType.TORTOISE);
        child3Child1Cnt1.setOwner(root);
        this.contractService.addContract(child3Child1Cnt1);

        Contract child3Child1Cnt2 = new Contract();
        child3Child1Cnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child3Child1Cnt2.setType(ContractType.RABBIT);
        child3Child1Cnt2.setOwner(child3Child1);
        this.contractService.addContract(child3Child1Cnt2);

        Partner child4Child1 = new Partner("Child4Child1").setParent(child1Root);
        this.partnerService.save(child4Child1);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child1Child2 = new Partner("Child1Child2").setParent(child2Root);
        this.partnerService.save(child1Child2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child2Child2 = new Partner("Child2Child2").setParent(child2Root);
        this.partnerService.save(child2Child2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child1Child1Child2 = new Partner("Child1Child1Child2").setParent(child1Child2);
        this.partnerService.save(child1Child1Child2);
        Contract child1Child1Child2Cnt1 = new Contract();
        child1Child1Child2Cnt1.setStartDate(currentDate);
        child1Child1Child2Cnt1.setType(ContractType.TORTOISE);
        child1Child1Child2Cnt1.setOwner(child1Child1Child2);
        this.contractService.addContract(child1Child1Child2Cnt1);

        Contract child1Child1Child2Cnt2 = new Contract();
        child1Child1Child2Cnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child1Child1Child2Cnt2.setType(ContractType.RABBIT);
        child1Child1Child2Cnt2.setOwner(child1Child1Child2);
        this.contractService.addContract(child1Child1Child2Cnt2);
        /////////////////////////////////////////////////////////////////////////////////
        Partner child2Child1Child2 = new Partner("Child2Child1Child2").setParent(child1Child2);
        this.partnerService.save(child2Child1Child2);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child1Child2Child2 = new Partner("Child1Child2Child2").setParent(child2Child2);
        this.partnerService.save(child1Child2Child2);

        Partner child1Child4 = new Partner("Child1Child4").setParent(child4Root);
        this.partnerService.save(child1Child4);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child2Child4 = new Partner("Child2Child4").setParent(child4Root);
        this.partnerService.save(child2Child4);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child3Child4 = new Partner("Child3Child4").setParent(child4Root);
        this.partnerService.save(child3Child4);
        Contract child3Child4Cnt1 = new Contract();
        child3Child4Cnt1.setStartDate(currentDate);
        child3Child4Cnt1.setType(ContractType.TORTOISE);
        child3Child4Cnt1.setOwner(child3Child4);
        this.contractService.addContract(child3Child4Cnt1);

        Contract child3Child42 = new Contract();
        child3Child42.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child3Child42.setType(ContractType.RABBIT);
        child3Child42.setOwner(child3Child4);
        this.contractService.addContract(child3Child42);
        /////////////////////////////////////////////////////////////////////////////////
        Partner child4Child4 = new Partner("Child4Child4").setParent(child4Root);
        this.partnerService.save(child4Child4);

        /////////////////////////////////////////////////////////////////////////////////
        Partner child5Child4 = new Partner("Child5Child4").setParent(child4Root);
        this.partnerService.save(child5Child4);
        Contract child5Child4Cnt1 = new Contract();
        child5Child4Cnt1.setStartDate(currentDate);
        child5Child4Cnt1.setType(ContractType.TORTOISE);
        child5Child4Cnt1.setOwner(child5Child4);
        this.contractService.addContract(child5Child4Cnt1);

        Contract child5Child4Cnt2 = new Contract();
        child5Child4Cnt2.setStartDate(DateUtil.increaseDate(currentDate, -6, DateUtil.DateType.MONTH));
        child5Child4Cnt2.setType(ContractType.RABBIT);
        child5Child4Cnt2.setOwner(child5Child4);
        this.contractService.addContract(child5Child4Cnt2);
        /////////////////////////////////////////////////////////////////////////////////
        Partner child6Child4 = new Partner("Child6Child4").setParent(child4Root);
        this.partnerService.save(child6Child4);

    }

    private void makeSimpleData() {
        Date current = new Date();
        Partner c = new Partner("C");
        makeRandomContracts(current, c, 300);
        Partner b = new Partner("B");
        b.setParent(c);
        makeRandomContracts(current, b, 20);
        Partner a = new Partner("A");
        a.setParent(b);
        makeRandomContracts(current, a, 5);

    }

    private void testCase3() {
        Date current = new Date();
        Partner c = new Partner("C");
        makeRandomContracts(current, c, 51);
        Partner b = new Partner("B");
        b.setParent(c);
        makeRandomContracts(current, b, 11);
        Partner a = new Partner("A");
        a.setParent(b);
        makeRandomContracts(current, a, 5);

    }

    private void makeRandomContracts(Date current, Partner b, int i2) {
        for (int i = 0; i < i2; i++) {
            Contract cCnt = new Contract();
            if (i % 2 == 0) {
                cCnt.setType(ContractType.TORTOISE);
            } else {
                cCnt.setType(ContractType.RABBIT);
            }
            cCnt.setStartDate(DateUtil.increaseDate(current, -(i * 10), DateUtil.DateType.DAY));
            cCnt.setOwner(b);
            contractService.addContract(cCnt);
        }
    }

    @Test
    void rewardDifference() {
        makeSimpleData();
        Iterable<Partner> allPartners = this.partnerService.getAll();
        for (Partner partner : allPartners) {
            int rewardDifference = this.contractService.getRewardDifference(partner.getId(), new TimePeriod());
            System.out.println("rewardDifference for partner : " + partner.getName() + " is : " + rewardDifference);
        }
    }

    @Test
    void contextLoads() {
        makeSinglePartner();
        makeMultiLevelPartners();

        Iterable<Partner> allPartners = this.partnerService.getAll();
        for (Partner allPartner : allPartners) {
            System.out.println(allPartner.toString());
        }
        Iterable<Contract> allContracts = this.contractService.getAll();
        for (Contract allContract : allContracts) {
            System.out.println(allContract.toString());
        }
    }

    @Test
    void saleReport() throws FileNotFoundException {
        initializeDB();
        this.saleService.getSaleReport();
    }

    @Test
    void getPartnerLevelInTimePeriod() {
        initializeDB();
        Iterable<Partner> allPartners = this.partnerService.getAll();
        for (Partner partner : allPartners) {
            LevelType partnerLevel = this.contractService.getPartnerLevel(partner.getId(), new TimePeriod(2020, Quarter.QUARTER1));
            System.out.println(partnerLevel);
        }

    }

    @Test
    void getPartnerRewardInTimePeriod() {
        initializeDB();
        Iterable<Partner> allPartners = this.partnerService.getAll();
        for (Partner partner : allPartners) {
            int reward = this.rewardService.getRewardsInTimePeriod(partner.getId(), new TimePeriod(2020, Quarter.QUARTER1));
            System.out.println(reward);
        }
    }

    @Test
    void getPartnerReward() {
        // makeSimpleData();
        testCase3();
        Iterable<Contract> all = this.contractService.getAll();
        for (Contract contract : all) {
            System.out.println(contract.toString());
        }
        Iterable<Partner> allPartners = this.partnerService.getAll();
        for (Partner partner : allPartners) {
            System.out.println(this.rewardService.getAllRewards(partner.getId()));
        }
    }
}
