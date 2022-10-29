package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FileUtils {


    public static CompletableFuture<Either<Throwable, List<String>>> readFileAsync(final String path) {

        return CompletableFuture.supplyAsync(() -> readFile(path));
    }

    public static Either<Throwable, List<String>> readFile(final String path) {
        try (
                final FileReader fileReader = new FileReader(path);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {

            final List<String> result = new ArrayList<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                result.add(line);
            }

            return Either.createRight(result);

        } catch (IOException e) {
            return Either.createLeft(new RuntimeException("Failed to read file", e));
        }

    }

}

