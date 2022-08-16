package database.con;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public final class DatabaseCon {


	
	
	private static DatabaseCon dbCon;
	private Connection conn;
	private DatabaseCon() {  
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://"+System.getenv("TEST_DB_URL"), System.getenv("TEST_DB_USERNAME"), System.getenv("TEST_DB_PASSWORD"));
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public static DatabaseCon getInstance() {
		if(dbCon == null) {
			dbCon = new DatabaseCon();
		}

		return dbCon;
	}


	private void runSQLCommand(String command) {
		try {
			Statement stmt = conn.createStatement();
			String sql = command;
			stmt.executeQuery(sql);
			stmt.close();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
	}	

	public void deleteUserByUserName(String userName) {
		runSQLCommand("delete from users where user_name='"+userName+"'");
	}
	
	public void deleteHomeById(int homeID) {
		runSQLCommand("delete from users_home where home_id="+homeID+""); // normally server should run this command itself with postgresql cascade
		runSQLCommand("delete from home where id="+homeID+"");
	}
	
	
}



