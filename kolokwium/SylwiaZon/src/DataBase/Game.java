package DataBase;

import java.io.Serializable;

public class Game implements Serializable {
    public int id;
    public User user1;
    public User user2;
    public int result;
    public Game (User _user1, User _user2){
        user1 = _user1;
        user2 = _user2;
    }
    public Game (int _id, User _user1, User _user2){
        id = _id;
        user1 = _user1;
        user2 = _user2;
    }
    public void setWinner(int id){
        result = id;
    }
    public void setUser1 (User user){
        user1 = user;
    }
    public void setUser2 (User user){
        user2 = user;
    }
    public void setId(int _id){
        id = _id;
    }
}
