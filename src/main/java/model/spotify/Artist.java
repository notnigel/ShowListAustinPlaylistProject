package model.spotify;

public class Artist {
	private String name;
	private String id;
	private String trackUris;
	
	public Artist (){
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrackUris() {
		return trackUris;
	}
	public void setTrackUris(String trackUris) {
		this.trackUris = trackUris;
	}
}
