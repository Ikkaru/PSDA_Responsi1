public class Main {

    public static void main(String[] args) throws InterruptedException {
        MusicCatalogue catalogue = new MusicCatalogue();
        MusicPlayer player = new MusicPlayer();
        UserInterface UI = new UserInterface(player, catalogue);

        // DUMMY 

        // ==========================================
        // 1. TAMBAHKAN SEMUA LAGU KE CATALOGUE
        // ==========================================

        // --- Indo Pop, Rock, & Indie ---
        catalogue.addSong(new Music("M001", "Monokrom", "Tulus", "Pop", 214, 5200000));
        catalogue.addSong(new Music("M002", "Kangen", "Dewa 19", "Rock", 300, 8500000));
        catalogue.addSong(new Music("M003", "Sesuatu di Jogja", "Adhitia Sofyan", "Indie", 245, 1200000));
        catalogue.addSong(new Music("M004", "Runtuh", "Feby Putri, Fiersa Besari", "Indie", 220, 3100000));
        catalogue.addSong(new Music("M005", "Sial", "Mahalini", "Pop", 243, 4000000));

        // --- OST Game & Anime ---
        catalogue.addSong(new Music("M006", "Surasthana Fantasia", "HOYO-MiX", "Orchestral", 185, 3400000));
        catalogue.addSong(new Music("M007", "Wildfire", "HOYO-MiX", "Rock", 192, 4500000));
        catalogue.addSong(new Music("M008", "Wuthering Waves Main Theme", "Kuro Games", "Instrumental", 150, 890000));
        catalogue.addSong(new Music("M009", "Idol", "YOASOBI", "J-Pop", 213, 9500000));
        catalogue.addSong(new Music("M010", "Kick Back", "Kenshi Yonezu", "J-Rock", 193, 8200000));

        // --- Lo-Fi, Electronic & Coding Vibes ---
        catalogue.addSong(new Music("M011", "Lo-Fi Coding Night", "DevBeats", "Lo-Fi", 3600, 45000));
        catalogue.addSong(new Music("M012", "CachyOS Boot Sound", "Tux", "Electronic", 12, 500));
        catalogue.addSong(new Music("M013", "Midnight City", "M83", "Synthpop", 243, 6700000));


        // ==========================================
        // 2. BANGUN STRUKTUR TREE PLAYLIST (FOLDERS)
        // ==========================================
        Playlist root = catalogue.getRootPlaylist();

        // Level 1: Folder Utama
        Playlist folderIndo = new Playlist("Indo Vibes");
        Playlist folderGaming = new Playlist("Gaming & Anime");
        Playlist folderKuliah = new Playlist("Kuliah & Ngoding");

        root.addSubFolder(folderIndo);
        root.addSubFolder(folderGaming);
        root.addSubFolder(folderKuliah);

        // Level 2: Sub-Folder
        Playlist subGalau = new Playlist("Mode Galau Malam");
        Playlist subChill = new Playlist("Chill Sore Sore");
        folderIndo.addSubFolder(subGalau);
        folderIndo.addSubFolder(subChill);

        Playlist subGacha = new Playlist("Lagu Gacha Wangi");
        Playlist subJpop = new Playlist("J-Pop Hype");
        folderGaming.addSubFolder(subGacha);
        folderGaming.addSubFolder(subJpop);

        Playlist subSDA = new Playlist("Praktikum SDA");
        folderKuliah.addSubFolder(subSDA);


        // ==========================================
        // 3. MASUKKAN LAGU KE DALAM MASING-MASING FOLDER
        // ==========================================

        // Isi Sub-folder Galau & Chill
        subGalau.addSong(catalogue.findByTitle("Runtuh"));
        subGalau.addSong(catalogue.findByTitle("Sial"));
        subGalau.addSong(catalogue.findByTitle("Kangen"));

        subChill.addSong(catalogue.findByTitle("Sesuatu di Jogja"));
        subChill.addSong(catalogue.findByTitle("Monokrom"));

        // Isi Sub-folder Gaming & J-Pop
        subGacha.addSong(catalogue.findByTitle("Surasthana Fantasia"));
        subGacha.addSong(catalogue.findByTitle("Wildfire"));
        subGacha.addSong(catalogue.findByTitle("Wuthering Waves Main Theme"));

        subJpop.addSong(catalogue.findByTitle("Idol"));
        subJpop.addSong(catalogue.findByTitle("Kick Back"));

        // Isi Sub-folder Coding
        subSDA.addSong(catalogue.findByTitle("Lo-Fi Coding Night"));
        subSDA.addSong(catalogue.findByTitle("Midnight City"));
        subSDA.addSong(catalogue.findByTitle("CachyOS Boot Sound"));


        // Start UI
        UI.run();

    }

}
