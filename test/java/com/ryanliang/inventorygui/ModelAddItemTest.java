package com.ryanliang.inventorygui;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelAddItemTest {
	private static Viewable view;
	private static Modellable model;
	private Media [] searchResult;
	private Media media;
	private static String db_URL = "jdbc:mysql://localhost:3306/media?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static String user = "root";
	private static String password = "asasas";
	private static Random random;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		view = new InventoryViewDummy();
		random = new Random();
		model = new InventoryModel();
		model.setView(view);
		model.getDBConnection(db_URL, user, password);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		model.disconnectFromDatabase();
		model = null;
		view = null;
		random = null;
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
	public void testAddItem() {
		String itemID = model.getID();
		Integer randomNum = random.nextInt();
		String title = "book" + randomNum.toString(); 
		media = new Book(itemID, title, "desc1", "gen1", "author1", "isbn");	
		
		String quantity = "1";
		model.addItem(media, quantity);
		model.searchItem(itemID);
		searchResult = model.getSearchResult();
		
		if (searchResult.length < 1){
			Assert.fail("Fail to search for the new item after it was added");
		}
		else{
			media = searchResult[0];

			//Book title shall be the same as when it was added.  Note that this also test the SearchItem method.  So testSearchItem unit test is not needed.
			Assert.assertEquals(title, media.getTitle());
			
			//add test for dvd and book as well as check other fields such as desc, genre....
		}
	}

}
