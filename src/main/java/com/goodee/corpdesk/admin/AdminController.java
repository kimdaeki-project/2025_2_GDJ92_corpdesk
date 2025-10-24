package com.goodee.corpdesk.admin;

import com.goodee.corpdesk.employee.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

    @Value("${cat.admin}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@GetMapping
	public String list(@PageableDefault(size = 10)Pageable pageable, Model model) {
		int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
		Page<Map<String, Object>> page = adminService.employeeList(pageable.withPage(pageNumber));
		List<Map<String, Object>> employeeList = page.getContent();
		List<Role> roleList = adminService.roleList();

		Integer startPage = (int) Math.floor(page.getNumber() / 5) * 5;
		Integer endPage = (int) Math.min(startPage + 4, page.getTotalPages() - 1);

		model.addAttribute("employeeList", employeeList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("page", page);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/list";
	}

	@PostMapping
	@ResponseBody
	public String updateRole(@RequestBody Map<String, Object> payload) {
		adminService.updateRole((String) payload.get("username"), (Integer) payload.get("roleId"));
		return "success";
	}
}
