package com.goodee.corpdesk.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error/**")
public class ExceptionController {

    @GetMapping("{errorCode}")
    public String handleError(@PathVariable("errorCode") String errorCode,
                              @ModelAttribute(value = "errorMessage", binding = false) String errorMessage,
                              Model model){

        model.addAttribute("errorCode", errorCode);

        // Flash attribute로 전달된 errorMessage가 없을 경우 기본 메시지 설정
        if(errorMessage == null || errorMessage.isEmpty()) errorMessage = "오류가 발생했습니다.";
        model.addAttribute("errorMessage", errorMessage);

        return "error/error_page";

    }

}
