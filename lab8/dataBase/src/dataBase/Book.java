package dataBase;

public class Book {
    public String ISBN;
    public String title;
    public String author;
    public int year;
    Book(String _ISBN, String _title, String _author, int _year){
        ISBN = _ISBN;
        author =_author;
        year = _year;
        title = _title;
    }
    Book(){}
    public void setTitle(String _title){title = _title;}
    public void setAuthor(String _author) {author = _author;}
    public void setISBN (String _ISBN) { ISBN = _ISBN;}
    public void setYear(int _year) {year = _year;}
}
