package com.syllabix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String dashboard() {
        return "forward:/index.html";
    }

    @GetMapping("/subjects")
    public String subjects() {
        return "forward:/subjects.html";
    }

    @GetMapping("/syllabus")
    public String syllabus() {
        return "forward:/syllabus.html";
    }

    @GetMapping("/calendar")
    public String calendar() {
        return "forward:/calendar.html";
    }

    @GetMapping("/planner")
    public String planner() {
        return "forward:/planner.html";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "forward:/analytics.html";
    }

    @GetMapping("/sessions")
    public String sessions() {
        return "forward:/sessions.html";
    }

    @GetMapping("/insights")
    public String insights() {
        return "forward:/insights.html";
    }

    @GetMapping("/profile")
    public String profile() {
        return "forward:/profile.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }
}
