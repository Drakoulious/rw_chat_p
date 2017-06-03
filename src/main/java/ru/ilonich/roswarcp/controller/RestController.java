package ru.ilonich.roswarcp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ilonich.roswarcp.client.Authentificator;
import ru.ilonich.roswarcp.client.CurrentState;
import ru.ilonich.roswarcp.model.SqlCommand;
import ru.ilonich.roswarcp.repo.RawSqlExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public String execSql(@RequestParam("value") String sql){
        return rawSqlExecutor.executeDirectQuery(sql);
    }

    @GetMapping("/command")
    public List<SqlCommand> getSqlCommands(){
        List<SqlCommand> result = new ArrayList<>();
        List<List<String>> res = rawSqlExecutor.executeQuery("SELECT * FROM sql_commands");
        if (res.size() < 2) return result;
        for (int i = 1; i < res.size(); i++) {
            List<String> row = res.get(i);
            result.add(new SqlCommand(Integer.parseInt(row.get(0)), row.get(1), row.get(2)));
        }
        return result;
    }

    @PostMapping("/command")
    public ResponseEntity addSqlCommand(@RequestParam("title") String title, @RequestParam("sqlx") String sql){
        rawSqlExecutor.executeDirectQuery(String.format("INSERT INTO sql_commands (title, sql) VALUES ('%s', '%s')", title, sql));
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("/command")
    public void deleteSqlCommand(@RequestParam("id") Integer id){
        rawSqlExecutor.executeDirectQuery(String.format("DELETE FROM sql_commands WHERE line_num=%d", id));
    }

}
