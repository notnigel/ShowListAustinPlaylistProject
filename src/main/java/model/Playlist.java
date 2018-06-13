package model;

import model.spotify.Artist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Playlist {
	private Date date;
	private String name;
	private ArrayList<Artist> artists;
	private String description;
	private String id;

	private enum dateFormats {
		yyyyMMMMdd,
		yyyyMMMMd
	}
	
	public Playlist(String searchDate, ArrayList<Artist> artists) throws ParseException {
		this.name = "ShowListAustin : " + convertDateFormat(searchDate);
		this.description = "This playlist contains bands playing on " + searchDate;
		this.artists = artists;
	}
	
	public Playlist(ArrayList<Artist> artists) {
		this.id = "1P3hy2Rt9fi91w823NiuuP";
		this.artists = artists;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*
	 * This function converts a string date into the java.util Date.
	 */
	public String convertDateFormat(String inputDate) throws ParseException {
		
		String formatUsed = "yyyyMMMMdd";
		
		for (dateFormats format : dateFormats.values()) { 
	          SimpleDateFormat sdf1 = new SimpleDateFormat(format.toString());
	            try {
	                sdf1.parse(inputDate);
	                formatUsed = (format.toString());
	            } catch (ParseException e) {
	            	System.out.println(formatUsed);
	            }
		}

		SimpleDateFormat sdf = new SimpleDateFormat(formatUsed);
		Date date = sdf.parse(inputDate);
		sdf = new SimpleDateFormat("MMMM dd, yyyy");
		return sdf.format(date);
	
	}
}
