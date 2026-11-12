package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
public class Navigator {
    public Navigator() {

    }
    public Path searchDestinationFolder(String endung, Map<String, String> regeln) throws IOException {
        String opposite = regeln.get(endung);
        Path home = Paths.get(System.getProperty("user.home"));
        Path plate = Paths.get(System.getProperty("user.home"));
        plate = plate.getRoot();
        if (regeln.containsKey(endung)) {
            return switch (opposite) {
                case "Dokumente" -> home.resolve("Documents").resolve("DL-Cleaner");
                case "Bilder" -> home.resolve("Pictures").resolve("DL-Cleaner");
                case "Archive" -> plate.resolve("Archive").resolve("DL-Cleaner");
                case "Videos" -> home.resolve("Videos").resolve("DL-Cleaner");
                case "Installer" -> plate.resolve("Installer").resolve("DL-Cleaner");
                case "Musik" -> home.resolve("Music");
                default -> home.resolve("Downloads", "Junk").resolve("DL-Cleaner");
            };
        } else {
            return home.resolve("Downloads", "Junk").resolve("DL-Cleaner");
        }
    }
}
