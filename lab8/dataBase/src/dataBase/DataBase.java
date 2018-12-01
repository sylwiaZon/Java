package dataBase;

import javafx.scene.layout.Border;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String table;
    DataBase(String _table){
        table = _table;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/zon","user","password");
        } catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQGLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public ArrayList<Book> listData() {
        ArrayList<Book> books = null;
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * From " + table);
            books = downloadData(resultSet);
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
        return books;
    }

    public ArrayList<Book> searchViaAuthor (String author) {
        ArrayList<Book> books = null;
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * From " + table + " where author  like \"%" + author + "%\"");
            books = downloadData(resultSet);
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
        return books;
    }

    public ArrayList<Book> searchViaISBN (String ISBN) {
        ArrayList<Book> books = null;
        try {
            connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * From " + table + " where isbn  = \"" + ISBN + "\"");
            books = downloadData(resultSet);
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
        return books;
    }

    public void addBook(Book book) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO " + table + "(isbn, title, author, year)" + "values ('" + book.ISBN + "', '" + book.title + "','" + book.author + "','" + book.year + "')"
        );
    }

    private ArrayList<Book> downloadData(ResultSet resultSet) throws SQLException {
        ArrayList<Book> books = new ArrayList<Book>();
        Book book;
        while (resultSet.next()) {
            book = new Book();
            book.setAuthor(resultSet.getString("author"));
            book.setTitle(resultSet.getString("title"));
            book.setISBN(resultSet.getString("isbn"));
            book.setYear(resultSet.getInt("year"));
            books.add(book);
        }
        return books;
    }

    public boolean connected() {
        return connection != null;
    }
}

