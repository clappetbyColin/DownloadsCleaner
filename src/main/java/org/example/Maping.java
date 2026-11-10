package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
public class Maping {
    public Maping() {

    }
    public void baueRefmap(java.util.Map<String, String> regeln) {
        //Music
        regeln.put("mp3", "Musik");
        regeln.put("wav", "Musik");
        regeln.put("flac", "Musik");
        regeln.put("aac", "Musik");
        regeln.put("m4a", "Musik");
        regeln.put("ogg", "Musik");
        regeln.put("opus", "Musik");
        regeln.put("wma", "Musik");
        regeln.put("aiff", "Musik");
        regeln.put("aif", "Musik");
        regeln.put("alac", "Musik");
        regeln.put("ape", "Musik");
        regeln.put("wv", "Musik");
        regeln.put("dsf", "Musik");
        regeln.put("dff", "Musik");
        regeln.put("mka", "Musik");
        regeln.put("amr", "Musik");
        regeln.put("mid", "Musik");
        regeln.put("midi", "Musik");
// Dokumente
        regeln.put("pdf", "Dokumente");
        regeln.put("doc", "Dokumente");
        regeln.put("docx", "Dokumente");
        regeln.put("odt", "Dokumente");
        regeln.put("rtf", "Dokumente");
        regeln.put("txt", "Dokumente");
        regeln.put("md", "Dokumente");
        regeln.put("xls", "Dokumente");
        regeln.put("xlsx", "Dokumente");
        regeln.put("ods", "Dokumente");
        regeln.put("csv", "Dokumente");
        regeln.put("ppt", "Dokumente");
        regeln.put("pptx", "Dokumente");
        regeln.put("odp", "Dokumente");
        regeln.put("epub", "Dokumente");
        regeln.put("mobi", "Dokumente");
        regeln.put("pages", "Dokumente");
        regeln.put("numbers", "Dokumente");
        regeln.put("key", "Dokumente");
        regeln.put("tex", "Dokumente");

// Bilder
        regeln.put("jpg", "Bilder");
        regeln.put("jpeg", "Bilder");
        regeln.put("png", "Bilder");
        regeln.put("gif", "Bilder");
        regeln.put("bmp", "Bilder");
        regeln.put("webp", "Bilder");
        regeln.put("svg", "Bilder");
        regeln.put("tif", "Bilder");
        regeln.put("tiff", "Bilder");
        regeln.put("ico", "Bilder");
        regeln.put("heic", "Bilder");
        regeln.put("heif", "Bilder");
        regeln.put("avif", "Bilder");
        regeln.put("raw", "Bilder");
        regeln.put("cr2", "Bilder");
        regeln.put("cr3", "Bilder");
        regeln.put("nef", "Bilder");
        regeln.put("arw", "Bilder");
        regeln.put("dng", "Bilder");
        regeln.put("psd", "Bilder");
        regeln.put("xcf", "Bilder");

// Archive
        regeln.put("zip", "Archive");
        regeln.put("rar", "Archive");
        regeln.put("7z", "Archive");
        regeln.put("tar", "Archive");
        regeln.put("gz", "Archive");
        regeln.put("tgz", "Archive");
        regeln.put("bz2", "Archive");
        regeln.put("xz", "Archive");
        regeln.put("zst", "Archive");
        regeln.put("cab", "Archive");
        regeln.put("iso", "Archive");
        regeln.put("img", "Archive");
        regeln.put("dmg", "Archive");

// Videos
        regeln.put("mp4", "Videos");
        regeln.put("mkv", "Videos");
        regeln.put("avi", "Videos");
        regeln.put("mov", "Videos");
        regeln.put("wmv", "Videos");
        regeln.put("flv", "Videos");
        regeln.put("webm", "Videos");
        regeln.put("m4v", "Videos");
        regeln.put("mpg", "Videos");
        regeln.put("mpeg", "Videos");
        regeln.put("3gp", "Videos");
        regeln.put("ts", "Videos");
        regeln.put("m2ts", "Videos");
        regeln.put("vob", "Videos");
        regeln.put("ogv", "Videos");

//Unfertig
        regeln.put("crdownload", "DontTouch");
        regeln.put("part", "DontTouch");
        regeln.put("partial", "DontTouch");
        regeln.put("download", "DontTouch");
        regeln.put("tmp", "DontTouch");
        regeln.put("opdownload", "DontTouch");
//Programme
        regeln.put("exe", "Installer");
        regeln.put("msi", "Installer");
        regeln.put("msix", "Installer");
        regeln.put("appx", "Installer");
        regeln.put("appxbundle", "Installer");
    }
}
