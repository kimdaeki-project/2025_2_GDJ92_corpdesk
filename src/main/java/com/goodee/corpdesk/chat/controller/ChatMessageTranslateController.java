package com.goodee.corpdesk.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;
import com.goodee.corpdesk.chat.dto.TranslateDto;


@RestController
@RequestMapping("/chat/translate")

public class ChatMessageTranslateController {
	@Value("${api.deepl.key}")
	private String auth;

    @Value("${cat.chat}")
    private String cat;

    @ModelAttribute("cat")
    public String getCat() {
        return cat;
    }

	@PostMapping("")
	public String translate(@RequestBody TranslateDto translateDto) throws Exception {
		if(translateDto ==null) {
			return null;
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~"+translateDto.getTargetLang()+"~~~~~~~"+translateDto.getText());
		DeepLClient client = new DeepLClient(auth);
		TextResult result= client.translateText(translateDto.getText(),null,translateDto.getTargetLang());
		System.out.println("~~~~~~~~~~~~"+result.getText());
		return result.getText();
	}
}
