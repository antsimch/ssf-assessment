package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import vttp2023.batch3.ssf.frontcontroller.model.User;

@Controller
public class ProtectedController {

	// TODO Task 5
	// Write a controller to protect resources rooted under /protected
	@GetMapping(path = "/protected/{resourceName}")
	public String getProtectedResource(@PathVariable String resourceName, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		
		if (!user.isAuthenticated())
			return "view0";

		return resourceName;
	}
}
