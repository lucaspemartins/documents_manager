package org.debreuck.neirynch.documentsmanager.model;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Generated
@JsonIgnoreProperties(value = { "serialVersionUID" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report implements Serializable {
	
	private static final long serialVersionUID = 504384766655269934L;
	
	private Set<Rendering> rendering;
	private Summary summary = new Summary();
	
	public Report(Set<Rendering> rederingSet) {
		this.rendering = rederingSet;
	}
}
