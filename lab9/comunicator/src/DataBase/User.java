package DataBase;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String login;
    public String password;
    public User (String _login, String _password) {
        login = _login;
        password = _password;
    }
    public User(int _id, String _login, String _password) {
        id = _id;
        login = _login;
        password = _password;
    }

    public User() {}

    public void setId(int _id){
        id = _id;
    }
    public void setLogin (String _login){
        login = _login;
    }
    public void setPassword(String _password){
        password = _password;
    }

}
