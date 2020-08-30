package de.peter.tool.filesplitter;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Datei einlesen und zu Dateien mit je 5.000 Zeilen stueckeln
// C:\DEV\OpenJDK\jdk-14.0.2\bin\java.exe
// C:\Program Files\JetBrains\IntelliJ IDEA Community Edition

/*
Git is not installed:
Download:
https://github.com/git-for-windows/git/releases/download/v2.28.0.windows.1/Git-2.28.0-64-bit.exe
IntelliJ IDEA Github Integration Plugin access token
Pfad zu Git.exe unter File - Settings... angegeben



 */

public class FileSplitter {

    private static final DateTimeFormatter FORMATER_FOR_SPLIT_FILE = DateTimeFormatter.ofPattern("YYYYMMdd_HHmmssSSS");

    private static final long BUFFER_IN_LINES = 5000;

    public static void main(String[] args) {
        // TODO File file = handleInput();
        File file = new File("C:\\Users\\vorname.nachname\\Desktop\\E01E025.sql");

        BufferedReader bufferedReader =null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            long remainingLines = bufferedReader.lines().count();

            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = null;
            while (remainingLines > 0) {
                System.out.println("Remaining lines in file: " + remainingLines);
                long nextTarget = remainingLines - BUFFER_IN_LINES;
                if (BUFFER_IN_LINES > remainingLines) {
                    nextTarget = 0;
                }
                stringBuilder = new StringBuilder();
                String tempLine = null;
                do {
                    tempLine = bufferedReader.readLine();
                    remainingLines--;
                    if (!tempLine.isEmpty()) {
                        stringBuilder.append(tempLine);
                        stringBuilder.append("\r\n");
                    }
                } while(remainingLines != nextTarget);
                writeInSeparateFile(file, stringBuilder.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File handleInput() {
        File file = null;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            file = new File(scanner.next());
            if (file.exists()) {
                System.out.println("Pfad mit Dateinamen uebergeben (e.g. C:\\Users\\vorname.nachname\\Desktop\\E01E025.sql): "
                        + file);
            } else {
                file = new File("C:\\Users\\vorname.nachname\\Desktop\\E01E025.sql");
                System.out.println("Keine Eingabe. Folgender Default wird genommen: "
                        + file.getAbsolutePath());
            }
        } else {
            file = new File("C:\\Users\\vorname.nachname\\Desktop\\E01E025.sql");
            System.out.println("Keine Eingabe. Folgender Default wird genommen: "
                    + file.getAbsolutePath());
        }
        return file;
    }

    private static void writeInSeparateFile(File file, String content) {
        if (content.isEmpty()) {
            System.out.println("No content for separate file.");
            return;
        }
        BufferedWriter bufferedWriter = null;
        try  {
            File splitFile = null;
            do {
                splitFile =new File(file.getParent(),
                        file.getName().substring(0,
                                file.getName().length() - file.getName().lastIndexOf('.')) + "_"
                                + FORMATER_FOR_SPLIT_FILE.format(LocalDateTime.now()) + ".sql");
            } while (splitFile.exists());
            bufferedWriter = new BufferedWriter(new FileWriter(splitFile));
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Beispiel mit UTF-8
    public void parseConfig(URL resource) {
        File file = new File(resource.getFile());
        StringBuilder sb = new StringBuilder();
        String line = null;

        // try-with-resources
        try (
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            //
        } finally {
            System.out.println(sb.toString());
        }
    }

}
