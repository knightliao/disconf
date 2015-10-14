package com.baidu.dsp.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.dsp.common.annotation.NoAuth;

@Controller
public class IndexController {

	@NoAuth
	@RequestMapping("/index")
	public String index() {
		return "redirect:/html/index.html";  
	}
}
