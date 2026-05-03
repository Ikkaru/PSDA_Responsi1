import java.security.PublicKey;

// Class Untuk Object Lagu
public class Music {
    // Attribute untuk masing-masing lagu
    String id;
    String title;
    String artist;
    String genre;
    int duration;
    int listeners_count;

    // Constructor untuk class lagu
    public Music(String id, String title, String artist, String genre, int duration, int listeners_count){
        this.id = id;
        this. title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.listeners_count = listeners_count;

    }
}
