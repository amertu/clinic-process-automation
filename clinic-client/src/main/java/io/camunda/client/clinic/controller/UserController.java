package io.camunda.client.clinic.controller;

import io.camunda.client.clinic.dto.ApiResponse;
import io.camunda.client.clinic.dto.Treatment;
import io.camunda.client.clinic.dto.User;
import io.camunda.client.clinic.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

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

    @GetMapping("/register")
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

    @GetMapping("/dashboard")
    public Mono<String> dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not found. Please log in.");
            return Mono.just("redirect:/index");
        }
        Long userId = user.getId();
        model.addAttribute("userId", userId);
        return Mono.just("dashboard");
    }

    @GetMapping("/treatmentSent")
    public Mono<String> treatmentSentPage(HttpSession session, Model model) {
        Treatment treatment = (Treatment) session.getAttribute("treatment");
        User user = (User) session.getAttribute("user");

        if (treatment == null || user == null) {
            model.addAttribute("error", "No treatment or user information found.");
            return Mono.just("redirect:/dashboard");
        }

        model.addAllAttributes(Map.of(
                "userName", user.getUserName(),
                "type", treatment.getType(),
                "date", treatment.getDate(),
                "time", treatment.getTime(),
                "notes", treatment.getNotes()
        ));
        return Mono.just("treatmentSent");
    }

    @PostMapping("/registerUser")
    public Mono<String> registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        return userService.registerUser(user)
                .flatMap(savedUser -> Mono.just("redirect:/login"))
                .switchIfEmpty(Mono.defer(() -> {
                    redirectAttributes.addFlashAttribute("error", "Registration failed");
                    return Mono.just("redirect:/register");
                }))
                .onErrorResume(error -> {
                    redirectAttributes.addFlashAttribute("error", "Registration failed");
                    return Mono.just("redirect:/register");
                });
    }

    @PostMapping("/loginUser")
    public Mono<String> loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        User user = new User(email, password);

        return userService.loginUser(user)
                .flatMap(response -> {
                    if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                        model.addAttribute("error", "Login failed: User not authenticated");
                        return Mono.just("redirect:/index");
                    }

                    ApiResponse<User> apiResponse = response.getBody();
                    User loginUser = apiResponse.getData();
                    String processInstanceId = apiResponse.getProcessInstanceId();

                    session.setAttribute("user", loginUser);
                    LOG.debug("Process instance ID: {}", processInstanceId);

                    return Mono.just("redirect:/dashboard");
                })
                .onErrorResume(ex -> {
                    model.addAttribute("message", "Login service unavailable. Please try again later.");
                    return Mono.just("redirect:/index");
                });
    }

    @PostMapping("/sendRequest")
    public Mono<String> requestSent(@RequestParam("type") String type,
                                    @RequestParam("date") String date,
                                    @RequestParam("time") String time,
                                    @RequestParam("note") String note,
                                    Model model,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("message", "User not found. Please log in.");
            return Mono.just("redirect:/index");
        }

        Long userId = user.getId();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Date parsedDate = Date.valueOf(LocalDate.parse(date, dateFormatter));
        Time parsedTime = Time.valueOf(LocalTime.parse(time, timeFormatter));

        Treatment treatment = new Treatment(
                user.getId(),
                type,
                parsedDate,
                parsedTime,
                note,
                Treatment.AppointmentStatus.ACTIVE
        );

        return userService.requestTreatment(treatment)
                .map(response -> {
                    session.setAttribute("treatment", treatment);
                    return "redirect:/treatmentSent";
                })
                .onErrorResume(error -> {
                    LOG.info("Treatment request failed: {}", error.getMessage());
                    model.addAttribute("message", "Treatment request failed. Please try again.");
                    redirectAttributes.addFlashAttribute("userId", userId);
                    return Mono.just("redirect:/dashboard");
                });
    }

}

