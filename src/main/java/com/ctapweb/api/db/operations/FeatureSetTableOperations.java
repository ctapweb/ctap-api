package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.db.pojos.TablePojo;

public class FeatureSetTableOperations extends TableOperations {
	public static final String COLUMN_OWNER_ID = "owner_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CREATE_TIMESTAMP = "create_timestamp";


	public FeatureSetTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getFeatureSetTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		FeatureSet featureSet = (FeatureSet) entry;
		long featureSetId = featureSet.getId();

		updateName(featureSetId, featureSet.getName());
		updateDescription(featureSetId, featureSet.getDescription());
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_OWNER_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getUserTableName() + "("
				+ "" + UserTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_NAME + " TEXT NOT NULL, "
				+ COLUMN_DESCRIPTION + " TEXT,"
				+ COLUMN_CREATE_TIMESTAMP + " TIMESTAMP NOT NULL)"; 

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<FeatureSet> getAllEntries() throws SQLException {
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
	public List<FeatureSet> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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
		FeatureSet featureSet = (FeatureSet) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_OWNER_ID + "," + COLUMN_NAME + ","
				+ COLUMN_DESCRIPTION + "," + COLUMN_CREATE_TIMESTAMP +") "
				+ "VALUES(?, ?, ?, CURRENT_TIMESTAMP) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, featureSet.getOwnerId());
			ps.setString(2, featureSet.getName());
			ps.setString(3, featureSet.getDescription());

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
	public void updateName(long corpusId, String newName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newName);
			ps.setLong(2, corpusId);

			ps.executeUpdate();
		}
	}

	public void updateDescription(long corpusId, String newDescription) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_DESCRIPTION + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newDescription);
			ps.setLong(2, corpusId);

			ps.executeUpdate();
		}
	}

	@Override
	public FeatureSet getEntry(long entryId) throws SQLException {
		FeatureSet corpus = null;

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
		long[] addedEntryIds = new long[size];

		for(int i = 0; i < entries.size(); i++) {
			//cast the entry
			FeatureSet featureSet = (FeatureSet) entries.get(i);
			addedEntryIds[i] = addEntry(featureSet);

		}

		return addedEntryIds;
	}


	public boolean isUserOwner(long userId, long featureSetId) throws SQLException {
		String queryStr = "SELECT " + COLUMN_ID + " FROM " + tableName 
				+ " WHERE " + COLUMN_OWNER_ID + " = ? AND " 
				+ COLUMN_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, userId);
			ps.setLong(2, featureSetId);

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

	public List<FeatureSet> getAllEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_OWNER_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, ownerId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	public List<FeatureSet> getEntriesByOwner(long ownerId, String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_OWNER_ID + " = ? "
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, ownerId);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	private FeatureSet getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets corpus info
			return new FeatureSet(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_OWNER_ID),
					rs.getString(COLUMN_NAME),
					rs.getString(COLUMN_DESCRIPTION),
					rs.getTimestamp(COLUMN_CREATE_TIMESTAMP)
					);
		}
		return null;
	}

	private List<FeatureSet> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<FeatureSet> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				FeatureSet corpus = new FeatureSet(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_OWNER_ID),
						rs.getString(COLUMN_NAME),
						rs.getString(COLUMN_DESCRIPTION),
						rs.getTimestamp(COLUMN_CREATE_TIMESTAMP)
						);
				entryList.add(corpus);
			}
		}
		return entryList;
	}


	public long getNumEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName + " WHERE " + COLUMN_OWNER_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, ownerId);
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
