package com.contractmanagementsystem.utils;

import com.contractmanagementsystem.exception.TextExtractionException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class TextExtractionUtil {

    public String extractText(String filePath) throws Exception {

        File file =
                new File(filePath);

        if (!file.exists()) {

            throw new TextExtractionException(
                    "File not found for text extraction"
            );
        }

        Tika tika =
                new Tika();

        String extractedText =
                tika.parseToString(file);

        if (extractedText == null
                || extractedText.isBlank()) {

            throw new TextExtractionException(
                    "Could not extract text"
            );
        }

        return extractedText;
    }
}
