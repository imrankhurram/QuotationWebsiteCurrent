package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.List;

import com.nextcontrols.bureaudomain.Template;

public class GenericTemplatesPageBean implements Serializable {
	private List<Template> letterTemplates ;
	private Template selectedLetterTemplate;
	private List<Template> filteredLetterTemplates;
	private Template newLetterTemplate;
	public List<Template> getLetterTemplates() {
		return letterTemplates;
	}
	public void setLetterTemplates(List<Template> letterTemplates) {
		this.letterTemplates = letterTemplates;
	}
	public Template getSelectedLetterTemplate() {
		return selectedLetterTemplate;
	}
	public void setSelectedLetterTemplate(Template selectedLetterTemplate) {
		this.selectedLetterTemplate = selectedLetterTemplate;
	}
	public List<Template> getFilteredLetterTemplates() {
		return filteredLetterTemplates;
	}
	public void setFilteredLetterTemplates(List<Template> filteredLetterTemplates) {
		this.filteredLetterTemplates = filteredLetterTemplates;
	}
	public Template getNewLetterTemplate() {
		return newLetterTemplate;
	}
	public void setNewLetterTemplate(Template newLetterTemplate) {
		this.newLetterTemplate = newLetterTemplate;
	}
	
	
	
	
	
}
