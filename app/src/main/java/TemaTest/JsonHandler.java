package TemaTest;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonHandler {
    // Convert string to arraylist
    public static <T> ArrayList<T> jsonToArrayList(String filePath, Class<T[]> clazz) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = content.toString();

        if (!jsonString.isEmpty()) {
            Gson gson = new Gson();
            T[] array = gson.fromJson(jsonString, clazz);
            return new ArrayList<>(Arrays.asList(array));
        } else {
            return new ArrayList<>(); // Failed to read JSON file
        }
    }
    // Deserialize class as json
    public static <T> String writeClassAsJson(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    // Write json string to file
    public static void writeToFile(String text, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
