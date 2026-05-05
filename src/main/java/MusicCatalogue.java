import java.util.*;

// Seluruh List Lagu
public class MusicCatalogue {
    // Membuat List Untuk Semua Lagu
    private List<Music> musicList = new ArrayList<>();
    // Mapping musik berdasarkan ID untuk pencarian musik
    private Map<String, Music> musicMap= new HashMap<>();
    // Set untuk Genre
    private  Set<String> genreSet = new HashSet<>();
    // BSt untuk mengindekskan lagu ke dalam tree agar dapat melakukan pencarian
    private BinarySearchTree musicBST = new BinarySearchTree();

    // Method untuk menambahkan lagu
    public void addSong(Music music){
        musicList.add(music);
        musicMap.put(music.id, music);
        genreSet.add(music.genre);
        musicBST.insert(music);
    }

    // Method Untuk mengembalikan seluruh List lagu
    public List<Music> getAllSongs() {
        return Collections.unmodifiableList(musicList);
    }

    // Method untukk mencari lagu berdasarkan ID
    public Music findById(String id) {
        return musicMap.get(id);
    }

    // Method untuk mencari Musik berdasarkan judul
    public Music findByTitle(String title) {
        return musicBST.find(title);
    }

    // Method untuk mengembalikan semua genre yang tersedia
    public Set<String> getGenres() {
        return Collections.unmodifiableSet(genreSet);
    }

    public int getTotalSongs() {
        return musicList.size();
    }

    // Method untuk mengambil lagu berdasarkan indeks
    public Music get(int index) {
        return musicList.get(index);
    }

    // Membuat root Tree untuk playlist
    private Playlist rootPlaylist = new Playlist("My Playlist");

    public Playlist getRootPlaylist(){
        return rootPlaylist;
    }
}
