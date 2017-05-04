package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.Message;

import java.util.List;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Repository
@Transactional(readOnly = true)
public interface MessageMapper {

    @Transactional
    @Insert({
    "INSERT INTO system_messages",
            "(id, player_id, date, room_id, type, channel, text, linked_pid, level, clan_id, flags, nickname, fraction, clan_name, clan_status)",
            "VALUES",
    "<foreach collection=\"messages\" item=\"mes\", separator=\", \" open =\"(\" close=\")\" >",
    "#{mes.getId()}, #{mes.getPlayerId()}, #{mes.getTime()}, #{mes.getRoomId()}, #{mes.getType()}, #{mes.getChannel()}, #{mes.getText()}, #{mes.getLinkedPlayerId()}, #{mes.getLevel()}, #{mes.getClanId()}, #{mes.getFlags()}, #{mes.getNickName()}, #{mes.getFraction()}, #{mes.getClanName()}, #{mes.getClanStatus()}",
    "</foreach>"})
    void insertMessages(@Param("messages") List<Message> messages);
}
