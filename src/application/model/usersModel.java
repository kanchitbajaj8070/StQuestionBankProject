package application.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class usersModel {

	private StringProperty username;
	private StringProperty role;
	private StringProperty categoryAssigned;
	public StringProperty getUserName() {
		return username;
	}
	
	public void setUserName(StringProperty userName) {
		this.username = userName;
	}

	public void setRole(StringProperty role) {
		this.role = role;
	}

	public void setCategoryAssigned(StringProperty categoryAssigned) {
		this.categoryAssigned = categoryAssigned;
	}

	public StringProperty getRole() {
		return role;
	}
	public StringProperty getCategoryAssigned() {
		return categoryAssigned;
	}
	public usersModel(String username, String role, String categoryAssigned) {
		this.username = new SimpleStringProperty(username);
		this.role = new SimpleStringProperty(role);
		this.categoryAssigned = new SimpleStringProperty(categoryAssigned);
	}
	

	public StringProperty usernameProperty() {
		return username;
	}
	public StringProperty roleProperty() {
		return role;
	}
		public StringProperty categoryAssignedProperty() {
			return categoryAssigned;
		}

	}


	