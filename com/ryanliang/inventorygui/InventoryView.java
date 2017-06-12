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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class InventoryView extends JFrame implements Viewable{
	
	private final Controllable controller;
	private Modellable model;
	
	private static String itemID = "0";
	private String itemQuantity = "0";
	
	private CustomDialog subDialog = null;
	
	private JTextField DBServerURLField = new JTextField("jdbc:mysql://localhost:3306/media",50);
	private JTextField userNameField = new JTextField();
	private JTextField passWordField = new JPasswordField();
	private String DBServerURL;
	private String userName;
	private String passWord;
	   
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu editMenu = new JMenu("Edit");
	private final JMenu helpMenu = new JMenu("Help");
	
	private final JMenuItem loginFileMenu = new JMenuItem("Login");
	private final JMenuItem exitFileMenu = new JMenuItem("Exit");
	
	private final JMenuItem newEditMenu = new JMenuItem("New");
	private final JMenuItem editEditMenu = new JMenuItem("Edit"); 
	private final JMenuItem deleteEditMenu = new JMenuItem("Delete");
	private final JMenuItem deleteAllCDsEditMenu = new JMenuItem("Delete all CDs");
	private final JMenuItem deleteAllDVDsEditMenu = new JMenuItem("Delete all DVDs");
	private final JMenuItem deleteAllBooksEditMenu = new JMenuItem("Delete all Books");
	private final JMenuItem searchEditMenu = new JMenuItem("Search"); 
	
	private final JMenuItem aboutHelpMenu = new JMenuItem("About");
	
	private final JButton newToolBarButton = new JButton("New"); 
	private final JButton editToolBarButton = new JButton("Edit"); 
	private final JButton deleteToolBarButton = new JButton("Delete"); 
	private final JButton findToolBarButton = new JButton("Search");  
	private final JButton CDsToolBarButton = new JButton("CDs"); 
	private final JButton DVDsToolBarButton = new JButton("DVDs"); 
	private final JButton BooksToolBarButton = new JButton("Books"); 
	
	private final JToolBar toolBar = new JToolBar();
	private final JPanel statusPanel = new JPanel();
	
	private final JLabel searchResultLabel = new JLabel("Search result: ");
	private JLabel searchResultStatus = new JLabel("");
	
	private JTable table = null;
	private JScrollPane scrollPane = null;
	private int tableRowNum = 0;
	private boolean tableRowSelected = false; 
	
	public InventoryView(Controllable controller) {
		super("Media inventory system");
		this.controller = controller;
				
		organizeUI();
		addListeners();
		getLoginCredential();
			
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	private void getLoginCredential() {
		   Object[] message = {
			   "Database Server URL:", DBServerURLField,
		       "User Name:", userNameField,
		       "Password:", passWordField
		   };

		   int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
		   if (option == JOptionPane.OK_OPTION) {
			   DBServerURL = DBServerURLField.getText().trim();
			   userName = userNameField.getText().trim();
			   passWord = passWordField.getText().trim();
			   
			   controller.getDBConnection(DBServerURL, userName, passWord);
		   }
	}

	private void addListeners() {
		
		loginFileMenu.addActionListener(event -> getLoginCredential());
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Media inventory system v1.0 Copyright 2017 RLTech Inc");
		});
		
		newEditMenu.addActionListener(event -> newItem());
		searchEditMenu.addActionListener(event -> searchItem());
		deleteEditMenu.addActionListener(event -> deleteItem());
		
		deleteAllCDsEditMenu.addActionListener(event -> deleteMultipleItems(MediaCategory.CD));
		deleteAllDVDsEditMenu.addActionListener(event -> deleteMultipleItems(MediaCategory.DVD));
		deleteAllBooksEditMenu.addActionListener(event -> deleteMultipleItems(MediaCategory.BOOK));
		editEditMenu.addActionListener(event -> editItem());
		
		newToolBarButton.addActionListener(event -> newItem());
		findToolBarButton.addActionListener(event -> searchItem());
		deleteToolBarButton.addActionListener(event -> deleteItem());
		editToolBarButton.addActionListener(event -> editItem());
		
		CDsToolBarButton.addActionListener(event -> controller.searchItem("", MediaCategory.CD));
		DVDsToolBarButton.addActionListener(event -> controller.searchItem("", MediaCategory.DVD));
		BooksToolBarButton.addActionListener(event -> controller.searchItem("", MediaCategory.BOOK));

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	private void organizeUI() {
		fileMenu.add(loginFileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitFileMenu);
		
		editMenu.add(newEditMenu);
		editMenu.add(editEditMenu);
		editMenu.add(deleteEditMenu);
		editMenu.add(deleteAllCDsEditMenu);
		editMenu.add(deleteAllDVDsEditMenu);
		editMenu.add(deleteAllBooksEditMenu);
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

		add(toolBar, BorderLayout.NORTH);
				
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.add(searchResultLabel);
		statusPanel.add(searchResultStatus);
		setSearchResultStatusVisible(false);
	}

	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		controller.disconnectFromDatabase();
    		System.exit(0);
    	}
    }

	private void deleteMultipleItems(MediaCategory media) {
		int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all " + media + "?");
		if (answer == JOptionPane.YES_OPTION){	
			controller.deleteItem(media);
			removeTable();
		}
	}
	
	private void searchItem() {	
		openCustomDialog(null, "", DialogMode.SEARCH_ITEMS);
		if (subDialog.getDone() == true){
			removeTable();
			
			MediaCategory media = subDialog.getMedia();
			String search = subDialog.getSearch();
			
			if (search != null){				
				search = search.trim();
				if (!search.equals("") && Utility.isNumeric(search)){
					controller.searchItem(search);
				}
				else{
					controller.searchItem(search, media);
				}
			}
		}
		if (subDialog != null){
			subDialog.initUI();
		}
	}

	private void removeTable() {
		if (scrollPane != null){
			remove(scrollPane);
			tableRowSelected = false;
			validate();	
			repaint();	
		}
	}

	private void editItem() {
		String input;
		String message = "Enter item ID number or select a table row then click the Edit button";
		
		setSearchResultStatusVisible(false);		
		input = getItemID(message);
		
		if (input != null){
			removeTable();
			controller.searchItemForEditing(input);
		}
	}

	private String getItemID(String message) {
		String input; 
		
		//No need to input item ID if user already selected a JTable row.
		if (tableRowSelected){
			input = table.getValueAt(tableRowNum, 0).toString();
			tableRowSelected = false;
		}
		else 
			input = JOptionPane.showInputDialog(message);
		
		return input;
	}

	private void deleteItem(){
		String input;
		String message = "Enter item ID number or select a table row then click the Delete button";
		
		setSearchResultStatusVisible(false);	
		input = getItemID(message);
		
		if (input != null){
			removeTable();
			controller.deleteItem(input);
		}
	}

	private void newItem() {
		setSearchResultStatusVisible(false);
		
		//Generate item ID which will be needed in the openCustomDialog()
		controller.generateID();

		openCustomDialog(null, "", DialogMode.NEW_ITEM);
		if (subDialog.getDone() == true){
			removeTable();
			
			Media item = subDialog.getItem();
			controller.addItem(item, subDialog.getQuantity());
			//Call searchItem() here causes new item to be displayed.
			controller.searchItem(item.getID());
		}
		if (subDialog != null){
			subDialog.initUI();
		}
	}
	
	private void openCustomDialog(Media m, String quantity, DialogMode mode) {
		if (subDialog == null)
			subDialog = new CustomDialog(this);
		
		subDialog.resetRadioButtonGroup();
		if (mode == DialogMode.NEW_ITEM){
			subDialog.showDialog(itemID);
		}
		else if (mode == DialogMode.EDIT_ITEM){
			subDialog.showDialog(m, quantity);
		}
		else if (mode == DialogMode.SEARCH_ITEMS){
			subDialog.showDialog();
		}
		subDialog.setLocationRelativeTo(this);
		subDialog.setDone(false);
		subDialog.setVisible(true);
	}

	@Override
	public void setModel(Modellable model) {
		this.model = model;
	}

	@Override
	public void start() {
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
			if (model.getNumberOfRows() < 1){
				setSearchResultStatusVisible(false);
				removeTable();
				JOptionPane.showMessageDialog(null, "Item does not exist", "alert", JOptionPane.ERROR_MESSAGE); 
			}
			else {
				displayTable();	
				setSearchResultStatusVisible(true);
			}
			//Must clear search result or edit dialog won't get expected data.  Calling model.getSearchResult() would also do the same thing and more.
			model.clearSearchResult();
		}
		else if (ut == UpdateType.EDIT){
			Media [] result = model.getSearchResult();
			
			if (model.getNumberOfRows() < 1)
				JOptionPane.showMessageDialog(null, "Item does not exist", "alert", JOptionPane.ERROR_MESSAGE); 
			else
				editResult(result[0]);			
		}
		else if (ut == UpdateType.ID){
			itemID = model.getID();
		}
		else if (ut == UpdateType.ITEM_QUANTITY){
			itemQuantity = model.getItemQuantity();
		}
		else if (ut == UpdateType.DB_CONNECTION_ERROR){
			JOptionPane.showMessageDialog(null, "Database connection related error has occurred.  Try login again, check database server status, and etc...", "alert", JOptionPane.ERROR_MESSAGE);  
		}
	}

	private void setSearchResultStatusVisible(boolean v) {
		searchResultLabel.setVisible(v);
		searchResultStatus.setVisible(v);
		validate();	
	}

	private void editResult(Media mm) {
		model.checkItemQuantity(mm.getID());
		openCustomDialog(mm, itemQuantity, DialogMode.EDIT_ITEM);	
		
		if (subDialog.getDone() == true){	
			editResultHelper(subDialog.getItem());
		}
		if (subDialog != null){
			subDialog.initUI();
		}
	}

	private void editResultHelper(Media mm) {
		Media temp;
		String ID = mm.getID();
		String title = mm.getTitle();
		String quantity = subDialog.getQuantity();
		String genre = mm.getGenre();
		String description = mm.getDescription();
		
		if (mm instanceof CD){
			CD item = (CD) mm;

			String artist = item.getArtist();
			temp = new CD(ID, title, description , genre, artist);
			controller.editItem(temp, quantity); 
		}
		else if (mm instanceof DVD){
			DVD item = (DVD) mm;

			String cast = item.getCast();
			temp = new DVD(ID, title, description , genre, cast);
			controller.editItem(temp, quantity); 
		}
		else if (mm instanceof Book){
			Book item = (Book) mm;

			String author = item.getAuthor();
			String ISBN = item.getISBN();
			temp = new Book(ID, title, description , genre, author, ISBN);
			controller.editItem(temp, quantity); 
		}
		controller.searchItem(ID);

	}

	private void displayTable() {
		//Remove old table from frame
		removeTable();
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment( SwingConstants.LEFT);
		
		table = new JTable((TableModel) model); 
		table.setDefaultRenderer(Integer.class, leftRenderer);
        table.getSelectionModel().addListSelectionListener(new RowListener());
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
        table.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		
		searchResultStatus.setText(String.valueOf((model.getNumberOfRows())));
		
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