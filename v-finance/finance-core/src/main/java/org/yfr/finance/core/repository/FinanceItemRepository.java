package org.yfr.finance.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yfr.finance.core.pojo.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface FinanceItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameAndTypeOrderByDateTime(String name, Short type);

    List<Item> findByNameAndTypeOrderByDateTimeDesc(String name, Short type);

    Long countByNameAndTypeAndDateTime(String name, Short type, LocalDateTime dateTime);

}