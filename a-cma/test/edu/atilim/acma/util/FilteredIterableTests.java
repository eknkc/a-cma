package edu.atilim.acma.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FilteredIterableTests implements Selector<Integer, String> {
	@Test
	public void testFilteredIterable() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
		
		FilteredIterable<Integer, String> fi = new FilteredIterable<Integer, String>(list, this);
		
		for (String s : fi) {
			int val = Integer.parseInt(s);
			assertTrue((val % 2) == 0);
		}
	}

	@Override
	public String select(Integer in) {
		if ((in % 2) == 0) {
			return in.toString();
		}
		return null;
	}
}
