package ru.ilonich.roswarcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ilonich.roswarcp.client.Parser;
import ru.ilonich.roswarcp.model.Message;
import ru.ilonich.roswarcp.repo.MessageMapper;

import java.util.List;

/**
 * Created by Никола on 03.05.2017.
 */
@Service
public class SystemMessagesSсheduledSaver {

    @Autowired
    private MessageMapper messageMapper;

    @Scheduled(fixedRate = 30000)
    public void getAndSaveSystemMessages(){
        List<Message> messages = Parser.getMessagesFromChat();
        if (messages != null){
            messageMapper.insertMessages(messages);
        }
    }
}
