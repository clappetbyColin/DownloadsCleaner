package org.example;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ReversedCursedTechnique extends Printer {
    private final ActionLogger dietrich;
    private final ExecutingInstance executer;
    private final NameModifier nameM;

    public ReversedCursedTechnique(ActionLogger log, ExecutingInstance exe, NameModifier mod) {
        this.dietrich = log;
        this.executer = exe;
        this.nameM = mod;
    }
    private Path getNewestlogFilePath(Path path) throws IOException {
        Path newest = null;
        List<Path> eintrag = new ArrayList<>();

        try (Stream<Path> inhalt = Files.list(path)) {
            inhalt.forEach(eintrag::add);
        } catch (Exception e) {
            print("Unable to load log files");
            terminate();
        }
        try {
            newest = eintrag.getFirst();
        } catch (Exception e) {

        }

        for (int i = 1; i < eintrag.size(); i++) {
            if (eintrag.get(i).getFileName().toString().compareTo(newest.getFileName().toString()) > 0) {
                newest = eintrag.get(i);
            }
        }
        return newest;
    }
    private List<String> getNewestlogFile(Path path) throws IOException {
        Path newest = null;
        List<Path> eintrag = new ArrayList<>();

        try (Stream<Path> inhalt = Files.list(path)) {
            inhalt.forEach(eintrag::add);
        } catch (Exception e) {
            print("Unable to list log files "+e.getMessage());
        }
        try {
            newest = eintrag.getFirst();
        } catch (Exception e) {
            print("No Log found nothing to undo");
            terminate();
        }

        for (int i = 1; i < eintrag.size(); i++) {
            if (eintrag.get(i).getFileName().toString().compareTo(newest.getFileName().toString()) > 0) {
                newest = eintrag.get(i);
            }
        }
        return Files.readAllLines(newest);
    }
    private void deleteLog(Path path)throws IOException {
        Path ziel =Path.of(System.getProperty("user.home")).resolve("DL-Cleaner").resolve("usedLogs");
        try {
            Files.createDirectories(ziel);
        } catch (IOException e) {
            print("Could not create directory"+e.getMessage());
            try {
                Files.delete(path);
                print("Deleted log-file");
            } catch (Exception f) {
                print("Deleting log-file failed"+f.getMessage());
            }
        }

        try {
            Files.move(path,ziel.resolve(path.getFileName().toString()));
            print("Log-file moved");
        } catch (IOException e) {
            print("Moving log-file failed");
        }

    }
    private Path backToTheStart(Path path,List<String[]> collection) {
        for(int i = 0; i < collection.size();i++) {
            if(collection.get(i)[3].equals(path.toString())) {
                return Path.of(collection.get(i)[2]);
            }
        }
        return Path.of(System.getProperty("user.home")).resolve("downloads").resolve("DL-Cleaner").resolve("lost-Data");
    }
    public void reverse() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        List<String> snippets = null;
        try {
            snippets = getNewestlogFile(Path.of(System.getProperty("user.home")).resolve("DL-Cleaner").resolve("logs"));
        } catch (Exception e) {
            print("Reading file error"+e.getMessage());
            terminate();
        }
        String[][] tempcollection = new String[snippets.size()][];
        List<String[]> collection = new ArrayList<>();
        for (int i = 0; i < snippets.size(); i++) {
            tempcollection[i] = snippets.get(i).split("\t");
        }
        for (int i = 0; i < tempcollection.length; i++) {
            if (tempcollection[i].length > 3) {
                if ((tempcollection[i][1].equals("MOVED")) || (tempcollection[i][1].equals("TRASHED"))) {
                    collection.add(snippets.get(i).split("\t"));
                }
            }
        }
        if(collection.isEmpty()) {
            print("Nothing to undo from the last run");
            terminate();
        }
        print("\n\n");
        for(int i = 0; i < collection.size(); i++) {
            print("Moving: "+collection.get(i)[3]+" -> " + backToTheStart(Path.of(collection.get(i)[3]),collection));
        }
        print("Do you want to move the files listed Above?");
        while (true) {
            print("Yes (Y)   No (N)");
            input = br.readLine();
            if ((input.equals("Y")) || (input.equals("y"))) {
                for(int i = 0; i < collection.size();i++) {
                    try {
                        Files.createDirectories(backToTheStart(Path.of(collection.get(i)[3]),collection).getParent());
                        if(Files.exists(backToTheStart(Path.of(collection.get(i)[3]),collection))) {
                            Path secondChoicePath = executer.buildNewDestinationPath(backToTheStart(Path.of(collection.get(i)[3]),collection).getParent(),nameM.removeEndung(backToTheStart(Path.of(collection.get(i)[3]),collection).getFileName().toString()),nameM.holeDateiEndungen(backToTheStart(Path.of(collection.get(i)[3]),collection).toString()));
                            Files.move(Path.of(collection.get(i)[3]),secondChoicePath);
                        }else {
                            Files.move(Path.of(collection.get(i)[3]),backToTheStart(Path.of(collection.get(i)[3]),collection));
                        }
                    } catch (IOException e) {
                        if(Files.exists(Path.of(collection.get(i)[3]))) {
                            print("Could not Move "+collection.get(i)[3]);
                        }else {
                            print("File is missing "+collection.get(i)[3]);
                        }

                        print(e.getMessage());
                    }

                }
                deleteLog(getNewestlogFilePath(Path.of(System.getProperty("user.home")).resolve("DL-Cleaner").resolve("logs")));
                break;
            } else if ((input.equals("N")) || (input.equals("n"))) {
                break;
            } else {
                print("Input not accepted");
            }
        }

    }
}
