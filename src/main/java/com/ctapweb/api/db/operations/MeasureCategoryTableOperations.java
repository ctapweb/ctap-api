package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.db.pojos.TablePojo;

public class MeasureCategoryTableOperations extends TableOperations {
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LANGUAGE = "language";
	public static final String COLUMN_REQUIRED_PIPELINE = "required_pipeline";
	public static final String COLUMN_CLASS_NAME = "class_name";

	public MeasureCategoryTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getMeasureCategoryTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		MeasureCategory measureCategory = (MeasureCategory) entry;
		long categoryId = measureCategory.getId();

		updateName(categoryId, measureCategory.getName());
		updateDescription(categoryId, measureCategory.getDescription());

	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_NAME + " VARCHAR(50) NOT NULL,"
				+ COLUMN_DESCRIPTION + " TEXT, "
				+ COLUMN_LANGUAGE + " VARCHAR(20), "
				+ COLUMN_REQUIRED_PIPELINE + " VARCHAR(50), "
				+ COLUMN_CLASS_NAME + " TEXT,"
				+ " UNIQUE(" + COLUMN_NAME + ", " + COLUMN_LANGUAGE + ")"
				+ ")";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<MeasureCategory> getAllEntries() throws SQLException {

		String queryStr = "SELECT * FROM " + tableName;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				ResultSet rs = ps.executeQuery();
				) {
			return getEntriesFromResultSet(rs);
		}

	}

	public List<MeasureCategory> getAllEntriesByLanguage(String language) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_LANGUAGE+ " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, language);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}

	}

	@Override
	public List<MeasureCategory> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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

	public List<MeasureCategory> getEntriesByLanguage(String language, String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_LANGUAGE+ " = ? "
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, language);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

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
		MeasureCategory measureCategory = (MeasureCategory) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_NAME + ", " 
				+ COLUMN_DESCRIPTION + ", "
				+ COLUMN_LANGUAGE + ", " 
				+ COLUMN_REQUIRED_PIPELINE + ", " 
				+ COLUMN_CLASS_NAME + ")"
				+ " VALUES (?, ?, ?, ?, ?) RETURNING " + COLUMN_ID;
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setString(1, measureCategory.getName());
			ps.setString(2, measureCategory.getDescription());
			ps.setString(3, measureCategory.getLanguage());
			ps.setString(4, measureCategory.getRequiredPipeline());
			ps.setString(5, measureCategory.getClassName());

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


	//table specific operations
	public boolean isCategoryExist(String name, String language) throws SQLException {

		String queryStr = "SELECT " + COLUMN_ID + " FROM " + tableName 
				+ " WHERE " + COLUMN_NAME + " = ?"
				+ " AND " + COLUMN_LANGUAGE + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, name);
			ps.setString(2, language);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return rs.isBeforeFirst(); 
			}
		}
	}

	public void updateName(long categoryId, String name) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, name);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		}
	}
	
	public void updateLanguage(long categoryId, String language) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_LANGUAGE + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, language);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		}
	}

	public void updateClassName(long categoryId, String className) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_CLASS_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, className);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		}
	}	

	public void updateDescription(long categoryId, String description) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_DESCRIPTION + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, description);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		}
	}

	public void updateRequiredPipeline(long categoryId, String requiredPipeline) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_REQUIRED_PIPELINE + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, requiredPipeline);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		}
	}

	

	

	@Override
	public MeasureCategory getEntry(long entryId) throws SQLException {

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

	public MeasureCategory getEntryByNameAndLanguage(String name, String language) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_NAME + " = ? "
				+ " AND " + COLUMN_LANGUAGE + " = ? ";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, name);
			ps.setString(2, language);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntryFromResultSet(rs);
			}
		}
	}
	public MeasureCategory getEntry(String categoryName, String language) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_NAME + " = ?"
				+ " AND " + COLUMN_LANGUAGE + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, categoryName);
			ps.setString(2, language);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntryFromResultSet(rs);
			}
		}
	}

	public long getNumEntriesByLanguage(String language) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName 
				+ " WHERE " + COLUMN_LANGUAGE+ " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, language);
			try(
					ResultSet rs = ps.executeQuery();
					) {
				if(rs.isBeforeFirst()) {
					rs.next();
					return rs.getLong(1);
				}
			}
			return 0;
		}
	}
	@Override
	public long[] addEntries(List<? extends TablePojo> entries) throws SQLException {
		int size = entries.size();
		long[] addedEntryIds = new long[size];

		for(int i = 0; i < entries.size(); i++) {
			//cast the entry
			MeasureCategory userAccount = (MeasureCategory) entries.get(i);
			addedEntryIds[i] = addEntry(userAccount);

		}

		return addedEntryIds;
	}

	private MeasureCategory getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets account info
			return new MeasureCategory(
					rs.getLong(COLUMN_ID), 
					rs.getString(COLUMN_NAME), 
					rs.getString(COLUMN_DESCRIPTION),
					rs.getString(COLUMN_LANGUAGE),
					rs.getString(COLUMN_REQUIRED_PIPELINE),
					rs.getString(COLUMN_CLASS_NAME)
					);
		}
		return null;
	}

	private List<MeasureCategory> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<MeasureCategory> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				MeasureCategory measureCategory = new MeasureCategory(
						rs.getLong(COLUMN_ID), 
						rs.getString(COLUMN_NAME), 
						rs.getString(COLUMN_DESCRIPTION),
						rs.getString(COLUMN_LANGUAGE),
						rs.getString(COLUMN_REQUIRED_PIPELINE),
						rs.getString(COLUMN_CLASS_NAME)
						);
				entryList.add(measureCategory);
			}
		}
		return entryList;
	}

}
