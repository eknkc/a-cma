package edu.atilim.acma.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class BiMapTests {
	@Test
	public void testBiMap() {
		BiMap<String, String> map = new BiMap<String, String>();
		map.put("testk", "testv");
		assertEquals("testv", map.get("testk"));
		assertEquals("testk", map.getKey("testv"));
		map.put("testk2", "testv2");
		assertEquals("testv", map.get("testk"));
		assertEquals("testk", map.getKey("testv"));
		assertEquals("testv2", map.get("testk2"));
		assertEquals("testk2", map.getKey("testv2"));
		assertTrue(map.containsKey("testk"));
		assertTrue(map.containsKey("testk2"));
		assertFalse(map.containsKey("testk3"));
		map.put("testk3", "testv");
		assertEquals(2, map.size());
		assertEquals("testv", map.get("testk3"));
		assertEquals("testk3", map.getKey("testv"));
		map.clear();
		assertEquals(0, map.size());
	}
}
