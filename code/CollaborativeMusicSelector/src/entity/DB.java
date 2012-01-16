package entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {
	
	
	private static String mysqlServer = "jdbc:mysql://db1.acm.uiuc.edu/acoustics";
	private static String mysqlUser = "acoustics_ro";
	private static String mysqlPass = "";
	
	private static DB theDB = new DB(mysqlServer, mysqlUser, mysqlPass);

	private Connection theConn;
	
	private DB(String serverURL, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			theConn = DriverManager.getConnection(serverURL, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DB getInstance() {
		return theDB;
	}
	
	public ResultSet query(String aQuery) throws SQLException {
		Statement st = theConn.createStatement();
		return st.executeQuery(aQuery);
	}

}
