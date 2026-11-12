package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
public class ExecutingInstance extends Printer {
    private final NameModifier nameMod;
    private final ActionLogger dietrich;
    public ExecutingInstance(NameModifier mod, ActionLogger log) {
        this.nameMod = mod;
        this.dietrich = log;
    }
    public List<Path> holeAlleDateien(String adresse) throws IOException {
        Path path = Path.of(adresse);
        List<Path> dateien = new ArrayList<>();
        try (Stream<Path> inhalt = Files.list(path)) {
            inhalt.forEach(eintrag -> {
                if (Files.isRegularFile(eintrag)) {
                    try {
                        if (!Files.isHidden(eintrag)) {
                            dateien.add(eintrag);
                        }
                    } catch (Exception e) {
                        print(e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            print(e.getMessage());
        }
        return dateien;
    }

    public List<Path> filtereDateien(String pfad, List<Path> listeVonDateien, Map<String, String> regeln) throws IOException {
        Path path = Path.of(pfad);
        List<Path> gefilterteDateien = new ArrayList<>();
        print("Found files:");
        for (int i = 0; i < listeVonDateien.size(); i++) {
            String endung = nameMod.holeDateiEndungen(listeVonDateien.get(i).toString());
            if (regeln.containsKey(endung)) {
                if (!regeln.get(endung).equals("DontTouch")) {
                    gefilterteDateien.add(listeVonDateien.get(i));
                    print(listeVonDateien.get(i).getFileName().toString());
                } else {
                    dietrich.logSkipped(listeVonDateien.get(i),"file is currently downloading");
                }
            } else {
                gefilterteDateien.add(listeVonDateien.get(i));
                print(listeVonDateien.get(i).getFileName().toString());
            }
        }
        print("\n\n\n\n");
        return gefilterteDateien;
    }



    public Path buildNewDestinationPath(Path zielpfad, String dateiName, String endung) {
        int counter = 1;
        String urspruenglicherName = dateiName;
        String neuerDateiName = "";
        Path neuerPfad = zielpfad;


        while (true) {
            neuerDateiName = nameMod.fuegeZusammen(dateiName, endung);
            neuerPfad = zielpfad.resolve(neuerDateiName);
            if (!(new File(neuerPfad.toString()).exists())) {
                return neuerPfad;
            } else {
                dateiName = nameMod.baueNeuenNamen(urspruenglicherName, counter);
                counter++;
            }
        }
    }
}
