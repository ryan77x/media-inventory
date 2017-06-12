/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public interface Modellable {

	public void setView(Viewable view);
	
	public void addItem(Media media, String quantity);

	public void editItem(Media media, String quantity);
	
	public void deleteItem(String itemID);
	public void deleteItem(MediaCategory media);
	
	public void searchItem(String query);
	public void searchItem(String query, MediaCategory media); 
	public Media[] getSearchResult();
	
	public void searchItemForEditing(String itemID);
	
	public void generateID(); 
	public String getID(); 
	
	public void checkItemQuantity(String itemID); 
	public String getItemQuantity();
	
	public void disconnectFromDatabase();

	public int getNumberOfRows();

	public void clearSearchResult();

	public void getDBConnection(String DBServerURL, String userName, String passWord);
	
}
