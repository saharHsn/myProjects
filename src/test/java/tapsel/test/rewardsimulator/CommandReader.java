package tapsel.test.rewardsimulator;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
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
import java.util.Scanner;

@Component
public class CommandReader implements CommandLineRunner {
    private final PartnerService partnerService;
    @Autowired
    ContractService contractService;
    @Autowired
    RewardService rewardService;
    @Autowired
    SaleService saleService;

    public CommandReader(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RewardSimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //REGISTER 1
        task1();
        //REGISTER 2 1
        task2();
        //LOAD filename
        task3();
        //LEVEL 2 2020 1
        task4();
        //REWARDS 2 2020 1
        task5();
        //REWARDS 2
        task6();
    }

    private void task1() {
        System.out.println("Executing task : REGISTER partnerId");
        System.out.println("Enter command ");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        String[] commands = command.split(" ");
        Partner single = new Partner("Partner");
        single.setId(Long.valueOf(commands[1]));
        this.partnerService.save(single);
        System.out.println("Partner saved with id : " + single.getId() + " and name : " + single.getName());
    }

    private void task2() {
        System.out.println("Executing task : REGISTER partnerId parentPartnerId");
        System.out.println("Enter command ");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        String[] commands = command.split(" ");
        Partner partner = this.partnerService.get(Long.valueOf(commands[2]));
        Partner parent = new Partner("Parent of Partner");
        parent.setId(Long.valueOf(commands[1]));
        this.partnerService.save(parent);
        partner.setParent(parent);
        this.partnerService.save(partner);
        System.out.println("Parent was added  id : " + parent.getId() + " and name : " + parent.getName());
    }

    private void task3() throws FileNotFoundException {
        System.out.println("Executing task : LOAD filename");
        makeSimpleData();
        String saleReport = this.saleService.getSaleReport();
        System.out.println("Report file is saved in : " + saleReport);
    }

    private void task4() {
        System.out.println("Executing task : LEVEL partnerId year quarter");
        System.out.println("Enter command ");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        String[] commands = command.split(" ");
        TimePeriod timePeriod = new TimePeriod(Integer.valueOf(commands[2]), getQuarter(Integer.valueOf(commands[3])));
        LevelType partnerLevel = this.contractService.getPartnerLevel(Long.valueOf(commands[1]), timePeriod);
        System.out.println("partnerLevel : " + partnerLevel.name());
    }

    private void task5() {
        System.out.println("Executing task : REWARDS partnerId year quarter");
        System.out.println("Enter command ");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        String[] commands = command.split(" ");
        TimePeriod timePeriod = new TimePeriod(Integer.valueOf(commands[2]), getQuarter(Integer.valueOf(commands[3])));
        int reward = this.rewardService.getRewardsInTimePeriod(Long.valueOf(commands[1]), timePeriod);
        System.out.println("reward : " + reward);
    }

    private void task6() {
        System.out.println("Executing task : ALL_REWARDS partnerId");
        System.out.println("Enter command ");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        String[] commands = command.split(" ");
        System.out.println(this.rewardService.getAllRewards(Long.valueOf(commands[1])));
    }

    private Quarter getQuarter(Integer intVal) {
        return Quarter.getByNumber(intVal);
    }


    private void makeSimpleData() {
        System.out.println("Making some mock data, it takes some times please wait ...");
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

}
