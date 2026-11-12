package org.example;

import java.time.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;

public class ActionLogger extends Printer {
    private Path logPath;
    private final String zeit;

    public ActionLogger() throws IOException {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        this.zeit = currentTime.format(formatted);
        Path home = Path.of(System.getProperty("user.home"));
        Path destination = home.resolve("DL-Cleaner").resolve("logs");
        try {
            Files.createDirectories(destination);
            this.logPath = destination;
        } catch (Exception e) {
            print(e.getMessage());
            print("Could not create log file");
            terminate();
        }

    }
    private void schreibe(String aktion, String quelle, String ziel, String meldung)throws IOException {
        updateLogFile(aktion + "\t" + quelle + "\t" + ziel + "\t" + meldung);
    }

    public void logRunStart(String ordner, boolean dryRun, boolean safeRun)throws IOException {
        schreibe("RUN_START", ordner, "", "dryRun=" + dryRun + ", safeRun=" + safeRun);
    }

    public void logUserChoice(String frage, String antwort)throws IOException {
        schreibe("USER_CHOICE", "", "", frage + ": " + antwort);
    }

    public void logMoved(Path quelle, Path ziel)throws IOException {
        schreibe("MOVED", quelle.toString(), ziel.toString(), "");
    }

    public void logTrashed(Path quelle, Path ziel)throws IOException {
        schreibe("TRASHED", quelle.toString(), ziel.toString(), "");
    }

    public void logDeleted(Path datei)throws IOException {
        schreibe("DELETED", datei.toString(), "", "");
    }

    public void logSkipped(Path datei, String grund)throws IOException {
        schreibe("SKIPPED", datei.toString(), "", grund);
    }

    public void logError(Path datei, String meldung)throws IOException {
        String pfad = (datei == null) ? "" : datei.toString();
        schreibe("ERROR", pfad, "", meldung);
    }

    public void logRunEnd(int verschoben, int fehler)throws IOException {
        schreibe("RUN_END", "", "", verschoben + " moved, " + fehler + " errors");
    }

    public Path getLogFile() {
        return logPath.resolve(zeit + " logs.log");
    }
    public void updateLogFile(String content) throws IOException {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatted = DateTimeFormatter.ofPattern("HH-mm-ss");
            String timeNow = currentTime.format(formatted);
            content = timeNow+"\t "+content;
            Files.writeString(this.logPath.resolve(this.zeit + " logs.log"), content + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            print(e.getMessage());
            print("An error occurred, log file could be corrupted");
        }
    }
}
