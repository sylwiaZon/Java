package DataBase;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/zon1", "zon1", "SG6pqk6RrH722zqb");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQGLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(User userToLog) {
        User user = null;
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Users WHERE login = '" + userToLog.login + "' and password = '" + userToLog.password + "'");
            if (!resultSet.next()) {
                return null;
            }
            user = downloadData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlEx) {
                }
                resultSet = null;
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                }
                statement = null;
            }
        }
        return user;
    }

    public void register(User user) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO Users (login, password)" + "values ('" + user.login + "','" + user.password + "')"
        );
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = null;
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Users");
            users = downloadUsers(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlEx) {
                }
                resultSet = null;
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                }
                statement = null;
            }
        }
        return users;
    }

    private ArrayList<User> downloadUsers(ResultSet resultSet) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        User user;
        while (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);
        }
        return users;
    }

    private boolean validateRegistration(User user) {
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * From Users where login ='" + user.login + "'");
            if (resultSet == null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User downloadData(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }

    public boolean connected() {
        return connection != null;
    }

    public void sendMessage(Message message) {
        try {
            connect();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Messages (fromId, toId, message) VALUES (" + message.fromId + ", " + message.toId + ",'" + message.message + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> getMessages(int id) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Messages WHERE toId = " + id + " or fromId = " + id);
            messages = downloadMessages(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public ArrayList<Message> downloadMessages(ResultSet resultSet) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        Message message;
        while (resultSet.next()) {
            message = new Message();
            message.setMessageId(resultSet.getInt("messageID"));
            message.setFromId(resultSet.getInt("toId"));
            message.setToId(resultSet.getInt("fromId"));
            message.setMessage(resultSet.getString("message"));
            messages.add(message);
        }
        return messages;
    }
}

