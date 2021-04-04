public class Budget {

    private String yearMonth;
    private Integer amount;
    private Double unitAmt;

    public Budget(String yearMonth, Integer amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public Integer getAmount() {
        return amount;
    }

    public Double getUnitAmt() {
        return unitAmt;
    }

    public void setUnitAmt(Double unitAmt) {
        this.unitAmt = unitAmt;
    }
}
