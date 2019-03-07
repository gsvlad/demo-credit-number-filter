package demo.number.card;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("I need 2 params: input directory, output directory");
            return;
        }
        String inputDirName = args[0];
        String outputDirName = args[1];

        if (inputDirName.equals(outputDirName)) {
            System.out.println("Input & Output directory should not be the same");
            return;
        }

        File inputDir = new File(inputDirName);
        File outputDir = new File(outputDirName);

        if (!inputDir.isDirectory() || !outputDir.isDirectory()) {
            System.out.println("Both params should be valid directories");
            return;
        }

        processFilesInParallel(inputDir.listFiles(), outputDirName);
    }

    private static void processFilesInParallel(File[] inputFiles, String outputDirectory) {
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        Stream.of(inputFiles)
                .forEach(it -> processFile(it, outputDirectory, executorService));

        executorService.shutdown();
    }

    private static void processFile(File f, String outputDirectory, ExecutorService executorService) {
        executorService.submit(() -> {
            String inputFileName = f.getName();
            System.out.println("Starting processing: " + inputFileName);

            String outputFileName = outputDirectory + "/" + inputFileName;

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
                Files.lines(f.toPath())
                        .filter(LineValidator::hasCardNumber)
                        .forEach(line -> writeLine(bufferedWriter, line));

            } catch (IOException e) {
                e.printStackTrace();
            }

            deleteIfEmpty(outputFileName);
            System.out.println("Finished processing: " + inputFileName);
        });
    }

    private static void deleteIfEmpty(String fileName) {
        File outputFile = new File(fileName);
        if (outputFile.length() == 0) {
            System.out.println("Created file '" + fileName + "' is empty and will be removed");
            outputFile.deleteOnExit();
        }
    }

    private static void writeLine(BufferedWriter bufferedWriter, String line) {
        try {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }
}
