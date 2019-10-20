package com.ryanliang.inventorygui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelEditItemTest {
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
	public void testEditItem() {
		String itemID = model.getID();
		String title = "book123"; 
		String desc = "desc1"; 
		String genre = "gen1"; 
		String author = "authro1"; 
		String isbn = "isbn1"; 
		media = new Book(itemID, title, desc, genre, author, isbn);	
		
		String quantity = "1";
		model.addItem(media, quantity);

		//Check that item is added before deleting it
		model.searchItem(itemID);
		searchResult = model.getSearchResult();
		if (searchResult.length < 1){
			fail("Fail to search for the new item after it was added");
		}
		else{
			quantity = "22";
			title = "xbook123x"; 
			desc = "xdesc1x"; 
			genre = "xgen1x"; 
			author = "xauthro1x"; 
			isbn = "xisbn1x"; 
			media = new Book(itemID, title, desc, genre, author, isbn);	
			model.editItem(media, quantity);
			model.searchItem(itemID);
			searchResult = model.getSearchResult();

			media = searchResult[0];

			//Book title shall be the same as when it was modified. 
			assertEquals(title, media.getTitle());
			assertEquals(desc, media.getDescription());
			assertEquals(genre, media.getGenre());
			
			if (media instanceof Book){
				assertEquals(author, ((Book) media).getAuthor());
				assertEquals(isbn, ((Book) media).getISBN());
			}
			else{
				fail("Media is not a Book object.");
			}
			
			model.checkItemQuantity(itemID);
			
			//Quantity shall be the same as when it was modified.
			assertEquals(quantity, model.getItemQuantity());
		}
	}
}
