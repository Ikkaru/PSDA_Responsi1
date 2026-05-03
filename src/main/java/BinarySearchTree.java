// Menggunakan Binary Serach Tree untuk menggunakan tree sebagai metode Searching Lagu berdasarkan Judul

// Node untuk Tree
class BSTNode {
    Music music;
    BSTNode left; // Left Node
    BSTNode right; // Right Node

    public BSTNode(Music music) {
        this.music = music;
        this.left = null;
        this.right = null;
    }
}

public class BinarySearchTree {
    private BSTNode root;

    // Method untuk memasukkan lagu ke node
    public void insert(Music newMusic) {
        root = insertRec(root, newMusic);
    }

    private BSTNode insertRec(BSTNode root, Music newMusic) {
        // Jika tree kosong, buat node baru
        if (root == null) {
           root = new BSTNode(newMusic);
           return root;
        }

        // Bandingkan Abjad Judul
        int compareResults = newMusic.title.compareToIgnoreCase(root.music.title);

        // Kalo lebih rendah pergi ke node kiri
        if (compareResults < 0) {
            root.left = insertRec(root.left, newMusic);
        }
        // Kalo lebih tinggi pergi ke node kanan
        else if (compareResults > 0) {
            root.right = insertRec(root.right, newMusic);
        }

        return root;
    }

    // Method untuk mencari berdasarkan judul
    public Music find(String musicTitle) {
        BSTNode searchResults = findRec(root, musicTitle);
        if (searchResults == null) {
            return null;
        }
        return searchResults.music;
    }

    private BSTNode findRec(BSTNode root, String musicTitle) {
        // Jika Root Kosong atau judul ditemukan di root ini
        if (root == null || root.music.title.equalsIgnoreCase(musicTitle)) {
            return root;
        }

        // Compare Judul yang di cari dengan yanga ada di node
        int compareResults = musicTitle.compareToIgnoreCase(root.music.title);

        // Kalo lebih rendah cari ke node kiri
        if (compareResults < 0) {
            return findRec(root.left, musicTitle);
        }
        // Kalo kebih tinggi cari ke node kanan
        return findRec(root.right, musicTitle);
    }

}
