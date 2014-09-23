package com.carecaminnovations.mobile.model;

import java.util.List;

/** Maintains the data for a single field in a post form. */
public interface FormPostElement {
	
	/**
	 * Gets the form data for this element. 
	 * @return a <code>List of result data
	 */
	List<FormPostData> getFormData();
}
