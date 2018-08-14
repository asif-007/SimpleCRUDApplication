package com.asif.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class StudentsDBUtil {
	private DataSource dataSource;
	
	public StudentsDBUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRes = null;
		
		try {
			//get a connection
			myConn = dataSource.getConnection();
			
			//create sql statement
			String sql = "select * from student order by last_name";
			myStmt = myConn.createStatement();
			
			//execute statement
			myRes = myStmt.executeQuery(sql);
			
			//process result set
			while(myRes.next()) {
				//Retrieve data from resultset row
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				//create new student  object
				Student tempStudent = new Student(id, firstName, lastName, email);
				//add it to the list of students
				students.add(tempStudent);
			}
			return students;
			}
		finally {
			//close jdbc object
			close(myConn, myStmt, myRes);
		}
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRes) {
		// TODO Auto-generated method stub
		try {
			if(myRes != null) {
				myRes.close();
			}
			if(myStmt != null) {
				myRes.close();
			}
			if(myConn != null) {
				myConn.close();
			}
		}
		catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception{
		// TODO Auto-generated method stub
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql for insert
			String sql = "insert into student " +"(first_Name, last_Name, email) " +"values(?, ?, ?)";
			myStmt = myConn.prepareStatement(sql);
			
			//set the param value for student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//execute sql insert
			myStmt.execute();
		}
		catch (Exception ex) {
			// clean up jdbc objects
			close(myConn, myStmt, null);
		}
		
	}

	public Student getStudents(String theStudentId) throws Exception {
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRes = null;
		int studentId;
		
		try {
			//convert student id into int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create sql to get selected student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRes = myStmt.executeQuery();
			
			//retrieve data from result set row
			if(myRes.next()) {
				String firstName = myRes.getString("first_Name");
				String lastName = myRes.getString("last_Name");
				String email = myRes.getString("email");
				
				//use the studentId during construct
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find the student id: "+studentId);
			}
		
			return theStudent;
		}
		finally {
			close(myConn, myStmt, myRes);
		}
	}

	public void updateStudent(Student theStudent) throws Exception{
		// TODO Auto-generated method stub
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
		//get db connection
		myConn = dataSource.getConnection();
		
		//create sql update statement
		String sql = "update student "+"set first_name=?, last_name=?, email =? "+"where id=?";
		
		//prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		//set params
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		
		//execute sql statement
		myStmt.execute();
		}
		finally {
			//clean up jdbc objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//convert studentId to int
			int studentId = Integer.parseInt(theStudentId);
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql to delete student
			String sql = "delete from student where id=?";
			
			//prepare statements
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute sql statement
			myStmt.execute();
			
		}
		finally {
			//clean up jdbc object
			close(myConn, myStmt, null);
		}
	}
}
