package ru.ilonich.roswarcp;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ilonich.roswarcp.controller.dto.LoginPassPair;

import java.io.IOException;

/**
 * Created by Никола on 12.01.2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        LoginPassPair pair = new LoginPassPair();
        pair.setLogin("13dasd");
        pair.setPassword("ghwrswyhtrwh");

        String json = objectMapper.writeValueAsString(pair);
        System.out.println(json);
    }
}
