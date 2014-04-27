package com.coltsoftware.liquidsledgehammer.subtransactions.strategies;

import java.util.ArrayList;

public final class ContainsDescriptionStrategy implements DescriptionStrategy {

	private final String groupName;
	private final ArrayList<String> matches = new ArrayList<String>();

	public ContainsDescriptionStrategy(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String unassigned(String description) {
		if (description == null)
			return null;
		description = description.toLowerCase();
		for (String match : matches) {
			if (description.contains(match))
				return groupName;
		}
		return null;
	}

	public void addMatch(String match) {
		matches.add(match.toLowerCase());
	}

}
