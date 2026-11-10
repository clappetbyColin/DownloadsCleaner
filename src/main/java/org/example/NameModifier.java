package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
public class NameModifier extends Printer {
    public NameModifier() {

    }
    public String holeDateiEndungen(String pfad) {

        String endung = "";
        char[] dateiName = pfad.toLowerCase().toCharArray();
        for (int i = dateiName.length - 1; i >= 0; i--) {
            if (dateiName[i] == (char) 46) {
                try {
                    for (int j = i + 1; j < dateiName.length; j++) {
                        endung = endung + dateiName[j];
                    }
                } catch (Exception e) {
                    print(e.getMessage());
                    print(". is not a valid format for a file");
                } finally {
                    break;
                }

            }
        }
        return endung;
    }
    public String setzeZusammen(String dateiName, String endung) {
        char[] neueEndung = endung.toCharArray();
        for (int i = 0; i < neueEndung.length; i++) {
            dateiName = dateiName + neueEndung[i];
        }
        return dateiName;
    }

    public String fuegeZusammen(String dateiName, String endung) {
        char[] neueEndung = endung.toCharArray();
        if (!endung.isEmpty()) {
            dateiName = dateiName + (char) 46;
        }
        for (int i = 0; i < neueEndung.length; i++) {
            dateiName = dateiName + neueEndung[i];
        }
        // dateiName = dateiName + endung would end up in the same answer
        return dateiName;
    }
    public String baueNeuenNamen(String dateiName, int counter) {
        String neuerDateiName;
        try {
            neuerDateiName = setzeZusammen(dateiName, " (" + counter + ")");
        } catch (Exception e) {
            neuerDateiName = dateiName;
            print(e.getMessage());
        }
        return neuerDateiName;
    }

    public String removeEndung(String name) {
        char[] neueEndung = name.toCharArray();
        String temp = "";
        for (int i = neueEndung.length - 1; i >= 0; i--) {
            if (neueEndung[i] == (char) 46) {
                for (int j = 0; j < i; j++) {
                    temp = temp + neueEndung[j];
                }
                return temp;
            }
        }
        return name;
    }
}
