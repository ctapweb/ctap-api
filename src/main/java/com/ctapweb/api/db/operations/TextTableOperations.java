package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ctapweb.api.db.pojos.TablePojo;
import com.ctapweb.api.db.pojos.Text;

public class TextTableOperations extends TableOperations {
	public static final String COLUMN_CORPUS_ID = "corpus_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_TAG_ID = "tag_id";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_UPDATE_TIMESTAMP = "update_timestamp";


	public TextTableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		super(dataSource);
		this.tableName = tableNames.getTextTableName();
	}

	@Override
	public <T extends TablePojo> void updateEntry(T entry) throws SQLException {
		//cast the entry type
		Text text = (Text) entry;
		long textId = text.getId();

		updateTitle(textId, text.getTitle());
		updateContent(textId, text.getContent());
		if(text.getTagId() != null && text.getTagId() != 0) {
			try {
				updateTag(textId, text.getTagId());
			} catch (ClassNotFoundException | IOException e) {
				throw logger.throwing(new SQLException(e));
			}
		}
	}

	@Override
	public void createTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tableName + "("
				+ COLUMN_ID + " BIGSERIAL PRIMARY KEY NOT NULL,"
				+ COLUMN_CORPUS_ID + " BIGINT NOT NULL REFERENCES "
				+ "" + tableNames.getCorpusTableName() + "("
				+ "" + CorpusTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_TITLE + " TEXT NOT NULL, "
				+ COLUMN_CONTENT + " TEXT,"
				+ COLUMN_TAG_ID + " BIGINT REFERENCES " 
				+ "" + tableNames.getTagTableName() + "("
				+ "" + TagTableOperations.COLUMN_ID + ") ON DELETE CASCADE,"
				+ COLUMN_STATUS + " VARCHAR(20),"
				+ COLUMN_UPDATE_TIMESTAMP+ " TIMESTAMP NOT NULL)"; 

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(createTableStr);
				) {
			ps.execute();
		}
	}

	@Override
	public List<Text> getAllEntries() throws SQLException {
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
	public List<Text> getEntries(String orderByColumn, long limit, long offset) throws SQLException {

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
		Text text = (Text) entry;

		//the query 
		String addEntryStr = "INSERT INTO " + tableName + " (" 
				+ COLUMN_CORPUS_ID + "," + COLUMN_TITLE + ","
				+ COLUMN_CONTENT + ","  + COLUMN_TAG_ID + ","
				+ COLUMN_STATUS + "," + COLUMN_UPDATE_TIMESTAMP +") "
				+ "VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING " + COLUMN_ID;

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(addEntryStr);
				) {

			ps.setLong(1, text.getCorpusId());
			ps.setString(2, text.getTitle());
			ps.setString(3, text.getContent());

			if(text.getTagId() == null) {
				ps.setNull(4, Types.NULL);
			} else {
				ps.setLong(4, text.getTagId());
			}

			ps.setString(5, text.getStatus());

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

	public void updateTitle(long textId, String newTitle) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_TITLE + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newTitle);
			ps.setLong(2, textId);

			ps.executeUpdate();
		}
	}

	/**
	 * This update commands also sets the analysis status to "not analyzed".
	 */
	public void updateContent(long textId, String newContent) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_CONTENT + " = ?, " 
				+ COLUMN_STATUS + " = ?, "
				+ COLUMN_UPDATE_TIMESTAMP + " = CURRENT_TIMESTAMP "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newContent);
			ps.setString(2, Text.STATUS_NOT_ANALYZED);
			ps.setLong(3, textId);

			ps.executeUpdate();
		}
	}

	public void updateTag(long textId, long newTagId) throws SQLException, ClassNotFoundException, IOException {
		//makes sure the tag and the text belong to the same corpus
		TagTableOperations tagTableOperations = new TagTableOperations(dataSource);
		long tagCorpusId = tagTableOperations.getEntry(newTagId).getCorpusId();
		long textCorpusId = getEntry(textId).getCorpusId();
		if(tagCorpusId != textCorpusId) {
			throw logger.throwing(new SQLException("Tag and texts do not belong to the same corpus."));
		}

		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_TAG_ID + " = ? " 
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setLong(1, newTagId);
			ps.setLong(2, textId);

			ps.executeUpdate();
		}
	}

	public void removeTag(long textId) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_TAG_ID + " = NULL " 
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setLong(1, textId);

			ps.executeUpdate();
		}

	}

	public void updateStatus(long textId, String newStatus) throws SQLException {
		String updateStr = "UPDATE " + tableName + " "
				+ "SET " + COLUMN_STATUS + " = ? "
				+ "WHERE " + COLUMN_ID + " = ?";
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(updateStr);
				) {

			ps.setString(1, newStatus);
			ps.setLong(2, textId);

			ps.executeUpdate();
		}
	}


	@Override
	public Text getEntry(long entryId) throws SQLException {
		Text text = null;

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
			Text text = (Text) entries.get(i);
			addedEntryIds[i] = addEntry(text);

		}

		return addedEntryIds;
	}


	public boolean isUserOwner(long userId, long textId) throws SQLException {
		String queryStr = "SELECT t.*  FROM " + tableNames.getTextTableName() + " AS t "
				+ "JOIN " + tableNames.getCorpusTableName() + " AS c "
				+ "ON t." + COLUMN_CORPUS_ID + " = c." + CorpusTableOperations.COLUMN_ID
				+ " WHERE t." + TextTableOperations.COLUMN_ID + " = ? "
				+ " AND c." + CorpusTableOperations.COLUMN_OWNER_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setLong(1, textId);
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

	public List<Text> getAllEntriesByCorpus(long corpusId) throws SQLException {
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

	public List<Text> getAllEntriesByTag(long tagId) throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_TAG_ID + " = ?";

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

	public List<Text> getEntriesByCorpus(long corpusId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_CORPUS_ID + " = ?"
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

	public List<Text> getEntriesByTag(long tagId, String orderByColumn, long limit, long offset) 
			throws SQLException {
		String queryStr = "SELECT * FROM " + tableName 
				+ " WHERE " + COLUMN_TAG_ID + " = ?"
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
	private Text getEntryFromResultSet(ResultSet rs) throws SQLException {
		if(rs.isBeforeFirst()) {
			rs.next();

			//gets text info
			return new Text(
					rs.getLong(COLUMN_ID),
					rs.getLong(COLUMN_CORPUS_ID),
					rs.getString(COLUMN_TITLE),
					rs.getString(COLUMN_CONTENT),
					rs.getLong(COLUMN_TAG_ID),
					rs.getString(COLUMN_STATUS),
					rs.getTimestamp(COLUMN_UPDATE_TIMESTAMP)
					);
		}
		return null;
	}

	private List<Text> getEntriesFromResultSet(ResultSet rs) throws SQLException {
		List<Text> entryList = null;

		if(rs.isBeforeFirst()) {
			entryList = new ArrayList<>();
			while(rs.next()) {
				Text text = new Text(
						rs.getLong(COLUMN_ID),
						rs.getLong(COLUMN_CORPUS_ID),
						rs.getString(COLUMN_TITLE),
						rs.getString(COLUMN_CONTENT),
						rs.getLong(COLUMN_TAG_ID),
						rs.getString(COLUMN_STATUS),
						rs.getTimestamp(COLUMN_UPDATE_TIMESTAMP)
						);
				entryList.add(text);
			}
		}
		return entryList;
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

	public long getNumEntriesByTag(long tagId) throws SQLException {
		String queryStr = "SELECT COUNT(*)  FROM " + tableName 
				+ " WHERE " + COLUMN_TAG_ID + " = ?";
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

}
