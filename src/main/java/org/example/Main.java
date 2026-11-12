package org.example;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;

public class Main extends Printer {
    private void helper() {
        print("--dry-run  ->   in dryRun mode no files will be moved you will only be showed where they would have been moved\n(THIS DOES NOT EFFECT THE DUPLICATES DELETE FUNCTION!!!\ndry-run can be enabled during runTime when the intro was not skipped.");
        print("example: C:\\Users\\example\\testfolder   ->   you can give the program directory path that you want to be cleaned. (THIS MUST BE A DIRECTORY otherwise the program will choose your downloads folder");
    }

    public void main(String[] args) throws IOException, InterruptedException {
        ActionLogger dietrich = new ActionLogger();
        try {
            for (String arg : args) {
                if (arg.equals("--help")) {
                    helper();
                    dietrich.updateLogFile("\tactivated help \t parameters");
                    dietrich.updateLogFile("\t");
                    terminate();
                }
            }
        } catch (Exception e) {
            print(e.getMessage());
        }
        print("It can be necessary to start the application with admin-permissions");
        Maping mapper = new Maping();
        Navigator navigator = new Navigator();
        Maping maping = new Maping();
        NameModifier nameM = new NameModifier();
        ExecutingInstance executer = new ExecutingInstance(nameM, dietrich);
        DuplicatesWorker duplicates = new DuplicatesWorker(executer, nameM, dietrich);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input;
            boolean safeRun = true;
            print("Enable safeRun?");
            print("When not enabled the program wont ask you twice. If you misclick at the deleting section your files are immediately deleted (NOT MOVED TO TRASH!!!).");
            print("With safeRun enabled the program wont delete files, it will move them to another directory and open it for you");
            print("Enable safeRun?");
            while (true) {
                print("Yes (Y)   No (N)");
                input = br.readLine();
                if ((input.equals("Y")) || (input.equals("y"))) {
                    dietrich.logUserChoice("Enable safeRun?","Yes");
                    break;
                } else if ((input.equals("N")) || (input.equals("n"))) {
                    safeRun = false;
                    dietrich.logUserChoice("Enable safeRun?","No");
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
                    dietrich.logUserChoice("Skip intro?","Yes");
                    print("Skipped");
                    Thread.sleep(500);
                    break;
                } else if ((input.equals("N")) || (input.equals("n"))) {
                    print("Not skipped");
                    dietrich.logUserChoice("Skip intro?","No");
                    break;
                } else {
                    print("input not accepted.");
                }
            }
            if (intro) {
                print("Welcome to Download-Cleaner\nThis program will clear your Download Folder and transfer all Files to specific locations");
                print("You can view the destination-folders in the 'README' file on gitHub");
                print("Press enter once you are ready to start. :)");
                br.readLine();
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
                    dietrich.updateLogFile("\tdry-Run enabled\tparameters");
                } else if (arg.startsWith("--")) {
                    print("unknown operation: " + arg);
                    dietrich.updateLogFile("\tthrown away\tparameters\t\tunknown operation");
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
                            dietrich.logError(folder, "Found file instead of folder");
                            terminate();
                        }
                    }
                }
                dietrich.logRunStart(ordnerPfad,dryRun,safeRun);
            } catch (Exception e) {
                print("Something went wrong. " + e.getMessage());
                dietrich.updateLogFile("\terror: " + e.getMessage());
                terminate();
            }

            dietrich.updateLogFile("\tFolder to clean \t" + ordnerPfad);
            List<Path> listeAllerDateien = new ArrayList<>();
            listeAllerDateien = executer.holeAlleDateien(ordnerPfad);
            List<Path> filteredListofFiles = new ArrayList<>();

            filteredListofFiles = executer.filtereDateien(ordnerPfad, listeAllerDateien, regeln);
            Thread.sleep(1000);
            print("Searching duplicates...");
            redundantData = duplicates.searchDuplicates(filteredListofFiles, hashMap);
            if (!redundantData.isEmpty()) {
                print("Found redundant files.");
                print("listing...\n");
                for (int i = 0; i < redundantData.size(); i++) {
                    print(redundantData.get(i).getFileName().toString());
                }
                if (!safeRun) {
                    print("\nDelete files?");
                    Thread.sleep(1500);
                    while (true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if ((input.equals("Y")) || (input.equals("y"))) {
                            dietrich.logUserChoice("Delete files?","Yes");
                            duplicates.deleteDuplicateFiles(redundantData);
                            filteredListofFiles.removeAll(redundantData);
                            redundantData.clear();
                            break;
                        } else if ((input.equals("N")) || (input.equals("n"))) {
                           dietrich.logUserChoice("Delete files?","No");
                            break;
                        } else {
                            print("Input not accepted");
                        }
                    }

                } else {
                    print("\nMove files?");
                    Thread.sleep(1500);
                    while (true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if ((input.equals("Y")) || (input.equals("y"))) {
                            print("Moving files to Downloads\\DL-Cleaner\\Trash...");
                            dietrich.logUserChoice("Move files?","Yes");
                            duplicates.trashMoving(redundantData);
                            break;
                        } else if ((input.equals("N")) || (input.equals("n"))) {
                            dietrich.logUserChoice("Move files?","No");
                            break;
                        } else {
                            print("Input not accepted");
                        }
                    }

                    filteredListofFiles.removeAll(redundantData);
                    redundantData.clear();

                }
            } else {
                print("No redundant data found");
            }

            Thread.sleep(1000);
            print("Press enter to continue.");
            dietrich.logUserChoice("Press enter to continue","done");
            br.readLine();
            if (dryRun) {
                print("Dry-run enabled");
            } else {
                print("Dry-run disabled");
                if (intro) {
                    print("Do you want to enable Dry-run?");
                    print("Dry-run will not move files, it will only list the destination Paths where the files would've been moved to.");
                    print("Activate Dry-run ? Yes(Y)   No (N)");
                    while (true) {
                        print("Yes (Y)   No (N)");
                        input = br.readLine();
                        if ((input.equals("Y")) || (input.equals("y"))) {
                            dietrich.logUserChoice("Activate dry-run?","Yes");
                            dryRun = true;
                            print("Dry-run enabled");
                            break;
                        } else if ((input.equals("N")) || (input.equals("n"))) {
                            print("Continue without Dry-run");
                            dietrich.logUserChoice("Activate dry-run?","No");
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
            print("Are you sure you want to move following files to the folders listed beneath?\nThis action can only be reversed by the user himself.");
            for (int i = 0; i < filteredListofFiles.size(); i++) {
                endung = nameM.holeDateiEndungen(filteredListofFiles.get(i).getFileName().toString());
                Path destinationFolderPath = (navigator.searchDestinationFolder(endung, regeln));
                Path destinationPath = executer.buildNewDestinationPath(destinationFolderPath, nameM.removeEndung(filteredListofFiles.get(i).getFileName().toString()), endung);
                print(filteredListofFiles.get(i).getFileName().toString() + "\n-> " + destinationPath.toString() + "\n");
            }
            while (true) {
                Thread.sleep(1000);
                print("\n\nMove all files to the folders");
                if (dryRun) {
                    print("You are currently in dryRun-mode\nno matter if you press y or n no files will be moved :)");
                }
                print("Yes (Y)   No (N)");
                input = br.readLine();
                if ((input.equals("Y")) || (input.equals("y"))) {
                    dietrich.logUserChoice("Sort all files in folders?","Yes");
                    break;
                } else if ((input.equals("N")) || (input.equals("n"))) {
                    dietrich.logUserChoice("Sort all files in folders?","No");
                    print("Ending program...");
                    print("Goodbye :)");
                    dietrich.updateLogFile("\tEnding program");
                    terminate();
                } else {
                    print("input not accepted.");
                }
            }
            if (!dryRun) {
                for (int i = 0; i < filteredListofFiles.size(); i++) {
                    endung = nameM.holeDateiEndungen(filteredListofFiles.get(i).getFileName().toString());
                    Path destinationFolderPath = (navigator.searchDestinationFolder(endung, regeln));
                    Path destinationPath = executer.buildNewDestinationPath(destinationFolderPath, nameM.removeEndung(filteredListofFiles.get(i).getFileName().toString()), endung);
                    //print(filteredListofFiles.get(i).getFileName().toString()+"\n-> "+destinationPath.toString());
                    try {
                        if (!Files.exists(destinationFolderPath)) {
                            Files.createDirectories(destinationFolderPath);
                        }
                        Files.move(filteredListofFiles.get(i), destinationPath);
                       dietrich.logMoved(filteredListofFiles.get(i), destinationPath);
                        print(filteredListofFiles.get(i).getFileName().toString() + " moved");
                    } catch (Exception e) {
                        print("malfunction: " + filteredListofFiles.get(i).getFileName().toString() + e.getMessage());
                        dietrich.logError(filteredListofFiles.get(i),"Could not move file");
                    }

                }

            } else {
                print("Simulation terminated");
                print("Open folder with 'deleted' data?");
                while (true) {
                    print("Yes (Y)   No (N)");
                    input = br.readLine();
                    if ((input.equals("Y")) || (input.equals("y"))) {
                        Desktop.getDesktop().open(Path.of(System.getProperty("user.home")).resolve("downloads").resolve("DL-Cleaner").resolve("Trash").toFile());
                        dietrich.logUserChoice("Open folder with log-files?","Yes");
                        break;
                    } else if ((input.equals("N")) || (input.equals("n"))) {
                        dietrich.logUserChoice("Open folder with log-files?","No");
                        break;
                    } else {
                        print("Input not accepted");
                    }
                }
                print("Goodbye :)");
                dietrich.terminate();
            }
            print("All Files moved.");
            print("Logfiles can be found in Users\\yourUser\\DL-Cleaner\\logs");
            print("Goodbye :)");
            dietrich.terminate();
        } catch (Exception e) {
            print(e.getMessage());
            dietrich.updateLogFile("Run failed program collapsed");
        }
    }
}
