package com.goodee.corpdesk.vacation;

import com.goodee.corpdesk.vacation.repository.VacationRepository;
import com.goodee.corpdesk.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VacationScheduler {

    @Autowired
    private VacationService vacationService;

    @Scheduled(cron = "0 0 2 * * *")
    public void updateVacationsDaily() throws Exception {
        vacationService.updateVacationsByHireDate(LocalDate.now());
    }

}
