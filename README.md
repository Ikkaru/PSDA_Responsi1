<img width="2000" height="840" alt="Teks paragraf Anda (1)" src="https://github.com/user-attachments/assets/ea70034b-4c4d-404f-a8f5-55a2680c54c3" />

Castify is a simple terminal-based music catalogue and player application built in Java.
---

## Prerequisites

- Java JDK 21 (or newer) installed and available on `PATH`.
- A terminal that supports ANSI/VT sequences (Linux terminals, Windows Terminal, or PowerShell on modern Windows).
- `lib/lanterna-3.1.1.jar` must exist in the project folder (already included in this repo).

Notes:
- Maven is not required for normal execution using `run.bat`.

---


## One-click on Windows

A `run.bat` script was added to the repository. From a Command Prompt or PowerShell (project root) run:

```
run.bat
```

`run.bat` will compile source files with `javac` and run the program directly with local dependency `lib/lanterna-3.1.1.jar`.
This means the app can run even when Maven Wrapper is broken or Maven is not installed.



## Troubleshooting

- "Unsupported major.minor version": your Java runtime is older than the code was compiled for — install Java 21 or change `pom.xml` to target a lower Java version.
- If the terminal UI looks broken on Windows, use Windows Terminal or PowerShell (modern versions) or run inside WSL.
- If dependencies fail to download, check internet access and proxy settings.

---

Licensed for educational use.
