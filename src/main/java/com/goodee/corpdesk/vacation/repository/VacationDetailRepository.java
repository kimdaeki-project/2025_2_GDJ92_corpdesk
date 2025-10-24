package com.goodee.corpdesk.vacation.repository;

import com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO;
import com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationDetailRepository extends JpaRepository<VacationDetail, Long> {

    @NativeQuery("""
        WITH vd AS (
        	SELECT *
        	FROM vacation_detail
        	WHERE (vacation_id = (SELECT vacation_id FROM vacation WHERE username = :username))
        		AND start_date <= :date
        )
        SELECT *
        FROM vd
        WHERE :date <= end_date
        ORDER BY updated_at DESC
        LIMIT 1
    """)
    VacationDetail findVacationDetailOnDate(@Param("username") String username, @Param("date")LocalDate date);

    @NativeQuery("""
        SELECT sum(used_days)
        FROM vacation_detail
        WHERE username = :username
    """)
    Integer countUsedVacationDays(@Param("username") String username);

    List<VacationDetail> findAllVacationDetailByUseYnAndVacationId(Boolean useYn, Integer vacationId);

    List<VacationDetail> findAllVacationDetailByUseYn(Boolean useYn);

    List<VacationDetail> findAllVacationDetailByUseYnAndVacationIdAndVacationTypeId(Boolean useYn, Integer vacationId, Integer vacationTypeId);

    List<VacationDetail> findAllVacationDetailByUseYnAndVacationTypeId(Boolean useYn, Integer vacationTypeId);

	// 캘린더
	@Query("""
	SELECT new com.goodee.corpdesk.vacation.dto.VacationDetailTypeDTO(
		vd.vacationDetailId,
		vd.vacationId,
		vd.vacationTypeId,
		vd.startDate,
		vd.endDate,
		vt.vacationTypeName
	)
	FROM VacationDetail vd
	JOIN Vacation v ON v.vacationId = vd.vacationId
	JOIN VacationType vt ON vd.vacationTypeId = vt.vacationTypeId
	WHERE v.username = :username
	AND vd.useYn = true
	AND (
		vd.startDate <= :end
		AND
		vd.endDate >= :start
	)
""")
	List<VacationDetailTypeDTO> findAllByVacationIdAndDate(String username, LocalDate start, LocalDate end);

	@Query("""
	SELECT new com.goodee.corpdesk.vacation.dto.VacationDetailUsernameDTO(
		vd.vacationDetailId,
		vd.vacationId,
		vd.vacationTypeId,
		vd.startDate,
		vd.endDate,
		e.name
	)
	FROM VacationDetail vd
	JOIN Vacation v ON vd.vacationId = v.vacationId
	JOIN Employee e ON v.username = e.username
	WHERE vd.useYn = true
	AND (
		vd.startDate <= :end
		AND
		vd.endDate >= :start
	)
""")
	List<VacationDetailUsernameDTO> findEveryByDate(LocalDate start, LocalDate end);
}
