
import java.rmi.*;
import java.util.ArrayList;

public interface Services extends Remote {

    public boolean insertTrack(String title, String species, String basicSinger, int durationSeconds) throws RemoteException;

    public String searchTrackWithTitle(String title) throws RemoteException;

    public ArrayList<String> searchSongsWithSinger(String basicSinger) throws RemoteException;

    public boolean searchAndRate(String title, String nameOfUser, int rate) throws RemoteException;

    public ArrayList<String> displaySong(int rate) throws RemoteException;
}
