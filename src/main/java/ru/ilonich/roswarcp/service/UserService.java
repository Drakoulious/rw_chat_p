package ru.ilonich.roswarcp.service;

import org.springframework.security.core.userdetails.User;

public interface UserService {
    User getUser(String login);
}
