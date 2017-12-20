package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.Fs_Me;
import com.ctapweb.api.db.pojos.TablePojo;

public class FS_METableOperations extends TableOperations {
	public static final String COLUMN_FS_ID = "fs_id";
	public static final String COLUMN_MEASURE_ID = "measure_id";


	public FS_METableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getFsMeTableName();
	}

	public void deleteEntry(long fsId, long measureId) throws SQLException {
		String deleteStr = "DELETE FROM " + tableName 
				+ " WHERE " + COLUMN_FS_ID + " = ? "
				+ " AND " + COLUMN_MEASURE_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(deleteStr);
				){
			ps.setLong(1, fsId);
			ps.setLong(2, measureId);
			ps.executeUpdate();
		}


	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_FS_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getFeatureSetTableName() + "("
				+ "" + FeatureSetTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_MEASURE_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getMeasureTableName() + "("
				+ "" + MeasureTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ " UNIQUE(" + COLUMN_FS_ID + ", " + COLUMN_MEASURE_ID + "))"; 

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Fs_Me> getAllEntries() throws SQLException {
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
	public List<Fs_Me> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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

	public List<Fs_Me> getEntriesByFeatureSet(long featureSetId, String orderByColumn, long limit, long offset) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_FS_ID + " = ?" 
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, featureSetId);
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

		//cast entry to fs_me
		Fs_Me fs_me = (Fs_Me) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_FS_ID + ","  
				+ COLUMN_MEASURE_ID  + ") "
				+ "VALUES(?, ?) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, fs_me.getFs_id());
			ps.setLong(2, fs_me.getMeasure_id());

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


	@Override
	public Fs_Me getEntry(long entryId) throws SQLException {
		Fs_Me corpus = null;

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
			Fs_Me fs_me = (Fs_Me) entries.get(i);
			addedEntryIds[i] = addEntry(fs_me);

		}

		return addedEntryIds;
	}


	public boolean isMeasureExistInFeatureSet(long featureSetId, long measureId) 
			throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_FS_ID + " = ? "
				+ " AND " + COLUMN_MEASURE_ID + " = ?";

				try(
						Connection conn = dataSource.getConnection();
						PreparedStatement ps = conn.prepareStatement(queryStr);
						) {
					ps.setLong(1, featureSetId);
					ps.setLong(2, measureId);

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

	public List<Fs_Me> getAllEntriesByFeatureSet(long featureSetId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_FS_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, featureSetId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	private Fs_Me getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets corpus info
			return new Fs_Me(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_FS_ID),
					rs.getLong(COLUMN_MEASURE_ID)
					);
		}
		return null;
	}

	private List<Fs_Me> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Fs_Me> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Fs_Me fs_me = new Fs_Me(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_FS_ID),
						rs.getLong(COLUMN_MEASURE_ID)
						);
				entryList.add(fs_me);
			}
		}
		return entryList;
	}


	public long getNumEntriesByFeatureSet(long categoryId) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName 
				+ " WHERE " + COLUMN_FS_ID + " = ?";

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


	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//does nothing, no need to update entry
	}
}
