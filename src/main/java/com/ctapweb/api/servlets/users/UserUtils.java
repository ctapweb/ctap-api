package com.ctapweb.api.servlets.users;

public class UserUtils {
//	private static UserTableOperations userTableOperations;
//	
//	public UserUtils() throws ClassNotFoundException, IOException, SQLException {
//		userTableOperations = new UserTableOperations(DataSourceManager.getDataSource());
//	}
	public static boolean isEmailValid(String email) {
		return email.matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
	}
	
}
