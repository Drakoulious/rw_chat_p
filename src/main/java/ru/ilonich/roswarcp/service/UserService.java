package ru.ilonich.roswarcp.service;

import org.springframework.security.core.userdetails.User;

/**
 * Created by Илоныч on 28.04.2017.
 */
public interface UserService {
    User getUser(String login);
}
