package org.example;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;

public class Main extends Printer{
    private void helper() {
        print("--dry-run  ->   in dryRun mode no files will be moved you will only be showed where they would have been moved\n(THIS DOES NOT EFFECT THE DUPLICATES DELETE FUNCTION!!!\ndry-run can be enabled during runTime when the intro was not skipped.");
        print("example: C:\\Users\\example\\testfolder   ->   you can give the program directory path that you want to be cleaned. (THIS MUST BE A DIRECTORY otherwise the program will choose your downloads folder");
    }
    public void main(String[] args) throws IOException, InterruptedException {
        try{
            for (String arg : args) {
                if (arg.equals("--help")) {
                   helper();
                   while(true){

                   }
                }
            }
        } catch (Exception e) {

        }
        print("It can be necessary to start the application with admin-permissions");
        Maping mapper = new Maping();
        Navigator navigator = new Navigator();
        Maping maping = new Maping();
        NameModifier modder = new NameModifier();
        ExecutingInstance executer = new ExecutingInstance(modder);
        DuplicatesWorker duplicates = new DuplicatesWorker(executer, modder);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input;
            boolean safeRun = true;
            print("Enable safeRun?");
            print("When not enabled the programm wont ask you twice. If you misclick at the deleting section your files are immediately deleted (NOT MOVED TO TRASH!!!).");
            print("With safeRun enabled the programm wont delete files, it will move them to another directory and open it for you");
            print("Enable safeRun?");
            while(true) {
                print("Yes (Y)   No (N)");
                input = br.readLine();
                if((input.equals("Y"))|| (input.equals("y"))) {
                    safeRun = true;
                    break;
                } else if ((input.equals("N "))||(input.equals("n"))) {
                    safeRun = false;
                    break;
                } else {
                    print("Input not accepted");
                }
            }
            List<Path> redundantData;
            Map<Long, List<Path>> hashMap = new HashMap<>();
            boolean intro = true;
            print("Skip intro-guide?");

            while (true) {
                print("Yes (Y)   No (N)");
                input = br.readLine();
                if ((input.equals("Y")) || (input.equals("y"))) {
                    intro = false;
                    print("Skipped");
                    Thread.sleep(500);
                    break;
                } else if ((input.equals("N")) || (input.equals("n"))) {
                    print("Not skipped");
                    intro = true;
                    break;
                } else {
                    print("input not accepted.");
                }
            }
            if (intro) {
                print("Welcome to Download-Cleaner\nThis Programm will clear your Download Folder and transfer all Files to specific locations");
                print("You can view the destination-folders in the 'README' file on gitHub");
                print("Press enter once you are ready to start. :)");
                br.read();
            }
            Map<String, String> regeln = new HashMap<>();
            String endung;
            mapper.baueRefmap(regeln);
            boolean deleteDuplicates = false;
            boolean dryRun = false;
            String ordnerPfad = null;
            for (String arg : args) {
                if (arg.equals("--dry-run")) {
                    dryRun = true;
                } else if (arg.startsWith("--")) {
                    print("Unbekannte operation: " + arg);
                } else {
                    ordnerPfad = arg;
                }
            }
            try {
                if (ordnerPfad == null) {
                    ordnerPfad = System.getProperty("user.home") + "//Downloads";
                } else {
                    Path folder = Path.of(ordnerPfad);
                    if (!Files.exists(folder)) {
                        print("The system could not find the specified path");
                        print("Setting path to Downloads...\n\n");
                        ordnerPfad = System.getProperty("user.home") + "//Downloads";
                        Thread.sleep(1000);
                    } else {
                        print("Path found");
                        if (!Files.isDirectory(folder)) {
                            print("Found a file instead of a path.\nTerminating Session");
                            System.exit(0);
                        }
                    }
                }
            } catch (Exception e) {
                print("Something went wrong. " + e.getMessage());
                System.exit(0);
            }

            try {

            } catch (Exception e) {
                print("The --dry-run command was either spelled wrong or simply not entered.\nThe Programm will continue in standart-mode");
                Thread.sleep(1000);
            }


            List<Path> listeAllerDateien = new ArrayList<>();
            listeAllerDateien = executer.holeAlleDateien(ordnerPfad);
            List<Path> filteredListofFiles = new ArrayList<>();

            filteredListofFiles = executer.filtereDateien(ordnerPfad, listeAllerDateien, regeln);
            Thread.sleep(1000);
            print("Searching duplicates...");
            redundantData = duplicates.searchDuplicates(filteredListofFiles, hashMap);
            if(!redundantData.isEmpty()) {
                print("Found redundant files.");
                print("listing...\n");
                for (int i = 0; i < redundantData.size();i++) {
                    print(redundantData.get(i).getFileName().toString());
                }
                if(!safeRun) {
                    print("\nDelete files?");
                    Thread.sleep(1500);
                    while(true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if((input.equals("Y"))|| (input.equals("y"))) {
                            duplicates.deleteDuplicateFiles(redundantData);
                            break;
                        } else if ((input.equals("N"))||(input.equals("n"))) {
                            break;
                        } else {
                            print("Input not accepted");
                        }
                    }
                } else {
                    print("\nMove files?");
                    Thread.sleep(1500);
                    while(true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if((input.equals("Y"))|| (input.equals("y"))) {
                            print("Moving files to Downloads\\DL-Cleaner\\Trash...");
                            duplicates.trashMoving(redundantData);
                            break;
                        } else if ((input.equals("N"))||(input.equals("n"))) {
                            break;
                        } else {
                            print("Input not accepted");
                        }
                    }


                }
            }else {
                print("No redundant data found");
            }
            Thread.sleep(1000);
            print("Press enter to continue.");
            br.read();
            if (dryRun == true) {
                print("Dryrun enabled");
            } else {
                print("Dryrun disabled");
                if (intro == true) {
                    print("Do you want to enable Dryrun?");
                    print("Dryrun will not move files, it will only list the destination Paths where the files would've been moved to.");
                    print("Activate Dryrun ? Yes(Y)   No (N)");
                    while (true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if ((input.equals("Y")) || (input.equals("y"))) {
                            dryRun = true;
                            print("Dryrun enabled");
                            break;
                        } else if ((input.equals("N")) || (input.equals("n"))) {
                            print("Continue without Dryrun");
                            dryRun = false;
                            break;
                        } else {
                            print("input not accepted.");
                        }
                    }
                    Thread.sleep(2000);
                    print("Listing files...");
                    Thread.sleep(2000);
                }
            }
            //User abfrage


            print("Are you sure you want to move following files to the folders listed beneath?\nThis action can only be reversed by the user himself.");
            for (int i = 0; i < filteredListofFiles.size(); i++) {
                endung = modder.holeDateiEndungen(filteredListofFiles.get(i).getFileName().toString());
                Path destinationFolderPath = (navigator.searchDestinationFolder(endung, regeln));
                Path destinationPath = executer.buildNewDestinationPath(destinationFolderPath, modder.removeEndung(filteredListofFiles.get(i).getFileName().toString()), endung);
                print(filteredListofFiles.get(i).getFileName().toString() + "\n-> " + destinationPath.toString() + "\n");
            }
            while (true) {
                Thread.sleep(1000);
                print("\n\nMove all files to the folders");
                if (dryRun == true) {
                    print("You are currently in dryRun-mode\nno matter if you press y or n no files will be moved :)");
                }
                print("Yes (Y)   No (N)");
                input = br.readLine();
                if ((input.equals("Y")) || (input.equals("y"))) {
                    break;
                } else if ((input.equals("N")) || (input.equals("n"))) {
                    print("Ending Programm...");
                    print("Goodbye :)");
                    Thread.sleep(2000);
                    System.exit(0);
                } else {
                    print("input not accepted.");
                }
            }
            if (dryRun == false) {
                for (int i = 0; i < filteredListofFiles.size(); i++) {
                    endung = modder.holeDateiEndungen(filteredListofFiles.get(i).getFileName().toString());
                    Path destinationFolderPath = (navigator.searchDestinationFolder(endung, regeln));
                    Path destinationPath = executer.buildNewDestinationPath(destinationFolderPath, modder.removeEndung(filteredListofFiles.get(i).getFileName().toString()), endung);
                    //print(filteredListofFiles.get(i).getFileName().toString()+"\n-> "+destinationPath.toString());
                    try {
                        if (!Files.exists(destinationFolderPath)) {
                            Files.createDirectories(destinationFolderPath);
                        }
                        Files.move(filteredListofFiles.get(i), destinationPath);
                        print(filteredListofFiles.get(i).getFileName().toString() + " moved");
                    } catch (Exception e) {
                        print("Malfunktion: " + filteredListofFiles.get(i).getFileName().toString() + e.getMessage());
                    }

                }
            } else {
                print("Simulation terminated");
                if(safeRun == true) {
                    print("Open folder with 'deleted' data?");
                    while(true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if((input.equals("Y"))|| (input.equals("y"))) {
                            Desktop.getDesktop().open(Path.of(System.getProperty("user.home")).resolve("downloads").resolve("DL-Cleaner").resolve("Trash").toFile());
                            break;
                        } else if ((input.equals("N"))||(input.equals("n"))) {
                            break;
                        } else {
                            print("Input not accepted");
                        }
                    }
                }
                print("Goodbye :)");
                System.exit(0);
            }
            print("All Files moved.");
            print("Goodbye :)");

            // print(holeDateiEndungen("bild.jpg").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
