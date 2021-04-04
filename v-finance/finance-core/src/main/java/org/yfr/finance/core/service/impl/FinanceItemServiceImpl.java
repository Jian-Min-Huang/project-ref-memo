package org.yfr.finance.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yfr.finance.core.pojo.dto.DayItem;
import org.yfr.finance.core.pojo.dto.MinuteItem;
import org.yfr.finance.core.pojo.dto.OperationResponse;
import org.yfr.finance.core.pojo.entity.Item;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.repository.FinanceItemRepository;
import org.yfr.finance.core.service.FinanceItemService;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FinanceItemServiceImpl implements FinanceItemService {

    @Value("${common.dump-file-dir}")
    private String dumpFileDir;

    @Resource
    private FinanceItemRepository financeItemRepository;

    @Override
    public List<Stock> findHourTSEA(Short sortType) {
        List<Item> items = (sortType.equals((short) 1))
                ? financeItemRepository.findByNameAndTypeOrderByDateTime("TSEA", (short) 0)
                : financeItemRepository.findByNameAndTypeOrderByDateTimeDesc("TSEA", (short) 0);

        return items.stream()
                .map(v -> Stock.builder()
                            .dateTime(v.getDateTime())
                            .openPrice(v.getOpenPrice())
                            .highestPrice(v.getHighestPrice())
                            .lowestPrice(v.getLowestPrice())
                            .closePrice(v.getClosePrice())
                            .build())
                .filter(v -> timeFileter(v.getDateTime()))
                .map(v -> {
                    if (v.getDateTime().getHour() == 13 && v.getDateTime().getMinute() == 30) {
                        LocalDateTime dateTime = v.getDateTime();
                        v.setDateTime(LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 14, 0, 0));
                    }

                    return v;
                })
                .collect(toList());
    }

    @Override
    public List<Stock> findDayTSEA(Short sortType) {
        List<Item> items = (sortType.equals((short) 1))
                ? financeItemRepository.findByNameAndTypeOrderByDateTime("TSEA", (short) 4)
                : financeItemRepository.findByNameAndTypeOrderByDateTimeDesc("TSEA", (short) 4);

        return items.stream()
                .map(v -> Stock.builder()
                        .dateTime(v.getDateTime())
                        .openPrice(v.getOpenPrice())
                        .highestPrice(v.getHighestPrice())
                        .lowestPrice(v.getLowestPrice())
                        .closePrice(v.getClosePrice())
                        .build())
                .collect(toList());
    }

    @Override
    public OperationResponse saveMinuteItems() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dumpFileDir + File.separator + "TSEA0.csv")));

        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        List<Item> items = lines.parallelStream()
                .map(v -> Arrays.asList(v.split(",")))
                .map(v -> MinuteItem.builder()
                        .name(v.get(0).trim())
                        .dateTime(v.get(1).trim())
                        .openPrice(v.get(2).trim())
                        .highestPrice(v.get(3).trim())
                        .lowestPrice(v.get(4).trim())
                        .closePrice(v.get(5).trim())
                        .build()
                        .toItemEntity())
                .sorted(Comparator.comparing(Item::getDateTime).reversed())
                .collect(toList());

        for (Item item : items) {
            Long count = financeItemRepository.countByNameAndTypeAndDateTime(item.getName(), (short) 0, item.getDateTime());
            if (null != count && count != 0) {
                log.info("found duplicate {}, skip rest ... ", item);
                break;
            }

            financeItemRepository.save(item);
        }

        return OperationResponse.builder().message("saveMinuteItems success !").build();
    }

    @Override
    public OperationResponse saveDayItems() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dumpFileDir + File.separator + "TSEA4.csv")));

        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        List<Item> items = lines.parallelStream()
                .map(v -> Arrays.asList(v.split(",")))
                .map(v -> DayItem.builder()
                        .name(v.get(0).trim())
                        .date(v.get(1).trim())
                        .openPrice(v.get(2).trim())
                        .highestPrice(v.get(3).trim())
                        .lowestPrice(v.get(4).trim())
                        .closePrice(v.get(5).trim())
                        .build()
                        .toItemEntity())
                .sorted(Comparator.comparing(Item::getDateTime).reversed())
                .collect(toList());

        for (Item item : items) {
            Long count = financeItemRepository.countByNameAndTypeAndDateTime(item.getName(), (short) 4, item.getDateTime());
            if (null != count && count != 0) {
                log.info("found duplicate {}, skip rest ... ", item);
                break;
            }

            financeItemRepository.save(item);
        }

        return OperationResponse.builder().message("saveDayItems success !").build();
    }

    private boolean timeFileter(LocalDateTime dateTime) {
        return (dateTime.getHour() == 10 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 11 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 12 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 13 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 13 && dateTime.getMinute() == 30);
    }

}
