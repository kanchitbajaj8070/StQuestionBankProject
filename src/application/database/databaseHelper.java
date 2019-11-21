package application.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

import application.alert.alertMaker;
import application.controllers.homeController;
import application.controllers.loginController;
import application.model.generatedPapersModel;
import application.model.questionsAllDetials;
import application.model.questionsModel;
import application.model.usersModel;
import javafx.application.Platform;

public class databaseHelper {
	private static databaseHelper handler = null;
	private static String dbUrl = null;
	private static String dbUsername = null;
	private static String dbPassword = null;
	private static Connection conn;
	private static Statement stmt;
	static {
		createConnection();
	}
	public static databaseHelper getInstance() {
		if (handler == null) {
			handler = new databaseHelper();
		}
		return handler;
	}

	private static void createConnection() {
		try {
			FileInputStream fis = new FileInputStream("resources/db.properties");
			Properties p = new Properties();
			p.load(fis);
			dbUrl = (String) p.get("db.url");
			dbUsername = (String) p.get("db.user");
			dbPassword = (String) p.get("db.password");
			conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			System.out.println("connection made");

		} catch (Exception e) {
			e.printStackTrace();
			alertMaker.showErrorMessage("Error connecting to database server", "");
		}
	}

	public static void closeConnection() {
		try {
			handler.getInstance().getConnection().close();
		} catch (SQLException e) {
			alertMaker.showErrorMessage("Not able to close Db connection", "");
		}

	}

	public Boolean checkLogin(String username, String password) throws Exception {
		ResultSet result = null;
		String query = "SELECT count(*) from qb_user_profiles Where  username = ? and  password = ?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setString(1, username.toLowerCase());
		preparedStatement.setString(2, password);
		System.out.println(preparedStatement.toString());
		result = preparedStatement.executeQuery();
		if (result.next()) {
			if (result.getInt(1) == 1)
				return true;

		}

		return false;

	}

	public int countBySubcategoryId(int id, int level, int type) throws Exception {
		int p = -1;
		ResultSet result = null;
		String query = "SELECT count(*) from question_bank Where mark_to_delete=0 and subcategory_id=? and difficulty = ? and type =?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, id);
		preparedStatement.setInt(2, level);
		preparedStatement.setInt(3, type);
		result = preparedStatement.executeQuery();
		if (result.next())
			return result.getInt(1);
		return p;
	}

	public ArrayList<usersModel> getHomeDetials() {
		System.out.println("in home detials");
		 ArrayList<usersModel> list = new ArrayList<usersModel>();
		
		 ResultSet rs = null;
		
		 try{String query = "SELECT a.username,a.role,b.category_name from qb_user_profiles a, qb_categories b where a.category_assigned=b.category_id and a.username='"
				+ loginController.loggedUser.toLowerCase() + "'";
		rs = execQuery(query);
		while(rs.next())
		{	list.add(new usersModel(rs.getString(1),rs.getString(2),rs.getString(3)));
		}
		}
		 catch(Exception e)
		 {
			 alertMaker.showErrorMessage("error loading user detials","");
					 return null;
		 }
		return list;

	}

	public ArrayList<usersModel> loadUsersData() {
		ResultSet rs = null;
		ArrayList<usersModel> list = new ArrayList<usersModel>();
		try
		{
			String query = "SELECT a.username,a.role,b.category_name from qb_user_profiles a, qb_categories b where a.category_assigned=b.category_id and a.mark_to_delete=0 ";
		rs = execQuery(query);
		while(rs.next())
		{ 
		list.add(new usersModel(rs.getString(1),rs.getString(2),rs.getString(3)) );
		
		}
	

	}catch (Exception e) {
		alertMaker.showErrorMessage("error loading users data", "");
	}
		return list;
	}

	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException ex) {
			System.out.println("Exception at execQuery" + ex.getLocalizedMessage());
			return null;
		}

		return result;
	}

	public Connection getConnection() {
		return conn;
	}

	public static ArrayList<String> getCategories() throws Exception {
		ArrayList<String> categoriesList = new ArrayList<String>();
		ResultSet result = null;
		String query = "SELECT category_name from qb_categories Where mark_to_delete=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 0);
		result = preparedStatement.executeQuery();
		while (result.next())
			categoriesList.add(result.getString(1));
		return categoriesList;

	}

	public static int getCategoryId(String categoryName)  {
		int ans = 0;
		try{
			ResultSet result = null;
		
		String query = "SELECT category_id from qb_categories Where category_name=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setString(1, categoryName.toLowerCase());
		result = preparedStatement.executeQuery();
		if (result.next())

		{
			ans = result.getInt(1);
			System.out.println("id of " + categoryName + " is " + ans);
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return ans;

	}

	public static ArrayList<String> getSubCategories(String categoryName) throws Exception {
		System.out.println(" in get sub categories function");
		ArrayList<String> subCategories = new ArrayList<String>();
		ResultSet result = null;
		int id;
		try {
			id = getCategoryId(categoryName);

			String query = "SELECT category_name from qb_subcategories Where parent_id=?";

			PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
			preparedStatement.setInt(1, id);
			System.out.println("anout to execute query");
			result = preparedStatement.executeQuery();
			System.out.println("a executed query");
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());// TODO: handle exception
		}

		while (result.next()) {
			System.out.println("in while");
			subCategories.add(result.getString(1));

		}
		System.out.println("end of subcategory function");
		System.out.println(subCategories.toString());
		return subCategories;

	}

	public void updatePassword(String newPassword) throws Exception {
		try {
			String query = "update qb_user_profiles set password= ? where username =? ";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, newPassword);
			preparedStatement.setString(2, loginController.loggedUser);
			int p = preparedStatement.executeUpdate();
			System.out.println(p);
			if (p == 1) {
				System.out.println("Your password was updated succesfully ");

			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public ArrayList<questionsModel> loadQuestionData() throws Exception {
          ArrayList<questionsModel> list = new ArrayList<questionsModel>();
		ResultSet rs = null;
		try {
			String query = "select question_text,type,subcategory_id,q_id from question_bank where mark_to_delete=0  and subcategory_id in (select subcategory_id from qb_subcategories where mark_to_delete=0) and category_id in (select category_id from qb_categories where mark_to_delete=0) order by q_id desc";
			rs = execQuery(query);

			while (rs.next()) {
				questionsModel newq = new questionsModel(rs.getString(1),
						getQuestionTypeName(rs.getInt(2)),
						getQuestionSubcategoryName(rs.getInt(3)));
				newq.setId(rs.getInt(4));
				list.add(newq);
			
		}
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	public int loadCorrectAnswer(int id) throws Exception {
		int s = 0;
		ResultSet rs = null;
		try {
			String query = "select correct_answer from question_bank where mark_to_delete=0 and q_id =" + id;
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		if (rs.next())
			return rs.getInt(1);
		else
			return -1;

	}

	public String getQuestionCategoryName(int n) throws Exception {

		ResultSet rs = null;
		String query = "SELECT category_name from qb_categories where category_id =" + n;
		rs = execQuery(query);
		if (rs.next())
			return rs.getString(1);

		return null;
	}

	public String getQuestionSubcategoryName(int n) throws Exception {
		ResultSet rs = null;

		String query = "SELECT category_name from qb_subcategories where subcategory_id =" + n;
		rs = execQuery(query);
		if (rs.next())
			return rs.getString(1);

		return null;

	}

	/*
	 * public String getQuestionType(int n) throws Exception { return
	 * types.get(n-1);
	 * 
	 * ResultSet rs=null; String query =
	 * "SELECT type_name from qb_question_types where type_id ="+ n+";";
	 * rs=execQuery(query); if( rs.next()) return rs.getString(1); return null;
	 * 
	 * 
	 * }
	 */
	public int getSubcategoryId(String name) {
		ResultSet rs = null;
		
		try
		{String query = "SELECT  subcategory_id from qb_subcategories where category_name ='" + name + "'";
		rs = execQuery(query);
		if (rs.next())
			return rs.getInt(1);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int getTypeId(String type) throws Exception {
		ResultSet rs = null;
		try {
			String query = "select type_id from qb_type where lower(type_name)='" + type.toLowerCase() + "'";
			rs = execQuery(query);
			if (rs.next())
				return rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;
	}

	public int getDifficultyId(String type) throws Exception {
		int j = 0;
		ResultSet rs = null;
		try {
			String query = "select difficulty_level_id from qb_difficulty_level where lower(difficulty_level_name)='"
					+ type.toLowerCase() + "'";
			rs = execQuery(query);
			if (rs.next())
				return rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;
	}

	public int deleteQuestion(questionsModel rowToDelete) {
		int p = 0;
		try {
			String query = "update question_bank set mark_to_delete=1 where  q_id= ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(query);

			System.out.println("row id is" + rowToDelete.getId());
			preparedStatement.setInt(1, rowToDelete.getId());
			p = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("value of p is " + p);
		return p;
	}
	public int undeleteQuestion(questionsModel rowToDelete) {
		int p = 0;
		try {
			String query = "update question_bank set mark_to_delete=0 where  q_id= ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(query);

			System.out.println("row id is" + rowToDelete.getId());
			preparedStatement.setInt(1, rowToDelete.getId());
			p = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("value of p is " + p);
		return p;
	}

	public 	ArrayList<String> getAllCategories() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_categories where mark_to_delete=0";
			rs = execQuery(query);
			while(rs.next())
             list.add(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	public ArrayList<String> getAllSubcategories() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_subcategories where mark_to_delete=0";
			rs = execQuery(query);
			while(rs.next())
             list.add(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	public ArrayList<String> getAllUsers() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select username from qb_user_profiles where mark_to_delete=0";
			rs = execQuery(query);
			while(rs.next())
             list.add(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	public ArrayList<String> getAllTypes() throws Exception {

		ResultSet rs = null;
		try {
			String query = "select type_name from qb_type";
			rs = execQuery(query);

		} catch (Exception e) {
			alertMaker.showErrorMessage(e);
		}
		ArrayList<String> list = new ArrayList<String>();
		while (rs.next()) {
			list.add(rs.getString(1));

		}
		return list;

	}

	public questionsAllDetials getAllDetialsOfQuestionByQuestionId(int id) {
		questionsAllDetials obj = null;
		ResultSet rs = null;
		try {
			System.out.println(id);
			String query = "select q_id,question_text,choice1,choice2,choice3,choice4,marks,time_assigned_in_minutes,image,category_id,subcategory_id,type,difficulty,added_by,correct_answer from question_bank where q_id = "
					+ id;
			rs = execQuery(query);
			if (rs.next()) {
				Blob b = null;
				System.out.println(rs.getInt(1));
				obj = new questionsAllDetials(rs.getInt("q_id"), rs.getString("question_text"), rs.getString("choice1"),
						rs.getString("choice2"), rs.getString("choice3"), rs.getString("choice4"), rs.getInt("marks"),
						rs.getInt("time_assigned_in_minutes"), rs.getInt("category_id"), rs.getInt("subcategory_id"),
						rs.getInt("type"), rs.getInt("difficulty"), rs.getString("added_by"),
						rs.getInt("correct_answer"), rs.getBlob("image"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("in catch");
		}
		return obj;

	}

	public ResultSet getDetialsForPdf(int id) throws Exception {//not in use after configuring getAllDetialsByquestionId(int)

		ResultSet rs = null;
		try {
			String query = "select question_text,CHOICE1,CHOICE2,CHOICE3,CHOICE4,MARKS,TIME_ASSIGNED_IN_MINUTES from question_bank where q_id = "
					+ id;
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return rs;
		

	}

	public String getQuestionTypeName(int n) throws Exception {

		ResultSet rs = null;
		String query = "SELECT type_name from qb_type where type_id =" + n;
		rs = execQuery(query);
		if (rs.next())
			return rs.getString(1);

		return null;
	}

	public ArrayList<String> getSubcategoriesByCategoryId(int id)  {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_subcategories where parent_id = " + id
					+ "and mark_to_delete=0";
			rs = execQuery(query);
			while (rs.next()) {
				list.add(rs.getString(1));

		}} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		return list;
	}
	public ArrayList<String> getDeletedOrUndeletedSubcategoriesByCategoryId(int id)  {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_subcategories where parent_id = " + id;
			rs = execQuery(query);
			while (rs.next()) {
				list.add(rs.getString(1));

		}} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		return list;
	}

	public Boolean deleteCategoryById(int id) throws Exception {

		String query = "update  qb_categories set mark_to_delete =? Where category_id=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 1);
		preparedStatement.setInt(2, id);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);
		if (p == 0)
			return false;
		else
			return true;

	}
	public Boolean undeleteCategoryById(int id) throws Exception {

		String query = "update  qb_categories set mark_to_delete =0 Where category_id=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, id);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);
		if (p == 0)
			return false;
		else
			return true;

	}

	public Boolean deleteSubcategoryById(int childId, int parentId) throws Exception {

		String query = "update  qb_subcategories set mark_to_delete =? Where subcategory_id=? and parent_id=?";
		System.out.println("sub category is " + childId + " parent id is " + parentId);
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 1);
		preparedStatement.setInt(2, childId);
		preparedStatement.setInt(3, parentId);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);
		if (p == 0)
			return false;
		else
			return true;

	}
	public Boolean undeleteSubcategoryById(int childId, int parentId) throws Exception {

		String query = "update  qb_subcategories set mark_to_delete =? Where subcategory_id=? and parent_id=?";
		System.out.println("sub category is " + childId + " parent id is " + parentId);
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 0);
		preparedStatement.setInt(2, childId);
		preparedStatement.setInt(3, parentId);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);
		if (p == 0)
			return false;
		else
			return true;

	}
	public void deleteSubcategoryQuestion(int id) throws Exception {
	
try
{
		String query = "update  question_bank set mark_to_delete =? Where subcategory_id=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 1);
		preparedStatement.setInt(2, id);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);

	}catch (Exception e) {

alertMaker.showErrorMessage("Failed in deleting questions of this subcategory","");
	}
	}
	public void undeleteSubcategoryQuestion(int id) throws Exception {
		
try
{
		String query = "update  question_bank set mark_to_delete =? Where subcategory_id=?";
		PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
		preparedStatement.setInt(1, 0);
		preparedStatement.setInt(2, id);
		int p = preparedStatement.executeUpdate();
		System.out.println("val p " + p);

	}catch (Exception e) {

alertMaker.showErrorMessage("Failed in deleting questions of this subcategory","");
	}
	}

	public int deleteUser(usersModel rowToDelete) {

		int p = 0;
		try {
			String query = "update qb_user_profiles set mark_to_delete=1 where  username= ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			System.out.println("a");
			System.out.println("username is" + rowToDelete.getUserName().getValue().toString().toLowerCase());
			preparedStatement.setString(1, rowToDelete.getUserName().getValue().toString().toLowerCase());
			p = preparedStatement.executeUpdate();
			System.out.println("e");

		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("value of p is " + p);
		return p;
	}

	public ArrayList<String> getAllDifficultyLevels() {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select difficulty_level_name from qb_difficulty_level ";
			rs = execQuery(query);
			while(rs.next())
             list.add(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	private int getPreviousqid() throws Exception {

		ResultSet rs = null;
		try {
			String query = "select max(q_id) from question_bank ";
			rs = execQuery(query);
			if (rs.next()) {
				System.out.println(rs.getInt(1));
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;
	}

	private int getPreviousqidForEdit() throws Exception {

		ResultSet rs = null;
		try {
			String query = "select max(q_id) from question_bank_edit_history ";
			rs = execQuery(query);
			if (rs.next()) {
				System.out.println(rs.getInt(1));
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;
	}


	public void insertQuestion(String question_text, String category_name, String subcategory_name, String path,
			String type, String difficulty, String marks, String added_by, String firstchoice, String secondchoice,
			String thirdchoice, String fourthchoice, String correct_answer, int i, String time_assigned)
			throws Exception {

		int p = 0;
		try {
		
			int prev_qid = getPreviousqid();
			String query = "insert into  question_bank(q_id,question_text,category_id,subcategory_id,type,difficulty,marks,added_by,choice1,choice2,choice3,choice4,correct_answer,mark_to_delete,time_assigned_in_minutes,image) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			System.out.println("a");
			preparedStatement.setInt(1, prev_qid + 1);
			preparedStatement.setString(2, question_text);
			preparedStatement.setInt(3, getCategoryId(category_name));
			preparedStatement.setInt(4, getSubcategoryId(subcategory_name));

			preparedStatement.setInt(5, getTypeId(type));
			preparedStatement.setInt(6, getDifficultyId(difficulty));
			preparedStatement.setInt(7, Integer.valueOf(marks));
			preparedStatement.setString(8, added_by);

			preparedStatement.setString(9, firstchoice);
			preparedStatement.setString(10, secondchoice);
			preparedStatement.setString(11, thirdchoice);
			preparedStatement.setString(12, fourthchoice);
			int t = -1;
			if (correct_answer == null) {
				t = -1;
			} else {
				t = Integer.valueOf(correct_answer);
			}
			preparedStatement.setInt(13, t);
			preparedStatement.setInt(14, 0);
			preparedStatement.setInt(15, Integer.valueOf(time_assigned));
			if (path != null) {
				FileInputStream fin = new FileInputStream(path);

				preparedStatement.setBinaryStream(16, fin, fin.available());
			} else {
				Blob b = null;
				preparedStatement.setBlob(16, b);

			}
			preparedStatement.executeUpdate();
			System.out.println("e");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public Blob viewImage(int id) {
		ResultSet rs = null;
  Blob b=null;
		try {
			String query = "select image from question_bank where q_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
          while(rs.next())
        	  b=rs.getBlob("image");
		} catch (Exception e) {
	alertMaker.showErrorMessage("Error loading image of the question","");
		}
		return b;
	}

	public void addCategory(String text) throws Exception {
		int prevCategoryId = prevCategoryIdfn();
		String query = "insert into  qb_categories values (?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, prevCategoryId + 1);
		preparedStatement.setString(2, text);
		preparedStatement.setString(3, loginController.loggedUser);
		preparedStatement.setInt(4, 0);
		preparedStatement.executeUpdate();
	}

	private int prevCategoryIdfn() {

		ResultSet rs = null;
		try {
			String query = "select max(category_id) from qb_categories ";
			rs = execQuery(query);
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;

	}

	public void addSubcategory(String text, String parent) throws Exception {
		int prevSubcategoryId = prevSubcategoryIdfn();
		String query = "insert into  qb_subcategories values (?,?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, prevSubcategoryId + 1);
		preparedStatement.setInt(2, getCategoryId(parent));
		preparedStatement.setString(3, text);
		preparedStatement.setString(4, loginController.loggedUser);
		preparedStatement.setInt(5, 0);
		preparedStatement.executeUpdate();
	}

	private int prevSubcategoryIdfn() {

		ResultSet rs = null;
		try {
			String query = "select max(subcategory_id) from qb_subcategories ";
			rs = execQuery(query);
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return -1;

	}

	public ArrayList<Integer> getQIdBySubcategoryId(int id, int level, int type) {
		ArrayList<Integer> qIds = new ArrayList();
		ResultSet rs = null;
		try {
			String query = "select q_id from question_bank where mark_to_delete=0 and subcategory_id= ? and difficulty=? and type=? ";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, level);
			preparedStatement.setInt(3, type);
			rs = preparedStatement.executeQuery();
			while (rs.next())
				qIds.add(rs.getInt(1));

		} catch (Exception e) {
			System.out.println("error in finding qids!!!!!");
			// TODO: handle exception
		}
		return qIds;

	}

	public String getDifficultyLevel(int id) throws Exception {

		ResultSet rs = null;
		try {
			String query = "select difficulty_level_name from qb_difficulty_level where difficulty_level_id =" + id
					+ " ";
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		if (rs.next())
			return rs.getString(1);
		return null;
	}

	public int typeByQid(int id) {
		ResultSet rs = null;
		try {
			String query = "select type from question_bank where q_id =" + id + " ";
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}

		try {
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	public ArrayList<questionsModel> getSearchResults(String type, String category, String subcategory, String user) {
		ResultSet rs = null;
		ArrayList<questionsModel> list = new ArrayList<questionsModel>();
		try {

			String query = "select question_text,type,subcategory_id,q_id from question_bank where mark_to_delete=0 and type=nvl(?,type) and category_id=nvl(?,category_id) and subcategory_id=nvl(?,subcategory_id) and added_by=nvl(?,added_by)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			if (type == null)
				preparedStatement.setString(1, type);
			else
				preparedStatement.setInt(1, getTypeId(type.toLowerCase()));
			if (category == null)
				preparedStatement.setString(2, category);
			else
				preparedStatement.setInt(2, getCategoryId(category.toLowerCase()));
			if (subcategory == null)
				preparedStatement.setString(3, subcategory);
			else
				preparedStatement.setInt(3, getSubcategoryId(subcategory.toLowerCase()));
			preparedStatement.setString(4, user);
			rs = preparedStatement.executeQuery();
			while(rs.next())
			{
			questionsModel newq = new questionsModel(rs.getString(1),getQuestionTypeName(rs.getInt(2)),getQuestionSubcategoryName(rs.getInt(3)));
			newq.setId(rs.getInt(4));
			list.add(newq);

		}
		}
			catch (Exception e) {
			alertMaker.showErrorMessage("Error loading search results","");
		}
		return list;
	}

	public void addUser(String username, String Category) {
		try {
			String query = "insert into  qb_user_profiles values (?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, "question_setter");
			preparedStatement.setInt(3, getCategoryId(Category));
			preparedStatement.setInt(4, 0);
			preparedStatement.setString(5, "password");
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean isImagePresent(int id) {
		ResultSet rs = null;
		Blob b=null;
		try {
			String query = "select image from question_bank where q_id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				b=rs.getBlob(1);
			}
		} catch (SQLException e) {
	
		}

		if(b==null)
			return false;
		else
			return true;

	}

	public void insertPaperInformationInBank(String paperName, String questionsList, int totalNOoFQuestions,
			String targetCourse, String targetCollege, String paperPurpose, Date createdDate, Date examDate,
			String addedby) {
		ResultSet rs = null;
		try {
			String query = "insert into  qb_paper(paper_id,paper_name,question_list,no_of_questions,target_college,course,created_by,created_purpose,created_date,exam_date,mark_to_delete) values (?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, prevPID() + 1);
			preparedStatement.setString(2, paperName);
			preparedStatement.setString(3, questionsList);
			preparedStatement.setInt(4, totalNOoFQuestions);
			preparedStatement.setString(5, targetCollege);
			preparedStatement.setString(6, targetCourse);
			preparedStatement.setString(7, addedby);
			preparedStatement.setString(8, paperPurpose);
			preparedStatement.setDate(9, createdDate);
			preparedStatement.setDate(10, examDate);
			preparedStatement.setInt(11, 0);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("catch of insert paper");

		}

	}

	public int prevPID()

	{
		int ans = 0;
		try {
			String query = "select max(paper_id)from qb_paper ";
			ResultSet rs = execQuery(query);
			while (rs.next())
				ans = rs.getInt(1);
			System.out.println(ans);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return ans;
	}

	public ArrayList<generatedPapersModel> loadPapersData() {
         ArrayList<generatedPapersModel> list = new ArrayList<generatedPapersModel>();
		ResultSet rs = null;
		try {
			String query = "select paper_name,created_date,exam_date,target_college,course,created_purpose,created_by,paper_id from qb_paper where mark_to_delete=0 order by paper_id desc" ;
			rs = execQuery(query);
			while(rs.next())
			{
				generatedPapersModel newObj=new generatedPapersModel(rs.getString("paper_name"), rs.getDate("created_date"), rs.getDate("exam_date"), rs.getString("target_college"), rs.getString("course"), rs.getString("created_purpose"), rs.getString("created_by"));
				newObj.setPaperId(rs.getInt(("paper_id")));
				list.add(newObj);
			}	

		} catch (Exception e) {
    alertMaker.showErrorMessage("Error loading papers data", "");
		}
		return list;

	}

	public int loadType(int id) {
		ResultSet rs = null;
		try {
			String query = "select type from question_bank where mark_to_delete=0 and q_id =" + id;
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		try {
			if (rs.next())
				return rs.getInt(1);
			else
				return -1;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return -1;
	}
	public boolean editQuestionDatabaseChangesHandler(String question_text, String category_name, String subcategory_name, Blob image,
			String type, String difficulty, String marks, String added_by, String firstchoice, String secondchoice,
			String thirdchoice, String fourthchoice, String correct_answer, String time_assigned,int questionId)
			throws Exception {
		boolean ans=false;
		int p = 0;
		try {
			System.out.println(questionId);
			int prev_qid = getPreviousqid();
			String query = "insert into  question_bank(q_id,question_text,category_id,subcategory_id,type,difficulty,marks,added_by,choice1,choice2,choice3,choice4,correct_answer,mark_to_delete,time_assigned_in_minutes,image) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, prev_qid + 1);
			preparedStatement.setString(2, question_text);
			preparedStatement.setInt(3, getCategoryId(category_name));
			preparedStatement.setInt(4, getSubcategoryId(subcategory_name));

			preparedStatement.setInt(5, getTypeId(type));
			preparedStatement.setInt(6, getDifficultyId(difficulty));
			preparedStatement.setInt(7, Integer.valueOf(marks));
			preparedStatement.setString(8, added_by);

			preparedStatement.setString(9, firstchoice);
			preparedStatement.setString(10, secondchoice);
			preparedStatement.setString(11, thirdchoice);
			preparedStatement.setString(12, fourthchoice);
			int t = -1;
			if (correct_answer == null) {
				t = -1;
			} else {
				t = Integer.valueOf(correct_answer);
			}
			preparedStatement.setInt(13, t);
			preparedStatement.setInt(14, 0);
			preparedStatement.setInt(15, Integer.valueOf(time_assigned));
	          Blob b=image;
				preparedStatement.setBlob(16, b);
			preparedStatement.executeUpdate();

			int prev_qidEdit = getPreviousqidForEdit()+1;
			String query2 = "insert into  question_bank_edit_history( Q_ID,QUESTION_TEXT , CATEGORY_ID,  SUBCATEGORY_ID  ,IMAGE ,TYPE,  DIFFICULTY, MARKS,  ADDED_BY,CHOICE1,  CHOICE2 , CHOICE3 , CHOICE4  ,CORRECT_ANSWER  , MARK_TO_DELETE  ,TIME_ASSIGNED_IN_MINUTES)   select  ?,QUESTION_TEXT , CATEGORY_ID,  SUBCATEGORY_ID  ,IMAGE ,TYPE,  DIFFICULTY, MARKS,  ADDED_BY,CHOICE1,  CHOICE2 , CHOICE3 , CHOICE4  ,CORRECT_ANSWER  , MARK_TO_DELETE  ,TIME_ASSIGNED_IN_MINUTES   from question_bank where q_id=" +questionId ;
			PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
			preparedStatement2.setInt(1,prev_qidEdit);
			preparedStatement2.executeUpdate();
			String query3 = "delete from  question_bank  where q_id=" +questionId ;
			PreparedStatement preparedStatement3 = conn.prepareStatement(query3);
			preparedStatement3.executeUpdate();
		ans=true;
			
       
		} catch (Exception e) {
			e.printStackTrace();
			alertMaker.showErrorMessage("Error saving changes", "Failure");
			ans=false;
      
		}finally {
			return ans;
		}
	

	}

	public void deleteCategoryQuestions(int id) {
		
		try
		{
				String query = "update  question_bank set mark_to_delete =? Where category_id=?";
				PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
				preparedStatement.setInt(1, 1);
				preparedStatement.setInt(2, id);
				int p = preparedStatement.executeUpdate();
				System.out.println("val p " + p);

			}
		catch (Exception e) {

		alertMaker.showErrorMessage("Failed in deleting questions of this Category","");
			}}
public void undeleteCategoryQuestions(int id) {
		
		try
		{
				String query = "update  question_bank set mark_to_delete =? Where category_id=?";
				PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(query);
				preparedStatement.setInt(1, 0);
				preparedStatement.setInt(2, id);
				int p = preparedStatement.executeUpdate();
				System.out.println("val p " + p);

			}
		catch (Exception e) {

		alertMaker.showErrorMessage("Failed in deleting questions of this Category","");
			}}
	public 	ArrayList<String> getAllDeletedCategories() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_categories ";
			rs = execQuery(query);
			while(rs.next())
             list.add(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}

	public ArrayList<String> getDeletedSubcategoriesByCategoryId(int id) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			String query = "select category_name from qb_subcategories where parent_id = " + id
					+ "and mark_to_delete=1";
			rs = execQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		while (rs.next()) {
			list.add(rs.getString(1));

		}
		rs.close();
		return list;
	}
	public 	boolean isCategoryDeleted(int id) {
		int ans=-1;
		ResultSet rs = null;
		try {
			String query = "select mark_to_delete from qb_categories where category_id ="+id;
			rs = execQuery(query);
			while(rs.next())
             ans=rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		if(ans==1)
			return true;
		else
			return false;

	}
	public 	boolean isSubcategoryDeleted(int id) {
		int ans=-1;
		ResultSet rs = null;
		try {
			String query = "select mark_to_delete from qb_subcategories where subcategory_id ="+id;
			rs = execQuery(query);
			while(rs.next())
             ans=rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		if(ans==1)
			return true;
		else
			return false;

	}
	public 	boolean isQuestionDeleted(int id) {
		int ans=-1;
		ResultSet rs = null;
		try {
			String query = "select mark_to_delete from question_bank where q_id ="+id;
			rs = execQuery(query);
			while(rs.next())
             ans=rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		if(ans==1)
			return true;
		else
			return false;

	}
	public ArrayList<questionsModel> loadDeletedAndUndeletedQuestionData() throws Exception {
        ArrayList<questionsModel> list = new ArrayList<questionsModel>();
		ResultSet rs = null;
		try {
			String query = "select question_text,type,subcategory_id,q_id from question_bank  order by q_id desc";
			rs = execQuery(query);

			while (rs.next()) {
				questionsModel newq = new questionsModel(rs.getString(1),
						getQuestionTypeName(rs.getInt(2)),
						getQuestionSubcategoryName(rs.getInt(3)));
				newq.setId(rs.getInt(4));
				list.add(newq);

		} 
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;

	}
}