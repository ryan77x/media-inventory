/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public class Book extends Media{

		private String author;
		private String ISBN;

	public Book(String ID, String title, String description, String genre, String author, String ISBN) {
		super(ID, title, description, genre);
		this.author = author;
		this.ISBN = ISBN;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public void DisplayItemDetails(){
		System.out.println("ID: " + super.getID() + "\nTitle: " + super.getTitle() + "\nAuthor(s): " + author + "\nISBN: " + ISBN + "\nGenre: " + super.getGenre() + "\nDescription: " + super.getDescription());
	}

	@Override
	public String toString() {
		return "Book [author=" + author + ", ISBN=" + ISBN + "]";
	}
	
}
