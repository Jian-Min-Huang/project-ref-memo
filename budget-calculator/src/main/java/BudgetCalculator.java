import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetCalculator {

    private DateTimeFormatter df_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    private DateTimeFormatter df_yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    private BudgetRepo budgetRepo;

    public BudgetCalculator(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(String start, String end) {
        String startYearMonthStr = start.substring(0, 6);
        String endYearMonthStr = end.substring(0, 6);
        LocalDate startDate = LocalDate.parse(start, df_yyyyMMdd);
        LocalDate endDate = LocalDate.parse(end, df_yyyyMMdd);
        YearMonth startYearMonth = YearMonth.from(startDate);
        YearMonth endYearMonth = YearMonth.from(endDate);

        // yearMonthDaysMap = {
        //   "202101" -> 1,
        //   "202102" -> 28,
        //   "202103" -> 2,
        // }
        Map<String, Integer> yearMonthDaysMap = new HashMap<>();
        if (startYearMonth.equals(endYearMonth)) {
            yearMonthDaysMap.put(startYearMonthStr, LocalDate.parse(end, df_yyyyMMdd).getDayOfMonth() - LocalDate.parse(start, df_yyyyMMdd).getDayOfMonth() + 1);
        } else {
            yearMonthDaysMap.put(startYearMonthStr, (startDate.lengthOfMonth() - startDate.getDayOfMonth() + 1));

            YearMonth tmp = YearMonth.from(startDate);
            while (tmp.plusMonths(1).isBefore(endYearMonth)) {
                tmp = tmp.plusMonths(1);
                yearMonthDaysMap.put(df_yyyyMM.format(tmp.atEndOfMonth()), tmp.lengthOfMonth());
            }

            yearMonthDaysMap.put(endYearMonthStr, endDate.getDayOfMonth());
        }

        // Budget = {"202101", 31, 0}
        // yearMonthBudgetMap = {
        //   "202101" -> [{"202101", 31, 1.0}],
        //   "202102" -> [{"202102", 28, 2.0}],
        //   "202103" -> [{"202103", 31, 3.0}]
        // }
        Map<String, List<Budget>> yearMonthBudgetMap = budgetRepo.getAll().stream()
                .filter(budget -> yearMonthDaysMap.containsKey(budget.getYearMonth()))
                .map(budget -> {
                    budget.setUnitAmt(budget.getAmount() / (double) LocalDate.parse(budget.getYearMonth() + "01", df_yyyyMMdd).lengthOfMonth());
                    return budget;
                })
                .collect(Collectors.groupingBy(Budget::getYearMonth));

        return yearMonthDaysMap.entrySet().stream()
                .map(v -> v.getValue() * yearMonthBudgetMap.get(v.getKey()).get(0).getUnitAmt())
                .reduce(Double::sum)
                .get();
    }
}
