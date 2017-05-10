package ru.ilonich.roswarcp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ilonich.roswarcp.client.Authentificator;
import ru.ilonich.roswarcp.client.CurrentState;
import ru.ilonich.roswarcp.repo.RawSqlExecutor;

import java.util.concurrent.Callable;

/**
 * Created by Илоныч on 28.04.2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {
    @Autowired
    RawSqlExecutor rawSqlExecutor;

    @PostMapping
    public Callable<String> authentificate(String login, String password){
        return () -> Authentificator.authentificate(login, password);
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
    public CurrentState.State startMessagesRequests(){
        CurrentState.tryStart();
        return CurrentState.getStatus();
    }

    @GetMapping("/off")
    public CurrentState.State stopMessagesRequests(){
        CurrentState.tryStop();
        return CurrentState.getStatus();
    }

    @PostMapping("/sql")
    public String execSql(String sql){
        System.out.println(sql);
        String re = rawSqlExecutor.executeDirectQuery(sql);
        System.out.println(re);
        return re;
    }

}
