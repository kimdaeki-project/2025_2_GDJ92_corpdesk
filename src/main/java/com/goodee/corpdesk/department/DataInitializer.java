package com.goodee.corpdesk.department;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;

@Configuration
public class DataInitializer {


	
	 @Bean
	    CommandLineRunner initData(DepartmentRepository departmentRepository,
                                   PositionRepository positionRepository) {
	        return args -> {
//	            // Department 초기 데이터
//	            if (departmentRepository.count() == 0) {
//	                // root (level 0)
//                    departmentRepository.save(new Department(1, null, "인피니티오토"));
//
//                    // (level 1)
//                    departmentRepository.save(new Department(2, 1, "개발팀"));
//	                departmentRepository.save(new Department(3, 1, "인사팀"));
//                    departmentRepository.save(new Department(4, 1, "기획팀"));
//
//                    // (level 2)
//                    departmentRepository.save(new Department(5, 2, "백엔드팀"));
//                    departmentRepository.save(new Department(6, 2, "프론트엔드팀"));
//
//                    departmentRepository.save(new Department(7, 4, "서비스기획팀"));
//                    departmentRepository.save(new Department(8, 4, "상품기획팀"));
//
//                    // (level 3)
//                    departmentRepository.save(new Department(9, 8, "서비스기획1파트"));
//                    departmentRepository.save(new Department(10, 8, "서비스기획2파트"));
//	            }
//
//	            // Position 초기 데이터
//	            if (positionRepository.count() == 0) {
//	                positionRepository.save(new Position(null, "부장"));
//	                positionRepository.save(new Position(1, "차장"));
//	                positionRepository.save(new Position(2, "과장"));
//	                positionRepository.save(new Position(3, "대리"));
//	                positionRepository.save(new Position(4, "사원"));
//	            }
	        };
	    }
}