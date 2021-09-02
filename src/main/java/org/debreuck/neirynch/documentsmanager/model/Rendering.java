package org.debreuck.neirynch.documentsmanager.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Generated
public class Rendering implements Serializable, Comparable<Rendering> {
	private static final long serialVersionUID = 1135837495831748648L;

	@EqualsAndHashCode.Include
	private Long document;
	@EqualsAndHashCode.Include
	private Long page;
	@EqualsAndHashCode.Include
	private String uid;
	@ApiModelProperty(value = "List of data", example = "[\"2010-10-06 09:03:05,873\", \"2010-10-06 09:03:06,547\"]")
	private List<LocalDateTime> start = new ArrayList<>();
	@ApiModelProperty(value = "List of data", example = "[\"2010-10-06 09:03:05,873\", \"2010-10-06 09:03:06,547\"]")
	private List<LocalDateTime> get = new ArrayList<>();

	public Rendering(long document, long page, String uid) {
		this.document = document;
		this.page = page;
		this.uid = uid;
	}

	@Override
	public int compareTo(Rendering o) {
		return Comparator.comparing(Rendering::getDocument).thenComparing(Rendering::getPage)
				.thenComparing(Rendering::getUid).compare(this, o);
	}
}
