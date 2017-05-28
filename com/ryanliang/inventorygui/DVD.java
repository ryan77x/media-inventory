/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public class DVD extends Media{
	
	private String cast;
	
	public DVD(String ID, String title, String description, String genre, String cast) {
		super(ID, title, description, genre);
		this.cast = cast;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}

	public void DisplayItemDetails(){
		System.out.println("ID: " + super.getID() + "\nTitle: " + super.getTitle() + "\nCast(s): " + cast + "\nGenre: " + super.getGenre() + "\nDescription: " + super.getDescription());
	}
	@Override
	public String toString() {
		return "DVD [cast=" + cast + "]";
	}
	
}
