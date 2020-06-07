package com.medsko.dean.scraping;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

public class DeanUtil {

	public final static String BASE_URL = "https://www.robertchristgau.com";
	
	public final static String ARTIST_URL = "https://www.robertchristgau.com/get_alist.php?k="; 
	
	private DeanUtil() {}
	
	/**
	 * The good stuff is usually in the second cell of the second row of the table that makes up
	 * the main body.
	 * @param document - The raw web page document.
	 * @return the element containing 'the right stuff' (as popularized by the New kids on the block).
	 */
	public static Optional<Element> getJuicyContent(Element document) {
		Elements rows = document.select("body table tbody tr");
		if (rows.size() < 2) return Optional.empty();
		Element juicyRow = rows.get(1);
		Elements cells = juicyRow.select("td[valign]");
		if (cells.size() > 2) return Optional.empty();
		return Optional.ofNullable(cells.get(1));
	}
	
}
