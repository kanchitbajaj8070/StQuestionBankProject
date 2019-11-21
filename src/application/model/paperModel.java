package application.model;

import java.util.Comparator;

import application.database.databaseHelper;

public class paperModel  {
    int pid;
    private static databaseHelper handler=null;
    String category;
    String subcategory;
    String noOfQuestions;
    String difficulty;
 String type;
    public paperModel(String category, String subcategory, String noOfQuestions, String difficulty,	String type) {
        this.category = category;
        this.subcategory = subcategory;
        this.noOfQuestions = noOfQuestions;
        this.difficulty = difficulty;
        this.type=type;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
