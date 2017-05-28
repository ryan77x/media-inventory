/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.sql.SQLException;

public interface Controllable {
	public void saveData();

	public void loadData();

	public void searchItem(String query);
	public void searchItemForEditing(String itemID);

	public void addItem(Media media, String quantity);
	
	public void editItem(Media media, String quantity);
	
	public void deleteItem(String itemID) throws SQLException, IllegalStateException;
	
	public void generateID(); 
	
	public void disconnectFromDatabase();
}
