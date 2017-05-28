/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public abstract class Media {

	private String ID;
	private String title;
	private String description;
	private String genre;

	public Media(String ID, String title, String description, String genre) {
		super();
		this.ID = ID;
		this.title = title;
		this.description = description;
		this.genre = genre;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public abstract void DisplayItemDetails();
	
	@Override
	public String toString() {
		return "Media [ID=" + ID + ", title=" + title + ", description=" + description + ", genre=" + genre + "]";
	}
}
