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
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	
	private Media [] searchResult;
	private static String itemID = "0";
	private String itemQuantity = "0";
	
	private ItemDialog itemDialog = null;
	private SearchDialog searchDialog = null;
	
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu editMenu = new JMenu("Edit");
	private final JMenu helpMenu = new JMenu("Help");
	
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
			
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	private void addListeners() {
		
		exitFileMenu.addActionListener(event -> {
			quitApp();
		});
	
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
		if (searchDialog == null)
			searchDialog = new SearchDialog(this);
		
		searchDialog.resetRadioButtonGroup();
		
		searchDialog.setLocationRelativeTo(this);
		searchDialog.setDone(false);
		searchDialog.setVisible(true);
		
		if (searchDialog.getDone() == true){
			removeTable();
			
			MediaCategory media = searchDialog.getMedia();
			String search = searchDialog.getSearch();
			
			if (search != null){				
				search = search.trim();
				if (!search.equals("") && Utility.isNumeric(search)){
					controller.searchItem(search);
				}
				else
					controller.searchItem(search, media);
			}
		}
	}

	private void removeTable() {
		if (scrollPane != null){
			remove(scrollPane);
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
		Media temp = null;
		
		//Generate item ID which will be needed in the openItemDialog()
		controller.generateID();

		openItemDialog(temp, "");
		if (itemDialog.getDone() == true){
			removeTable();
			
			Media item = itemDialog.getItem();
			controller.addItem(item, itemDialog.getQuantity());
			//Call searchItem() here causes new item to be displayed.
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
			System.out.println(searchResult.length);
			if (model.getNumberOfRows() < 1){
				setSearchResultStatusVisible(false);
				removeTable();
				JOptionPane.showMessageDialog(null, "Item does not exist", "alert", JOptionPane.ERROR_MESSAGE); 
			}
			else {
				displayTable();	
				setSearchResultStatusVisible(true);
			}
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
	}

	private void setSearchResultStatusVisible(boolean v) {
		searchResultLabel.setVisible(v);
		searchResultStatus.setVisible(v);
		validate();	
	}

	private void editResult(Media mm) {
		model.checkItemQuantity(mm.getID());
		openItemDialog(mm, itemQuantity);	
		
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