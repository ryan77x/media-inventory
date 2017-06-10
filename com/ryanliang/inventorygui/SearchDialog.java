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

	private boolean done = false;
	private String search = "";
	private MediaCategory media = MediaCategory.CD;
	
	private final JPanel radioButtonPanel = new JPanel();
	private final JPanel textFieldPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();

	private final ButtonGroup radioGroup = new ButtonGroup();
	private final JRadioButton CDRadioButton = new JRadioButton("CD", false);
	private final JRadioButton DVDRadioButton = new JRadioButton("DVD", false);
	private final JRadioButton bookRadioButton = new JRadioButton("Book", false);
	
	private final JTextField searchField = new JTextField(20);
	private final JButton doneButton = new JButton("Search"); 

	public SearchDialog(JFrame frame){
		super(frame, "Search dialog", true);
		
		Dimension frameSize = frame.getSize();
		int frameWidth = frameSize.width;
		int frameHeight = frameSize.height;
		setSize(frameWidth/5,frameHeight/7);
		
		organizeUI();
		addListeners();
	}

	private void addListeners() {	
		CDRadioButton.addActionListener(event -> {
			media = MediaCategory.CD;
		});
		
		DVDRadioButton.addActionListener(event -> {
			media = MediaCategory.DVD;
		});
		
		bookRadioButton.addActionListener(event -> {
			media = MediaCategory.BOOK;
		});
		
		doneButton.addActionListener(this);
	}

	private void organizeUI() {
		radioGroup.add(CDRadioButton);
		radioGroup.add(DVDRadioButton);
		radioGroup.add(bookRadioButton);
		radioButtonPanel.add(CDRadioButton);
		radioButtonPanel.add(DVDRadioButton);
		radioButtonPanel.add(bookRadioButton);

		buttonPanel.add(doneButton);
		
		textFieldPanel.add(searchField);
		
		add(radioButtonPanel, BorderLayout.NORTH);
		add(textFieldPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		search = searchField.getText().trim();		
		resetSettings();
	}
	
	private void resetSettings() {
		done = true;
		setVisible(false);
	}

	public void setDone(boolean b) {
		done = b;
	}

	public boolean getDone() {
		return done;
	}

	public void resetRadioButtonGroup() {
		radioGroup.clearSelection();
		searchField.setText("");
		media = MediaCategory.CD;
	}

	public String getSearch() {
		return search;
	}

	public MediaCategory getMedia() {
		return media;
	}
}
