/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InventoryModel extends AbstractTableModel implements Modellable {

	private Viewable view;
	private final ArrayList<Media> searchResult = new ArrayList<>();

	private static int IDCounter = 0;
	
	private final Connection connection;
	private PreparedStatement nonQueryStatement;
	private Statement queryStatement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	private boolean connectedToDatabase = false;
	
	public InventoryModel(String url, String username, String password) throws SQLException {
		connection = DriverManager.getConnection(url, username, password);
		nonQueryStatement = null;
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
	public void addItem(Media media, String quantity) {
		if (media != null && quantity != null){
			String ID = media.getID();
	
			setData(media, SQLCommand.INSERT);
			
			//Modify only if quantity is not empty and is a number (not consisting of alphabetic characters)
			String sqlString;
			if (!quantity.equals("") && Utility.isNumeric(quantity)){
				sqlString = "INSERT INTO inventory (MediaID, Quantity) VALUES(?, ?)";
				try {
					nonQueryStatement = connection.prepareStatement(sqlString);
					nonQueryStatement.setInt(1, Integer.valueOf(ID));
					nonQueryStatement.setInt(2, Integer.valueOf(quantity));
					nonQueryStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			sqlString = "UPDATE mediaID SET IDCounter = ? WHERE MediaID = 1";
			try {
				nonQueryStatement = connection.prepareStatement(sqlString);
				nonQueryStatement.setInt(1, ++IDCounter);
				nonQueryStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else 
			System.out.println("addItem(Media media, String quantityA) reference is null.");
	}
	
	private void setData(Media media, SQLCommand command) {		
		String none = "None";
		String sqlString = null;

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
			if (command == SQLCommand.UPDATE){
				sqlString = "UPDATE cd SET Title = ?, Description = ?, Genre = ?, Artist = ? WHERE CDID = ?";
				updateTable(sqlString, title, description, genre, artist, ID);
			}
			else if (command == SQLCommand.INSERT){
				sqlString = "INSERT INTO cd (CDID, Title, Description, Genre, Artist) VALUES(?, ?, ?, ?, ?)";
				insertToTable(sqlString, ID, title, description, genre, artist);
			}
		}
		else if (media instanceof DVD){
			String cast = ((DVD) media).getCast();
			
			cast = cast.trim().equals("")?none:cast;
			if (command == SQLCommand.UPDATE){
				sqlString = "UPDATE dvd SET Title = ?, Description = ?, Genre = ?, Cast = ? WHERE DVDID = ?";
				updateTable(sqlString, title, description, genre, cast, ID);
			}
			else if (command == SQLCommand.INSERT){
				sqlString = "INSERT INTO dvd (DVDID, Title, Description, Genre, Cast) VALUES(?, ?, ?, ?, ?)";
				insertToTable(sqlString, ID, title, description, genre, cast);
			}
		}
		else if (media instanceof Book){
			String author = ((Book) media).getAuthor();
			String ISBN = ((Book) media).getISBN();
			
			author = author.trim().equals("")?none:author;
			ISBN = ISBN.trim().equals("")?none:ISBN;
			if (command == SQLCommand.UPDATE){
				sqlString = "UPDATE book SET Title = ?, Description = ?, Genre = ?, Author = ?, ISBN = ? WHERE BookID = ?";
				updateTable(sqlString, title, description, genre, author, ISBN, ID);
			}
			else if (command == SQLCommand.INSERT){
				sqlString = "INSERT INTO book (BookID, Title, Description, Genre, Author, ISBN) VALUES(?, ?, ?, ?, ?, ?)";
				insertToTable(sqlString, ID, title, description, genre, author, ISBN);
			}
		}
	}
	
	private void insertToTable(String sqlString, String... parameters) {
		
		//***Do not modify this method until all callers (users) of this method are not affected.***
		
		int size = parameters.length;
		try {
			nonQueryStatement = connection.prepareStatement(sqlString);
			
			nonQueryStatement.setInt(1, Integer.valueOf(parameters[0]));
			for (int ii = 1; ii < size; ii++){
				nonQueryStatement.setString(ii+1, parameters[ii]);
			}
			nonQueryStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateTable(String sqlString, String... parameters) {
		
		//***Do not modify this method until all callers (users) of this method are not affected.***
		
		int size = parameters.length;
		try {
			nonQueryStatement = connection.prepareStatement(sqlString);
			
			for (int ii = 1; ii < size; ii++){
				nonQueryStatement.setString(ii, parameters[ii-1]);
			}
			nonQueryStatement.setInt(size, Integer.valueOf(parameters[size-1]));
			nonQueryStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteFromTable(String sqlString, String... parameters) {
		
		//***Do not modify this method until all callers (users) of this method are not affected.***
		
		try {
			nonQueryStatement = connection.prepareStatement(sqlString);
			
			nonQueryStatement.setInt(1, Integer.valueOf(parameters[0]));
			nonQueryStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void editItem(Media media, String quantity) {
		if (media != null && quantity != null){
			String ID = media.getID();
			setData(media, SQLCommand.UPDATE);

			//Modify only if quantity is not empty and is a number (not consisting of alphabetic characters)
			String sqlString;
			if (!quantity.equals("") && Utility.isNumeric(quantity)){
				sqlString = "UPDATE inventory SET Quantity = " + quantity + " WHERE MediaID = " + ID;
				try {
					nonQueryStatement.execute(sqlString);
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
	//not used at this time
	}
	
	@Override
	public void loadData() {		

		String sqlString;
		String counter;
		
		sqlString = "SELECT * FROM mediaID WHERE MediaID = 1";
		try {
			ResultSet resultSet = queryStatement.executeQuery(sqlString);
			resultSet.last();
			int numberOfRows = resultSet.getRow();
			resultSet.first();
			if (numberOfRows > 0){
				if (resultSet.getObject(2) != null){
					counter = resultSet.getObject(2).toString();
					if (counter != null)
						IDCounter = Integer.valueOf(counter);
				}
			}
			//Create MediaID "1" if the database is empty.  This happens only once during the life of this application.
			else{
				sqlString = "INSERT INTO mediaID (MediaID, IDCounter) VALUES(?, ?)";
				nonQueryStatement = connection.prepareStatement(sqlString);
				nonQueryStatement.setInt(1, 1);
				nonQueryStatement.setInt(2, 0);
				nonQueryStatement.executeUpdate();
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
			query = query.trim();
			
			if (!query.equals("") && Utility.isNumeric(query)){
				searchItemHelper(query);
				view.update(UpdateType.SEARCH_RESULT);
			}
		}
		else 
			System.out.println("searchItem(String query) reference is null.");
	}
	
	@Override
	public void searchItem(String query, MediaCategory media) {
		if (query != null){
			query = query.trim();

			searchItemHelper(query, media);
			view.update(UpdateType.SEARCH_RESULT);
		}
		else 
			System.out.println("searchItemString query, MediaCategory media) reference is null.");
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
			itemID = itemID.trim();
			if (!itemID.equals("") && Utility.isNumeric(itemID)){
				String sqlString = "DELETE FROM cd WHERE CDID = ?";
				deleteFromTable(sqlString, itemID);
/*				
				nonQueryStatement = connection.prepareStatement(sqlString);
				nonQueryStatement.setInt(1, Integer.valueOf(itemID));
				nonQueryStatement.executeUpdate();
*/
				sqlString = "DELETE FROM dvd WHERE DVDID = ?";
				deleteFromTable(sqlString, itemID);

				sqlString = "DELETE FROM book WHERE BookID = ?";
				deleteFromTable(sqlString, itemID);

				sqlString = "DELETE FROM inventory WHERE MediaID = ?";
				deleteFromTable(sqlString, itemID);
			}
		}
		else 
			System.out.println("deleteItem(String itemID) reference is null.");			
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
		String sqlString = null;
		//String value;

		String ID = "";
		String title = "";
		String description = "";
		String genre = "";
		String artist = "";
		String cast = "";
		String author = "";
		String ISBN = "";

		//ID based search
		while (true){
			sqlString = "SELECT * FROM cd WHERE CDID = " + query;
			try {
				resultSet = queryStatement.executeQuery(sqlString);
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
			sqlString = "SELECT * FROM dvd WHERE DVDID = " + query;
			try {
				resultSet = queryStatement.executeQuery(sqlString);
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
			sqlString = "SELECT * FROM book WHERE BookID = " + query;
			try {
				resultSet = queryStatement.executeQuery(sqlString);
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
	
	private void searchItemHelper(String query, MediaCategory media) {
		String sqlString = null;
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
			//Word phrase based search

			if (media == MediaCategory.CD){
				sqlString = "SELECT * FROM cd WHERE Title LIKE '%" + query + "%' OR Description LIKE '%" + query + "%' OR Genre LIKE '%" + query + "%' OR Artist LIKE '%" + query + "%'";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
			else if (media == MediaCategory.DVD){
				sqlString = "SELECT * FROM dvd WHERE Title LIKE '%" + query + "%' OR Description LIKE '%" + query + "%' OR Genre LIKE '%" + query + "%' OR Cast LIKE '%" + query + "%'";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
								cast = resultSet.getObject(5).toString();

							searchResult.add(new DVD(ID, title, description, genre, cast));

						}while (resultSet.next());
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			else if (media == MediaCategory.BOOK){
				sqlString = "SELECT * FROM book WHERE Title LIKE '%" + query + "%' OR Description LIKE '%" + query + "%' OR Genre LIKE '%" + query + "%' OR Author LIKE '%" + query + "%' OR ISBN LIKE '%" + query + "%'";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
		else{
			//simply return all CDs, DVDs or Books
			if (media == MediaCategory.CD){
				sqlString = "SELECT * FROM cd";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
			else if (media == MediaCategory.DVD){
				sqlString = "SELECT * FROM dvd";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
								cast = resultSet.getObject(5).toString();

							searchResult.add(new DVD(ID, title, description, genre, cast));

						}while (resultSet.next());
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else if (media == MediaCategory.BOOK){
				sqlString = "SELECT * FROM book";
				try {
					resultSet = queryStatement.executeQuery(sqlString);
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
								author = resultSet.getObject(5).toString();
							if (resultSet.getObject(6) != null)
								ISBN = resultSet.getObject(6).toString();

							searchResult.add(new Book(ID, title, description, genre, author, ISBN));

						}while (resultSet.next());
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

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
		String sqlString;
		String quantity = "0";
		if (itemID != null){
			sqlString = "SELECT * FROM inventory WHERE MediaID = " + itemID;
			try {
				ResultSet resultSet = queryStatement.executeQuery(sqlString);
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

