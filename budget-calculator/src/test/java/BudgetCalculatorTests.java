import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BudgetCalculatorTests {

    private BudgetRepo budgeRepo = mock(BudgetRepo.class);
    private BudgetCalculator budgetCalculator = new BudgetCalculator(budgeRepo);

    @Test
    void testSingleDayInSingleMonth() {
        when(budgeRepo.getAll()).thenReturn(Arrays.asList(
                new Budget("202101", 31)
        ));
        String start = "20210101", end = "20210101";

        amountShouldBe(start, end, 1 * 1.0);
    }

    @Test
    void testMultiDayInSingleMonth() {
        when(budgeRepo.getAll()).thenReturn(Arrays.asList(
                new Budget("202101", 31)
        ));
        String start = "20210103", end = "20210107";

        amountShouldBe(start, end, 5 * 1.0);
    }

    @Test
    void testMultiDayInMultiMonth() {
        when(budgeRepo.getAll()).thenReturn(Arrays.asList(
                new Budget("202101", 31),
                new Budget("202102", 28 * 2),
                new Budget("202103", 31 * 3)
        ));
        String start = "20210131", end = "20210302";

        amountShouldBe(start, end, (1 * 1.0) + (28 * 2.0) + (2 * 3.0));
    }

    private void amountShouldBe(String start, String end, double expected) {
        Assertions.assertThat(budgetCalculator.query(start, end)).isEqualTo(expected);
    }
}
