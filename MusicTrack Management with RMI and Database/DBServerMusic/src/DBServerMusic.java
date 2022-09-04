
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.registry.Registry;

public class DBServerMusic extends UnicastRemoteObject implements Services {

    public DBServerMusic() throws RemoteException {
        super();
    }

    public synchronized boolean insertTrack(String title, String species, String basicSinger, int durationSeconds) throws RemoteException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
            Statement stat = con.createStatement();

            ResultSet rs = stat.executeQuery("SELECT * FROM songs  WHERE title='" + title + "' AND singer='" + basicSinger + "';");

            if (rs.next()) {
                con.close();
                return false;
            } else {
                //stat.executeUpdate("INSERT INTO songs values ('" + title + "','" + basicSinger + "','" + species + "','" + durationSeconds + "');");
                //stat.executeUpdate("insert into songs values('" + title+  "','" + basicSinger + "','" + species + "','" + durationSeconds + ")'");
                stat.executeUpdate("INSERT INTO songs (title, singer, species, duration)" + " VALUES ('" + title + "', '" + basicSinger + "', '" + species + "', '" + durationSeconds + "');");
                con.setAutoCommit(true);
                con.close();
                return true;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized String searchTrackWithTitle(String title) throws RemoteException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("Select * from songs where title='" + title + "';");

            if (rs.next()) {
                String result = "The song: " + rs.getString("title") + " ";
                result += "From: " + rs.getString("singer") + " ";
                result += "Species: " + rs.getString("species") + " ";
                result += "Duration: " + rs.getString("duration") + " IS EXIST IN DATA BASE";
                result += "Rating: " + rs.getString("rating") + "/10";
                con.close();
                return result;
            } else {
                con.close();
                return null;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public synchronized ArrayList<String> searchSongsWithSinger(String basicSinger) throws RemoteException {

        ArrayList<String> result = new ArrayList<String>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("Select * from songs where singer='" + basicSinger + "';");
            String s = "";

            while (rs.next()) {
                s = "Title: " + rs.getString("title") + " ";
                s += "Singer: " + rs.getString("singer") + " ";
                s += "Species: " + rs.getString("species") + " ";
                s += "Duration: " + rs.getString("duration");
                s += "Rating: " + rs.getString("rating") + "/10";
                result.add(s);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean searchAndRate(String title, String nameOfUser, int rate) throws RemoteException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("Select * from songs where title='" + title + "';");

            if (rs.next()) {
                stat.executeUpdate("INSERT INTO users(name,rating_title_song,rating) VALUES('" + nameOfUser + "','" + title + "','" + rate + "');");
                stat.executeUpdate("UPDATE songs SET rating=(SELECT AVG(rating) FROM users where rating_title_song='" + title + "') where  title='" + title + "';");
                con.close();
                return true;
            } else {
                con.close();
                return false;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public ArrayList<String> displaySong(int rate) throws RemoteException {

        ArrayList<String> result = new ArrayList<String>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("Select title from songs where rating>='" + rate + "'");
            String s = null;

            while (rs.next()) {
                s = "Title: " + rs.getString("title") + " ";
                result.add(s);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBServerMusic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException, ClassNotFoundException, SQLException {

        DBServerMusic dbsm = new DBServerMusic();
        Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
        Naming.rebind("//localhost/DBServerMusic", dbsm);

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "12345");
        Statement stat = con.createStatement();
        System.out.println("Database connection....");

        stat.executeUpdate("Drop table if exists songs;");
        stat.executeUpdate("CREATE TABLE songs(title varchar (50), singer varchar(50), species varchar(50), duration int, rating int);");
        stat.executeUpdate("Drop table if exists users;");
        stat.executeUpdate("Create table users(name varchar(50), rating_title_song varchar(50), rating int) ;");
        con.close();

    }

}
