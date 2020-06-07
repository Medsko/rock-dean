package com.medsko.dean.scraping;

import com.medsko.dean.domain.Album;
import com.medsko.dean.domain.Artist;
import com.medsko.dean.scraping.parsing.ArtistParser;
import com.medsko.dean.scraping.parsing.ReviewParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
public class Probe {

	private PlausibleRequestHelper requestHelper;
	
	private WebSiteRequestConscience conscience;
	
	private List<Artist> scrapedArtists;
	
	public Probe() {
		requestHelper = new PlausibleRequestHelper();
		conscience = new WebSiteRequestConscience(DeanUtil.BASE_URL);
	}
	
	public boolean fetchArtists(String firstChar) {
		if (!Pattern.matches("[A-Za-z]", firstChar))
			throw new IllegalArgumentException("Input should be one letter!");
		
		scrapedArtists = new ArrayList<>();
		String url = DeanUtil.ARTIST_URL + firstChar;
		
		if (!requestHelper.sendPageRequest(url) || !requestHelper.getCanParseResponse()) {
			return false;
		}
		Response response = requestHelper.getResponse();
		
		try {
			Document document = response.parse();
			
			Elements listItems = document.select("li");
			
			for (Element listItem : listItems) {
				Artist artist = buildArtist(listItem, firstChar);
				scrapedArtists.add(artist);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Enriches the given Artist by fetching the discography of that artist, parsing it and 
	 * populating the list of albums.
	 * @param artist - The artist to enrich.
	 * @return {@code true} if successful.
	 */
	public boolean fetchDiscography(Artist artist) {
		String url = DeanUtil.BASE_URL + artist.getOverviewUrl();
		Optional<Document> maybeDoc = fetchDocument(url);
		if (!maybeDoc.isPresent()) {
			return false;
		}
		
		Document rawDiscography = maybeDoc.get();
		Element secondTableRow = rawDiscography.select("tr").get(1);
		Elements paragraphs = secondTableRow.select("p");
		
		ArtistParser builder = new ArtistParser(artist);
		builder.withDiscography(paragraphs);
		
		return true;
	}
	
	private Optional<Document> fetchDocument(String url) {
		while (!conscience.isRightTime()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Execute a request for the given URL and make sure the response can be parsed.
		if (!requestHelper.sendPageRequest(url) || !requestHelper.getCanParseResponse()) {
			return Optional.empty();
		}
		
		Response response = requestHelper.getResponse();
		
		try {
			Document document = response.parse();
			return Optional.of(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	private Artist buildArtist(Element artistListItem, String firstChar) {
		Element linkInListItem = artistListItem.select("a").first();
		String overviewUrl = linkInListItem.attr("href");
		String fullName = linkInListItem.text();
		
		return new Artist(fullName, firstChar, overviewUrl);
	}
	
	public List<Artist> getScrapedArtists() {
		return scrapedArtists;
	}

	private static void fetchAlbum(Optional<Document> maybeDocument) {
		if (!maybeDocument.isPresent()) {
			System.out.println("No luck!");
			return;
		}
		
		Optional<Element> maybeContent = DeanUtil.getJuicyContent(maybeDocument.get());
		if (!maybeContent.isPresent()) {
			System.out.println("No luck! Content could not be determined.");
			return;
		}
		
		Element content = maybeContent.get();
		Element albumParagraph = content.select("p").first();
		
		ReviewParser parser = new ReviewParser();
		Album album = new Album(albumParagraph.text());
		
		parser.parseArtist(album, albumParagraph);
		parser.parseRating(album, albumParagraph);
		parser.parseRelease(album);
		parser.parseReview(album, albumParagraph);
		parser.parseTitle(album, albumParagraph);
		System.out.println(album);
	}

	public static void main(String[] args) {
		
		Probe probe = new Probe();
//		if (!probe.fetchArtists("B")) {
//			System.out.println("Failed to fetch the document!");
//		}
//		
//		for (Artist artist : probe.getScrapedArtists()) {
//			System.out.println(artist);
//		}
//		
		Artist test = new Artist("Bad Brains", "B", "/get_artist.php?id=99");
		probe.fetchDiscography(test);
		
		for (Album album : test.getAlbums()) {
			System.out.println("Album data scraped: " + album);
			System.out.println("From raw content: " + album.getRawContent());
			System.out.println();
		}
//		String url = BASE_URL + "/get_album.php?id=3911";
//		Optional<Document> maybeDocument = probe.fetchDocument(url);
//		fetchAlbum(maybeDocument);
	}
}
