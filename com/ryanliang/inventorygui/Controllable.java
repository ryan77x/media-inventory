/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public interface Controllable {

	public void searchItem(String query);
	public void searchItem(String query, MediaCategory media);
	public void searchItemForEditing(String itemID);

	public void addItem(Media media, String quantity);
	
	public void editItem(Media media, String quantity);
	
	public void deleteItem(String itemID);
	public void deleteItem(MediaCategory media);
	
	public void generateID(); 
	
	public void disconnectFromDatabase();

	public void checkItemQuantity(String itemID);
	public void getDBConnection(String DBServerURL, String userName, String passWord); 
}
