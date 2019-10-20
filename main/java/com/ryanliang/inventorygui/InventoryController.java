/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

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
		public void searchItem(String query) {
			model.searchItem(query);
			
		}
		@Override
		public void searchItem(String query, MediaCategory media) {
			model.searchItem(query, media);
			
		}
		
		@Override	
		public void searchItemForEditing(String itemID){
			model.searchItemForEditing(itemID);
		}

		@Override
		public void deleteItem(String itemID){
			model.deleteItem(itemID);
		}
		
		@Override
		public void deleteItem(MediaCategory media){
			model.deleteItem(media);
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
		
		@Override
		public void checkItemQuantity(String itemID){
			model.checkItemQuantity(itemID);
		}
		@Override
		public void getDBConnection(String DBServerURL, String userName, String passWord) {
			model.getDBConnection(DBServerURL, userName, passWord);
		}
}
