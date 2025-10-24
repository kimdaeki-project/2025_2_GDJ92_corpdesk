package com.goodee.corpdesk.calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Value("${cat.calendar}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@GetMapping("/list")
	public void list() {
	}
}
