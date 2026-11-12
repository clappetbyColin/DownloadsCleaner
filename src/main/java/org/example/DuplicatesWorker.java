package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Map;

public class DuplicatesWorker extends Printer {
    private final ExecutingInstance executer;
    private final NameModifier nameMod;
    private final ActionLogger dietrich;

    public DuplicatesWorker(ExecutingInstance exe, NameModifier mod, ActionLogger log) {
        this.executer = exe;
        this.nameMod = mod;
        this.dietrich = log;
    }

    public String buildFileHash(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] puffer = new byte[8192];
        try (InputStream input = Files.newInputStream(path)) {
            int eingelesen;
            while ((eingelesen = input.read(puffer)) != -1) {
                for (int i = 0; i < eingelesen; i++) {
                    digest.update(puffer[i]);
                }
            }
        }
        
        return HexFormat.of().formatHex(digest.digest());

    }
    public boolean sizeOfDataAlreadyThere(Path data, Long size, Map<Long, List<Path>> hashMap) {
        return hashMap.containsKey(size);
    }

    private List<HashHolder> sortTheHashHolders(List<HashHolder> hashHolders) {
        HashHolder temp;
        boolean sorted = true;
        while (true) {
            for (int i = 0; i < hashHolders.size() - 1; i++) {
                if ((hashHolders.get(i).getName().length()) > (hashHolders.get(i + 1).getName().length())) {
                    sorted = false;
                    temp = hashHolders.get(i);
                    hashHolders.set(i, hashHolders.get(i + 1));
                    hashHolders.set(i + 1, temp);
                }
            }
            if (sorted) {
                break;
            }
            sorted = true;
        }
        return hashHolders;
    }

    public List<Path> searchDuplicates(List<Path> listeAllerDateien, Map<Long, List<Path>> hashMap) throws IOException, NoSuchAlgorithmException {
        List<Long> sizeList = new ArrayList<>();
        List<Path> redundantData = new ArrayList<>();
        Path quelle = null;
        int errorIndex = 0;
        try {
            for (int i = 0; i < listeAllerDateien.size(); i++) {
                errorIndex = i;
                quelle = listeAllerDateien.get(i);
                if (!sizeOfDataAlreadyThere(listeAllerDateien.get(i), Files.size(listeAllerDateien.get(i)), hashMap)) {
                    hashMap.put(Files.size(listeAllerDateien.get(i)), new ArrayList<>());
                    hashMap.get(Files.size(listeAllerDateien.get(i))).add(listeAllerDateien.get(i));
                    sizeList.add(Files.size(listeAllerDateien.get(i)));
                } else {
                    hashMap.get(Files.size(listeAllerDateien.get(i))).add(listeAllerDateien.get(i));
                }
            }
        } catch (Exception e) {
            print("Error when casting the file-size.");
            dietrich.logError(listeAllerDateien.get(errorIndex),"Failed when casting the file-size "+e.getMessage());
            print(e.getMessage());

        }
        try {
            List<HashHolder> hashHolders = new ArrayList<>();
            for (int i = 0; i < hashMap.size(); i++) {
                if ((hashMap.get(sizeList.get(i)).size() > 1)) {
                    for (int j = 0; j < hashMap.get(sizeList.get(i)).size(); j++) {
                        try {
                            hashHolders.add(new HashHolder(buildFileHash(hashMap.get(sizeList.get(i)).get(j)), hashMap.get(sizeList.get(i)).get(j), sizeList.get(i)));
                        } catch (IOException e) {
                            dietrich.updateLogFile("\thash-building error\t" + sizeList.get(i) + "\t\t" + e.getMessage());
                        }
                    }
                }

            }
            sortTheHashHolders(hashHolders);
            for (int i = 0; i < hashHolders.size(); i++) {
                for (int j = i + 1; j < hashHolders.size(); j++) {
                    if ((hashHolders.get(i).getContentHash().equals(hashHolders.get(j).getContentHash())) && (hashHolders.get(i).getSize().equals(hashHolders.get(j).getSize()))) {
                        if (!redundantData.contains(hashHolders.get(i).getPath())) {
                            redundantData.add(hashHolders.get(j).getPath());
                        }
                    }
                }
            }
            print("\n");
        } catch (Exception e) {
            print("Error on hash-building");
            dietrich.updateLogFile("\thash-building error\tprogram\t\t" + e.getMessage());
            print(e.getMessage());
        }
        return redundantData;
    }

    public void deleteDuplicateFiles(List<Path> data) throws IOException {

        for (int i = 0; i < data.size(); i++) {
            Path quelle = data.get(i);
            print("Deleting " + data.get(i).getFileName().toString());
            try {
                Files.delete(data.get(i));
                print(data.get(i).getFileName().toString() + " deleted");
               dietrich.logDeleted(data.get(i));

            } catch (IOException e) {
                print(data.get(i).getFileName().toString() + " could not be deleted");
                dietrich.logError(data.get(i),"Could not delete file");
                print(e.getMessage());
            }


        }
        print("done...");
        dietrich.updateLogFile("\tdone deleting\tprogram");
    }

    public void trashMoving(List<Path> data) throws IOException {
        Path zielVerzeichnis = Path.of(System.getProperty("user.home"));
        Path trashOrdner = zielVerzeichnis.resolve("downloads").resolve("DL-Cleaner").resolve("Trash");

        for (int i = 0; i < data.size(); i++) {
            Path quelle = data.get(i);
            String name = quelle.getFileName().toString();
            Path ziel = executer.buildNewDestinationPath(trashOrdner, nameMod.removeEndung(name), nameMod.holeDateiEndungen(name));

            try {
                Files.createDirectories(trashOrdner);
                Files.move(quelle, ziel);
                print("moved");
                dietrich.logTrashed(quelle,ziel);
            } catch (IOException e) {
                print(e.getMessage());
                dietrich.logError(quelle,"Failed to move file to "+ziel.toString());
            }
        }
    }
}
