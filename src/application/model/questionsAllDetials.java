package application.model;

import java.sql.Blob;

public class questionsAllDetials {

	
public questionsAllDetials(int qid, String questionText, String firstChoice, String secondChoice,
			String thirdChoice, String fourthChoice, int marks, int timeAssigned, int categoryid,
			int subcategoryid, int type_id, int difficulty,String addedBy,int correctAnswer,Blob image) {
		super();
		this.qid = qid;
		this.questionText = questionText;
		this.firstChoice = firstChoice;
		this.secondChoice = secondChoice;
		this.thirdChoice = thirdChoice;
		this.fourthChoice = fourthChoice;
		this.marks = marks;
		this.timeAssigned = timeAssigned;
		this.categoryid = categoryid;
		this.subcategoryid = subcategoryid;
		this.type_id = type_id;
		this.difficulty = difficulty;
		this.addedBy=addedBy;
		this.correctAnswer=correctAnswer;
	this.image=image;}
int qid;
String questionText;
String firstChoice;
String secondChoice;
String thirdChoice;
String fourthChoice;
int marks;
int timeAssigned;
Blob image;
int categoryid;
int subcategoryid;
int type_id;
int difficulty;
String addedBy;
int correctAnswer;


public int getCorrectAnswer() {
	return correctAnswer;
}
public void setCorrectAnswer(int correctAnswer) {
	this.correctAnswer = correctAnswer;
}
public String getAddedBy() {
	return addedBy;
}
public void setAddedBy(String addedBy) {
	this.addedBy = addedBy;
}
public int getType_id() {
	return type_id;
}
public void setType_id(int type_id) {
	this.type_id = type_id;
}
public int getDifficulty() {
	return difficulty;
}
public void setDifficulty(int difficulty) {
	this.difficulty = difficulty;
}
public int getCategoryid() {
	return categoryid;
}
public void setCategoryid(int categoryid) {
	this.categoryid = categoryid;
}
public int getSubcategoryid() {
	return subcategoryid;
}
public void setSubcategoryid(int subcategoryid) {
	this.subcategoryid = subcategoryid;
}
public int getQid() {
	return qid;
}
public void setQid(int qid) {
	this.qid = qid;
}
public String getQuestionText() {
	return questionText;
}
public void setQuestionText(String questionText) {
	this.questionText = questionText;
}
public String getFirstChoice() {
	return firstChoice;
}
public void setFirstChoice(String firstChoice) {
	this.firstChoice = firstChoice;
}
public String getSecondChoice() {
	return secondChoice;
}
public void setSecondChoice(String secondChoice) {
	this.secondChoice = secondChoice;
}
public String getThirdChoice() {
	return thirdChoice;
}
public void setThirdChoice(String thirdChoice) {
	this.thirdChoice = thirdChoice;
}
public String getFourthChoice() {
	return fourthChoice;
}
public void setFourthChoice(String fourthChoice) {
	this.fourthChoice = fourthChoice;
}
public int getMarks() {
	return marks;
}
public void setMarks(int marks) {
	this.marks = marks;
}
public int getTimeAssigned() {
	return timeAssigned;
}
public void setTimeAssigned(int timeAssigned) {
	this.timeAssigned = timeAssigned;
}
public Blob getImage() {
	return image;
}
public void setImage(Blob image) {
	this.image = image;
}




}
