package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch3.ssf.frontcontroller.model.Captcha;
import vttp2023.batch3.ssf.frontcontroller.model.Login;
import vttp2023.batch3.ssf.frontcontroller.model.User;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
public class FrontController {

	AuthenticationService authService;

	public FrontController(AuthenticationService authService) {
		this.authService = authService;
	}

	@GetMapping(path = "/login")
	public String getLandingPage(Model model) {
		model.addAttribute("user", new User());
		return "view0";
	}

	// TODO: Task 2, Task 3, Task 4, Task 6
	@PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded", produces = "text/html")
	public String postLogin(@Valid User user, BindingResult binding, Captcha captcha, Model model, HttpSession session) throws Exception {
		
		Login login = (Login) session.getAttribute("login");

		if (login == null) 
			login = new Login();

		// System.out.println(login.getLoginAttempts());

		//captcha authentication
 		if (login.getLoginAttempts() > 0) {

			if (captcha.getAnswer() != captcha.getResult()) {

				login.setLoginAttempts(login.getLoginAttempts() + 1);
				session.setAttribute("login", login);
				model.addAttribute("captcha", new Captcha());

				if (login.getLoginAttempts() >= 3) {

					// System.out.println("disabling user");
					authService.disableUser(login.getUsername());
					session.invalidate();
					return "view2";
				}

				return "view0";
			}
		}

		//if there are validation errors, return to view0
		if (binding.hasErrors())
			return "view0";

		//check if user is disabled
		if (authService.isLocked(user.getUsername())) {
			model.addAttribute("user", user);
			return "view2";
		}

		//try catch for 400 range status code but only count 400 and 401 status codes as failed login attempts
		try {

			authService.authenticate(user.getUsername(), user.getPassword());

		} catch (HttpClientErrorException httpClientErrorException) {

			if (!httpClientErrorException.getStatusCode().equals(HttpStatus.UNAUTHORIZED) &&
					!httpClientErrorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {

				return "view0";
			}

			// System.out.println("exception");
			login.setUsername(user.getUsername());
			login.setLoginAttempts(login.getLoginAttempts() + 1);
			session.setAttribute("login", login);
			model.addAttribute("captcha", new Captcha());

			if (login.getLoginAttempts() >= 3) {

				authService.disableUser(login.getUsername());
				session.invalidate();
				return "view2";
			}

			if (httpClientErrorException.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) 
				model.addAttribute("error", 
						"Invalid username and/or password. Please try again.");
			
			if (httpClientErrorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) 
				model.addAttribute("error", 
						"Invalid payload, viz the POST request's payload is incorrect");

			return "view0";
		}

		user.setAuthenticated(true);
		session.setAttribute("login", login);
		return "view1";
	}
}
