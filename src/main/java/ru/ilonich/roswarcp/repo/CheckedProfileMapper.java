package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.CheckedProfile;

@Transactional(readOnly = true)
public interface CheckedProfileMapper {

    @Transactional
    @Insert({"INSERT INTO checked_profiles",
            "(message_id, player_id, gift_data_id, date) VALUES",
            "(#{cp.playerId}, #{cp.triggerSysMesId}, #{cp.lastPrizeDataId}, #{cp.lastPrizeDate})",
            "ON CONFLICT (message_id) DO NOTHING"})
    void save(@Param("cp") CheckedProfile cp);
}
