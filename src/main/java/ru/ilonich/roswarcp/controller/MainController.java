package ru.ilonich.roswarcp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.ilonich.roswarcp.repo.RawSqlExecutor;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private RawSqlExecutor rawSqlExecutor;

    @GetMapping("/")
    public String root() {
        return "redirect:console";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/console")
    public String console() {
        return "console";
    }

    @PostMapping(value = "/console")
    public ModelAndView downloadExcel(@RequestParam("sql") String sql) {
        List<List<String>> result = rawSqlExecutor.executeQuery(sql);
        return new ModelAndView("excelView", "result", result);
    }
}
