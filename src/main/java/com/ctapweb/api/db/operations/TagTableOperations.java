package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.TablePojo;
import com.ctapweb.api.db.pojos.Tag;

public class TagTableOperations extends TableOperations {
	public static final String COLUMN_CORPUS_ID = "corpus_id";
	public static final String COLUMN_NAME = "name";


	public TagTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getTagTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		Tag tag = (Tag) entry;
		long tagId = tag.getId();

		updateName(tagId, tag.getName());
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_CORPUS_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getCorpusTableName() + "("
				+ "" + CorpusTableOperations.COLUMN_ID + ") ON DELETE CASCADE, "
				+ COLUMN_NAME + " VARCHAR(50) NOT NULL, "
				+ " UNIQUE(" + COLUMN_CORPUS_ID + ", " + COLUMN_NAME + "))";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Tag> getAllEntries() throws SQLException {
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
	public List<Tag> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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
		Tag tag = (Tag) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_CORPUS_ID + "," + COLUMN_NAME + " ) "
				+ "VALUES(?, ?) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, tag.getCorpusId());
			ps.setString(2, tag.getName());

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

	public void updateName(long tagId, String newName) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_NAME + " = ? " 
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newName);
			ps.setLong(2, tagId);

			ps.executeUpdate();
		}
	}

	

	@Override
	public Tag getEntry(long entryId) throws SQLException {
		Tag tag = null;

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
			Tag tag = (Tag) entries.get(i);
			addedEntryIds[i] = addEntry(tag);

		}

		return addedEntryIds;
	}


	public boolean isUserOwner(long userId, long tagId) throws SQLException {
		String queryStr = "SELECT tag.*  FROM " + tableName + " AS tag "
				+ "JOIN " + tableNames.getCorpusTableName() + " AS c "
				+ "ON tag." + COLUMN_CORPUS_ID + " = c." + CorpusTableOperations.COLUMN_ID
				+ " WHERE tag." + TagTableOperations.COLUMN_ID + " = ? "
				+ " AND c." + CorpusTableOperations.COLUMN_OWNER_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, tagId);
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

	public List<Tag> getAllEntriesByCorpus(long corpusId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_CORPUS_ID + " = ?";

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

	public List<Tag> getAllEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT tag.*  FROM " + tableName + " AS tag "
				+ "JOIN " + tableNames.getCorpusTableName() + " AS c "
				+ "ON tag." + COLUMN_CORPUS_ID + " = c." + CorpusTableOperations.COLUMN_ID
				+ " WHERE c." + CorpusTableOperations.COLUMN_OWNER_ID + " = ?";

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

	public List<Tag> getEntriesByOwner(long ownerId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT tag.*  FROM " + tableName + " AS tag "
				+ "JOIN " + tableNames.getCorpusTableName() + " AS c "
				+ "ON tag." + COLUMN_CORPUS_ID + " = c." + CorpusTableOperations.COLUMN_ID
				+ " WHERE c." + CorpusTableOperations.COLUMN_OWNER_ID + " = ? "
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
	
	public List<Tag> getEntriesByCorpus(long corpusId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT * FROM " + tableName + " WHERE " + COLUMN_CORPUS_ID + " = ?"
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

	private Tag getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets text info
			return new Tag(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_CORPUS_ID),
					rs.getString(COLUMN_NAME));
		}
		return null;
	}

	private List<Tag> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Tag> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Tag tag = new Tag(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_CORPUS_ID),
						rs.getString(COLUMN_NAME));
				entryList.add(tag);
			}
		}
		return entryList;
	}


	public long getNumEntriesByOwner(long ownerId) throws SQLException {
		String queryStr = "SELECT COUNT(*)  FROM " + tableName + " AS tag "
				+ "JOIN " + tableNames.getCorpusTableName() + " AS c "
				+ "ON tag." + COLUMN_CORPUS_ID + " = c." + CorpusTableOperations.COLUMN_ID
				+ " WHERE c." + CorpusTableOperations.COLUMN_OWNER_ID + " = ? ";
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

	public long getNumEntriesByCorpus(long corpusId) throws SQLException {
		String queryStr = "SELECT COUNT(*)  FROM " + tableName 
				+ " WHERE " + COLUMN_CORPUS_ID + " = ?";
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

	public boolean isTagExist(long corpusId, String tagName) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_CORPUS_ID + " = ? "
				+ " AND " + COLUMN_NAME + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {

			ps.setLong(1, corpusId);
			ps.setString(2, tagName);
			try( 
					ResultSet rs = ps.executeQuery();
					) {
				if(rs.isBeforeFirst()) {
					return true;
				}
			}
			return false;
		}

	}
}
