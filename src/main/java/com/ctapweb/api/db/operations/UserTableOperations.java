package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.ctapweb.api.db.pojos.TablePojo;
import com.ctapweb.api.db.pojos.UserAccount;

public class UserTableOperations extends TableOperations {
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_INSTITUTION = "institution";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PASSWD = "passwd";
	public static final String COLUMN_CREATE_TIMESTAMP = "create_timestamp";
	public static final String COLUMN_LAST_LOGIN = "last_login";

	public UserTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getUserTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		UserAccount userAccount = (UserAccount) entry;
		long userId = userAccount.getId();

		updateFirstName(userId, userAccount.getFirstName());
		updateLastName(userId, userAccount.getLastName());
		updateInstitution(userId, userAccount.getInstitution());
		updatePasswd(userId, BCrypt.hashpw(userAccount.getPasswd(), BCrypt.gensalt()));

	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_FIRST_NAME + " VARCHAR(30),"
				+ COLUMN_LAST_NAME + " VARCHAR(30),"
				+ COLUMN_INSTITUTION + " VARCHAR(100),"
				+ COLUMN_EMAIL + " VARCHAR(254) NOT NULL UNIQUE, "
				+ COLUMN_PASSWD + " TEXT NOT NULL, "
				+ COLUMN_CREATE_TIMESTAMP + " TIMESTAMP NOT NULL, "
				+ COLUMN_LAST_LOGIN + " TIMESTAMP)";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<UserAccount> getAllEntries() throws SQLException {

		String queryStr = "SELECT * FROM " + tableName;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				ResultSet rs = ps.executeQuery();
				) {
			return getEntriesFromResultSet(rs);
		}

	}

	@Override
	public List<UserAccount> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName + " ORDER BY " 
				+ orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, limit);
			ps.setLong(2, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}


		}
	}

	@Override
	public <T extends TablePojo> long addEntry(T entry) throws SQLException {
		long insertId = 0;
		//cast entry to user account
		UserAccount userAccount = (UserAccount) entry;

		// encrypt the password
		String hashedPasswd = BCrypt.hashpw(userAccount.getPasswd(), BCrypt.gensalt());

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" + COLUMN_FIRST_NAME + ", " 
				+ COLUMN_LAST_NAME + "," + COLUMN_INSTITUTION + "," + COLUMN_EMAIL + ","
				+ COLUMN_PASSWD + "," + COLUMN_CREATE_TIMESTAMP + ")"
				+ " VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING " + COLUMN_ID;
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setString(1, userAccount.getFirstName());
			ps.setString(2, userAccount.getLastName());
			ps.setString(3, userAccount.getInstitution());
			ps.setString(4, userAccount.getEmail());
			ps.setString(5, hashedPasswd);

			try(
					ResultSet rs = ps.executeQuery();
					) {

				//get the inserted id
				if(rs.isBeforeFirst()) {
					rs.next();
					insertId =  rs.getLong(1);
				}
			}
		}
		return insertId;
	}


	//user table specific operations
	public boolean isAccountExist(String email) throws SQLException {

		String queryStr = "SELECT " + COLUMN_ID + " FROM " + tableName 
				+ " WHERE " + COLUMN_EMAIL + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, email);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return rs.isBeforeFirst(); 
			}
		}
	}

	public String resetPasswd(long userId) throws SQLException {
		String newPasswd = RandomStringUtils.randomAlphanumeric(10);

		updatePasswd(userId, newPasswd);

		return newPasswd;
	}

	public void updateLastLogin() throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_LAST_LOGIN + " = CURRENT_TIMESTAMP";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {
			ps.executeUpdate();
		}

	}

	public void updateFirstName(long userId, String firstName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_FIRST_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, firstName);
			ps.setLong(2, userId);

			ps.executeUpdate();
		}
	}

	public void updateLastName(long userId, String lastName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_LAST_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {
			ps.setString(1, lastName);
			ps.setLong(2, userId);

			ps.executeUpdate();

		}

	}

	public void updateInstitution(long userId, String institution) throws SQLException{
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_INSTITUTION + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, institution);
			ps.setLong(2, userId);

			ps.executeUpdate();
		}

	}

	/**
	 * 
	 * @param userId
	 * @param passwd in plain text, the function will do the encryption
	 */
	public void updatePasswd(long userId, String passwd) throws SQLException{
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_PASSWD + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {
			ps.setString(1, BCrypt.hashpw(passwd, BCrypt.gensalt()));
			ps.setLong(2, userId);

			ps.executeUpdate();

		}
	}

	@Override
	public UserAccount getEntry(long entryId) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, entryId);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntryFromResultSet(rs);
			}
		}


	}

	@Override
	public long[] addEntries(List<? extends TablePojo> entries) throws SQLException {
		int size = entries.size();
		long[] addedUserIds = new long[size];

		for(int i = 0; i < entries.size(); i++) {
			//cast the entry
			UserAccount userAccount = (UserAccount) entries.get(i);
			addedUserIds[i] = addEntry(userAccount);

		}

		return addedUserIds;
	}

	/**
	 * Check if the plainPasswd correct for the user with the email.
	 * @param email the account's email
	 * @param plainPasswd
	 * @return
	 */
	public boolean checkPasswd(String email, String plainPasswd) throws SQLException {
		String queryStr = "SELECT " + COLUMN_PASSWD + " FROM " + tableName + " WHERE "
				+ COLUMN_EMAIL + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, email);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				if(rs.isBeforeFirst()) {
					rs.next();
					String hashedPasswd = rs.getString(COLUMN_PASSWD);
					return BCrypt.checkpw(plainPasswd, hashedPasswd);
				}
			}
		}
		return false;
	}

	private UserAccount getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets account info
			return new UserAccount(
					rs.getLong(COLUMN_ID), 
					rs.getString(COLUMN_FIRST_NAME), 
					rs.getString(COLUMN_LAST_NAME),
					rs.getString(COLUMN_INSTITUTION), 
					rs.getString(COLUMN_EMAIL), 
					rs.getString(COLUMN_PASSWD), 
					rs.getTimestamp(COLUMN_CREATE_TIMESTAMP), 
					rs.getTimestamp(COLUMN_LAST_LOGIN));
		}
		return null;
	}

	private List<UserAccount> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<UserAccount> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				UserAccount userAccount = new UserAccount(
						rs.getLong(COLUMN_ID), 
						rs.getString(COLUMN_FIRST_NAME), 
						rs.getString(COLUMN_LAST_NAME),
						rs.getString(COLUMN_INSTITUTION), 
						rs.getString(COLUMN_EMAIL), 
						rs.getString(COLUMN_PASSWD), 
						rs.getTimestamp(COLUMN_CREATE_TIMESTAMP), 
						rs.getTimestamp(COLUMN_LAST_LOGIN));
				entryList.add(userAccount);
			}
		}
		return entryList;
	}

}
