import java.util.*;
public class MusicPlayer {
    private Music currentMusic; // Untuk mentrack music yang sedang diputar saatini
    Queue<Music> musicQueue = new LinkedList<>();
    private Stack<Music> history = new Stack<>();

    // Method untuk memutar Musik
    public void playMusic(Music music) {
        if (currentMusic != null) {
            history.push(currentMusic);
        }
        currentMusic = music;
    }

    // Method untuk menambahkan ke queue
    public void addToQueue(Music music) {
        musicQueue.offer(music);
    }

    // Method untuk play next
    public void playNext(){
        // Jika QUeue Kosong
        if (musicQueue.isEmpty()) {
            System.out.println("Queue Kosong!");
            currentMusic = null;
            return;
        }

        // Putar lagu berikutnya
        playMusic(musicQueue.poll());
    }

    // Method untuk play previous
    public void playPrevious() {
        // Jika Stack Kosong
        if (history.isEmpty()) {
            System.out.println("Ini adalah lagu pertama!");
            return;
        }

        // Putar lagu sebelumnya
        playMusic(history.pop());
    }

    // Method untuk mengambil Lagu saat ini
    public Music getCurrentMusic() {
        return currentMusic;
    }

}
