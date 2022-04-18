package com.timal.manager;

public interface AuthService {
    void start();
    void stop();
    String getNickByLoginPass(String login, String pass);
    boolean registerNewUser(String part, String part1, String part2);
}
