# Download-Cleaner

A small command-line tool for Windows that tidies up a folder by sorting files into folders based on their type — and finds duplicate files along the way.

By default, it cleans up your **Downloads** folder. If you specify a different folder when starting the program, that folder is cleaned up instead.

## Features

- Sorts files into **Documents**, **Pictures**, **Videos**, **Music**, **Archives**, **Installers** and **Junk**
- **Finds duplicate files** by their content and moves them to a separate folder or deletes them
- Shows a **preview** of every move and asks for confirmation before anything happens
- **Dry-run mode** to see where files would go without moving a single file
- Never overwrites anything: if a file name already exists at the destination, it becomes `file (1).ext`, `file (2).ext`, …
- Leaves **unfinished downloads** and **hidden files** alone
- A short **intro guide** for first-time users

## Download

Grab the latest `DL-Cleaner.exe` from the [Releases](../../releases/latest) page. No installation and no Java required.

Or download it from the command line:

```
curl -L -O https://github.com/clappetbyColin/DownloadsCleaner/releases/latest/download/DL-Cleaner.exe
```

The file is saved in the folder your command prompt is currently in.

> **Windows warning:** The `.exe` is not digitally signed, so Windows may show a SmartScreen warning ("Windows protected your PC"). Click **More info → Run anyway**.
>
> If *Smart App Control* is enabled on your PC, the `.exe` will be blocked entirely. In that case, use `DL-Cleaner.jar` from the release instead. It requires **Java 25 or newer**:
> ```
> java -jar DL-Cleaner.jar
> ```

## Usage

### Double-click

Just start `DL-Cleaner.exe`. The tool will walk you through these steps:

1. Ask whether you want to enable **safeRun** (see [Duplicates](#duplicates))
2. Ask whether you want to skip the **intro guide**
3. Search your **Downloads** folder for **duplicates** and ask what to do with them
4. Offer to enable **dry-run** (only if you didn't skip the intro guide)
5. Show a **preview** of all planned moves and ask for confirmation
6. Sort the files

### Command line

```
DL-Cleaner.exe [folder] [--dry-run]
DL-Cleaner.exe --help
```

| Argument | Description |
|---|---|
| `folder` | The folder to clean up. Defaults to your Downloads folder. |
| `--dry-run` | Only show where files *would* be moved. No files are sorted. **Does not affect duplicates**, see below. |
| `--help` | Shows an overview of all options. |

The order of the arguments doesn't matter. Put paths containing spaces in quotes.

**Examples:**

```
DL-Cleaner.exe
DL-Cleaner.exe --dry-run
DL-Cleaner.exe "D:\My Files" --dry-run
```

**Which folder gets cleaned up?**

| You start the program … | Cleaned up folder |
|---|---|
| without a folder (e.g. by double-click) | your Downloads folder |
| with a folder that exists | that folder |
| with a folder that doesn't exist | your Downloads folder |
| with the path to a file instead of a folder | nothing — the program stops |

## Where do my files go?

`<user>` stands for your user folder (e.g. `C:\Users\YourName`), `<drive>` for the drive your user folder is on (usually `C:\`).

| Category | Extensions | Destination |
|---|---|---|
| Documents | pdf, doc, docx, odt, rtf, txt, md, xls, xlsx, ods, csv, ppt, pptx, odp, epub, mobi, pages, numbers, key, tex | `<user>\Documents\DL-Cleaner` |
| Pictures | jpg, jpeg, png, gif, bmp, webp, svg, tif, tiff, ico, heic, heif, avif, raw, cr2, cr3, nef, arw, dng, psd, xcf | `<user>\Pictures\DL-Cleaner` |
| Videos | mp4, mkv, avi, mov, wmv, flv, webm, m4v, mpg, mpeg, 3gp, ts, m2ts, vob, ogv | `<user>\Videos\DL-Cleaner` |
| Music | mp3, wav, flac, aac, m4a, ogg, opus, wma, aiff, aif, alac, ape, wv, dsf, dff, mka, amr, mid, midi | `<user>\Music` |
| Archives | zip, rar, 7z, tar, gz, tgz, bz2, xz, zst, cab, iso, img, dmg | `<drive>\Archive\DL-Cleaner` |
| Installers | exe, msi, msix, appx, appxbundle | `<drive>\Installer\DL-Cleaner` |
| Junk | everything else | `<user>\Downloads\Junk\DL-Cleaner` |

Extensions are matched case-insensitively, so `PHOTO.JPG` counts as a picture.

## Duplicates

Before sorting, Download-Cleaner looks for files with **identical content** — no matter what they are called. `photo.jpg` and `photo - Copy.jpg` are detected as duplicates if they contain exactly the same data.

**How it works:** Files are first compared by size. Only files of the same size are then compared by their content (using a SHA-256 hash). So two files of the same size but with different content are never mistaken for duplicates.

**Which file is kept?** Of every group of identical files, the one with the **shortest file name** is kept, because that's usually the original (`photo.jpg` instead of `photo (1).jpg` or `photo - Copy.jpg`). All others are treated as duplicates.

**What happens to the duplicates?** That depends on whether you enabled **safeRun** at the start:

| | safeRun **enabled** | safeRun **disabled** |
|---|---|---|
| Duplicates are … | **moved** to `<user>\Downloads\DL-Cleaner\Trash` | **permanently deleted** |
| Can be undone? | Yes — just move them back | **No.** They do **not** go to the Recycle Bin. |
| Asks before doing it? | Yes | Yes |

> ⚠️ **Important:** `--dry-run` does **not** apply to duplicates. Even in dry-run mode, duplicates are moved or deleted once you confirm. If you are unsure, enable **safeRun**.

The `Trash` folder is always inside your Downloads folder, even if you clean up a different folder.

**Note:** Empty files (0 bytes) all have the same content, so they count as duplicates of each other.

## What is never touched

- **Unfinished downloads:** `.crdownload`, `.part`, `.partial`, `.download`, `.tmp`, `.opdownload`
- **Hidden files**, such as `desktop.ini`
- **Subfolders** — only files directly inside the chosen folder are sorted

## Notes

- Files are **really moved**. If you're unsure, run with `--dry-run` first.
- **Administrator rights** may be needed, because the Archive and Installer folders are created directly on your drive (e.g. `C:\Archive`). If moving fails with an "access denied" error, start the program as administrator.
- If a single file can't be moved (e.g. because it's open in another program), it is reported and skipped; all other files are still processed.
- File extensions are normalized to lowercase when moved (`PHOTO.JPG` → `PHOTO.jpg`).
- **OneDrive:** If your Documents or Pictures folder is synced with OneDrive, Windows may store it in a different location (e.g. `C:\Users\YourName\OneDrive\Documents`). Download-Cleaner always uses the folders inside your user folder, so check where your files end up after the first run.

## Building from source

**Requirements:**

- JDK 25 or newer
- [GraalVM](https://www.graalvm.org/) and the Visual Studio Build Tools (only for building the `.exe`)

**Build the `.jar`:**

```
gradlew jar
```

The result is in `build\libs`.

**Build the native `.exe`:**

```
native-image -jar build\libs\DownloadsCleaner-1.0-SNAPSHOT.jar DL-Cleaner
```
## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
