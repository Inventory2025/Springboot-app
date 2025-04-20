package com.ims.inventory.helpers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CsvExportHelper {

    public void exportCsv(List<Map<String, Object>> data,
                          HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=data.csv");

        PrintWriter writer = response.getWriter();

        // Handle empty data
        if (data == null || data.isEmpty()) {
            writer.write("No data found");
            writer.flush();
            return;
        }

        // Write header row (keys from the first row)
        Set<String> headers = data.get(0).keySet();
        writer.println(String.join(",", headers));

        // Write data rows
        for (Map<String, Object> row : data) {
            List<String> values = headers.stream()
                    .map(key -> Optional.ofNullable(row.get(key)).orElse("").toString())
                    .map(value -> "\"" + value.replace("\"", "\"\"") + "\"") // handle commas and quotes
                    .toList();

            writer.println(String.join(",", values));
        }

        writer.flush();
    }

}
