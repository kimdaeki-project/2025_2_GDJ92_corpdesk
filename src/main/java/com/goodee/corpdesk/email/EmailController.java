package com.goodee.corpdesk.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email/**")
public class EmailController {

//	@Autowired
//	EmailService emailService;

    @Value("${cat.mail}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@GetMapping("received")
	public String received() {
		return "email/list";
	}

	@GetMapping("received/{page}")
	public String received(@PathVariable Integer page) {
		return "email/list";
	}

	@GetMapping("received/detail/{emailNo}")
	public String receivedDetail(@PathVariable Integer emailNo) {
		return "email/detail";
	}

	@GetMapping("sending")
	public void sending() {
	}

	@GetMapping("sent")
	public String sent() {
		return "email/list";
	}

	@GetMapping("sent/{page}")
	public String sent(@PathVariable Integer page) {
		return "email/list";
	}

	@GetMapping("sent/detail/{emailNo}")
	public String sentDetail(@PathVariable Integer emailNo) {
		return "email/detail";
	}
}
