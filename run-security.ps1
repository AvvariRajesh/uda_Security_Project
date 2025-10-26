# Set JavaFX SDK path
$javafxPath = "C:\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib"

# Backend source folders
$backendSrc = @(
    "SecurityService\src\main\java\com\udasecurity\imageservice\*.java",
    "SecurityService\src\main\java\com\udasecurity\security\*.java"
)

# GUI source
$guiSrc = "SecurityService\src\main\java\com\udasecurity\gui\SecurityGUI.java"

# Target classes folder
$targetClasses = "SecurityService\target\classes"

# Create target folder if not exists
if (!(Test-Path $targetClasses)) {
    New-Item -ItemType Directory -Force -Path $targetClasses
}

# Compile backend
Write-Host "Compiling backend..."
javac -d $targetClasses $backendSrc

# Compile GUI with backend in classpath
Write-Host "Compiling GUI..."
javac --module-path $javafxPath `
      --add-modules javafx.controls,javafx.fxml `
      -classpath $targetClasses `
      -d $targetClasses `
      $guiSrc

# Run the GUI
Write-Host "Running GUI..."
java --module-path $javafxPath `
     --add-modules javafx.controls,javafx.fxml `
     -classpath $targetClasses `
     com.udasecurity.gui.SecurityGUI
