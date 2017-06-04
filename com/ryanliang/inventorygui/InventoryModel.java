/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InventoryModel extends AbstractTableModel implements Modellable {

	private Viewable view;
	private final ArrayList<Media> searchResult = new ArrayList<>();

	private static long IDCounter = 0;
	
	private final Connection connection;
	private final Statement nonQueryStatement;
	private final Statement queryStatement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	private boolean connectedToDatabase = false;
	
	public InventoryModel(String url, String username, String password) throws SQLException {
		connection = DriverManager.getConnection(url, username, password);
		nonQueryStatement = connection.createStatement();
		queryStatement = connection.createStatement();
		//statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		connectedToDatabase = true;
	}
	
	@Override
	public void disconnectFromDatabase(){
		if (connectedToDatabase){
			try{
				if (resultSet != null)
					resultSet.close();
				if (nonQueryStatement != null)
					nonQueryStatement.close();
				if (queryStatement != null)
					queryStatement.close();
				if (connection != null)
					connection.close();
			}
			catch (SQLException sqlException){
				sqlException.printStackTrace();
			}
			finally{
				connectedToDatabase = false;
			}
		}
	}
	@Override
	public void addItem(Media media, String quantityA) {
		if (media != null && quantityA != null){
			String quantity = quantityA;
			String ID = media.getID();

			setData(media, SQLCommand.INSERT);
			
			//Modify only if quantity is not empty and is a number (not consisting of alphabetic characters)
			String temp;
			if (!quantity.equals("") && Utility.isNumeric(quantity)){
				temp = "INSERT INTO inventory (MediaID, Quantity) VALUES(" + ID + ", " + quantity + ")";
				try {
					nonQueryStatement.execute(temp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			temp = "UPDATE mediaID SET IDCounter = " + (++IDCounter) + " WHERE MediaID = 1";
			try {
				nonQueryStatement.execute(temp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else 
			System.out.println("addItem(Media media, String quantityA) reference is null.");
	}
	
	private void setData(Media media, SQLCommand command) {		
		String none = "None";
		String temp = null;
		
		String ID = media.getID();
		String title = media.getTitle();
		String description = media.getDescription();
		String genre = media.getGenre();
		
		title = title.trim().equals("")?none:title;
		description = description.trim().equals("")?none:description;
		genre = genre.trim().equals("")?none:genre;
		
		if (media instanceof CD){
			String artist = ((CD) media).getArtist();
			
			artist = artist.trim().equals("")?none:artist;
			if (command == SQLCommand.UPDATE)
				temp = "UPDATE cd SET Title = '" + title + "', Description = '" + description + "', Genre = '" + genre + "', Artist = '" + artist + "' WHERE CDID = " + ID;
			else if (command == SQLCommand.INSERT)
				temp = "INSERT INTO cd (CDID, Title, Description, Genre, Artist) VALUES(" + ID + ", '" + title + "', '" + description + "', '" + genre + "', '" + artist + "'" + ")";
			try {
				if (temp != null)
					nonQueryStatement.execute(temp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (media instanceof DVD){
			String cast = ((DVD) media).getCast();
			
			cast = cast.trim().equals("")?none:cast;
			if (command == SQLCommand.UPDATE)
				temp = "UPDATE dvd SET Title = '" + title + "', Description = '" + description + "', Genre = '" + genre + "', Cast = '" + cast + "' WHERE DVDID = " + ID;
			else if (command == SQLCommand.INSERT)
				temp = "INSERT INTO dvd (DVDID, Title, Description, Genre, Cast) VALUES(" + ID + ", '" + title + "', '" + description + "', '" + genre + "', '" + cast + "'" + ")";
			try {
				if (temp != null)
					nonQueryStatement.execute(temp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (media instanceof Book){
			String author = ((Book) media).getAuthor();
			String ISBN = ((Book) media).getISBN();
			
			author = author.trim().equals("")?none:author;
			ISBN = ISBN.trim().equals("")?none:ISBN;
			if (command == SQLCommand.UPDATE)
				temp = "UPDATE book SET Title = '" + title + "', Description = '" + description + "', Genre = '" + genre + "', Author = '" + author + "', ISBN = '" + ISBN + "' WHERE BookID = " + ID;
			else if (command == SQLCommand.INSERT)
				temp = "INSERT INTO book (BookID, Title, Description, Genre, Author, ISBN) VALUES(" + ID + ", '" + title + "', '" + description + "', '" + genre + "', '" + author + "', '" + ISBN + "'" + ")";
			try {
				if (temp != null)
					nonQueryStatement.execute(temp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void editItem(Media media, String quantity) {
		if (media != null && quantity != null){
			String ID = media.getID();
			setData(media, SQLCommand.UPDATE);

			//Modify only if quantity is not empty and is a number (not consisting of alphabetic characters)
			String temp;
			if (!quantity.equals("") && Utility.isNumeric(quantity)){
				temp = "UPDATE inventory SET Quantity = " + quantity + " WHERE MediaID = " + ID;
				try {
					nonQueryStatement.execute(temp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else 
			System.out.println("editItem(Media media, String quantity) reference is null.");
	}

	@Override
	public void saveData() {
/*		try {
			FileOutputStream cdOut = new FileOutputStream(CDFile, false);
			CDList.store(cdOut, "CD List");
			cdOut.close();

			FileOutputStream dvdOut = new FileOutputStream(DVDFile, false);
			DVDList.store(dvdOut, "DVD List");
			dvdOut.close();

			FileOutputStream bookOut = new FileOutputStream(bookFile, false);
			bookList.store(bookOut, "Book List");
			bookOut.close();

			FileOutputStream inventoryOut = new FileOutputStream(inventoryFile, false);
			inventory.store(inventoryOut, "Inventory");
			inventoryOut.close();

			FileOutputStream IDOut = new FileOutputStream(IDMemoryFile, false);
			IDMemory.store(IDOut, "ID memory");
			IDOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/	
	}
	
	@Override
	public void loadData() {		
/*		File fileCD = new File(CDFile);
		File fileDVD = new File(DVDFile);
		File fileBook = new File(bookFile);
		File fileInventory = new File(inventoryFile);
		File fileID = new File(IDMemoryFile);
		
		try {
			if (fileCD.exists() && !fileCD.isDirectory()){
				FileInputStream cdIn = new FileInputStream(CDFile);
				CDList.load(cdIn);
				cdIn.close();
			}

			if (fileDVD.exists() && !fileDVD.isDirectory()){
				FileInputStream dvdIn = new FileInputStream(DVDFile);
				DVDList.load(dvdIn);
				dvdIn.close();
			}

			if (fileBook.exists() && !fileBook.isDirectory()){
				FileInputStream bookIn = new FileInputStream(bookFile);
				bookList.load(bookIn);
				bookIn.close();
			}
			
			if (fileInventory.exists() && !fileInventory.isDirectory()){
				FileInputStream inventoryIn = new FileInputStream(inventoryFile);
				inventory.load(inventoryIn);
				inventoryIn.close();
			}
			
			if (fileID.exists() && !fileID.isDirectory()){
				FileInputStream IDIn = new FileInputStream(IDMemoryFile);
				IDMemory.load(IDIn);
				String tempID = IDMemory.getProperty("IDCounter");
				
				if (tempID != null)
					IDCounter = Long.valueOf(tempID);
				
				IDIn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		String temp;
		String counter;
		
		temp = "SELECT * FROM mediaID WHERE MediaID = 1";
		try {
			ResultSet resultSet = queryStatement.executeQuery(temp);
			resultSet.last();
			int numberOfRows = resultSet.getRow();
			resultSet.first();
			if (numberOfRows > 0){
				if (resultSet.getObject(2) != null){
					counter = resultSet.getObject(2).toString();
					if (counter != null)
						IDCounter = Long.valueOf(counter);
				}
			}
			//Create MediaID 1 if the database is empty.  This happens only once during the life of this application.
			else{
				temp = "INSERT INTO mediaID (MediaID, IDCounter) VALUES(1, 0)";
				nonQueryStatement.execute(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setView(Viewable view) {
		this.view = view;
	}
	
	@Override
	public void searchItem(String query) {
		if (query != null){
			searchItemHelper(query);
			view.update(UpdateType.SEARCH_RESULT);
		}
		else 
			System.out.println("searchItem(String query) reference is null.");
	}
	
	@Override
	public Media[] getSearchResult(){
		Media [] result = searchResult.toArray(new Media[searchResult.size()]);
		searchResult.clear();
		return result;
	}

	@Override
	public void deleteItem(String itemID) throws SQLException, IllegalStateException {
		if (itemID != null){
			String temp = "DELETE FROM cd WHERE CDID = " + itemID;
			nonQueryStatement.execute(temp);
			//repeat for dvd this line
			//repeat for book this line
			temp = "DELETE FROM inventory WHERE MediaID = " + itemID;
			nonQueryStatement.execute(temp);
		}
		else 
			System.out.println("deleteItem(String itemID) reference is null.");	
/*		if (itemID != null){
			CDList.remove(itemID);
			DVDList.remove(itemID);
			bookList.remove(itemID);
			inventory.remove(itemID);
		}
		else 
			System.out.println("deleteItem(String itemID) reference is null.");	
*/			
	}

	@Override
	public void generateID() {
		view.update(UpdateType.ID);	
	}
	
	@Override
	public String getID() {	
		return String.valueOf(IDCounter);
	}
	
	private void searchItemHelper(String query) {	
		String temp = null;
		//String value;
		
		String ID = "";
		String title = "";
		String description = "";
		String genre = "";
		String artist = "";
		String cast = "";
		String author = "";
		String ISBN = "";

		if (!query.equals("")){
			//ID based search
			if (Utility.isNumeric(query)){	
				while (true){
					temp = "SELECT * FROM cd WHERE CDID = " + query;
					try {
						resultSet = queryStatement.executeQuery(temp);
						metaData = resultSet.getMetaData();
						//int numberOfColumns = metaData.getColumnCount();
						resultSet.last();
						numberOfRows = resultSet.getRow();
						resultSet.first();
						if (numberOfRows > 0){
							if (resultSet.getObject(2) != null)
								title = resultSet.getObject(2).toString();
							if (resultSet.getObject(3) != null)
								description = resultSet.getObject(3).toString();
							if (resultSet.getObject(4) != null)
								genre = resultSet.getObject(4).toString();
							if (resultSet.getObject(5) != null)
								artist = resultSet.getObject(5).toString();

							searchResult.add(new CD(query, title, description, genre, artist));
							break;
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
					temp = "SELECT * FROM dvd WHERE DVDID = " + query;
					try {
						resultSet = queryStatement.executeQuery(temp);
						metaData = resultSet.getMetaData();
						//int numberOfColumns = metaData.getColumnCount();
						resultSet.last();
						numberOfRows = resultSet.getRow();
						resultSet.first();
						if (numberOfRows > 0){
							if (resultSet.getObject(2) != null)
								title = resultSet.getObject(2).toString();
							if (resultSet.getObject(3) != null)
								description = resultSet.getObject(3).toString();
							if (resultSet.getObject(4) != null)
								genre = resultSet.getObject(4).toString();
							if (resultSet.getObject(5) != null)
								cast = resultSet.getObject(5).toString();

							searchResult.add(new DVD(query, title, description, genre, cast));
							break;
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
					temp = "SELECT * FROM book WHERE BookID = " + query;
					try {
						resultSet = queryStatement.executeQuery(temp);
						metaData = resultSet.getMetaData();
						//int numberOfColumns = metaData.getColumnCount();
						resultSet.last();
						numberOfRows = resultSet.getRow();
						resultSet.first();
						if (numberOfRows > 0){
							if (resultSet.getObject(2) != null)
								title = resultSet.getObject(2).toString();
							if (resultSet.getObject(3) != null)
								description = resultSet.getObject(3).toString();
							if (resultSet.getObject(4) != null)
								genre = resultSet.getObject(4).toString();
							if (resultSet.getObject(5) != null)
								author = resultSet.getObject(5).toString();
							if (resultSet.getObject(6) != null)
								ISBN = resultSet.getObject(6).toString();

							searchResult.add(new Book(query, title, description, genre, author, ISBN));
							break;
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			//simple return all CDs, DVDs or Books
			else if (query.equals("return-all-cds")){
				temp = "SELECT * FROM cd";
				try {
					resultSet = queryStatement.executeQuery(temp);
					metaData = resultSet.getMetaData();
					//int numberOfColumns = metaData.getColumnCount();
					resultSet.last();
					numberOfRows = resultSet.getRow();
					resultSet.first();
					if (numberOfRows > 0){
						do{
							if (resultSet.getObject(1) != null)
								ID = resultSet.getObject(1).toString();
							if (resultSet.getObject(2) != null)
								title = resultSet.getObject(2).toString();
							if (resultSet.getObject(3) != null)
								description = resultSet.getObject(3).toString();
							if (resultSet.getObject(4) != null)
								genre = resultSet.getObject(4).toString();
							if (resultSet.getObject(5) != null)
								artist = resultSet.getObject(5).toString();

							searchResult.add(new CD(ID, title, description, genre, artist));

						}while (resultSet.next());
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
/*		if (!query.equals("")){
			//ID based search
			if (Utility.isNumeric(query)){
				while (true){
					temp = CDList.getProperty(query);
					if (temp != null){
						searchResultHelper(query, temp, MediaCategory.CD);
						break;
					}		

					temp = DVDList.getProperty(query);
					if (temp != null){			
						searchResultHelper(query, temp, MediaCategory.DVD);
						break;
					}	

					temp = bookList.getProperty(query);
					if (temp != null){					
						searchResultHelper(query, temp, MediaCategory.BOOK);
						break;
					}	
					break;
				}
			}
			//simple return all CDs, DVDs or Books
			else if (query.equals("return-all-cds")){
				for (String key : CDList.stringPropertyNames()){
					temp = CDList.getProperty(key);
					searchResultHelper(key, temp, MediaCategory.CD);
				}
			}
			else if (query.equals("return-all-dvds")){
				for (String key : DVDList.stringPropertyNames()){
					temp = DVDList.getProperty(key);
					searchResultHelper(key, temp, MediaCategory.DVD);
				}
			}				
			else if (query.equals("return-all-books")){
				for (String key : bookList.stringPropertyNames()){
					temp = bookList.getProperty(key);
					searchResultHelper(key, temp, MediaCategory.BOOK);
				}				
			}
			//Word phrase based search
			else{
				query = query.toLowerCase();
				for (String key : CDList.stringPropertyNames()){
					temp = CDList.getProperty(key);
					value = temp.toLowerCase();
					if (value.contains(query)){
						searchResultHelper(key, temp, MediaCategory.CD);
					}
				}
				for (String key : DVDList.stringPropertyNames()){
					temp = DVDList.getProperty(key);
					value = temp.toLowerCase();
					if (value.contains(query)){
						searchResultHelper(key, temp, MediaCategory.DVD);
					}
				}
				for (String key : bookList.stringPropertyNames()){
					temp = bookList.getProperty(key);
					value = temp.toLowerCase();
					if (value.contains(query)){
						searchResultHelper(key, temp, MediaCategory.BOOK);
					}
				}
			}
		}
*/

	}

	private void searchResultHelper(String key, String str, MediaCategory media) {
/*		String[] parts = str.split(delimiter);	
		String title = parts[0]; 
		String description = parts[1]; 
		String genre = parts[2]; 
		
		if (media == MediaCategory.CD){
			String artist = parts[3]; 
			searchResult.add(new CD(key, title, description, genre, artist));
		}
		else if (media == MediaCategory.DVD){
			String cast = parts[3]; 
			searchResult.add(new DVD(key, title, description, genre, cast));
		}
		else if (media == MediaCategory.BOOK){
			String author = parts[3]; 
			String ISBN = parts[4]; 
			searchResult.add(new Book(key, title, description, genre, author, ISBN));
		}
*/
	}

	@Override
	public void searchItemForEditing(String itemID) {		
		if (itemID != null){
			searchItemHelper(itemID);
			view.update(UpdateType.EDIT);
		}
		else 
			System.out.println("searchItemForEditing(String itemID) reference is null.");
	}

	@Override
	public String getItemQuantity(String itemID) {
		/*		if (itemID != null)
			return inventory.getProperty(itemID);
		else{ 
			System.out.println("getItemQuantity(String itemID) reference is null.");
			return null;
		}
		 */
		String temp;
		String quantity = "0";
		if (itemID != null){
			temp = "SELECT * FROM inventory WHERE MediaID = " + itemID;
			try {
				ResultSet resultSet = queryStatement.executeQuery(temp);
				ResultSetMetaData metaData = resultSet.getMetaData();
				resultSet.last();
				int numberOfRows = resultSet.getRow();
				resultSet.first();
				if (numberOfRows > 0){
					if (resultSet.getObject(2) != null){
						quantity = resultSet.getObject(2).toString();
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return quantity;
		}
		else{ 
			System.out.println("getItemQuantity(String itemID) reference is null.");
			return null;
		}

	}
	
	@Override
	public String getColumnName(int column) 
	{ 
		try {
			return metaData.getColumnName(column+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	} 

	@Override
	public int getRowCount() 
	{ 
		return numberOfRows;
	} 

	@Override
	public int getColumnCount() 
	{ 
		try {
			return metaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	} 

	@Override
	public Object getValueAt(int row, int column) 
	{ 
		try {
			resultSet.absolute(row + 1);
			return resultSet.getObject(column +1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public Class getColumnClass(int column) 
	{ 
		try {
			String className = metaData.getColumnClassName(column + 1);
			return Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Object.class;
	}
}

