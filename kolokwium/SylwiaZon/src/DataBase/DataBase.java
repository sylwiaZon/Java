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
            connection = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/zon2", "zon2", "Tj3pXEJzJThU449B");
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

    public User register(User user) throws SQLException {
        statement = connection.createStatement();
        if (validateRegistration(user)) {
            statement.executeUpdate(
                    "INSERT INTO Users (login, password)" + "values ('" + user.login + "','" + user.password + "')"
            );
            return login(user);
        }
        return null;
    }
    public ArrayList<Game> getGames(){
        ArrayList<Game> games = new ArrayList<>();
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Games");
            games = downloadGames(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    private ArrayList<Game> downloadGames(ResultSet resultSet) throws SQLException {
        ArrayList<Game> games = new ArrayList<>();
        while(resultSet.next()){
            Game game = new Game();
            game.setId(resultSet.getInt("id"));
            ResultSet res1 = statement.executeQuery("SELECT * from Users where id =  " + resultSet.getInt("User1") + ";");
            User user1 = new User();
            user1.setId(res1.getInt("id"));
            user1.setLogin(res1.getString("login"));
            game.setUser1(user1);
            ResultSet res2 = statement.executeQuery("SELECT * from Users where id =  " + resultSet.getInt("User2") + ";");
            User user2 = new User();
            user2.setId(res2.getInt("id"));
            user2.setLogin(res2.getString("login"));
            game.setUser2(user2);
            games.add(game);
        }
        return games;
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
            if (!resultSet.next()) {
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
    public int newGame(User user1, int user2){
        int gameId = 0;
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO Games (User1, User2)" + "values ('" + user1.id + "','" + user2 + "')"
            );
            resultSet = statement.executeQuery("SELECT id FROM Games where User1 = '" + user1.id + "'and User2 = '" + user2 + "';");
            gameId = getGameId((resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameId;
    }
    private int getGameId(ResultSet resultSet) throws SQLException {
        int res = 0;
        while (resultSet.next()) {
            res = resultSet.getInt("id");
        }
        return res;
    }
    public void acceptGame(int game){
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE Games SET Accepted = '1' WHERE id = '" +  game + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void declineGame(int game){
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE Games SET Accepted = '-1' WHERE id = '" +  game + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void makeMove(int game, int move, int user){
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            ResultSet previousMoves = statement.executeQuery("Select Moves From Moves where Game ='" + game + "' and User='" + user + "';");
            String currentMoves = createMove(previousMoves) + ',' + move;
            statement.executeUpdate(
                    "UPDATE Moves SET Moves = '" + currentMoves + "' WHERE Game = '" +  game + "' and User='" + user + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String createMove(ResultSet resultSet) throws SQLException {
        String moves = new String();
        while (resultSet.next()) {
            moves = resultSet.getString("moves");
        }
        return moves;
    }
    public ArrayList<String> getMoves(int game, int user){
        ArrayList<String> moves = null;
        try {
            connect();
            resultSet = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select Moves From Moves where Game ='" + game + "' and User='" + user +  "'");
            moves = new ArrayList<>();
            moves = createMoves(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moves;
    }
    private ArrayList<String> createMoves(ResultSet resultSet) throws SQLException {
        ArrayList<String> moves = new ArrayList<>();
        while (resultSet.next()) {
            String move = new String();
            move = resultSet.getString("moves");
            moves.add(move);
        }
        return moves;
    }
}