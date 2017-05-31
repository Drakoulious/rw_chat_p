package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.GiftsSnapshot;

import java.sql.Timestamp;

/**
 * Created by Никола on 30.05.2017.
 */
@Transactional(readOnly = true)
public interface GiftsMapper {

    @Insert({"INSERT INTO may_gifts",
            "(new_gifts_quantity, last_gift_id, snapshot_time, id_delta, time_delta, horn_delta)",
            "VALUES",
            "(#{gifts.giftsQuantity}, #{gifts.lastGiftId}, #{gifts.time}, #{gifts.idDelta}, #{gifts.timeDelta}, #{gifts.hornDelta})"
    })
    @Transactional
    void insertRow(@Param("gifts") GiftsSnapshot giftsSnapshot);

    @Select({"SELECT new_gifts_quantity, last_gift_id, snapshot_time, id_delta, time_delta, horn_delta",
        "FROM may_gifts ORDER BY line_num DESC LIMIT 1"})
    @Results({
            @Result(property = "giftsQuantity", column = "new_gifts_quantity", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "lastGiftId", column = "last_gift_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "time", column = "snapshot_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "idDelta", column = "id_delta", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "hornDelta", column = "horn_delta", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "timeDelta", column = "time_delta", javaType = Long.class, jdbcType = JdbcType.INTEGER)
    })
    GiftsSnapshot getLast();
}
