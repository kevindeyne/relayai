package com.kevindeyne.tasker;

import java.util.HashSet;
import java.util.Set;

public class MatchFinder {

	public MatchFinder() {}

	public static int findMatches(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<String>(s1);
		intersection.retainAll(s2);

		System.out.println(intersection);

		return intersection.size();
	}

}
