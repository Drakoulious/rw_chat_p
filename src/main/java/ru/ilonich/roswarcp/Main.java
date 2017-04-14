package ru.ilonich.roswarcp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.ilonich.roswarcp.client.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by Никола on 12.01.2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String test ="{\"result\":{\"messages\":[{\"id\":\"44782513\",\"player_from\":\"131716\",\"time\":\"1492178873\",\"roomid\":\"0\",\"type\":\"system\",\"channel\":\"0\",\"fraction\":\"0\",\"level\":\"0\",\"message\":\"<span class=\\\"user vote\\\"><i class=\\\"resident\\\" title=\\\"\\u041a\\u043e\\u0440\\u0435\\u043d\\u043d\\u043e\\u0439\\\"><\\/i><a href=\\\"\\/clan\\/14\\/\\\"><img src=\\\"\\/@images\\/clan\\/clan_14_ico.png\\\" class=\\\"clan-icon\\\" title=\\\"EVOLUTION\\\" \\/><\\/a><a href=\\\"\\/player\\/365051\\/\\\">\\u0410\\u043b\\u0435\\u043a\\u0441\\u0430\\u043d\\u0434\\u0440955<\\/a> <span class=\\\"level\\\">[14]<\\/span><\\/span> \\u043e\\u0442\\u043a\\u0440\\u044b\\u0432\\u0430\\u0435\\u0442 \\u043b\\u043e\\u0442\\u0435\\u0440\\u0435\\u0439\\u043d\\u044b\\u0439 \\u0431\\u0438\\u043b\\u0435\\u0442 \\u00ab\\u0427\\u0435\\u0440\\u043d\\u044b\\u0439 \\u0414\\u0436\\u044d\\u043a\\u00bb \\u0438 \\u043f\\u043e\\u043b\\u0443\\u0447\\u0430\\u0435\\u0442 <br \\/><div class=\\\"objects\\\"><span class=\\\"object-thumb\\\">\\r\\n\\t\\t\\t\\t\\t\\t<div class=\\\"padding\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t<img src=\\\"\\/@\\/images\\/obj\\/jobs\\/item6.png\\\" alt=\\\"\\u041d\\u0435 \\u0442\\u043e\\u0440\\u043c\\u043e\\u0437\\u0438, \\u0441\\u043d\\u0438\\u043a\\u0435\\u0440\\u0441\\u043d\\u0438!\\\" title=\\\"\\u041d\\u0435 \\u0442\\u043e\\u0440\\u043c\\u043e\\u0437\\u0438, \\u0441\\u043d\\u0438\\u043a\\u0435\\u0440\\u0441\\u043d\\u0438!\\\" \\/>\\r\\n\\t\\t\\t\\t\\t\\t\\t<div class=\\\"count\\\">#500<\\/div>\\r\\n\\t\\t\\t\\t\\t\\t<\\/div>\\r\\n\\t\\t\\t\\t\\t<\\/span><\\/div>\",\"targets\":\"\",\"active\":\"1\"}],\"removed\":[],\"players\":{\"131716\":{\"id\":\"131716\",\"flags\":\"626697\",\"nickname\":\" \\u0413\\u0430\\u0437\\u0435\\u0442\\u0447\\u0438\\u043a\",\"lastactivitytime\":\"2013-07-05 06:59:14\",\"fraction\":\"arrived\",\"level\":\"1\",\"clan\":\"0\",\"avatar\":\"girl6.png\",\"forum_avatar\":\"0\",\"clan_status\":\"\",\"clan_id\":\"0\",\"clan_name\":\"\",\"status\":\"offline\"}}},\"error\":[],\"system\":[]}";
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        mapper.registerModule(module);
        List<Message> messages = mapper.readValue(test, List.class);
        System.out.println(messages.size());

        //TODO JsonMappingException (Can not deserialize instance of java.util.ArrayList out of START_OBJECT token)



        /*ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Thread.currentThread();
            while (!Thread.interrupted()){
                Parser.getMessagesFromChat();
            }
        });
        System.out.println(Authentificator.authentificate("", ""));
        try {
            TimeUnit.SECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CurrentState.tryStart();
        System.out.println("старт");
        try {
            TimeUnit.SECONDS.sleep(210);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CurrentState.tryStop();
        System.out.println("стоп");
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();*/
    }
}
