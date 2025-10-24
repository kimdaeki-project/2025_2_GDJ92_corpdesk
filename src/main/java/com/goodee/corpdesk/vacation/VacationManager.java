package com.goodee.corpdesk.vacation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class VacationManager {

    // 특정 입사일의 총발생연차 계산
    public Integer calTotalVacation(LocalDate hireDate) throws Exception {

        LocalDate today = LocalDate.now();

        // 2. 재직 기간 계산
        Integer monthsWorked = (int) ChronoUnit.MONTHS.between(hireDate, today);
        Integer yearsWorked = (int) ChronoUnit.YEARS.between(hireDate, today);

        Integer totalVacation = 0;

        // 3. 재직 기간 1년 미만인 경우
        if (yearsWorked == 0) {
            // 1개월마다 1일 부여 (최대 11일)
            totalVacation = monthsWorked;
        }
        // 4. 재직 기간 1년 이상인 경우
        else {
            // 기본 15일 부여
            totalVacation = yearsWorked * 15;

            // 5. 가산 연차 계산 (3년 이상 근속 시)
            if (yearsWorked >= 3) {
                // 2년마다 1일씩 추가
                int bonusVacation = (yearsWorked - 1) / 2;
                totalVacation += bonusVacation;
            }
        }

        return totalVacation;
    }
}
