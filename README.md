# Responsi1

Responsi1 is a simple terminal-based music catalogue and player application built in Java.
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

---


## Quick start (recommended)

Run the application with the included Maven Wrapper (no global Maven required):

Linux / macOS:
```bash
./mvnw -q -DskipTests compile exec:java -Dexec.mainClass=Main
```

Windows (PowerShell / Command Prompt):
```powershell
mvnw.cmd -q -DskipTests compile exec:java -Dexec.mainClass=Main
```

The first run will download the wrapper and the required Maven distribution, then Maven will download project dependencies and run `Main`.

---

## Alternative: run with your installed Maven

If you have Maven installed, you can run:

```bash
mvn -q -DskipTests compile exec:java -Dexec.mainClass=Main
```

Or build the project and run manually:

```bash
mvn -DskipTests package
# then (Linux/macOS)
java -cp "target/classes:target/dependency/*" Main
# or (Windows)
java -cp "target/classes;target/dependency/*" Main
```

If you use the manual classpath approach, you may first run:

```bash
mvn dependency:copy-dependencies package
```


## Creating a single executable (fat JAR)

If you want to distribute a single JAR that runs with only Java on the target machine, add a shading plugin (e.g., `maven-shade-plugin`) to the `pom.xml`, then run:

```bash
mvn -DskipTests package
# shaded jar will be in target/ (convention depends on plugin configuration)
java -jar target/<your-fat-jar>.jar
```

If you want, I can add the `maven-shade-plugin` configuration to `pom.xml` to automate fat-jar creation.

---

## Troubleshooting

- "Unsupported major.minor version": your Java runtime is older than the code was compiled for — install Java 21 or change `pom.xml` to target a lower Java version.
- If the terminal UI looks broken on Windows, use Windows Terminal or PowerShell (modern versions) or run inside WSL.
- If dependencies fail to download, check internet access and proxy settings.

---

## Files of interest

- `run.bat` — Windows shortcut that tries the wrapper, system Maven, then fallback classpath run.
- `mvnw`, `mvnw.cmd` and `.mvn/wrapper/` — Maven Wrapper files. Use `./mvnw` or `mvnw.cmd` to run without installing Maven.
- `pom.xml` — Maven build file and project settings.


Licensed for educational use.
