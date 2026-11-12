package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public class Printer {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public Printer() {

    }
    protected void print(String content) {
        System.out.println(content);
    }
    protected void terminate()throws IOException {
       print("\n\nPress enter to exit.");
       br.readLine();
       System.exit(0);
    }
}
