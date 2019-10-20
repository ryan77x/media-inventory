/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class InventoryTest {
    private static Logger logger = Logger.getLogger("com.ryanliang.inventorygui.InventoryTest");
    private static FileHandler fh;
	public static void main(String[] args) {
		try {
			fh = new FileHandler("mylog.txt");
			fh.setFormatter(new SimpleFormatter());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.addHandler(fh);
		logger.setLevel(Level.ALL);
		logger.finest("Inventory app starts");
		
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
