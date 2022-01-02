package net.javaguides.springboot.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
 
        return "redirect:/";
	}
	
	
	@GetMapping("/table")
	public String table() {
		return "table";
	}
	@GetMapping("/profile")
	public String profile() {
		return "profile";
	}

	@GetMapping("/")
	public String wether() {
		return "home";
	}
	@GetMapping("/home")
	public String landing() {
		return "home";
	}
	@GetMapping("/blog")
	public String blog() {
		return "blog";
	}
	@GetMapping("/post2")
	public String post2() {
		return "post2";
	}
	@GetMapping("/post3")
	public String post3() {
		return "post3";
	}
	@GetMapping("/post4")
	public String post4() {
		return "post4";
	}
	@GetMapping("/post5")
	public String post5() {
		return "post5";
	}
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	


}
