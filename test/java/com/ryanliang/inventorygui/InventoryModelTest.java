package com.ryanliang.inventorygui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryModelTest {
	private static Viewable view;
	private static Modellable model;
	private Media media;
	private static String db_URL = "jdbc:mysql://localhost:3306/media?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static String user = "root";
	private static String password = "asasas";
	
	@BeforeClass
	public static void DBsetup(){
		view = new InventoryViewDummy();
		model = new InventoryModel();
		model.setView(view);
		model.getDBConnection(db_URL, user, password);
	}
	
	@Before
	public void setup(){
 
	}
	
	@AfterClass
	public static void DBtearDown(){
		model.disconnectFromDatabase();
		model = null;
		view = null;
	}
	
	@After
	public void tearDown(){
		media = null;
	}
	
	@Test
	public void testGetItemQuantity(){
		String itemID = model.getID();
		media = new CD(itemID, "star3", "www", "eee", "rrr");	
		
		String quantity = "321";
		model.addItem(media, quantity);
		model.checkItemQuantity(itemID);
		
		//Quantity shall be the same as when it was added.
		Assert.assertEquals(quantity, model.getItemQuantity());
	}
	
	@Test
	public void testGetItemID(){
		String itemID_a = model.getID();
		String itemID_b = model.getID();
		
		//ID counter shall remain the same when no new item is added.
		Assert.assertEquals(itemID_a, itemID_b);
		
		media = new CD(itemID_a, "space1", "aa", "vv", "bb");	
		String quantity = "11";
		model.addItem(media, quantity);
		itemID_b = model.getID();
		
		Integer temp_a = Integer.valueOf(itemID_a) + 1;
		Integer temp_b = Integer.valueOf(itemID_b);
		
		//ID counter shall increment by 1 when a new item is added.
		Assert.assertEquals(temp_a, temp_b);
	}
}
