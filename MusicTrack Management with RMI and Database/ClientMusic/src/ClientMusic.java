
import java.util.Scanner;
import java.rmi.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientMusic extends JFrame {

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        JFrame frame = new JFrame();
        Scanner scan = new Scanner(System.in);
        int choice, durationSeconds;
        String title, species, basicSinger;
        Services services = (Services) Naming.lookup("//localhost/DBServerMusic");
        while (true) {
            System.out.println("-------------------------------------MENU-------------------------------------");
            System.out.println("1.  For insert track in database");
            System.out.println("2.  For search song with title");
            System.out.println("3.  For search songs with singer");
            System.out.println("4.  For search and rate registry");
            System.out.println("5.  For display songs with your value rate at least");
            System.out.println("6.  For exit");
            System.out.println("-------------------------------------Press the coice end enter");

            choice = scan.nextInt();
            scan.nextLine();
            boolean flag;
            String s = "";
            switch (choice) {
                case (1): {
                    System.out.print("Give the title of song:   ");
                    title = scan.nextLine();
                    System.out.print("Give the basic singer of song:    ");
                    basicSinger = scan.nextLine();
                    System.out.print("Give the species of song:   ");
                    species = scan.nextLine();
                    System.out.print("Give the duration in seconds of the song:   ");
                    durationSeconds = scan.nextInt();
                    scan.nextLine();
                    flag = services.insertTrack(title, species, basicSinger, durationSeconds);
                    if (flag == true) {
                        JOptionPane.showMessageDialog(null, "The song inserted succes...");
                    } else {
                        JOptionPane.showMessageDialog(null, "The song can NOT inserted...");

                    }
                    break;
                }
                case (2): {
                    System.out.println("Please enter the title for search");
                    title = scan.nextLine();
                    s = services.searchTrackWithTitle(title);
                    System.out.println(s);
                    JOptionPane.showMessageDialog(null, s);
                    break;
                }
                case (3): {
                    System.out.println("Please enter the name of singer for search him songs");
                    basicSinger = scan.nextLine();
                    ArrayList<String> result = new ArrayList<String>();
                    result = services.searchSongsWithSinger(basicSinger);

                    for (int i = 0; i < result.size(); i++) {
                        s += result.get(i) + "\n";
                        System.out.println(result.get(i));
                    }
                     JOptionPane.showMessageDialog(null, s);
                    break;
                }
                case (4): {
                    System.out.println("Give the title of song where you evaluate");
                    title = scan.nextLine();
                    System.out.println("Give your name");
                    String nameOfUser = scan.nextLine();
                    System.out.println("Give your rate for the " + title);
                    int rate = scan.nextInt();
                    flag = services.searchAndRate(title, nameOfUser, rate);
                    if (flag == true) {
                         JOptionPane.showMessageDialog(null, "your rate insert");
                    } else {
                         JOptionPane.showMessageDialog(null, "your rate DONT insert");
                    }
                    break;
                }
                case (5): {
                    System.out.println("Give the rating");
                    int r = scan.nextInt();
                    ArrayList<String> result = new ArrayList<String>();
                    result = services.displaySong(r);

                    for (int i = 0; i < result.size(); i++) {
                        System.out.println(result.get(i));
                        s += result.get(i) + "\n";
                    }
                    JOptionPane.showMessageDialog(null, s);

                    break;
                }
                case (6): {
                    return;
                }
                default: {
                    JOptionPane.showMessageDialog(null, "Your choice will be among 1-6");
                    break;
                }

            }

        }
    }

}
