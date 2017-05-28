/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.sql.SQLException;

public interface Modellable {

	public void setView(Viewable view);
	
	public void addItem(Media media, String quantity);

	public void saveData();
	public void loadData();

	public void editItem(Media media, String quantity);
	
	public void deleteItem(String itemID) throws SQLException, IllegalStateException;
	
	public void searchItem(String query);
	public Media[] getSearchResult();
	
	public void searchItemForEditing(String itemID);
	
	public void generateID(); 
	public String getID(); 
	
	public String getItemQuantity(String itemID); 
	
	public void disconnectFromDatabase(); 
	
}
