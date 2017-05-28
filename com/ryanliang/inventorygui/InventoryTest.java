/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.awt.EventQueue;
import java.sql.SQLException;

public class InventoryTest {
	final static String Database_URL = "jdbc:mysql://localhost:3306/media";
	final static String userName = "root";
	final static String password = "asasas";
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(() -> {
			try{
				InventoryModel model = new InventoryModel(Database_URL, userName, password);

				InventoryController controller = new InventoryController(model);
				InventoryView view = new InventoryView(controller);

				view.setModel (model);
				model.setView(view);
				view.start();
			}
			catch (SQLException sqlException){
				sqlException.printStackTrace();
			}
		});
	}


}
