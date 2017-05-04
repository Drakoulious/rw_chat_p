package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.User;

/**
 * Created by Илоныч on 27.04.2017.
 */
@Repository
@Transactional(readOnly = true)
public interface UserMapper {
    @Select("SELECT * FROM user_one WHERE login = #{login}")
    User getUser(String login);
}
