import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyType;
import  com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UserInterface {
    private static final int INITIAL_WIDTH = 191;
    private static final int INITIAL_HEIGHT = 52;

    // Menghubungkan dengan struktur data
    private MusicPlayer player;
    private MusicCatalogue catalogue;

    private Label textNowPlaying;
    private Label textNowPlayingSong; // Untuk di Player
    private Label textProgressBar;
    private Label textQueueList;
    private Label textQueueNowPlaying; // Untuk di Queue

    // Controll Variabel
    private Button btnPlayPause;
    private boolean isPaused = false;
    private long pauseTime = 0;

    private boolean isRunning = true;
    private long musicStartDuration = 0;

    // Contructutor
    public UserInterface(MusicPlayer player, MusicCatalogue catalogue) {
        this.player = player;
        this.catalogue = catalogue;
    }

    // Method untuk refresh Tabel
    private void refreshMusicTable(Table<String> table, List<Music> songs) {
        table.getTableModel().clear();
        for (Music music : songs) {
            int minutes = music.duration / 60;
            int second = music.duration % 60;
            String durationText = String.format("%02d:%02d", minutes, second);
            table.getTableModel().addRow(music.id, music.title, music.artist, music.genre, durationText, String.valueOf(music.listeners_count));
        }
    }

    // Method untuk membuat Interface folder hierarki playlist menggunakan traversal preorder
    private void buildPlaylistTreeUI(ActionListBox menu, Playlist node, int depth, Table<String> table) {
        // Efek visual indent
        String indent = "-".repeat(depth);
        String icon = node.getChildren().isEmpty() ? " ▶ " : ">";

        // Proses node saat ini tampilkan lagu / folder di node ini
        menu.addItem(indent + icon + node.getName(), () -> {
            refreshMusicTable(table, node.getSongs());
        });

        // Proses Childnya
        for (Playlist child : node.getChildren()) {
            buildPlaylistTreeUI(menu, child, depth + 1, table);
        }
    }

    public void run() {
        try {
            // Initialize
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setPreferTerminalEmulator(true);
            terminalFactory.setForceTextTerminal(false);
            terminalFactory.setTerminalEmulatorTitle("Castify");
            terminalFactory.setInitialTerminalSize(new TerminalSize(INITIAL_WIDTH, INITIAL_HEIGHT));

            Terminal terminal = terminalFactory.createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            Thread.sleep(300);

            // Membuat GUI manager
            WindowBasedTextGUI gui = new MultiWindowTextGUI(
                    screen,
                    new DefaultWindowManager(),
                    new EmptySpace(TextColor.ANSI.BLUE_BRIGHT)
            );

            // -------------------NavBar----------------------
            BasicWindow topWin = new BasicWindow();
            topWin.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
            Panel topPanel = new Panel(new BorderLayout());

            topPanel.addComponent(
                    new Label("(˶>⩊<˶)  Castify").addStyle(SGR.BOLD),
                    BorderLayout.Location.LEFT);
                topPanel.addComponent(new Label("[↑/↓] Navigate  [S] Search Title  [I] Search ID  [G] Genres  [Alt+P] Play/Pause  [F1-F3] Switch Workspace  [Q/Esc] Exit  "), BorderLayout.Location.RIGHT);
            topWin.setComponent(topPanel);



            // ----------------Right SideBar----------------
            BasicWindow rightWin = new BasicWindow("Queue");
            rightWin.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
            Panel rightPanel = new Panel(new LinearLayout(Direction.VERTICAL));

            textQueueNowPlaying = new Label("Now Playing:\nNothing\n");
            rightPanel.addComponent(textQueueNowPlaying);

            rightPanel.addComponent(new Label(""));
            rightPanel.addComponent(new Label("Next:"));


            textQueueList = new Label("૮(˶ㅠ︿ㅠ)ა Queue Kosong...");
            rightPanel.addComponent(textQueueList);
            rightWin.setComponent(rightPanel);

            // ------- Control Button ----------------
            Runnable actionPrev = () -> {
                player.playPrevious();
                musicStartDuration = System.currentTimeMillis();
                isPaused = false;
                if (btnPlayPause != null) btnPlayPause.setLabel("⏸");
            };

            Runnable actionTogglePause = () -> {
                if (player.getCurrentMusic() != null) {
                    isPaused = !isPaused;
                    if (isPaused) {
                        pauseTime = System.currentTimeMillis();
                        if (btnPlayPause != null) btnPlayPause.setLabel("▶");
                    } else {
                        musicStartDuration += (System.currentTimeMillis() - pauseTime);
                        if (btnPlayPause != null) btnPlayPause.setLabel("⏸");
                    }
                }
            };

            Runnable actionNext = () -> {
                player.playNext();
                musicStartDuration = System.currentTimeMillis();
                isPaused = false;
                if (btnPlayPause != null) btnPlayPause.setLabel("⏸");
            };


            // ---------------Player Window-------------------
            BasicWindow botWin = new BasicWindow();
            botWin.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
            Panel botPanel = new Panel(new BorderLayout());

            // Controll Panel
            Panel controlPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
            Button prevBtn = new Button("⏮", actionPrev);
            btnPlayPause = new Button("⏸", actionTogglePause);
            Button nextBtn = new Button("⏭", actionNext);

            controlPanel.addComponent(prevBtn);
            controlPanel.addComponent(btnPlayPause);
            controlPanel.addComponent(nextBtn);

            // Progress Bar
            textProgressBar = new Label("[>                                                ] 00:00 / 00:00");
            Panel progressWrapper = new Panel(new LinearLayout(Direction.VERTICAL));
            progressWrapper.addComponent(textProgressBar, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));


            // Status
            Panel infoPanel = new Panel(new LinearLayout(Direction.VERTICAL));
            textNowPlaying = new Label("Now Playing:");
            textNowPlayingSong = new Label("Nothing").addStyle(SGR.BOLD);
            infoPanel.addComponent(textNowPlaying);
            infoPanel.addComponent(textNowPlayingSong);

            botPanel.addComponent(controlPanel, BorderLayout.Location.LEFT);
            botPanel.addComponent(progressWrapper, BorderLayout.Location.CENTER);
            botPanel.addComponent(infoPanel, BorderLayout.Location.RIGHT);

            botWin.setComponent(botPanel);

            // -------------Content Panel-----------------------
            BasicWindow centerWin = new BasicWindow();
            centerWin.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
            Panel centerPanel = new Panel(new LinearLayout(Direction.VERTICAL));
            centerPanel.addComponent(new Label("Library "));

            // Membuat Tabel dengan 4 Kolom
            Table<String> musicTable = new Table<>("ID", "Judul", "Artist", "Genre", "Durasi", "Didengar");

            // Mengambil semua lagu dari list lagu dan memasukkannya ke tabel
            for (Music music : catalogue.getAllSongs()) {
                // Format durasi lagu
                int minutes = music.duration / 60;
                int second = music.duration % 60;
                String durationText = String.format("%02d:%02d", minutes, second);

                // Adding line to Table
                musicTable.getTableModel().addRow(music.id, music.title, music.artist, music.genre, durationText, String.valueOf(music.listeners_count));
            }

            // Pop Up DIalog ketika memmilih lagu
            musicTable.setSelectAction(() -> {
                int selectedIndex = musicTable.getSelectedRow();
                Music selectedMusic = catalogue.get(selectedIndex);

                // Menampilkan Dialog Box
                BasicWindow dialogWindow = new BasicWindow("Option");
                dialogWindow.setHints(Arrays.asList(Window.Hint.CENTERED));

                Panel dialogPanel = new Panel(new LinearLayout(Direction.VERTICAL));
                dialogPanel.setPreferredSize(new TerminalSize(40, 10));
                dialogPanel.addComponent(new Label("Choose an Action:"));
                dialogPanel.addComponent(new Label(selectedMusic.title + "-" + selectedMusic.artist));
                dialogPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

                // Opsi
                ActionListBox actionOption = new ActionListBox();

                // Play
                actionOption.addItem("[▶] Play Sekarang", () -> {
                    player.playMusic(selectedMusic);
                    musicStartDuration = System.currentTimeMillis();

                    isPaused = false;
                    if(btnPlayPause != null) btnPlayPause.setLabel("⏸");

                    dialogWindow.close();
                });

                // Opsi Add to Queue
                actionOption.addItem("[+] Add to Queue", () -> {
                    // Check apakah sudah ada musik yang diputar
                    if (player.getCurrentMusic() == null) {
                        player.playMusic(selectedMusic);
                        musicStartDuration = System.currentTimeMillis();
                        isPaused = false;
                        if (btnPlayPause != null) btnPlayPause.setLabel("⏸");
                    }
                    else {
                        player.addToQueue(selectedMusic);
                    }

                    dialogWindow.close();
                });

                // Opsi Cancel
                actionOption.addItem("[❌] Cancel", () -> {
                    dialogWindow.close();
                });

                dialogPanel.addComponent(actionOption);
                dialogWindow.setComponent(dialogPanel);
                gui.addWindow(dialogWindow);
            });

            centerPanel.addComponent(musicTable, LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

            centerWin.setComponent(centerPanel);


            // -----------------Left SideBar----------------
            BasicWindow leftWin = new BasicWindow("Playlist");
            leftWin.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
            Panel leftPanel = new Panel(new LinearLayout(Direction.VERTICAL));

            ActionListBox playlistMenu = new ActionListBox();

            // Tampilkan Playlist untuk semua lagu
            playlistMenu.addItem("All Songs", () -> {
                refreshMusicTable(musicTable, catalogue.getAllSongs());
            });

            // Panggil method untuk print folder menggunakan traversal preorder
            buildPlaylistTreeUI(playlistMenu, catalogue.getRootPlaylist(), 0, musicTable);

            leftPanel.addComponent(playlistMenu);
            leftWin.setComponent(leftPanel);

            // Position Calculation
            Runnable updateLayout = () -> {
                TerminalSize ts = screen.getTerminalSize();
                int W = Math.max(40, ts.getColumns());
                int H = Math.max(20, ts.getRows());

                int gapX = 2; // Celah kiri-kanan antar jendela
                int gapY = 1; // Celah atas-bawah antar jendela
                int marginX = 2; // Jarak dari pinggir kiri monitor
                int marginY = 1; // Jarak dari pinggir atas monitor

                // Kalkulasi Tinggi (H)
                int topH = 3;
                int botH = 4;
                int midH = Math.max(2, H - topH - botH - marginY - (gapY * 2) - 1);

                // Kalkulasi Lebar (W)
                int totalW = Math.max(10, W - marginX - 3);
                int leftW = Math.max(5, totalW / 4); // Sisi kiri dapat porsi 25%
                int rightW = leftW;                  // Sisi kanan dapat porsi 25%
                int centerW = Math.max(5, totalW - leftW - rightW - (gapX * 2)); // Sisa untuk tengah

                // --- APPLY POSISI (X, Y) ---
                topWin.setPosition(new TerminalPosition(marginX, marginY));
                leftWin.setPosition(new TerminalPosition(marginX, marginY + topH + gapY));
                centerWin.setPosition(new TerminalPosition(marginX + leftW + gapX, marginY + topH + gapY));
                rightWin.setPosition(new TerminalPosition(marginX + leftW + gapX + centerW + gapX, marginY + topH + gapY));
                botWin.setPosition(new TerminalPosition(marginX, marginY + topH + gapY + midH + gapY));

                // --- APPLY UKURAN ---
                topPanel.setPreferredSize(new TerminalSize(totalW - 2, topH - 2));
                leftPanel.setPreferredSize(new TerminalSize(leftW - 2, midH - 2));
                centerPanel.setPreferredSize(new TerminalSize(centerW - 2, midH - 2));
                rightPanel.setPreferredSize(new TerminalSize(rightW - 2, midH - 2));
                botPanel.setPreferredSize(new TerminalSize(totalW - 2, botH - 2));
            };

            // Jalankan kalkulasi pertama kali
            updateLayout.run();

            final boolean[] layoutDirty = {false};

            // Dengarkan perintah resize dari OS untuk mengubah ukuran on-the-fly
            terminal.addResizeListener((t, newSize) -> {
                layoutDirty[0] = true;
            });

            // Masukkan ke layar utama
            gui.addWindow(topWin);
            gui.addWindow(leftWin);
            gui.addWindow(rightWin);
            gui.addWindow(botWin);
            gui.addWindow(centerWin);

            // Key Listener
            gui.addListener((textGUI, keyStroke) -> {
                Character keyChar = keyStroke.getCharacter();

                if (keyStroke.getKeyType() == KeyType.EOF || keyStroke.getKeyType() == KeyType.Escape) {
                    isRunning = false;
                    return true;
                }

                if (keyStroke.getKeyType() == KeyType.Character && keyChar != null) {
                    char c = Character.toLowerCase(keyChar);

                    if (!keyStroke.isAltDown() && c == 'q') {
                        isRunning = false;
                        return true;
                    }

                    // ALt Combination
                    if (keyStroke.isAltDown() && keyStroke.getKeyType() == KeyType.Character) {
                        if (c == 'p') {
                            actionTogglePause.run();
                            return true;
                        } else if (c == 'n') {
                            actionNext.run();
                            return true;
                        } else if (c == 'b') {
                            actionPrev.run();
                            return true;
                        } else if (c == 'c') {
                            isRunning = false;
                            return true;
                        }
                    }
                    // Search bar
                    else {
                        if (c == 's') {
                            String searchTitle = TextInputDialog.showDialog(gui, "Serach Music", "Masukkan Judul Lagu", "");

                            // Jika user mengetikkan sesuatu
                            if (searchTitle != null && !searchTitle.isEmpty()) {
                                // Mencari lagu menggunakan method dari Binary Search Tree-mu
                                Music foundMusic = catalogue.findByTitle(searchTitle);

                                if (foundMusic != null) {
                                    // Jika ketemu, panggil refreshMusicTable dengan membawa 1 lagu tersebut (dibungkus ke dalam List)
                                    refreshMusicTable(musicTable, Arrays.asList(foundMusic));
                                } else {
                                    // Jika tidak ketemu, munculkan pop-up peringatan
                                    MessageDialog.showMessageDialog(gui, "Not Found", "Lagu '" + searchTitle + "' tidak ditemukan.");
                                }
                            }
                            return true;
                        }
                        else if (c == 'i') {
                            String searchId = TextInputDialog.showDialog(gui, "Search Music by ID", "Masukkan ID Lagu", "");

                            if (searchId != null && !searchId.isBlank()) {
                                Music foundMusic = catalogue.findById(searchId.trim().toUpperCase());

                                if (foundMusic != null) {
                                    refreshMusicTable(musicTable, Arrays.asList(foundMusic));
                                } else {
                                    MessageDialog.showMessageDialog(gui, "Not Found", "Lagu dengan ID '" + searchId + "' tidak ditemukan.");
                                }
                            }
                            return true;
                        }
                        else if (c == 'g') {
                            // Ambil data set genre dari MusicCatalogue
                            Set<String> genres = catalogue.getGenres();

                            if (genres.isEmpty()) {
                                MessageDialog.showMessageDialog(gui, "Genres", "Belum ada genre yang terdaftar.");
                            } else {
                                // Susun teks menggunakan StringBuilder
                                StringBuilder genreText = new StringBuilder();
                                for (String genre : genres) {
                                    genreText.append("- ").append(genre).append("\n");
                                }

                                // Tampilkan ke dalam Pop-up Dialog
                                MessageDialog.showMessageDialog(gui, "Available Genres", genreText.toString());
                            }
                            return true;
                        }
                    }
                }


                if (keyStroke.getKeyType() == KeyType.F1) {
                    gui.removeWindow(leftWin);
                    gui.addWindow(leftWin);
                    playlistMenu.takeFocus();
                    return true;

                } else if (keyStroke.getKeyType() == KeyType.F2) {
                    gui.removeWindow(centerWin);
                    gui.addWindow(centerWin);
                    musicTable.takeFocus();
                    return true;

                } else if (keyStroke.getKeyType() == KeyType.F3) {
                    gui.removeWindow(botWin);
                    gui.addWindow(botWin);
                    btnPlayPause.takeFocus();
                    return true;
                }

                return false;
            });



            // Player Loop
            while (isRunning) {
                if (layoutDirty[0]) {
                    updateLayout.run();
                    layoutDirty[0] = false;
                }

                gui.getGUIThread().processEventsAndUpdate();

                // Ambil lagu saat ini
                Music currentMusic = player.getCurrentMusic();

                if (currentMusic != null) {
                    // Update judul di Player dan di Queue sekaligus
                    String infoLagu = currentMusic.title + " - " + currentMusic.artist + "  ";
                    textNowPlayingSong.setText(infoLagu);
                    textQueueNowPlaying.setText("Now Playing:\n" + infoLagu + "\n");

                    // Progress Bar
                    long currentTime = isPaused ? pauseTime : System.currentTimeMillis();
                    int elapsedSeconds = (int) ((currentTime - musicStartDuration) / 1000);
                    int totalDuration = currentMusic.duration;

                    // Cek apakah lagu sudah selesai
                    if (elapsedSeconds >= totalDuration) {
                        player.playNext();

                        if (player.getCurrentMusic() != null) {
                            musicStartDuration = System.currentTimeMillis();
                        } else {
                            //Jika Queue Kosong
                            textNowPlayingSong.setText("Nothing");
                            textQueueNowPlaying.setText("Now Playing:\nNothing\n");
                            textProgressBar.setText("[>                                                ] 00:00 / 00:00");
                        }
                    } else {
                        // Render animasi Progress Bar
                        String elapsedStr = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
                        String totalStr = String.format("%02d:%02d", totalDuration / 60, totalDuration % 60);

                        int barLength = 50;
                        int progressIndex = (int) ((double) elapsedSeconds / totalDuration * barLength);

                        StringBuilder bar = new StringBuilder("[");
                        for (int i = 0; i < barLength; i++) {
                            if (i < progressIndex) bar.append("=");
                            else if (i == progressIndex) bar.append(">");
                            else  bar.append(" ");
                        }

                        bar.append("]").append(elapsedStr).append(" / ").append(totalStr);

                        // Update Label
                        textProgressBar.setText(bar.toString());
                    }

                }

                // Update Queue
                if (player.musicQueue.isEmpty()) {
                    textQueueList.setText("૮(˶ㅠ︿ㅠ)ა Queue Kosong...");
                } else {
                    StringBuilder queueText = new StringBuilder();
                    int number = 1;

                    for (Music musicQueue : player.musicQueue) {
                        queueText.append(number).append(". ").append(musicQueue.title).append("\n");
                        number++;
                    }

                    textQueueList.setText(queueText.toString());
                }

                Thread.sleep(16);
            }

            screen.stopScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}