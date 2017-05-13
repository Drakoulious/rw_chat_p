package ru.ilonich.roswarcp.task;

import ru.ilonich.roswarcp.client.Parser;
import ru.ilonich.roswarcp.model.Message;
import ru.ilonich.roswarcp.repo.MessageMapper;

import java.util.List;

/**
 * Created by Никола on 13.05.2017.
 */
public class GetAndSaveSystemMessagesTask {

    private MessageMapper messageMapper;

    public GetAndSaveSystemMessagesTask(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public void task(){
        List<Message> messages = Parser.getMessagesFromChat();
        if (messages != null && !messages.isEmpty()){
            messageMapper.insertMessages(messages);
        }
    }
}
