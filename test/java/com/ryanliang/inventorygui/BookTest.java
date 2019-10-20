package com.ryanliang.inventorygui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BookTest {
	
	Book book;
	String author;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		author = "Ryan L";
		book = new Book("2", "test", "des", "gen", author, "insb");
	}

	@After
	public void tearDown() throws Exception {
		author = null;
		book = null;
	}

	@Test
	public void test() {
		assertEquals(author, book.getAuthor());
	}

}
