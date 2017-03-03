package my.gambi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class MyFileVisitor extends SimpleFileVisitor<Path> {

    MyFileVisitor() throws IOException {
    }

    private String inputFile = "input-the-path-to-your-file-with-file-names.java";
    private String pathFileLogHeader = "input-path-to-out-write-logs";


    private List<File> filesToAlter = new ArrayList<>();
    private List<String> listFiles = Files.readAllLines(Paths.get(inputFile));

    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) throws IOException {

        String currentFile = path.getFileName().toString();
        new File(pathFileLogHeader).createNewFile();
        BufferedWriter writerLog = new BufferedWriter(new FileWriter(pathFileLogHeader));

        if(currentFile.endsWith(".java")) {
            if (listFiles.contains(currentFile)) {
                String tempFile = path.toString().concat("_T");

                new File(tempFile).createNewFile();
                FileWriter fw = new FileWriter(tempFile);
                BufferedWriter writer = new BufferedWriter(fw);

                List<String> allLinesFile = Files.readAllLines(path);

                if (allLinesFile.contains("*******************************************************************************/")
                    || allLinesFile.contains(" *******************************************************************************/")) {

                    String content = getContentHeader(currentFile);

                    allLinesFile.forEach(f -> {
                        System.out.println(f);
                        if (f.startsWith("*******************************************************************************/")
                            || f.startsWith(" *******************************************************************************/")) {
                            if (f.length() == 81 || f.length() == 80 || f.length() == 3) {
                                String repLine = f.replace(f, content);
                                try {
                                    writer.write(repLine);
                                    writer.newLine();

                                    writerLog.write("# OK: " + path);
                                    writerLog.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                writer.write(f);
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    writer.close();

                    Files.delete(path);
                    new File(tempFile).renameTo(new File(path.toString()));
                    filesToAlter.remove(currentFile);

                } else {
                    writerLog.write("# ERROR: " + path);
                    writerLog.newLine();
                }

            }
            writerLog.close();
            System.out.println("Nome do arquivo:" + path.getFileName());
        }
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes){
        return FileVisitResult.CONTINUE;
    }

    private static String getContentHeader(String currentFile) {
        return " "
            + "* ************************************************************\n"
            + " * Autor    : brunorocha\n"
            + " * Data     : 23/02/2017\n"
            + " * Empresa  : {your-company}\n"
            + " * Descrição: Classe responsavel por " + currentFile.replaceAll(".java", "") + "\n"
            + " * ID       : {some ID}\n"
            + " *******************************************************************************/";
    }
}
