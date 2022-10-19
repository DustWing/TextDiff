package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static Either<RuntimeException, List<String>> readFile(final String path) {
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

