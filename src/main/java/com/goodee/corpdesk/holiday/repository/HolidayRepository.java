package com.goodee.corpdesk.holiday.repository;

import com.goodee.corpdesk.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    void deleteByLocdateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByLocdate(LocalDate locdate);

}
