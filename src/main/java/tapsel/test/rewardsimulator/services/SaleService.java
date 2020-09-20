package tapsel.test.rewardsimulator.services;

import org.springframework.stereotype.Component;
import tapsel.test.rewardsimulator.models.Contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SaleService {
    private static final String CSV_FILE_NAME = "Sale-Report" + new Date() + ".csv";
    private final ContractService contractService;

    public SaleService(ContractService contractService) {
        this.contractService = contractService;
    }

    public String getSaleReport() throws FileNotFoundException {
        List<String[]> dataLines = getData();
        File csvOutputFile = new File(CSV_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        return csvOutputFile.getAbsolutePath();
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private List<String[]> getData() {
        List<String[]> dataLines = new ArrayList<>();
        Iterable<Contract> allContracts = this.contractService.getAll();
        for (Contract contract : allContracts) {
            String action = contract.isExpired() ? "END" : "BEGIN";
            dataLines.add(new String[]
                    {
                            String.valueOf(contract.getOwner().getId()),
                            String.valueOf(contract.getId()),
                            String.valueOf(contract.getType()),
                            String.valueOf(contract.getStartDate()),
                            action
                    });
        }
        return dataLines;
    }
}
