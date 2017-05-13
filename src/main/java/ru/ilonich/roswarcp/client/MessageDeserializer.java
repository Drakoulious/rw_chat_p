package ru.ilonich.roswarcp.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Илоныч on 14.04.2017.
 */
public class MessageDeserializer extends StdDeserializer<List<Message>> {

    public MessageDeserializer(){
        this(null);
    }

    public MessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Message> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode resultNode = jp.getCodec().readTree(jp);
        JsonNode messagesNode = resultNode.at("/result/messages");
        JsonNode playersNode = resultNode.at("/result/players");
        ArrayList<Message> result = new ArrayList<>();
        for (final JsonNode messageNode : messagesNode) {
            Message message = new Message();
            message.setId(messageNode.get("id").asInt());
            message.setPlayerId(messageNode.get("player_from").asInt());
            message.setTime(Timestamp.from(Instant.ofEpochMilli(messageNode.get("time").asLong() * 1000)));
            message.setRoomId(messageNode.get("roomid").asInt());
            message.setType(messageNode.get("type").asText());
            message.setChannel(messageNode.get("channel").asInt());
            message.setText(messageNode.get("message").asText());
            message.setLinkedPlayerId(messageNode.get("targets").asInt(0));
            JsonNode playerNode = playersNode.get(String.valueOf(message.getPlayerId()));
            message.setNickName(playerNode.get("nickname").asText());
            message.setFraction(playerNode.get("fraction").asText());
            message.setLevel(playerNode.get("level").asInt());
            message.setClanId(playerNode.get("clan_id").asInt());
            message.setItemName(playerNode.get("clan_status").asText(""));
            message.setItemCount(playerNode.get("flags").asInt());
            message.setClanName(playerNode.get("clan_name").asText());
            result.add(message);
        }
        return result;
    }
}
