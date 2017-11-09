package model;

import model.spotify.Artist;

import java.util.ArrayList;
import java.util.Date;

public class Playlist {
	private Date date;
	private String name;
	private ArrayList<Artist> artists;
	private String description;
	
	public Playlist(String searchDate){
		this.name = "ShowListAustin : " + searchDate;
		this.artists = new ArrayList<>();
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
}
