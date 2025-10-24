package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {

	Vacation findByUseYnAndUsername(Boolean useYn, String username);

}
