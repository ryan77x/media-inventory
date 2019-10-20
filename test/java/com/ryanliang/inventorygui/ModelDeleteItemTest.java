package com.ryanliang.inventorygui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelDeleteItemTest {
	private static Viewable view;
	private static Modellable model;
	private Media [] searchResult;
	private Media media;
	private static String db_URL = "jdbc:mysql://localhost:3306/media?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static String user = "root";
	private static String password = "asasas";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		view = new InventoryViewDummy();
		model = new InventoryModel();
		model.setView(view);
		model.getDBConnection(db_URL, user, password);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		model.disconnectFromDatabase();
		model = null;
		view = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		searchResult = null;
		media = null;
	}

	@Test
	public void testDeleteItem() {
		String itemID = model.getID();
		media = new Book(itemID, "book123", "desc1", "gen1", "author1", "isbn");	
		
		String quantity = "1";
		model.addItem(media, quantity);

		//Check that item is added before deleting it
		model.searchItem(itemID);
		searchResult = model.getSearchResult();
		if (searchResult.length < 1){
			Assert.fail("Fail to search for the new item after it was added");
		}
		else{
			model.deleteItem(itemID);
			model.searchItem(itemID);
			searchResult = model.getSearchResult();

			Assert.assertTrue("Fail to delete item", searchResult.length < 1);
		}
	}

}
