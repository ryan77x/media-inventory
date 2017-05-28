/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public class CD extends Media{
	
	private String artist;

	public CD(String ID, String title, String description, String genre, String artist) {
		super(ID, title, description, genre);
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void DisplayItemDetails(){
		System.out.println("ID: " + super.getID() + "\nTitle: " + super.getTitle()  + "\nArtist(s): " + artist + "\nGenre: " + super.getGenre() + "\nDescription: " + super.getDescription());
	}

	@Override
	public String toString() {
		return "CD [artist=" + artist + "]";
	}
	
}
