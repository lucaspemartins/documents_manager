package org.debreuck.neirynch.documentsmanager.model;

import java.io.Serializable;
import java.util.Comparator;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class Summary implements Serializable, Comparable<Summary> {
	private static final long serialVersionUID = -5975097825536055581L;

	private Long count;
	private Long duplicates;
	private Long unnecessary;
	
	@Override
	public int compareTo(Summary o) {
		return Comparator.comparing(Summary::getCount).thenComparing(Summary::getDuplicates)
				.thenComparing(Summary::getUnnecessary).compare(this, o);
	}
}
