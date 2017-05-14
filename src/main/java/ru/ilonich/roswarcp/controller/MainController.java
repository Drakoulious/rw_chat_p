package ru.ilonich.roswarcp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.ilonich.roswarcp.model.Message;
import ru.ilonich.roswarcp.repo.MessageMapper;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private MessageMapper messageMapper;

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

    @GetMapping(value = "/console/messages.xls")
    public ModelAndView downloadExcel() {
        List<Message> listBooks = messageMapper.getMessages();
        return new ModelAndView("excelView", "messages", listBooks);
    }
}
