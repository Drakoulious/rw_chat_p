package ru.ilonich.roswarcp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ilonich.roswarcp.client.Authentificator;
import ru.ilonich.roswarcp.client.CurrentState;

import java.util.concurrent.Callable;

/**
 * Created by Илоныч on 28.04.2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<Boolean> authentificate(String login, String password){
        Callable<Boolean> slowLogin = () -> Authentificator.authentificate(login, password);
        return slowLogin;
    }

    @GetMapping("/player")
    public String getSettedLogin(){
        return CurrentState.getSettedLogin();
    }

    @GetMapping("/status")
    public CurrentState.State getStatus(){
        return CurrentState.getStatus();
    }

    @GetMapping("/on")
    public boolean startMessagesRequests(){
        return CurrentState.tryStart();
    }

    @GetMapping("/off")
    public boolean stopMessagesRequests(){
        return CurrentState.tryStop();
    }

}
