package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.Message;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Transactional(readOnly = true)
public interface MessageMapper {

    @Transactional
    @Insert({"<script>",
    "INSERT INTO system_messages",
            "(id, player_id, date, room_id, type, channel, text, linked_pid, level, clan_id, item_count, nickname, fraction, clan_name, item_name)",
            "VALUES",
    "<foreach collection=\"messages\" item=\"mes\" separator=\", \" index=\"index\">",
    "(#{mes.id}, #{mes.playerId}, #{mes.time}, #{mes.roomId}, #{mes.type}, #{mes.channel}, #{mes.text}, #{mes.linkedPlayerId}, #{mes.level}, #{mes.clanId}, #{mes.itemCount}, #{mes.nickName}, #{mes.fraction}, #{mes.clanName}, #{mes.itemName})",
    "</foreach>",
            "ON CONFLICT (id) DO NOTHING", //FIXED?
    "</script>"})
    void insertMessages(@Param("messages") List<Message> messages);

    @Select("SELECT * FROM system_messages")
    @Results({
            @Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "playerId", column = "player_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "time", column = "date", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "roomId", column = "room_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "type", column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "channel", column = "channel", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "text", column = "text", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "linkedPlayerId", column = "linked_pid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "level", column = "level", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "clanId", column = "clan_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "itemCount", column = "item_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "nickName", column = "nickname", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "fraction", column = "fraction", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "clanName", column = "clan_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "itemName", column = "item_name", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    List<Message> getMessages();

    @Transactional
    @Delete("DELETE FROM system_messages")
    void deleteAll();
}
