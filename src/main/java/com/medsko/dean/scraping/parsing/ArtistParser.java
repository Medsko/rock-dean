package com.medsko.dean.scraping.parsing;

import com.medsko.dean.domain.Artist;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Parses artist data objects from document elements.
 * 
 * @author Medsko
 */
public class ArtistParser {
	
	// The artist under construction.
	private Artist artist;
	
	public ArtistParser() {}
	
	public ArtistParser(Artist artistToEnrich) {
		artist = artistToEnrich;
	}
	
	public void buildArtist(Element artistListItem, String firstChar) {
		Element linkInListItem = artistListItem.select("a").first();
		String overviewUrl = linkInListItem.attr("href");
		String fullName = linkInListItem.text();
		artist = new Artist(fullName, firstChar, overviewUrl); 
	}
	
	public void withDiscography(Elements artistParagraphs) {
		ReviewParser parser = new ReviewParser();
		parser.parseDiscography(artistParagraphs);
		artist.getAlbums().addAll(parser.getDiscography());
	}
	
	public Artist getArtist() {
		return artist;
	}
}
