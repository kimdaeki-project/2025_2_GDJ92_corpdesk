package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.position.entity.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

	private final StatsService statsService;

    @Value("${cat.stats}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@GetMapping
	public String list(Model model) {

		List<Department> departmentList = statsService.getDepartmentList();
		List<Position> positionList = statsService.getPositionList();

		model.addAttribute("departmentList", departmentList);
		model.addAttribute("positionList", positionList);

		return "stats/list";
	}
}
