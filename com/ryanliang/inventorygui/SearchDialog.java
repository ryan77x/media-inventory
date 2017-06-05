/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SearchDialog extends JDialog implements ActionListener{

	private Media item = null;
	private boolean done = false;
	private String itemID = null;
	private String search = "";
	private MediaCategory media = null;
		
	private final JPanel CDMainPanel = new JPanel(new BorderLayout());
	private final JPanel DVDMainPanel = new JPanel(new BorderLayout());
	private final JPanel bookMainPanel = new JPanel(new BorderLayout());
	
	private final JPanel radioButtonPanel = new JPanel();
	private final JPanel CDTextFieldPanel = new JPanel();
	private final JPanel DVDTextFieldPanel = new JPanel();
	private final JPanel bookTextFieldPanel = new JPanel();

	private final JPanel CDLabelPanel = new JPanel();
	private final JPanel DVDLabelPanel = new JPanel();
	private final JPanel bookLabelPanel = new JPanel();
	
	private final JPanel buttonPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	
	private final ButtonGroup radioGroup = new ButtonGroup();
	private final JRadioButton CDRadioButton = new JRadioButton("CD", false);
	private final JRadioButton DVDRadioButton = new JRadioButton("DVD", false);
	private final JRadioButton bookRadioButton = new JRadioButton("Book", false);
	
	private final JTextField searchField = new JTextField(20);
	
	private JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
	private final JButton doneButton = new JButton("Search"); 

	public SearchDialog(JFrame frame){
		super(frame, "Search dialog", true);
		
		Dimension frameSize = frame.getSize();
		int frameWidth = frameSize.width;
		int frameHeight = frameSize.height;
		setSize(frameWidth/3,frameHeight/3);
		
		organizeUI();
		addListeners();
	}

	private void addListeners() {	
		CDRadioButton.addActionListener(event -> {
			addJPanel(CDMainPanel);
			media = MediaCategory.CD;
			

		});
		
		DVDRadioButton.addActionListener(event -> {
			addJPanel(CDMainPanel);
			media = MediaCategory.DVD;
			

		});
		
		bookRadioButton.addActionListener(event -> {
			addJPanel(CDMainPanel);
			media = MediaCategory.BOOK;
			

		});
		
		doneButton.addActionListener(this);
	}

	private void organizeUI() {
		setLayout(new BorderLayout());
		GridLayout textFieldLayout = new GridLayout(0,1);

		radioGroup.add(CDRadioButton);
		radioGroup.add(DVDRadioButton);
		radioGroup.add(bookRadioButton);
		radioButtonPanel.add(CDRadioButton);
		radioButtonPanel.add(DVDRadioButton);
		radioButtonPanel.add(bookRadioButton);
				
		buttonPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new BorderLayout());

		buttonPanel.add(doneButton);
		southPanel.add(errorLabel, BorderLayout.CENTER);
		southPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		CDTextFieldPanel.setLayout(textFieldLayout);
		CDLabelPanel.setLayout(textFieldLayout);
		
		DVDTextFieldPanel.setLayout(textFieldLayout);
		DVDLabelPanel.setLayout(textFieldLayout);
		
		bookTextFieldPanel.setLayout(textFieldLayout);
		bookLabelPanel.setLayout(textFieldLayout);

		CDMainPanel.add(CDLabelPanel, BorderLayout.WEST);
		CDMainPanel.add(CDTextFieldPanel, BorderLayout.CENTER);
		DVDMainPanel.add(DVDLabelPanel, BorderLayout.WEST);
		DVDMainPanel.add(DVDTextFieldPanel, BorderLayout.CENTER);
		bookMainPanel.add(bookLabelPanel, BorderLayout.WEST);
		bookMainPanel.add(bookTextFieldPanel, BorderLayout.CENTER);
		
		CDTextFieldPanel.add(searchField);
		
	}

	private void addJPanel(JPanel panel) {
		removePanels();
		
		add(panel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);

		validate();
		repaint();	
	}

	public void actionPerformed(ActionEvent e) {
		

		errorLabel.setForeground(Color.RED);

		search = searchField.getText();

				
		resetSettings();

	}
	
	private void resetSettings() {
		done = true;
		setVisible(false);
	}

	
	public Media getItem() {
		return item;
	}

	public void setDone(boolean b) {
		done = b;
	}

	public boolean getDone() {
		return done;
	}

	public void initializeTextFields(Media m, String quant) {
		itemID = m.getID();
		
		if (m instanceof CD){   
			add(CDMainPanel);
			media = MediaCategory.CD;
			

		}
		else if (m instanceof DVD){   
			add(DVDMainPanel);
			media = MediaCategory.DVD;
			

		}
		else if (m instanceof Book){   
			add(bookMainPanel);
			media = MediaCategory.BOOK;
			

		}
		add(southPanel, BorderLayout.SOUTH);
	}

	public void inputItemDetails(String itemID) {
		this.itemID = itemID;

		add(radioButtonPanel, BorderLayout.NORTH);		
	}

	private void removePanels() {
		remove(CDMainPanel);
		remove(DVDMainPanel);
		remove(bookMainPanel);
		remove(southPanel);
	}

	public void resetRadioButtonGroup() {
		radioGroup.clearSelection();
	}

	public void initUI() {
		errorLabel.setText("");
		remove(radioButtonPanel);
		removePanels();
	}

	public String getSearch() {
		return search;
	}

	public MediaCategory getMedia() {
		return media;
	}
}
