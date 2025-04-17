package io.camunda.client.dentist.controller;

import io.camunda.client.dentist.model.User;
import io.camunda.client.dentist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;

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
            Model model
    ) {
        try {
            User user = new User(username, pass, number);
            userService.registerUser(user);
            userService.authenticateUser();
            userService.startProcess(user);
            return "dashboard";

        } catch (Exception e) {
            LOG.error("Error during registration: {}", e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        try {
            userService.login();
            return "index";
        } catch (RestClientException e) {
            LOG.error("Error during client start request", e);
            return "register";
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/dashboard")
    public String dash(@RequestParam("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "dashboard";
    }

    @RequestMapping(value = "/loginUser")
    public String loginUser(@RequestParam("username") String name,
                            @RequestParam("password") String pass,
                            Model model) {
        User user = new User(name, pass);
        var rt = userService.startLogin(user);
        if (rt.getBody() == null) {
            LOG.info("Password is not correct or user does not exist");
            model.addAttribute("message", "Password is not correct or user does not exist");
            return "index";
        }
        String id = String.valueOf(rt.getBody().get("id"));
        String password = String.valueOf(rt.getBody().get("password"));
        if (password == null || !password.equals(pass)) {
            LOG.info("Password is not correct");
            model.addAttribute("message", "Password is not correct");
            return "index";
        }
        model.addAttribute("pw", password);
        model.addAttribute("id", id);
        userService.authenticateUser();
        model.addAttribute("message", "");
        return "dashboard";
    }


}

