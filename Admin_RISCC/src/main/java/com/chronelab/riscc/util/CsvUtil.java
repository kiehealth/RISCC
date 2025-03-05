package com.chronelab.riscc.util;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class CsvUtil {
    private static final String TEMP_DIRECTORY = System.getProperty("catalina.home") + File.separator + "tmp_file";

    public String createCsv(String fileNameInitial, List<String> heading, List<List<String>> data) throws IOException {
        Path path = Paths.get(TEMP_DIRECTORY);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        //File file = new File(TEMP_DIRECTORY, "app_analytics_report_" + (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000) + ".csv");
        File file = new File(TEMP_DIRECTORY, fileNameInitial + "_" + (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000) + ".csv");

        CSVWriter csvWriter = new CSVWriter(new FileWriter(file));

        csvWriter.writeNext(heading.toArray(new String[0]));

        for (List<String> rowData : data) {
            csvWriter.writeNext(rowData.toArray(new String[0]));
        }

        csvWriter.close();
        return file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("tmp_file") - 1);
        //return file.getAbsolutePath();
    }
}
