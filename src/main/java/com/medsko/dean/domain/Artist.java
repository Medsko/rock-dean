package com.medsko.dean.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object representing an artist on the deans web site. 
 */
public class Artist {

	private String fullName;
	
	/** The first character of this artists name. */
	private String firstChar;
	
	/** The URL where the overview of the reviews for this artist can be found. */
	private String overviewUrl;
	
	private List<Album> albums;
	
	public Artist(String fullName, String firstChar, String overviewUrl) {
		this.fullName = fullName;
		this.firstChar = firstChar;
		this.overviewUrl = overviewUrl;
		albums = new ArrayList<>();
	}

	public String getFullName() {
		return fullName;
	}

	public String getFirstChar() {
		return firstChar;
	}

	public String getOverviewUrl() {
		return overviewUrl;
	}
	
	public List<Album> getAlbums() {
		return albums;
	}

	@Override
	public String toString() {
		String toString = "Overview of reviews for artist " + fullName + " can be found at: ";
		return toString + overviewUrl;
	}
}
