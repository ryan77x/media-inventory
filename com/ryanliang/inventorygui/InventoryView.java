/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class InventoryView extends JFrame implements Viewable{
	
	private final Controllable controller;
	private Modellable model;
	
	private Media [] searchResult;
	private int resultCounter = 0;
	private static String itemID = "0";	
	private ItemDialog itemDialog = null;
	
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu editMenu = new JMenu("Edit");
	private final JMenu helpMenu = new JMenu("Help");
	
	private final JMenuItem saveFileMenu = new JMenuItem("Save");
	private final JMenuItem exitFileMenu = new JMenuItem("Exit");
	
	private final JMenuItem newEditMenu = new JMenuItem("New");
	private final JMenuItem editEditMenu = new JMenuItem("Edit"); 
	private final JMenuItem deleteEditMenu = new JMenuItem("Delete");
	private final JMenuItem searchEditMenu = new JMenuItem("Search"); 
	
	private final JMenuItem aboutHelpMenu = new JMenuItem("About");
	
	private final JButton newToolBarButton = new JButton("New"); 
	private final JButton editToolBarButton = new JButton("Edit"); 
	private final JButton deleteToolBarButton = new JButton("Delete"); 
	private final JButton findToolBarButton = new JButton("Search"); 
	private final JButton firstToolBarButton = new JButton("First"); 
	private final JButton previousToolBarButton = new JButton("Previous"); 
	private final JButton nextToolBarButton = new JButton("Next"); 
	private final JButton lastToolBarButton = new JButton("Last"); 
	private final JButton CDsToolBarButton = new JButton("CDs"); 
	private final JButton DVDsToolBarButton = new JButton("DVDs"); 
	private final JButton BooksToolBarButton = new JButton("Books"); 
	
	private final JToolBar toolBar = new JToolBar();
	
	private final JPanel westPanel = new JPanel();
	private final JPanel statusPanel = new JPanel();
	
	private final JLabel searchResultLabel = new JLabel("Search result: ");
	private JLabel searchResultStatus = new JLabel("");
	private JLabel itemDetails = new JLabel("");
	
	private JTable table = null;
	private JScrollPane scrollPane = null;
	private int tableRowNum = 0;
	private boolean tableRowSelected = false; 
	
	public InventoryView(Controllable controller) {
		super("Media inventory system");
		this.controller = controller;
				
		organizeUI();
		addListeners();
			
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	private void addListeners() {
		saveFileMenu.addActionListener(event -> {
			controller.saveData();	
		});
		
		exitFileMenu.addActionListener(event -> {
			quitApp();
		});
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Media inventory system v1.0 Copyright 2017 RLTech Inc");
		});
		
		newEditMenu.addActionListener(event -> newItem());
		searchEditMenu.addActionListener(event -> searchItem());
		deleteEditMenu.addActionListener(event -> {
			try {
				deleteItem();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		editEditMenu.addActionListener(event -> editItem());
		
		newToolBarButton.addActionListener(event -> newItem());
		findToolBarButton.addActionListener(event -> searchItem());
		deleteToolBarButton.addActionListener(event -> {
			try {
				deleteItem();
			} catch (IllegalStateException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		editToolBarButton.addActionListener(event -> editItem());
		
		nextToolBarButton.addActionListener(event -> nextItem());
		previousToolBarButton.addActionListener(event -> previousItem());
		firstToolBarButton.addActionListener(event -> firstItem());
		lastToolBarButton.addActionListener(event -> lastItem());
		
		CDsToolBarButton.addActionListener(event -> getAllCDs());
		DVDsToolBarButton.addActionListener(event -> getAllDVDs());
		BooksToolBarButton.addActionListener(event -> getAllBooks());
      
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	private void organizeUI() {
		fileMenu.add(saveFileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitFileMenu);
		
		editMenu.add(newEditMenu);
		editMenu.add(editEditMenu);
		editMenu.add(deleteEditMenu);
		editMenu.add(searchEditMenu);
		
		helpMenu.add(aboutHelpMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		toolBar.add(newToolBarButton);
		toolBar.add(editToolBarButton);
		toolBar.add(deleteToolBarButton);
		toolBar.addSeparator();
		toolBar.add(CDsToolBarButton);
		toolBar.add(DVDsToolBarButton);
		toolBar.add(BooksToolBarButton);
		toolBar.addSeparator();
		toolBar.add(findToolBarButton);
		toolBar.addSeparator();
		toolBar.add(firstToolBarButton);
		toolBar.add(previousToolBarButton);
		toolBar.add(nextToolBarButton);
		toolBar.add(lastToolBarButton);

		add(toolBar, BorderLayout.NORTH);
		
		//add(westPanel, BorderLayout.WEST);
		//westPanel.add(new JLabel("      "));
		
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.add(searchResultLabel);
		statusPanel.add(searchResultStatus);
		setSearchResultStatusVisible(false);

		add(itemDetails);
	}

	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Save data?");
    	if (answer == JOptionPane.YES_OPTION){
    		controller.saveData();
    		controller.disconnectFromDatabase();
    		System.exit(0);
    	}
    	else if (answer == JOptionPane.NO_OPTION)
    		controller.disconnectFromDatabase();
    		System.exit(0);		
    }

	private void getAllCDs() {
		controller.searchItem("return-all-cds");
	}
	
	private void getAllDVDs() {
		controller.searchItem("return-all-dvds");
	}
	
	private void getAllBooks() {
		controller.searchItem("return-all-books");
	}
	
	private void lastItem() {
		if (searchResult != null){
			if (searchResult.length > 1){
				resultCounter = searchResult.length - 1;
				displayResult(searchResult[resultCounter]);		
			}
		}
	}

	private void firstItem() {
		if (searchResult != null){
			if (searchResult.length > 1){
				resultCounter = 0;
				displayResult(searchResult[resultCounter]);		
			}	
		}
	}

	private void nextItem() {
		if (searchResult != null){
			if (searchResult.length > 1 && (resultCounter < (searchResult.length-1))){
				resultCounter++;
				displayResult(searchResult[resultCounter]);		
			}	
		}
	}

	private void previousItem() {
		if (searchResult != null){
			if (searchResult.length > 1 && (resultCounter > 0)){
				resultCounter--;
				displayResult(searchResult[resultCounter]);		
			}	
		}
	}

	private void searchItem() {
		String input = JOptionPane.showInputDialog("Enter item ID number or a phrase"); 
		if (input != null)
			if (scrollPane != null)
				remove(scrollPane);
		
			controller.searchItem(input.trim());
	}

	private void editItem() {
		clearItemDetails();
		setSearchResultStatusVisible(false);
		searchResult = null;
		
		String input = JOptionPane.showInputDialog("Enter item ID number");
		if (input != null){
			if (scrollPane != null)
				remove(scrollPane);
			
			controller.searchItemForEditing(input.trim());
		}
	}

	private void deleteItem() throws SQLException, IllegalStateException {
		clearItemDetails();
		setSearchResultStatusVisible(false);
		searchResult = null;
		
		String input = JOptionPane.showInputDialog("Enter item ID number");
		if (input != null){
			if (scrollPane != null)
				remove(scrollPane);
			
			if (!input.equals("") && Utility.isNumeric(input))
				controller.deleteItem(input.trim());
		}
	}

	private void newItem() {
		clearItemDetails();
		setSearchResultStatusVisible(false);
		searchResult = null;
		
		Media temp = null;
		//Generate item ID which will be needed in the openItemDialog()
		controller.generateID();

		openItemDialog(temp, "");
		
		if (itemDialog.getDone() == true){
			if (scrollPane != null)
				remove(scrollPane);
			
			Media item = itemDialog.getItem();
			
			controller.addItem(item, itemDialog.getQuantity());
			//displayResult(item);
			controller.searchItem(item.getID());
		}
		if (itemDialog != null){
			itemDialog.initUI();
		}
	}
	
	private void openItemDialog(Media m, String quantity) {
		if (itemDialog == null)
			itemDialog = new ItemDialog(this);
		
		if (m == null){
			itemDialog.resetRadioButtonGroup();
			itemDialog.inputItemDetails(itemID);
		}
		else{
			itemDialog.initializeTextFields(m, quantity);
		}
		itemDialog.setLocationRelativeTo(this);
		itemDialog.setDone(false);
		itemDialog.setVisible(true);
	}

	@Override
	public void setModel(Modellable model) {
		this.model = model;
	}

	@Override
	public void start() {
		controller.loadData();
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = (int) (screenSize.width*0.8);
		int screenHeight = (int) (screenSize.height*0.8);
		this.setSize(screenWidth, screenHeight);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void update(UpdateType ut) {
		if (ut == UpdateType.SEARCH_RESULT){
			searchResult = model.getSearchResult();
			
			if (searchResult.length < 1){
				clearItemDetails();
				setSearchResultStatusVisible(false);
				JOptionPane.showMessageDialog(null, "Item does not exist", "alert", JOptionPane.ERROR_MESSAGE); 
			}
			else {
				searchResultStatus.setText(String.valueOf((searchResult.length)));
				validate();
				//reset counter
				resultCounter = 0;
				//displayResult(searchResult[0]);
				displayTable();
				
				setSearchResultStatusVisible(true);
			}
		}
		else if (ut == UpdateType.EDIT){
			Media [] result = model.getSearchResult();
			
			if (result.length < 1)
				JOptionPane.showMessageDialog(null, "Item does not exist", "alert", JOptionPane.ERROR_MESSAGE); 
			else
				editResult(result[0]);			
		}
		else if (ut == UpdateType.ID){
			itemID = model.getID();
		}
	}

	private void setSearchResultStatusVisible(boolean v) {
		searchResultLabel.setVisible(v);
		searchResultStatus.setVisible(v);
		validate();	
	}

	private void editResult(Media mm) {
		openItemDialog(mm, model.getItemQuantity(mm.getID()));	
		
		if (itemDialog.getDone() == true){	
			editResultHelper(itemDialog.getItem());
		}
	
		if (itemDialog != null){
			itemDialog.initUI();
		}
	}

	private void editResultHelper(Media mm) {
		Media temp;
		String ID = mm.getID();
		String title = mm.getTitle();
		String quantity = itemDialog.getQuantity();
		String genre = mm.getGenre();
		String description = mm.getDescription();
		
		if (mm instanceof CD){
			CD item = (CD) mm;

			String artist = item.getArtist();
			temp = new CD(ID, title, description , genre, artist);
			controller.editItem(temp, quantity); 
			//displayResult(temp);
		}
		else if (mm instanceof DVD){
			DVD item = (DVD) mm;

			String cast = item.getCast();
			temp = new DVD(ID, title, description , genre, cast);
			controller.editItem(temp, quantity); 
			//displayResult(temp);
		}
		else if (mm instanceof Book){
			Book item = (Book) mm;

			String author = item.getAuthor();
			String ISBN = item.getISBN();
			temp = new Book(ID, title, description , genre, author, ISBN);
			controller.editItem(temp, quantity); 
			//displayResult(temp);
		}
		controller.searchItem(ID);

	}

	private void displayResult(Media mm) {
		if (mm instanceof CD){						
			itemDetails.setText("<html>" + "<h3><font color=blue>Item ID</font></h3>" + mm.getID() + "<br>" 
					+ "<h3><font color=blue>Quantity</font></h3>" + model.getItemQuantity(mm.getID()) + "<br>"
					+ "<h3><font color=blue>Title</font></h3>" + mm.getTitle() + "<br>" 
					+ "<h3><font color=blue>Artist(s)</font></h3>" + ((CD)mm).getArtist() + "<br>"
					+ "<h3><font color=blue>Genre</font></h3>" + mm.getGenre() + "<br>" 
					+ "<h3><font color=blue>Description</font></h3>" + mm.getDescription() + "<br>" +  "</html>");
		}
		else if (mm instanceof DVD){						
			itemDetails.setText("<html>" + "<h3><font color=blue>Item ID</font></h3>" + mm.getID() + "<br>" 
					+ "<h3><font color=blue>Quantity</font></h3>" + model.getItemQuantity(mm.getID()) + "<br>"
					+ "<h3><font color=blue>Title</font></h3>" + mm.getTitle() + "<br>" 
					+ "<h3><font color=blue>Cast(s)</font></h3>" + ((DVD)mm).getCast() + "<br>"
					+ "<h3><font color=blue>Genre</font></h3>" + mm.getGenre() + "<br>" 
					+ "<h3><font color=blue>Description</font></h3>" + mm.getDescription() + "<br>" +  "</html>");
		}
		else if (mm instanceof Book){						
			itemDetails.setText("<html>" + "<h3><font color=blue>Item ID</font></h3>" + mm.getID() + "<br>" 
					+ "<h3><font color=blue>Quantity</font></h3>" + model.getItemQuantity(mm.getID()) + "<br>"
					+ "<h3><font color=blue>Title</font></h3>" + mm.getTitle() + "<br>" 
					+ "<h3><font color=blue>Author(s)</font></h3>" + ((Book)mm).getAuthor() + "<br>"
					+ "<h3><font color=blue>ISBN</font></h3>" + ((Book)mm).getISBN() + "<br>"
					+ "<h3><font color=blue>Genre</font></h3>" + mm.getGenre() + "<br>" 
					+ "<h3><font color=blue>Description</font></h3>" + mm.getDescription() + "<br>" +  "</html>");
		}
		validate();
	}

	private void clearItemDetails() {
		itemDetails.setText("");
	}

	private void displayTable() {
		//Remove old table from frame
		if (scrollPane != null)
			remove(scrollPane);
		
		table = new JTable((TableModel) model); 
        //table.getSelectionModel().addListSelectionListener(new RowListener());
        //table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
       // table.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		
		//Show total number of contacts on status bar
		//totalContact = contacts.getSize();
		//totalContactsStatus.setText(String.valueOf(totalContact));
		//statusPanel.setPreferredSize(new Dimension(getWidth(), 25));
		//statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		//totalContactsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		//totalContactsStatus.setHorizontalAlignment(SwingConstants.LEFT);
		
		//Refresh frame components in case table contents are changed.
		validate();
	}
	
    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            tableRowNum = table.getSelectedRow();
            tableRowSelected = true;
        }
    }
 
    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
        }
    }
}