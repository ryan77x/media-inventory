/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

import java.util.*; 
import javax.swing.table.*; 


@SuppressWarnings("serial")
public class PropertiesTableModel extends AbstractTableModel
{ 
	private String[][] data = null; 
	private String[] columnNames = {
		"itemID",
		"Title",
		"Description",
		"Genre",
		"Artist"
	};

	public PropertiesTableModel(Media [] mediaArray) 
	{ 

		List<String[]> list = new ArrayList<String[]>(); 

		for(Media mm : mediaArray) 
		{ 

			list.add(new String[] {mm.getID(), mm.getTitle(), mm.getDescription(), mm.getGenre(), ((CD)mm).getArtist()}); 
		} 


		this.data = list.toArray(new String[list.size()][columnNames.length]); 
	} 


	public String getColumnName(int column) 
	{ 

		return columnNames[column];
	} 


	public int getRowCount() 
	{ 
		return this.data.length; 
	} 


	public int getColumnCount() 
	{ 
		return columnNames.length; 
	} 


	public Object getValueAt(int row, int column) 
	{ 
		return data[row][column]; 
	} 
} 
