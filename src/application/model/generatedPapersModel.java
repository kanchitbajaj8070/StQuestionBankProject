package application.model;

import java.sql.Date;

public class generatedPapersModel {
	int paperId;
	String paperName;
	String createdBy;
	String college;
	String purpose;
	java.sql.Date createdDate;
	java.sql.Date examDate;
	String course;

public generatedPapersModel(String paperName, Date createdDate,Date examDate, String college,String course ,String purpose,String createdBy) 
{
		super();
		this.paperName = paperName;
		this.createdBy = createdBy;
		this.college = college;
		this.purpose = purpose;
		this.createdDate = createdDate;
		this.examDate = examDate;
		this.course=course;
	}

public String getCourse() {
	return course;
}
public void setCourse(String course) {
	this.course = course;
}
public int getPaperId() {
	return paperId;
}
public void setPaperId(int paperId) {
	this.paperId = paperId;
}
public String getPaperName() {
	return paperName;
}
public void setPaperName(String paperName) {
	this.paperName = paperName;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public String getCollege() {
	return college;
}
public void setCollege(String college) {
	this.college = college;
}
public String getPurpose() {
	return purpose;
}
public void setPurpose(String purpose) {
	this.purpose = purpose;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public Date getExamDate() {
	return examDate;
}
public void setExamDate(Date examDate) {
	this.examDate = examDate;
}



}
