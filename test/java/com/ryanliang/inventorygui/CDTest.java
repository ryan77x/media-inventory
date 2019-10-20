package com.ryanliang.inventorygui;

import org.junit.Assert;
import org.junit.Test;

public class CDTest {
	String artist ="ryan";
	CD cd1 = new CD("1", "star", "good music", "rock", artist);
	
	@Test
	public void getArtistTest(){
		Assert.assertEquals(artist, cd1.getArtist());
	}
	
}
