package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

public class DuplicatesWorker extends Printer {
    private ExecutingInstance executer;
    private NameModifier nameMod;
    public DuplicatesWorker(ExecutingInstance exe, NameModifier mod) {
        this.executer = exe;
        this.nameMod = mod;
    }
    public String buildFileHash(Path pfad) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] puffer = new byte[8192];
        File datei = new File(pfad.toString());
        try (InputStream input = Files.newInputStream(pfad)) {
            int eingelesen;
            while ((eingelesen = input.read(puffer)) != -1) {
                for (int i = 0; i < eingelesen; i++) {
                    digest.update(puffer[i]);
                }
            }
        }
        String hash = HexFormat.of().formatHex(digest.digest());
        return hash;

    }

    public void hashDuplicates(List<Path> filteredList) throws NoSuchAlgorithmException, IOException {
        List<String> hashList = null;

        for (int i = 0; i < filteredList.size(); i++) {
            hashList.add(buildFileHash(filteredList.get(i)));
        }
    }

    public boolean sizeOfDataAlreadyThere(Path data, Long size, Map<Long, List<Path>> hashMap) {
        if (hashMap.containsKey(size)) {
            return true;
        } else {
            return false;
        }
    }
    private List<HashHolder> sortTheHashHolders(List<HashHolder> hashHolders) {
        HashHolder temp;
        boolean sorted = true;
        while(sorted) {
            for(int i = 0; i < hashHolders.size()-1;i++) {
                if((hashHolders.get(i).getName().length())> (hashHolders.get(i+1).getName().length())) {
                    sorted = false;
                    temp = hashHolders.get(i);
                    hashHolders.set(i,hashHolders.get(i+1));
                    hashHolders.set(i+1,temp);
                }
            }
            if(sorted == true) {
                break;
            }
            sorted = true;
        }
        return hashHolders;
    }
    public List<Path> searchDuplicates(List<Path> listeAllerDateien, Map<Long, List<Path>> hashMap) throws IOException, NoSuchAlgorithmException {
        List<Long> sizeList = new ArrayList<>();
        List<Path> redundantData = new ArrayList<>();
        try {
            for (int i = 0; i < listeAllerDateien.size(); i++) {
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
            print(e.getMessage());

        }
        try {

            //Hashes aller infrage kommenden Dateien in der hashMap
            List<HashHolder> hashHolders = new ArrayList<>();
            for (int i = 0; i < hashMap.size(); i++) {
                if ((hashMap.get(sizeList.get(i)).size() > 1)) {
                    for (int j = 0; j < hashMap.get(sizeList.get(i)).size(); j++) {
                        hashHolders.add(new HashHolder(buildFileHash(hashMap.get(sizeList.get(i)).get(j)), hashMap.get(sizeList.get(i)).get(j), sizeList.get(i)));
                    }
                }

            }
            hashHolders = sortTheHashHolders(hashHolders);
            //Vergleich der Hashes in der jeweiligen gruppe
            for (int i = 0; i < hashHolders.size(); i++) {
                for (int j = i + 1; j < hashHolders.size(); j++) {
                    if ((hashHolders.get(i).getContentHash().equals(hashHolders.get(j).getContentHash())) && (hashHolders.get(i).getSize().equals(hashHolders.get(j).getSize()))) {
                        if (!redundantData.contains(hashHolders.get(i).getPath())) {
                            redundantData.add(hashHolders.get(j).getPath());
                        }
                    }
                }
            }
            //Auflisten der redundanten Dateien
           /* print("Following files are redundant and can be deleted:\n\n");
            for (int i = 0; i < redundantData.size(); i++) {
                print(redundantData.get(i).toString());
            }*/
            print("\n");
            //rückgabe der dateien

        } catch (Exception e) {
            print("Error on hash-building");
            print(e.getMessage());
        }
        try {
            return redundantData;
        } catch (Exception e) {
            print(e.getMessage());
        }
        return null;
    }
    public void deleteDuplicateFiles(List<Path> data)throws IOException {
        for (int i = 0; i < data.size();i++) {
            print("Deleting "+data.get(i).getFileName().toString());
            try {
                Files.delete(data.get(i));
                print(data.get(i).getFileName().toString()+" deleted");
            } catch (IOException e) {
                print(data.get(i).getFileName().toString()+" could not be deleted");
                print(e.getMessage());
            }


        }
        print("done...");
    }
    public void trashMoving(List<Path> data)throws IOException {
        Path zielVerzeichnis = Path.of(System.getProperty("user.home"));
        if(new File((zielVerzeichnis.resolve("downloads").resolve("DL-Cleaner").resolve("Trash")).toString()).exists()){
            for(int i = 0; i < data.size();i++) {
                try {
                    Files.move(data.get(i), executer.buildNewDestinationPath(zielVerzeichnis.resolve("downloads").resolve("DL-Cleaner").resolve("Trash"),nameMod.removeEndung(data.get(i).getFileName().toString()),nameMod.holeDateiEndungen(data.get(i).getFileName().toString())));
                } catch (IOException e) {
                    print(e.getMessage());
                    print("Fehler1");
                }
            }
        }else {
            try {
                Files.createDirectories(zielVerzeichnis.resolve("downloads").resolve("DL-Cleaner").resolve("Trash"));
            } catch (IOException e) {
                print(e.getMessage());
                print("Fehler2");
            }

            for(int i = 0; i < data.size();i++) {
                try {
                    Files.move(data.get(i), executer.buildNewDestinationPath(zielVerzeichnis.resolve("downloads").resolve("DL-Cleaner").resolve("Trash"),nameMod.removeEndung(data.get(i).getFileName().toString()),nameMod.holeDateiEndungen(data.get(i).getFileName().toString())));
                } catch (IOException e) {
                    print(e.getMessage());
                    print("Fehler3");
                }
            }
        }
    }
}
