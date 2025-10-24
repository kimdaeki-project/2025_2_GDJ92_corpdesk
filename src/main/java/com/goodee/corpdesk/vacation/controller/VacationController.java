package com.goodee.corpdesk.vacation.controller;

import com.goodee.corpdesk.approval.service.ApprovalService;
import com.goodee.corpdesk.vacation.dto.ReqVacationDTO;
import com.goodee.corpdesk.vacation.dto.ResVacationDTO;
import com.goodee.corpdesk.vacation.service.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vacation/**")
@Slf4j
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @Value("${cat.vacation}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

    @GetMapping("list")
    public String list(ReqVacationDTO reqVacationDTO,
                       Model model,
                       @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String username = userDetails.getUsername();

        ResVacationDTO vacation = new ResVacationDTO(); // 응답 DTO (연차 집계 정보)
        if("personal".equals(reqVacationDTO.getListType())) {
            ResVacationDTO vacationDTO = vacationService.getVacation(username);
            vacation.setVacationId(vacationDTO == null ? null : vacationDTO.getVacationId());
        }

        List<ResVacationDTO> details = new ArrayList<>(); // 응답 객체 (휴가 사용 목록)

        if(reqVacationDTO.getVacationType() == null){
            // 휴가 유형(연차, 병가 등)이 없는 경우

            if("personal".equals(reqVacationDTO.getListType())) {
                // 개인 휴가 현황

                vacation = vacationService.getVacation(username);

                model.addAttribute("vacation", vacation);
            }

            // vacationId == null -> 전사 휴가 목록 | vacationId != null -> 개인 휴가 목록
            details = vacationService.getVacationDetails(vacation.getVacationId());
        } else {
            // 휴가 유형(연차, 병가 등)이 있는 경우

            if("personal".equals(reqVacationDTO.getListType())) {
                // 개인 휴가 현황

                vacation = vacationService.getVacation(username);

                model.addAttribute("vacation", vacation);
            }

            // vacationId == null -> 전사 휴가 목록 | vacationId != null -> 개인 휴가 목록
            details = vacationService.getVacationDetails(vacation.getVacationId(), reqVacationDTO.getVacationType());

            model.addAttribute("vacationType", reqVacationDTO.getVacationType());
        }

        log.warn("details: {}", details);

        model.addAttribute("details", details);

//        if(reqVacationDTO.getUsername() != null) model.addAttribute("username", reqVacationDTO.getUsername());

        return "vacation/list";

    }

}
