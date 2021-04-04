package org.yfr.finance.core.pojo.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Result {

    private Double profitHBound;

    private Double profitLBound;

    private Long winCount;

    private Long loseCount;

    private Double winSum;

    private Double loseSum;

    private Double winAvg;

    private Double loseAvg;

    private Double winRate;

    private Double expValue;

    private Double avgTradingPeriod;

    public void setProfitHBound(Double profitHBound) {
        this.profitHBound = profitHBound;
    }

    public void setProfitLBound(Double profitLBound) {
        this.profitLBound = profitLBound;
    }

    public void setWinCount(Long winCount) {
        this.winCount = winCount;
    }

    public void setLoseCount(Long loseCount) {
        this.loseCount = loseCount;
    }

    public void setWinSum(Double winSum) {
        this.winSum = winSum;
        this.winAvg = (this.winCount == 0) ? 0 : winSum / this.winCount;
        this.winRate = (this.winCount + this.loseCount == 0) ? 0 : (double) this.winCount / (double) (this.winCount + this.loseCount);
    }

    public void setLoseSum(Double loseSum) {
        this.loseSum = loseSum;
        this.loseAvg = (this.loseCount == 0) ? 0 : loseSum / this.loseCount;
    }

    public void setExpValue(Double expValue) {
        this.expValue = expValue;
    }

    public void setAvgTradingPeriod(Double avgTradingPeriod) {
        this.avgTradingPeriod = avgTradingPeriod;
    }

    @Override
    public String toString() {
        return "Result{" +
                "\nprofitHBound=" + profitHBound +
                "\nprofitLBound=" + profitLBound +
                "\nwinCount=" + winCount +
                "\nloseCount=" + loseCount +
                "\nwinSum=" + winSum +
                "\nloseSum=" + loseSum +
                "\nwinAvg=" + winAvg +
                "\nloseAvg=" + loseAvg +
                "\nwinRate=" + winRate +
                "\nexpValue=" + expValue +
                "\navgTradingPeriod=" + avgTradingPeriod +
                "\n}";
    }

}
