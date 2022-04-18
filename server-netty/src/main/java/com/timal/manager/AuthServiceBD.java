package com.timal.manager;

import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class AuthServiceBD implements AuthService {

    private List<User> listUser;
    private static Connection connection;
    private static Statement stmt;

    private class User {
        private String name;
        private String login;
        private String pass;

        public User(String name, String login, String pass) {
            this.name = name;
            this.login = login;
            this.pass = pass;
        }
    }

    AuthServiceBD() {
        listUser = new ArrayList<>();
        try {
            start();
            loadUsers();
            log.info("Loading users from BD");
        } catch (SQLException e) {
            log.error(e);
        } catch (Exception e) {
            log.fatal(e);
        }
    }

    public boolean registerNewUser(String nickName, String login, String pass) {
        int result = 0;
        try {
            result = stmt.executeUpdate("INSERT INTO users (NickName, login, pass) VALUES ('" + nickName + "','" + login + "','" + pass + "');");
            listUser.add(new User(nickName, login, pass));
        } catch (SQLException e) {
            log.error(e);
        }
        return result > 0;
    }

    public void loadUsers() throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM users;")) {
            while (rs.next()) {
                listUser.add(new User(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        }
    }

    @Override
    public void start() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:server-netty/BD/users.db");
            stmt = connection.createStatement();
        } catch (SQLException e) {
            log.error(e);
            throw new RuntimeException("Unable to connection with BD.");
        }
    }

    @Override
    public void stop() {
        try {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (User user : listUser) {
            if (user.login.equals(login) && user.pass.equals(pass))
                return user.name;
        }
        return null;
    }
}
