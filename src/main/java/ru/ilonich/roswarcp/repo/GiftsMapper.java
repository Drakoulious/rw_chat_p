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
            "(new_gifts_quantity, last_gift_id, snapshot_time, id_delta, time_delta, horn_delta, small_count, medium_count, big_count, not_opened_count, small_count_p, medium_count_p, big_count_p)",
            "VALUES",
            "(#{gifts.giftsQuantity}, #{gifts.lastGiftId}, #{gifts.time}, #{gifts.idDelta}, #{gifts.timeDelta}, #{gifts.hornDelta}, #{gifts.small}, #{gifts.medium}, #{gifts.big}, #{gifts.notOpened}, #{gifts._small}, #{gifts._medium}, #{gifts._big})"
    })
    @Transactional
    void insertRow(@Param("gifts") GiftsSnapshot giftsSnapshot);

    @Select({"SELECT new_gifts_quantity, last_gift_id, snapshot_time, id_delta, time_delta, horn_delta, small_count, medium_count, big_count, not_opened_count, small_count_p, medium_count_p, big_count_p",
        "FROM may_gifts ORDER BY line_num DESC LIMIT 1"})
    @Results({
            @Result(property = "giftsQuantity", column = "new_gifts_quantity", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "lastGiftId", column = "last_gift_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "time", column = "snapshot_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "idDelta", column = "id_delta", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "hornDelta", column = "horn_delta", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "timeDelta", column = "time_delta", javaType = Long.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "small", column = "small_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "medium", column = "medium_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "big", column = "big_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "notOpened", column = "not_opened_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "_small", column = "small_count_p", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "_medium", column = "medium_count_p", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "_big", column = "big_count_p", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    GiftsSnapshot getLast();
}
