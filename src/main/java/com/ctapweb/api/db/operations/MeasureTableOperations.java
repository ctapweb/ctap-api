package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.TablePojo;

public class MeasureTableOperations extends TableOperations {
	public static final String COLUMN_CATEGORY_ID = "category_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";


	public MeasureTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getMeasureTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		Measure measure = (Measure) entry;
		long measureId = measure.getId();

		updateCategoryId(measureId, measure.getCategoryId());
		updateName(measureId, measure.getName());
		updateDescription(measureId, measure.getDescription());
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_CATEGORY_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getMeasureCategoryTableName() + "("
				+ "" + MeasureCategoryTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_NAME + " TEXT NOT NULL, "
				+ COLUMN_DESCRIPTION + " TEXT, "
				+ " UNIQUE(" + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + "))"; 

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Measure> getAllEntries() throws SQLException {
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
	public List<Measure> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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

		//cast entry to corpus
		Measure measure = (Measure) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_CATEGORY_ID + ","  
				+ COLUMN_NAME + "," + COLUMN_DESCRIPTION + ") "
				+ "VALUES(?, ?, ?) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, measure.getCategoryId());
			ps.setString(2, measure.getName());
			ps.setString(3, measure.getDescription());

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
	public void updateDescription(long measureId, String newDescription) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_DESCRIPTION + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newDescription);
			ps.setLong(2, measureId);

			ps.executeUpdate();
		}
	}
	
	public void updateName(long measureId, String newName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newName);
			ps.setLong(2, measureId);

			ps.executeUpdate();
		}
	}

	public void updateCategoryId(long measureId, long categoryId) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_CATEGORY_ID + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setLong(1, categoryId);
			ps.setLong(2, measureId);

			ps.executeUpdate();
		}
	}

	@Override
	public Measure getEntry(long entryId) throws SQLException {
		Measure corpus = null;

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

	public Measure getEntryByCategoryAndName(long categoryId, String name) throws SQLException {
		Measure corpus = null;

		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_CATEGORY_ID + " = ? "
				+ " AND " + COLUMN_NAME + " = ? ";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, categoryId);
			ps.setString(2, name);
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
		long[] addedEntryIds = new long[size];

		for(int i = 0; i < entries.size(); i++) {
			//cast the entry
			Measure corpus = (Measure) entries.get(i);
			addedEntryIds[i] = addEntry(corpus);

		}

		return addedEntryIds;
	}


	public boolean isMeasureOfLanguageExist(String measureName, String language) 
			throws SQLException {
		String queryStr = "SELECT m.* FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() + " AS c "
						+ " ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE m." + COLUMN_NAME + " = ? "
				+ " AND c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ?";
				
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, measureName);
			ps.setString(2, language);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				if(rs.isBeforeFirst()) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Measure> getAllEntriesByCategory(long categoryId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_CATEGORY_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, categoryId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	public List<Measure> getAllEntriesByLanguage(String language) throws SQLException {
		String queryStr = "SELECT m.* FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, language);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	public List<Measure> getAllEntriesByLanguageAndCategory(String language, long categoryId) 
			throws SQLException {
		String queryStr = "SELECT m.* FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ? "
				+ " AND m." + COLUMN_CATEGORY_ID + "=?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, language);
			ps.setLong(2, categoryId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	public List<Measure> getEntriesByCategory(long categoryId, String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_CATEGORY_ID + " = ? "
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, categoryId);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Measure> getEntriesByLanguage(String language, String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT m.* FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ?"
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

	public List<Measure> getEntriesByLanguageAndCategory(String language, long categoryId, 
			String orderByColumn, long limit, long offset) throws SQLException {
		String queryStr = "SELECT m.* FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ? "
				+ " AND m." + COLUMN_CATEGORY_ID + " = ? "
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, language);
			ps.setLong(2, categoryId);
			ps.setLong(3, limit);
			ps.setLong(4, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}
	
	private Measure getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets corpus info
			return new Measure(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_CATEGORY_ID),
					rs.getString(COLUMN_NAME),
					rs.getString(COLUMN_DESCRIPTION)
					);
		}
		return null;
	}

	private List<Measure> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Measure> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Measure measure = new Measure(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_CATEGORY_ID),
						rs.getString(COLUMN_NAME),
						rs.getString(COLUMN_DESCRIPTION)
						);
				entryList.add(measure);
			}
		}
		return entryList;
	}


	public long getNumEntriesByCategory(long categoryId) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName 
				+ " WHERE " + COLUMN_CATEGORY_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, categoryId);
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

	public long getNumEntriesByLanguage(String language) throws SQLException {
		String queryStr = "SELECT COUNT(m.*) FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ?";

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

	public long getNumEntriesByLanguageAndCategory(String language, long categoryId) 
			throws SQLException {
		String queryStr = "SELECT COUNT(m.*) FROM " + tableName + " AS m "
				+ " JOIN " + tableNames.getMeasureCategoryTableName() 
					+ " AS c ON m." + COLUMN_CATEGORY_ID + " = c." + MeasureCategoryTableOperations.COLUMN_ID
				+ " WHERE " +  " c." + MeasureCategoryTableOperations.COLUMN_LANGUAGE + " = ? "
				+ " AND m." + COLUMN_CATEGORY_ID + "=?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setString(1, language);
			ps.setLong(2, categoryId);
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
}
