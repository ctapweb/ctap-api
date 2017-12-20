package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.Analysis;
import com.ctapweb.api.db.pojos.TablePojo;

public class AnalysisTableOperations extends TableOperations {
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CORPUS_ID = "corpus_id";
	public static final String COLUMN_FEATURESET_ID = "featureset_id";
	public static final String COLUMN_PROGRESS = "progress";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_CREATE_TIMESTAMP = "create_timestamp";


	public AnalysisTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getAnalysisTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		Analysis analysis = (Analysis) entry;
		long analysisId = analysis.getId();

		updateName(analysisId, analysis.getName());
		updateDescription(analysisId, analysis.getDescription());
		updateCorpusId(analysisId, analysis.getCorpusId());
		updateFeatureSetId(analysisId, analysis.getFeatureSetId());
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_NAME + " TEXT NOT NULL, "
				+ COLUMN_DESCRIPTION + " TEXT,"
				+ COLUMN_CORPUS_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getCorpusTableName() + "("
				+ "" + CorpusTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_FEATURESET_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getFeatureSetTableName() + "("
				+ "" + FeatureSetTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_STATUS + " VARCHAR(20) NOT NULL, " //running or stopped
				+ COLUMN_PROGRESS + " NUMERIC, " //in percentage
				+ COLUMN_CREATE_TIMESTAMP + " TIMESTAMP NOT NULL)"; 

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Analysis> getAllEntries() throws SQLException {
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
	public List<Analysis> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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
		Analysis analysis = (Analysis) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_NAME + ","
				+ COLUMN_DESCRIPTION + "," 
				+ COLUMN_CORPUS_ID + "," 
				+ COLUMN_FEATURESET_ID + "," 
				+ COLUMN_STATUS + "," 
				+ COLUMN_PROGRESS + "," 
				+ COLUMN_CREATE_TIMESTAMP +") "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setString(1, analysis.getName());
			ps.setString(2, analysis.getDescription());
			ps.setLong(3, analysis.getCorpusId());
			ps.setLong(4, analysis.getFeatureSetId());
			ps.setString(5, Analysis.Status.STOPPED);
			ps.setDouble(6, 0);

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
	public void updateName(long analysisId, String newName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_NAME + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newName);
			ps.setLong(2, analysisId);

			ps.executeUpdate();
		}
	}
	


	public void updateStatus(long analysisId, String newStatus) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_STATUS + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newStatus);
			ps.setLong(2, analysisId);

			ps.executeUpdate();
		}
	}
	
	public void updateProgress(long analysisId, double progress) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_PROGRESS + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setDouble(1, progress);
			ps.setLong(2, analysisId);

			ps.executeUpdate();
		}
	}
	
	public void updateDescription(long analysis, String newDescription) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_DESCRIPTION + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newDescription);
			ps.setLong(2, analysis);

			ps.executeUpdate();
		}
	}

	public void updateCorpusId(long analysisId, long newCorpusId) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_CORPUS_ID + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setLong(1, newCorpusId);
			ps.setLong(2, analysisId);

			ps.executeUpdate();
		}
	}
	
	public void updateFeatureSetId(long analysisId, long newFeaturesetId) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_FEATURESET_ID + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setLong(1, newFeaturesetId);
			ps.setLong(2, analysisId);

			ps.executeUpdate();
		}
	}
	
	
	@Override
	public Analysis getEntry(long entryId) throws SQLException {
		Analysis corpus = null;

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
			Analysis featureSet = (Analysis) entries.get(i);
			addedEntryIds[i] = addEntry(featureSet);

		}

		return addedEntryIds;
	}


	public boolean isUserOwner(long userId, long analysisId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName + " AS a "
				+ " JOIN " + tableNames.getFeatureSetTableName() + " AS fs "
						+ " ON a." + COLUMN_FEATURESET_ID + " = fs." + FeatureSetTableOperations.COLUMN_ID
				+ " WHERE a." + COLUMN_ID + " = ? "
				+ " AND fs." + FeatureSetTableOperations.COLUMN_OWNER_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, analysisId);
			ps.setLong(2, userId);

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

	public List<Analysis> getAllEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT a.* FROM " + tableName + " AS a "
				+ " JOIN " + tableNames.getFeatureSetTableName() + " AS fs "
						+ " ON a." + COLUMN_FEATURESET_ID + " = fs." + FeatureSetTableOperations.COLUMN_ID
				+ " WHERE fs." + FeatureSetTableOperations.COLUMN_OWNER_ID + " = ?";

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

	public List<Analysis> getEntriesByOwner(long ownerId, String orderByColumn, long limit, long offset) throws SQLException {

		String queryStr = "SELECT a.* FROM " + tableName + " AS a "
				+ " JOIN " + tableNames.getFeatureSetTableName() + " AS fs "
						+ " ON a." + COLUMN_FEATURESET_ID + " = fs." + FeatureSetTableOperations.COLUMN_ID
				+ " WHERE fs." + FeatureSetTableOperations.COLUMN_OWNER_ID + " = ?"
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

	private Analysis getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets corpus info
			return new Analysis(
					rs.getLong(COLUMN_ID),
					rs.getString(COLUMN_NAME),
					rs.getString(COLUMN_DESCRIPTION),
					rs.getLong(COLUMN_CORPUS_ID),
					rs.getLong(COLUMN_FEATURESET_ID),
					rs.getString(COLUMN_STATUS),
					rs.getDouble(COLUMN_PROGRESS),
					rs.getTimestamp(COLUMN_CREATE_TIMESTAMP)
					);
		}
		return null;
	}

	private List<Analysis> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Analysis> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Analysis corpus = new Analysis(
						rs.getLong(COLUMN_ID),
						rs.getString(COLUMN_NAME),
						rs.getString(COLUMN_DESCRIPTION),
						rs.getLong(COLUMN_CORPUS_ID),
						rs.getLong(COLUMN_FEATURESET_ID),
						rs.getString(COLUMN_STATUS),
						rs.getDouble(COLUMN_PROGRESS),
						rs.getTimestamp(COLUMN_CREATE_TIMESTAMP)
						);
				entryList.add(corpus);
			}
		}
		return entryList;
	}


	public long getNumEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName + " AS a "
				+ " JOIN " + tableNames.getFeatureSetTableName() + " AS fs "
						+ " ON a." + COLUMN_FEATURESET_ID + " = fs." + FeatureSetTableOperations.COLUMN_ID
				+ " WHERE fs." + FeatureSetTableOperations.COLUMN_OWNER_ID + " = ?";

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
