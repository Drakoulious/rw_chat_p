package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.Message;

import java.util.List;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Transactional(readOnly = true)
public interface MessageMapper {

    @Transactional
    @Insert({"<script>",
    "INSERT INTO system_messages",
            "(id, player_id, date, room_id, type, channel, text, linked_pid, level, clan_id, flags, nickname, fraction, clan_name, clan_status)",
            "VALUES",
    "<foreach collection=\"messages\" item=\"mes\" separator=\", \" index=\"index\">",
    "(#{mes.id}, #{mes.playerId}, #{mes.time}, #{mes.roomId}, #{mes.type}, #{mes.channel}, #{mes.text}, #{mes.linkedPlayerId}, #{mes.level}, #{mes.clanId}, #{mes.flags}, #{mes.nickName}, #{mes.fraction}, #{mes.clanName}, #{mes.clanStatus})",
    "</foreach>",
            "ON CONFLICT (id) DO NOTHING", //FIXED?
    "</script>"})
    void insertMessages(@Param("messages") List<Message> messages);
}
