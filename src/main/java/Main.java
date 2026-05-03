public class Main {

    public static void main(String[] args) throws InterruptedException {
        MusicCatalogue catalogue = new MusicCatalogue();
        MusicPlayer player = new MusicPlayer();

        // Lagu Pop & Rock Indonesia
        catalogue.addSong(new Music("M001", "Monokrom", "Tulus", "Pop", 214, 5200000));
        catalogue.addSong(new Music("M002", "Kangen", "Dewa 19", "Rock", 300, 8500000));
        catalogue.addSong(new Music("M003", "Sesuatu di Jogja", "Adhitia Sofyan", "Indie", 245, 1200000));

        // Original Soundtrack (OST) Game
        catalogue.addSong(new Music("M004", "Surasthana Fantasia", "HOYO-MiX", "Orchestral", 185, 3400000));
        catalogue.addSong(new Music("M005", "Wildfire", "HOYO-MiX", "Rock", 192, 4500000));
        catalogue.addSong(new Music("M006", "Wuthering Waves Main Theme", "Kuro Games", "Instrumental", 150, 890000));

        // Lagu Tematik
        catalogue.addSong(new Music("M007", "Lo-Fi Coding Night", "DevBeats", "Lo-Fi", 3600, 45000));
        catalogue.addSong(new Music("M008", "CachyOS Boot Sound", "Tux", "Electronic", 12, 500));

        UserInterface UI = new UserInterface(player, catalogue);
        UI.run();
    }

}
