package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class questionsModel 
{
    private int id;
	private StringProperty questionText;
	private StringProperty type;
	private StringProperty subcategory;
	public questionsModel(String questionText, String type, String subcategory) {
		this.questionText = new SimpleStringProperty(questionText);
		this.type = new SimpleStringProperty(type);
		this.subcategory = new SimpleStringProperty(subcategory);
	}
	public StringProperty getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String string) {
		this.questionText = new SimpleStringProperty(string);
	}
	public StringProperty getType() {
		return type;
	}
	public void setType(StringProperty type) {
		this.type = type;
	}
	public StringProperty getSubcategory() {
		return subcategory;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSubcategory(StringProperty subcategory) {
		this.subcategory = subcategory;
	}
	public StringProperty questionTextProperty() {
		return questionText;
	}

	public StringProperty typeProperty() {
		return type;
	}
	public StringProperty subcategoryProperty() {
		return subcategory;
	}
}
