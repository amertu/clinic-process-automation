package io.camunda.client.dentist.controller;

import io.camunda.client.dentist.dto.ApiResponse;
import io.camunda.client.dentist.dto.Treatment;
import io.camunda.client.dentist.dto.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping( "/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping( "/dashboard")
    public String dash(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not found. Please log in.");
            return "redirect:/index";
        }
        Long userId = user.getId();
        model.addAttribute("userId", userId);
        return "dashboard";
    }

    @GetMapping("/treatmentSent")
    public String treatmentSentPage(HttpSession session, Model model) {
        Treatment treatment = (Treatment) session.getAttribute("treatment");
        User user = (User) session.getAttribute("user");
        if (treatment == null || user == null) {
            model.addAttribute("error", "No treatment or user information found.");
            return "redirect:/dashboard";
        }
        model.addAllAttributes(Map.of(
                "userName", user.getUsername(),
                "type", treatment.getType(),
                "date", treatment.getDate(),
                "time", treatment.getTime(),
                "notes", treatment.getNotes()
        ));
        return "treatmentSent";
    }

    @PostMapping("/registerUser")
    public String registerUser(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String pass,
            @RequestParam("number") String number,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        try {
            User user = new User(email, username, pass, number);
            ResponseEntity<User> registerResponse = userService.registerUser(user);
            if (registerResponse.getStatusCode() != HttpStatus.OK || registerResponse.getBody() == null) {
                model.addAttribute("error", "Registration failed: User not registered");
                return "redirect:/register";
            }
            User registeredUser = registerResponse.getBody();
            boolean authenticated = userService.authenticateUser(registeredUser);
            if (!authenticated) {
                model.addAttribute("error", "Registration failed: User not authenticated");
                return "redirect:/register";
            }
            ResponseEntity<ApiResponse<User>> response = userService.loginUser(registeredUser);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                model.addAttribute("error", "Registration failed: User not authenticated");
                return "redirect:/register";
            }
            session.setAttribute("user", registeredUser);
            redirectAttributes.addFlashAttribute("userId", registeredUser.getId());
            return "redirect:/dashboard";

        } catch (Exception e) {
            LOG.error("Error during registration: {}", e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/loginUser")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        try {
            User user = new User(email, password);
            ResponseEntity<ApiResponse<User>> loginResponse = userService.loginUser(user);

            if (loginResponse.getStatusCode() != HttpStatus.OK || loginResponse.getBody() == null) {
                model.addAttribute("error", "Login failed: User not authenticated");
                return "redirect:/index";
            }

            User loginUser = loginResponse.getBody().getData();
            String processInstanceId = loginResponse.getBody().getProcessInstanceId();
            LOG.debug("Process instance ID: {}", processInstanceId);
            session.setAttribute("user", loginUser);
            return "redirect:/dashboard";

/*            ResponseEntity<ApiResponse<ProcessTaskDto>> response = userService.getTaskInfo(processInstanceId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Unable to fetch current task");
            }

            ProcessTaskDto task = response.getBody().getData();
            String taskId = task.getTaskId();

            switch (taskId) {
                case "treatment_request" -> {
                    session.setAttribute("userId", userId);
                    return "redirect:/dashboard";
                }
                default -> {
                    model.addAttribute("error", "Task not found");
                    return "redirect:/dashboard";
                }
            }*/

        } catch (Exception e) {
            model.addAttribute("message", "Login service unavailable. Please try again later.");
            return "redirect:/index";
        }


    }

    @PostMapping("/sendRequest")
    public String requestSent(@RequestParam("type") String type,
                              @RequestParam("date") String date,
                              @RequestParam("time") String time,
                              @RequestParam("note") String note,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("message", "User not found. Please log in.");
            return "redirect:/index";
        }
        Long userId = user.getId();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Date parsedDate = Date.valueOf(LocalDate.parse(date, dateFormatter));
        Time parsedTime = Time.valueOf(LocalTime.parse(time, timeFormatter));
        Treatment treatment = new Treatment(user.getId(), type, parsedDate, parsedTime, note, Treatment.AppointmentStatus.ACTIVE);

        ResponseEntity<String> response = userService.requestTreatment(treatment);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            LOG.info("Treatment request failed");
            model.addAttribute("message", "Treatment request failed. Please try again.");
            redirectAttributes.addFlashAttribute("userId", userId);
            return "redirect:/dashboard";
        }
        session.setAttribute("treatment", treatment);
        return "redirect:/treatmentSent";
    }

}

