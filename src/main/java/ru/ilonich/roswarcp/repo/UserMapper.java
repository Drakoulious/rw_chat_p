package ru.ilonich.roswarcp.repo;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ilonich.roswarcp.model.User;

@Repository
@Transactional(readOnly = true)
public interface UserMapper {
    @Select("SELECT * FROM user_one WHERE login = #{login}")
    User getUser(String login);
}
