package ru.ilonich.roswarcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ilonich.roswarcp.repo.UserMapper;

import java.util.Collections;
import java.util.List;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    private static List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));

    @Override
    public User getUser(String login) {
        ru.ilonich.roswarcp.model.User user = this.userMapper.getUser(login);
        if (user == null) return null;
        return new User(user.getLogin(), user.getPassword(), authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUser(s);
    }
}
