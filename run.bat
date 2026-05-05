@echo off
setlocal
REM Windows launcher without Maven dependency
REM Compiles sources with javac and runs Main using local lanterna jar

echo Running Responsi1 (No Maven mode)...

where java >nul 2>&1
if errorlevel 1 (
  echo Java runtime not found in PATH.
  echo Please install JDK 21 or newer, then rerun.
  echo.
  pause
  exit /b 1
)

where javac >nul 2>&1
if errorlevel 1 (
  echo Java compiler ^(javac^) not found in PATH.
  echo Please install full JDK 21 or newer, then rerun.
  echo.
  pause
  exit /b 1
)

if not exist lib\lanterna-3.1.1.jar (
  echo Missing dependency: lib\lanterna-3.1.1.jar
  echo Make sure you submit/extract the full project folder including lib.
  echo.
  pause
  exit /b 1
)

if not exist target\classes (
  mkdir target\classes >nul 2>&1
)

REM Set initial console size (ignore error if terminal doesn't support this)
mode con: cols=200 lines=60 >nul 2>&1

echo Compiling Java sources...
javac -encoding UTF-8 -cp "lib\lanterna-3.1.1.jar" -d target\classes src\main\java\*.java
if errorlevel 1 (
  echo Compilation failed.
  echo.
  pause
  exit /b 1
)

echo Starting application...
java -cp "target\classes;lib\lanterna-3.1.1.jar" Main
set EXIT_CODE=%ERRORLEVEL%

if not "%EXIT_CODE%"=="0" (
  echo.
  echo Program exited with code %EXIT_CODE%.
  pause
)

exit /b %EXIT_CODE%
