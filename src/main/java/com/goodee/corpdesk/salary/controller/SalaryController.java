package com.goodee.corpdesk.salary.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salary")
public class SalaryController {

	@Value("${cat.salary}")
	private String cat;

	@ModelAttribute("cat")
	public String getCat() {
		return cat;
	}

	@RequestMapping
	public String list() {
		return "salary/list";
	}

//	@RequestMapping("/{page}")
//	public String list(@PathVariable Integer page) {
//		return "salary/list";
//	}

	@RequestMapping("/{paymentId}")
	public String detail(@PathVariable Long paymentId) {
		return "salary/detail";
	}
}
