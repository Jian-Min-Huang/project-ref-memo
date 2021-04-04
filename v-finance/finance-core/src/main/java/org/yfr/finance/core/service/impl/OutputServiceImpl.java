package org.yfr.finance.core.service.impl;

import org.springframework.stereotype.Service;
import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.MA;
import org.yfr.finance.core.service.OutputService;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OutputServiceImpl implements OutputService {

    @Override
    public void generateDataJs(Integer batchSize, List<Stock> stocks, List<MA> ma20, List<Optional<Output>> outputs) {
        int length = stocks.size();
        double countSize = Math.ceil(length / (double) batchSize);

        String countSizeStr = "var countSize = " + (int) countSize + ";";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        StringBuilder dateBuilder = new StringBuilder("var dateArr = [");
        for (int i = 0; i < countSize; i++) {
            if (i == 0) {
                dateBuilder.append("[");
                for (int j = 20; j < batchSize * (i + 1); j++) {
                    dateBuilder
                            .append(formatter.format(stocks.get(j).getDateTime()))
                            .append(",");
                }
                dateBuilder.deleteCharAt(dateBuilder.length() - 1);
                dateBuilder.append("],");
            } else if (i == countSize - 1) {
                dateBuilder.append("[");
                for (int j = batchSize * i; j < length; j++) {
                    dateBuilder
                            .append(formatter.format(stocks.get(j).getDateTime()))
                            .append(",");
                }
                dateBuilder.deleteCharAt(dateBuilder.length() - 1);
                dateBuilder.append("]");
            } else {
                dateBuilder.append("[");
                for (int j = batchSize * i; j < batchSize * (i + 1); j++) {
                    dateBuilder
                            .append(formatter.format(stocks.get(j).getDateTime()))
                            .append(",");
                }
                dateBuilder.deleteCharAt(dateBuilder.length() - 1);
                dateBuilder.append("],");
            }
        }
        dateBuilder.append("];");

        StringBuilder closePriceBuilder = new StringBuilder("var closePriceArr = [");
        for (int i = 0; i < countSize; i++) {
            if (i == 0) {
                closePriceBuilder.append("[");
                for (int j = 20; j < batchSize * (i + 1); j++) {
                    closePriceBuilder
                            .append(stocks.get(j).getClosePrice())
                            .append(",");
                }
                closePriceBuilder.deleteCharAt(closePriceBuilder.length() - 1);
                closePriceBuilder.append("],");
            } else if (i == countSize - 1) {
                closePriceBuilder.append("[");
                for (int j = batchSize * i; j < length; j++) {
                    closePriceBuilder
                            .append(stocks.get(j).getClosePrice())
                            .append(",");
                }
                closePriceBuilder.deleteCharAt(closePriceBuilder.length() - 1);
                closePriceBuilder.append("]");
            } else {
                closePriceBuilder.append("[");
                for (int j = batchSize * i; j < batchSize * (i + 1); j++) {
                    closePriceBuilder
                            .append(stocks.get(j).getClosePrice())
                            .append(",");
                }
                closePriceBuilder.deleteCharAt(closePriceBuilder.length() - 1);
                closePriceBuilder.append("],");
            }
        }
        closePriceBuilder.append("];");

        StringBuilder ma20Builder = new StringBuilder("var ma20Arr = [");
        for (int i = 0; i < countSize; i++) {
            if (i == 0) {
                ma20Builder.append("[");
                for (int j = 20; j < batchSize * (i + 1); j++) {
                    ma20Builder
                            .append(ma20.get(j).getMa())
                            .append(",");
                }
                ma20Builder.deleteCharAt(ma20Builder.length() - 1);
                ma20Builder.append("],");
            } else if (i == countSize - 1) {
                ma20Builder.append("[");
                for (int j = batchSize * i; j < length; j++) {
                    ma20Builder
                            .append(ma20.get(j).getMa())
                            .append(",");
                }
                ma20Builder.deleteCharAt(ma20Builder.length() - 1);
                ma20Builder.append("]");
            } else {
                ma20Builder.append("[");
                for (int j = batchSize * i; j < batchSize * (i + 1); j++) {
                    ma20Builder
                            .append(ma20.get(j).getMa())
                            .append(",");
                }
                ma20Builder.deleteCharAt(ma20Builder.length() - 1);
                ma20Builder.append("],");
            }
        }
        ma20Builder.append("];");

        StringBuilder outputBuilder = new StringBuilder("var outputArr = [");
        for (int i = 0; i < countSize; i++) {
            if (i == 0) {
                outputBuilder.append("[");
                for (int j = 20; j < batchSize * (i + 1); j++) {
                    outputBuilder
                            .append(outputs.get(j).isPresent() ? outputs.get(j).get().getAmount() : 0)
                            .append(",");
                }
                outputBuilder.deleteCharAt(outputBuilder.length() - 1);
                outputBuilder.append("],");
            } else if (i == countSize - 1) {
                outputBuilder.append("[");
                for (int j = batchSize * i; j < length; j++) {
                    outputBuilder
                            .append(outputs.get(j).isPresent() ? outputs.get(j).get().getAmount() : 0)
                            .append(",");
                }
                outputBuilder.deleteCharAt(outputBuilder.length() - 1);
                outputBuilder.append("]");
            } else {
                outputBuilder.append("[");
                for (int j = batchSize * i; j < batchSize * (i + 1); j++) {
                    outputBuilder
                            .append(outputs.get(j).isPresent() ? outputs.get(j).get().getAmount() : 0)
                            .append(",");
                }
                outputBuilder.deleteCharAt(outputBuilder.length() - 1);
                outputBuilder.append("],");
            }
        }
        outputBuilder.append("];");

        try {
            FileOutputStream fos = new FileOutputStream("./result/HourMa.js");
            fos.write((countSizeStr + dateBuilder.toString() + closePriceBuilder.toString() + ma20Builder.toString() + outputBuilder.toString()).getBytes());
            fos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
