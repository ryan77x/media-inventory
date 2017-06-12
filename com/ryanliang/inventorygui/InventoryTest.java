/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.awt.EventQueue;

public class InventoryTest {
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(() -> {
				InventoryModel model = new InventoryModel();

				InventoryController controller = new InventoryController(model);
				InventoryView view = new InventoryView(controller);

				view.setModel (model);
				model.setView(view);
				view.start();
		});
	}


}
