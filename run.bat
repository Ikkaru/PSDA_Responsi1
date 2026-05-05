@echo off
REM Simple runner for Responsi1 on Windows
REM Tries mvnw.cmd (wrapper), then mvn (system), then fallback to java with existing target/classes and target/dependency

echo Running Responsi1...

if exist mvnw.cmd (
  echo Found mvnw.cmd, using Maven Wrapper...
  mvnw.cmd -q -DskipTests compile exec:java -Dexec.mainClass=Main
  exit /b %errorlevel%
)

where mvn >nul 2>&1
if %ERRORLEVEL%==0 (
  echo Found mvn, using system Maven...
  mvn -q -DskipTests compile exec:java -Dexec.mainClass=Main
  exit /b %errorlevel%
)

REM Fallback: try to run with already-built classes and dependencies
if exist target\classes (
  if exist target\dependency (
    echo Running with existing classes and dependency jars...
    java -cp "target/classes;target/dependency/*" Main
    exit /b %errorlevel%
  )
)

echo.
echo Maven not found and built classes/dependencies missing.
echo Options:
echo 1) Install Maven or add it to PATH.
echo 2) Add Maven Wrapper (mvnw.cmd) to the project repository.
echo 3) Build the project on another machine and copy target/classes and target/dependency here.
echo.
pause
exit /b 1
