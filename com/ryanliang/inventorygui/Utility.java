/**
 *
 * @author Ryan L.
 */

package com.ryanliang.inventorygui;

public class Utility {

	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException ee)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
}
