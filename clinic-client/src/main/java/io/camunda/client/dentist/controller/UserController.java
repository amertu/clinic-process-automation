package io.camunda.client.dentist.controller;

import io.camunda.client.dentist.model.User;
import io.camunda.client.dentist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String pass,
            @RequestParam("number") String number,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = new User(username, pass, number);
            ResponseEntity<User> registerResponse = userService.registerUser(user);
            if (registerResponse.getStatusCode() != HttpStatus.OK || registerResponse.getBody() == null) {
                model.addAttribute("error", "Registration failed: User not registered");
                return "register";
            }
            User registeredUser = registerResponse.getBody();
            ResponseEntity<Boolean> response = userService.authenticateUser(registeredUser);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null || !response.getBody()) {
                model.addAttribute("error", "Registration failed: User not authenticated");
                return "register";
            }
            userService.startProcess(registeredUser);
            redirectAttributes.addFlashAttribute("user", registeredUser);
            return "redirect:/dashboard?id=" + registeredUser.getId();

        } catch (Exception e) {
            LOG.error("Error during registration: {}", e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/dashboard")
    public String dash() {
        return "dashboard";
    }

    @PostMapping("/loginUser")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes){

        try {
            User user = new User(username, password);
            ResponseEntity<User> loginResponse = userService.startLogin(user);

            if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED
                    || loginResponse.getBody() == null
                    || loginResponse.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("message", "Invalid username or password");
                return "index";
            }

            User loginUser = loginResponse.getBody();
            long userId = loginUser.getId();
            session.setAttribute("userId", loginUser.getId());
            redirectAttributes.addFlashAttribute("user", loginUser);
            return "redirect:/dashboard?id=" + userId;

        } catch (Exception e) {
            model.addAttribute("message", "Login service unavailable. Please try again later.");
            return "index";
        }
    }


}

