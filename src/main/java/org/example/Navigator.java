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
            if (opposite.equals("Dokumente")) {
                return home.resolve("Documents").resolve("DL-Cleaner");
            } else if (opposite.equals("Bilder")) {
                return home.resolve("Pictures").resolve("DL-Cleaner");
            } else if (opposite.equals("Archive")) {
                return plate.resolve("Archive").resolve("DL-Cleaner");
            } else if (opposite.equals("Videos")) {
                return home.resolve("Videos").resolve("DL-Cleaner");
            } else if (opposite.equals("Installer")) {
                return plate.resolve("Installer").resolve("DL-Cleaner");
            } else if (opposite.equals("Musik")) {
                return home.resolve("Music");
            }
            return home.resolve("Downloads", "Junk").resolve("DL-Cleaner");
        } else {
            return home.resolve("Downloads", "Junk").resolve("DL-Cleaner");
        }
    }
}
