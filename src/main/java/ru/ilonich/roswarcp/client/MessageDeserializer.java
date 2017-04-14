package ru.ilonich.roswarcp.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;

/**
 * Created by Илоныч on 14.04.2017.
 */
public class MessageDeserializer extends StdDeserializer<Message> {

    public MessageDeserializer(){
        this(null);
    }

    public MessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Message deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode resultNode = jp.getCodec().readTree(jp);
        JsonNode messagesNode = resultNode.get("messages");
        JsonNode playersNode = resultNode.get("players");
        Message message = new Message();
        message.setId(messagesNode.get("id").asInt());
        message.setPlayerId(messagesNode.get("player_from").asInt());
        message.setTime(messagesNode.get("time").asLong());
        message.setRoomId(messagesNode.get("roomid").asInt());
        message.setType(messagesNode.get("type").asText());
        message.setChannel(messagesNode.get("channel").asInt());
        message.setText(messagesNode.get("message").asText()); //переделать под юникод
        message.setLinkedPlayerId(messagesNode.get("targets").asInt(0));
        String playerIdNodeName = String.valueOf(message.getPlayerId());
        message.setNickName(playersNode.get(playerIdNodeName).get("nickname").asText()); //юникод вместо русского
        message.setFraction(playersNode.get(playerIdNodeName).get("fraction").asText());
        message.setLevel(playersNode.get(playerIdNodeName).get("level").asInt());
        message.setClanId(playersNode.get(playerIdNodeName).get("clan_id").asInt());
        message.setClanStatus(playersNode.get(playerIdNodeName).get("clan_status").asText(""));
        message.setFlags(playersNode.get(playerIdNodeName).get("flags").asInt());
        message.setClanName(playersNode.get(playerIdNodeName).get("clan_name").asText()); //юникод десу


        return message;
    }
}
