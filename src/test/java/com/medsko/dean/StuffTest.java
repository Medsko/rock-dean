package com.medsko.dean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class StuffTest {

	@Test
	void extractLinks() throws IOException {

		String inputFile = "C:/users/Medsko/Documents/bookmarks_6_7_20.html";
		Document document = Jsoup.parse(new File(inputFile), StandardCharsets.UTF_8.name());
		Elements links = document.getElementsByTag("a");

		String outputFile = "C:/users/Medsko/Documents/bookmarked.txt";

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile), StandardCharsets.UTF_8,
				StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {

			for (Element link : links ) {
				writer.write(link.attr("href"));
				writer.newLine();
			}
		}

	}
}
