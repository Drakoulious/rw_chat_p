package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.CheckedProfile;

@Transactional(readOnly = true)
public interface CheckedProfileMapper {

    @Transactional
    @Insert({"INSERT INTO checked_profiles",
            "(message_id, player_id, gift_data_id, date, message_date) VALUES",
            "(#{cp.triggerSysMesId}, #{cp.playerId}, #{cp.lastPrizeDataId}, #{cp.lastPrizeDate}, #{cp.triggerSysMesDate})",
            "ON CONFLICT (message_id) DO NOTHING"})
    void save(@Param("cp") CheckedProfile cp);
}
