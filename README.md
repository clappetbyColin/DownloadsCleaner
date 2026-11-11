Download-Cleaner

A small command-line tool for Windows that tidies up a folder by sorting files into folders based on their type.

By default, it cleans up your Downloads folder. If you specify a different folder when starting the program, that folder is cleaned up instead.

Features
Sorts files into Documents, Pictures, Videos, Archives, Installers, Music and Junk
Shows a preview of every move and asks for confirmation before anything happens
Dry-run mode to see what would happen without moving a single file
Never overwrites anything: if a file name already exists at the destination, it becomes file (1).ext, file (2).ext, …
Leaves unfinished downloads and hidden files alone
A short intro guide for first-time users
Download

Grab the latest .exe from the Releases page. No installation and no Java required.

Windows warning: The .exe is not digitally signed, so Windows may show a SmartScreen warning ("Windows protected your PC"). Click More info → Run anyway. If Smart App Control is enabled on your PC, the .exe will be blocked entirely. In that case, use the .jar from the release instead (requires Java 22 or newer):

java -jar download-cleaner.jar
Usage

Which folder gets cleaned up?

No folder specified (e.g. when started by double-click): your Downloads folder
Folder specified on the command line: that folder
Double-click

Just start download-cleaner.exe. The tool will:

Ask whether you want to see the intro guide
Clean up your Downloads folder
Offer to enable dry-run (if you chose the intro guide)
Show a preview of all planned moves and ask for confirmation
Command line
download-cleaner.exe [folder] [--dry-run]
Argument	Description
folder	The folder to clean up. Defaults to your Downloads folder.
--dry-run	Only show where files would be moved. Nothing is changed.

The order of the arguments doesn't matter. Put paths containing spaces in quotes.

Examples:

download-cleaner.exe
download-cleaner.exe --dry-run
download-cleaner.exe "D:\My Files" --dry-run

If the given folder doesn't exist, Download-Cleaner falls back to your Downloads folder. If the path points to a file instead of a folder, the program stops.

Where do my files go?

<user> stands for your user folder (e.g. C:\Users\YourName), <drive> for the drive your user folder is on (usually C:\).

Category	Extensions	Destination
Documents	pdf, doc, docx, odt, rtf, txt, md, xls, xlsx, ods, csv, ppt, pptx, odp, epub, mobi, pages, numbers, key, tex	<user>\Documents\DL-Cleaner
Pictures	jpg, jpeg, png, gif, bmp, webp, svg, tif, tiff, ico, heic, heif, avif, raw, cr2, cr3, nef, arw, dng, psd, xcf	<user>\Pictures\DL-Cleaner
Videos	mp4, mkv, avi, mov, wmv, flv, webm, m4v, mpg, mpeg, 3gp, ts, m2ts, vob, ogv, mp3, wav	<user>\Videos\DL-Cleaner
Archives	zip, rar, 7z, tar, gz, tgz, bz2, xz, zst, cab, iso, img, dmg	<drive>\Archive\DL-Cleaner
Installers	exe, msi, msix, appx, appxbundle	<drive>\Installer\DL-Cleaner
Junk	everything else	<user>\Downloads\Junk\DL-Cleaner

Extensions are matched case-insensitively, so PHOTO.JPG counts as a picture.

What is never touched
Unfinished downloads: .crdownload, .part, .partial, .download, .tmp, .opdownload
Hidden files, such as desktop.ini
Subfolders — only files directly inside the chosen folder are sorted
Notes
Files are really moved. If you're unsure, run with --dry-run first.
If a single file can't be moved (e.g. because it's open in another program), it is reported and skipped; all other files are still processed.
File extensions are normalized to lowercase when moved (PHOTO.JPG → PHOTO.jpg).
OneDrive: If your Documents or Pictures folder is synced with OneDrive, Windows may store it in a different location (e.g. C:\Users\YourName\OneDrive\Documents). Download-Cleaner always uses the folders inside your user folder, so check where your files end up after the first run.

With patch 1.3.6 the program is now able to detect redundant files in your folder and move or delete them.
On start the program will ask you if you want safeRun activated or not. 
With safeRun disabled the program will delete all redundant files from the folder it's running in (by default your Downloads-folder) it will ask list all redundant files and then ask you if you want to delete them or not.
When safeRun is enabled you also will be shown a list with redundant files but 
the program will not delete them. It will move all files into Downloads//DL-Cleaner//Trash and after that you can continue with the origin files moving function. after moving the redundant files you will also be asked if you want to open the folder where the redundant files where moved.
All this will be explained while the program during runtime if you choose not to skip the intro at the beginning.

Parameters:

--dry-run
--help
"A folderpath"

Building from source

Requirements:

JDK 22 or newer
GraalVM and the Visual Studio Build Tools (only for building the .exe)

Build the .jar:

gradlew jar

The result is in build\libs. To build the native .exe:

cmd:
"curl -L -O https://github.com/clappetbyColin/DownloadsCleaner/releases/latest/download/DL-Cleaner1.1.exe"


native-image -jar build\libs\<jar-name>.jar download-cleaner
License

This project is licensed under the MIT License. See LICENSE for details.
