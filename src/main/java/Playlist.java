import  java.util.*;

// Membuat Node untuk tree Playlist
public class Playlist {
    String name;
    private List<Playlist> children;
    private List<Music> songs;

    public Playlist(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.songs = new ArrayList<>();
    }

    // Method untuk menambahkan sub-folder
    public void addSubFolder(Playlist node){
        this.children.add(node);
    }

    // Method untuk menambahkan lagu ke playlist
    public void addSong(Music music) {
        this.songs.add(music);
    }

    // Method untuk mengambil data
    public String getName() {return name;}
    public List<Playlist> getChildren() {return children;}
    public List<Music> getSongs() {return songs;}
}
