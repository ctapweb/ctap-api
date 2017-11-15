package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.Result;
import com.ctapweb.api.db.pojos.TablePojo;

public class ResultTableOperations extends TableOperations {
	public static final String COLUMN_TEXT_ID = "text_id";
	public static final String COLUMN_MEASURE_ID = "measure_id";
	public static final String COLUMN_VALUE = "value";

	public ResultTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getResultTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		Result result = (Result) entry;

		updateValue(result.getId(), result.getValue());
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_TEXT_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getTextTableName() + "("
				+ "" + TextTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_MEASURE_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getMeasureTableName() + "("
				+ "" + MeasureTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_VALUE + " numeric NOT NULL,"
				+ "UNIQUE (" + COLUMN_TEXT_ID + ", " + COLUMN_MEASURE_ID + "))";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Result> getAllEntries() throws SQLException {
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
	public List<Result> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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

		//cast entry to text
		Result result = (Result) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_TEXT_ID + "," + COLUMN_MEASURE_ID + ","
				+ COLUMN_VALUE + ")"
				+ "VALUES(?, ?, ?) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, result.getTextId());
			ps.setLong(2, result.getMeasureId());
			ps.setDouble(3, result.getValue());

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

	public void updateValue(long resultId, double newValue) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_VALUE + " = ? " 
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setDouble(1, newValue);
			ps.setLong(2, resultId);

			ps.executeUpdate();
		}
	}






	@Override
	public Result getEntry(long entryId) throws SQLException {
		Result text = null;

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
			Result result = (Result) entries.get(i);
			addedEntryIds[i] = addEntry(result);

		}

		return addedEntryIds;
	}


	public List<Result> getAllEntriesByText(long textId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_TEXT_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, textId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Result> getAllEntriesByCorpusAndMeasure(long corpusId, long measureId) throws SQLException {
		String queryStr = "SELECT r.* FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ? "
				+ " AND r." + COLUMN_MEASURE_ID + " = ?";


		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, corpusId);
			ps.setLong(2, measureId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}
		}
	}
	public List<Result> getAllEntriesByCorpus(long corpusId) throws SQLException {
		String queryStr = "SELECT r.* FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, corpusId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Result> getAllEntriesByTag(long tagId) throws SQLException {
		String queryStr = "SELECT r.* FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS text "
				+ "ON r." + COLUMN_TEXT_ID + " = text." + TextTableOperations.COLUMN_ID
				+ " JOIN " + tableNames.getTagTableName() + " AS tag "
				+ "ON text." + TextTableOperations.COLUMN_TAG_ID + " = tag." + TagTableOperations.COLUMN_ID
				+ " WHERE tag." + TagTableOperations.COLUMN_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, tagId);
			try(
					ResultSet rs = ps.executeQuery();
					){
				return getEntriesFromResultSet(rs);
			}

		}
	}

	public List<Result> getEntriesByCorpus(long corpusId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT r.* FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ?"
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, corpusId);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Result> getEntriesByTag(long tagId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT r.* FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS text "
				+ "ON r." + COLUMN_TEXT_ID + " = text." + TextTableOperations.COLUMN_ID
				+ " JOIN " + tableNames.getTagTableName() + " AS tag "
				+ "ON text." + TextTableOperations.COLUMN_TAG_ID + " = tag." + TagTableOperations.COLUMN_ID
				+ " WHERE tag." + TagTableOperations.COLUMN_ID + " = ?"
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, tagId);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Result> getEntriesByText(long textId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_TEXT_ID + " = ?"
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, textId);
			ps.setLong(2, limit);
			ps.setLong(3, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	public List<Result> getEntriesByCorpusAndMeasure(long corpusId, long measureId, 
			String orderByColumn, long limit, long offset) 
					throws SQLException {
		String queryStr = "SELECT COUNT(r.*) FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ?"
				+ " AND r." + COLUMN_MEASURE_ID + " = ?"
				+ " ORDER BY " + orderByColumn + " LIMIT ? OFFSET ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, corpusId);
			ps.setLong(2, measureId);
			ps.setLong(3, limit);
			ps.setLong(4, offset);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				return getEntriesFromResultSet(rs);
			}
		}
	}

	private Result getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets text info
			return new Result(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_TEXT_ID),
					rs.getLong(COLUMN_MEASURE_ID),
					rs.getDouble(COLUMN_VALUE));
		}
		return null;
	}

	private List<Result> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Result> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Result result = new Result(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_TEXT_ID),
						rs.getLong(COLUMN_MEASURE_ID),
						rs.getDouble(COLUMN_VALUE));
				entryList.add(result);
			}
		}
		return entryList;
	}

	public long getNumEntriesByCorpus(long corpusId) throws SQLException {
		String queryStr = "SELECT COUNT(r.*) FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, corpusId);
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

	public long getNumEntriesByTag(long tagId) throws SQLException {
		String queryStr = "SELECT COUNT(r.*) FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS text "
				+ "ON r." + COLUMN_TEXT_ID + " = text." + TextTableOperations.COLUMN_ID
				+ " JOIN " + tableNames.getTagTableName() + " AS tag "
				+ "ON text." + TextTableOperations.COLUMN_TAG_ID + " = tag." + TagTableOperations.COLUMN_ID
				+ " WHERE tag." + TagTableOperations.COLUMN_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, tagId);
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

	public long getNumEntriesByText(long textId) throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName 
				+ " WHERE " + COLUMN_TEXT_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, textId);
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

	public long getNumEntriesByCorpusAndMeasure(long corpusId, long measureId) throws SQLException {
		String queryStr = "SELECT COUNT(r.*) FROM " + tableName + " AS r "
				+ " JOIN " + tableNames.getTextTableName() + " AS t "
				+ "ON r." + COLUMN_TEXT_ID + " = t." + TextTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_CORPUS_ID + " = ?"
				+ " AND r." + COLUMN_MEASURE_ID + " = ?";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, corpusId);
			ps.setLong(2, measureId);
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
