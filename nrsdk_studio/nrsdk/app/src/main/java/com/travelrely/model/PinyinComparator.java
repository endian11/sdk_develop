package com.travelrely.model;

import java.util.Comparator;

/**
 * 
 *
 */
public class PinyinComparator implements Comparator<CountrySelectModel> {

	public int compare(CountrySelectModel o1, CountrySelectModel o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
