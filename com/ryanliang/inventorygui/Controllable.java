/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.sql.SQLException;

public interface Controllable {

	public void searchItem(String query);
	public void searchItem(String query, MediaCategory media);
	public void searchItemForEditing(String itemID);

	public void addItem(Media media, String quantity);
	
	public void editItem(Media media, String quantity);
	
	public void deleteItem(String itemID);
	
	public void generateID(); 
	
	public void disconnectFromDatabase();
}
