package ru.ilonich.roswarcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ilonich.roswarcp.repo.UserMapper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    private static ArrayList<GrantedAuthority> emptyList = new ArrayList<>();

    @Override
    public User getUser(String login) {
        ru.ilonich.roswarcp.model.User user = this.userMapper.getUser(login);
        if (user == null) return null;
        return new User(user.getLogin(), user.getPassword(), emptyList);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUser(s);
    }
}
