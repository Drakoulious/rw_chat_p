package ru.ilonich.roswarcp.controller.dto;

/**
 * Created by Никола on 04.05.2017.
 */

public class LoginPassPair {
    public LoginPassPair() {
    }
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginPassPair{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
