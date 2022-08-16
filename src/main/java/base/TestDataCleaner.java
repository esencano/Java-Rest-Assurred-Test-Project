package base;

import static logger.LogUtil.log;

import database.con.DatabaseCon;

public class TestDataCleaner {
	public static void deleteUserByUserName(String userName) {
		log("Deleting User From DB: " + userName );
		DatabaseCon dbCon = DatabaseCon.getInstance();
		dbCon.deleteUserByUserName(userName);
	}

	public static void deleteHomeByHomeID(int homeID) {
		log("Deleting Home From DB: " + homeID );

		DatabaseCon dbCon = DatabaseCon.getInstance();
		dbCon.deleteHomeById(homeID);
	}
}
