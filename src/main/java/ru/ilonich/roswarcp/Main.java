package ru.ilonich.roswarcp;

import ru.ilonich.roswarcp.client.Authentificator;
import ru.ilonich.roswarcp.client.ChatMessagesRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import ru.ilonich.roswarcp.client.CurrentState;
import ru.ilonich.roswarcp.client.Parser;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by Никола on 12.01.2017.
 */
public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
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
        executorService.shutdownNow();
    }
}
