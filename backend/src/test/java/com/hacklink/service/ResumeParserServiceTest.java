package com.hacklink.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ResumeParserServiceTest {
    @Test
    void extractsKnownSkillsFromPdfText() throws Exception {
        try (PDDocument document = new PDDocument()) {
            document.addPage(new PDPage());
            try (PDPageContentStream stream = new PDPageContentStream(document, document.getPage(0))) {
                stream.beginText();
                stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                stream.newLineAtOffset(40, 700);
                stream.showText("Backend developer with Java, Spring Boot and PostgreSQL experience.");
                stream.endText();
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);

            ResumeParserService.ParsedResume parsed = new ResumeParserService().parse(output.toByteArray());

            assertThat(parsed.skills()).contains("Java", "Spring Boot", "PostgreSQL");
        }
    }
}
