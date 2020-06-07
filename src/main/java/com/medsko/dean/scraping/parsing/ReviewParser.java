package com.medsko.dean.scraping.parsing;

import com.medsko.dean.domain.Album;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ReviewParser {

	private List<Album> discography;
	
	public void parseDiscography(Elements artistParagraphs) {
		discography = new ArrayList<>();
		for (Element artistParagraph : artistParagraphs) {
			Album album = new Album(artistParagraph.text());
			discography.add(album);
			parseTitle(album, artistParagraph);
			parseRelease(album);
			parseReview(album, artistParagraph);
			parseRating(album, artistParagraph);
		}
	}
	
	public void parseRating(Album album, Element artistParagraph) {
		// Get the last <b> element.
		Element lastBold = artistParagraph.select("b").last();
		String ratingText = lastBold.text();
		if (ratingText != null && !ratingText.isEmpty()) {
			album.setRating(ratingText);
		} else {
			Element image = lastBold.select("img").first();
			if (image == null) return;
			String imageRating = image.attr("alt");
			album.setRating(imageRating);
		}
	}
	
	public void parseReview(Album album, Element artistParagraph) {
		// Iterate through the elements until the first line break is reached.
		List<Node> children = artistParagraph.childNodes();
		int reviewIndex = findFirstLineBreak(children);
		
		String review = "";
		while (reviewIndex > 0 && reviewIndex < children.size()) {
			Node reviewNode = children.get(reviewIndex);
			if ("b".equals(reviewNode.nodeName()) && reviewIndex + 1 == children.size()) {
				// The last bold tag is the rating, so we have reached the end of the review.
				break;
			}
			review += reviewNode.toString();
			reviewIndex++;
		}
		// Set the review - or an empty string, in case of rating only - on the album.
		album.setReview(review.trim());
	}
	
	private int findFirstLineBreak(List<Node> children) {
		int index = 0;
		boolean found = false;
		Node firstLineBreak = null;
		while (index < children.size()) {
			firstLineBreak = children.get(index);
			if ("br".equals(firstLineBreak.nodeName())) {
				found = true;
				break;
			}
			index++;
		}
		return found ? index : -1;
	}
	
	public void parseRelease(Album album) {
		String rawContent = album.getRawContent();
		int from = rawContent.indexOf("[");
		int to = rawContent.indexOf("]");
		if (from < 0 || to < 0) return;
		String release = rawContent.substring(from + 1, to);
		String[] labelAndYear = release.split(",");
		if (labelAndYear.length < 2) return;
		album.setRecordLabel(labelAndYear[0]);
		String yearString = labelAndYear[1];
		album.setYear(Integer.parseInt(yearString.trim()));
	}
	
	public void parseTitle(Album album, Element artistParagraph) {
		Element bold = artistParagraph.select("b").first();
		if (bold == null) return;
		Element cursive = bold.select("i").first();
		if (cursive == null) return;
		album.setTitle(cursive.text());
	}
	
	/**
	 * Tries to determine the artist from the given album paragraph. Only call this for a paragraph
	 * element that was found on a get_album page!
	 * @param album - The data object to enrich.
	 * @param albumParagraph - The 
	 */
	public void parseArtist(Album album, Element albumParagraph) {
		Element bold = albumParagraph.select("b").first();
		if (bold == null) return;
		Element link = bold.select("a").first();
		if (link == null) return;
		album.setArtist(link.text());
	}

	public final List<Album> getDiscography() {
		return discography;
	}
}
