package com.medsko.dean.domain;

public class Album {

	private String rawContent;
	
	private String title;
	
	private String recordLabel;
	
	private Integer year;
	
	private String review;
	
	private String rating;
	
	private String artist;
	
	public Album(String rawContent) {
		this.rawContent = rawContent;
	}

	public String getRecordLabel() {
		return recordLabel;
	}

	public void setRecordLabel(String recordLabel) {
		this.recordLabel = recordLabel;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public final String getArtist() {
		return artist;
	}

	public final void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public String toString() {
		return "Album [title=" + title + ", recordLabel=" + recordLabel + ", year=" + year
				+ ", review=" + review + ", rating=" + rating + "]";
	}
}
