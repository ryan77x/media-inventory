/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.sql.SQLException;

public class InventoryController implements Controllable {

		private final Modellable model;
		
		public InventoryController(Modellable model) {
			super();
			this.model = model;
		}
		@Override
		public void addItem(Media media, String quantity) {
			model.addItem(media, quantity);
			
		}
		@Override
		public void saveData() {
			model.saveData();
			
		}
		@Override
		public void searchItem(String query) {
			model.searchItem(query);
			
		}
		@Override	
		public void searchItemForEditing(String itemID){
			model.searchItemForEditing(itemID);
		}
		@Override
		public void loadData() {
			model.loadData();
			
		}
		@Override
		public void deleteItem(String itemID) throws SQLException, IllegalStateException{
			model.deleteItem(itemID);
			
		}
		@Override
		public void editItem(Media media, String quantity) {
			model.editItem(media, quantity);
			
		}
		@Override
		public void generateID() {
			model.generateID();
			
		}
		
		@Override
		public void disconnectFromDatabase(){
			model.disconnectFromDatabase();
		}
}
