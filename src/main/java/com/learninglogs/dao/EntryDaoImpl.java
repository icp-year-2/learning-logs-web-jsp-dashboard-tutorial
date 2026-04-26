package com.learninglogs.dao;

import com.learninglogs.entity.Entry;
import com.learninglogs.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EntryDaoImpl implements EntryDao {

    @Override
    public boolean insertEntry(Entry entry) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO entries (topic_id, title, text, link, image) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, entry.getTopicId());
            statement.setString(2, entry.getTitle());
            statement.setString(3, entry.getText());
            statement.setString(4, entry.getLink());
            statement.setString(5, entry.getImage());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting entry: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Entry> fetchAllEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM entries";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Entry entry = new Entry(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("topic_id"),
                    rs.getString("link"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching entries: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return entries;
    }

    @Override
    public ArrayList<Entry> fetchEntriesByTopicId(int topicId) {
        ArrayList<Entry> entries = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM entries WHERE topic_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, topicId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Entry entry = new Entry(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("topic_id"),
                    rs.getString("link"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching entries by topic: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return entries;
    }

    @Override
    public Entry findEntryById(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM entries WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Entry(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("topic_id"),
                    rs.getString("link"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding entry: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean updateEntry(Entry entry) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE entries SET title = ?, text = ?, link = ?, image = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, entry.getTitle());
            statement.setString(2, entry.getText());
            statement.setString(3, entry.getLink());
            statement.setString(4, entry.getImage());
            statement.setInt(5, entry.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating entry: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteEntry(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM entries WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting entry: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Entry> searchEntries(int topicId, String keyword) {
        ArrayList<Entry> entries = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM entries WHERE topic_id = ? AND LOWER(title) LIKE LOWER(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, topicId);
            statement.setString(2, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Entry entry = new Entry(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("topic_id"),
                    rs.getString("link"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.out.println("Error searching entries: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return entries;
    }

    // ============================================================
    // TODO 5: Implement countEntriesByUserId
    // ============================================================
    // Count ALL entries across all topics belonging to a user.
    //
    // Steps:
    //   1. SQL: SELECT COUNT(*) AS count
    //           FROM entries e
    //           JOIN topics t ON e.topic_id = t.id
    //           WHERE t.user_id = ?
    //   2. Set the userId parameter
    //   3. Execute and read rs.getInt("count")
    //
    // WHY JOIN: Entries don't have a user_id column — they belong
    // to topics, and topics belong to users. The JOIN connects
    // entries -> topics -> users so we can filter by user.
    //
    // See: references/01-aggregate-queries.md (JOIN Aggregates)
    //
    // The complete code:
    //
    //   @Override
    //   public int countEntriesByUserId(int userId) {
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT COUNT(*) AS count FROM entries e"
    //                      + " JOIN topics t ON e.topic_id = t.id"
    //                      + " WHERE t.user_id = ?";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setInt(1, userId);
    //           ResultSet rs = statement.executeQuery();
    //           if (rs.next()) {
    //               return rs.getInt("count");
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error counting entries: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return 0;
    //   }
    // ============================================================
    @Override
    public int countEntriesByUserId(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM entries e JOIN topics t ON e.topic_id = t.id WHERE t.user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting entries: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    // ============================================================
    // TODO 6: Implement countEntriesTodayByUserId
    // ============================================================
    // Count entries created TODAY for a given user.
    //
    // Steps:
    //   1. SQL: SELECT COUNT(*) AS count
    //           FROM entries e
    //           JOIN topics t ON e.topic_id = t.id
    //           WHERE t.user_id = ?
    //           AND DATE(e.created_at) = CURDATE()
    //   2. Set the userId parameter
    //   3. Execute and read the count
    //
    // WHY DATE(): created_at is a TIMESTAMP (date + time).
    // DATE() strips the time so we can compare just the date
    // part against CURDATE() (which has no time component).
    //
    // See: references/01-aggregate-queries.md (CURDATE)
    //
    // The complete code:
    //
    //   @Override
    //   public int countEntriesTodayByUserId(int userId) {
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT COUNT(*) AS count FROM entries e"
    //                      + " JOIN topics t ON e.topic_id = t.id"
    //                      + " WHERE t.user_id = ?"
    //                      + " AND DATE(e.created_at) = CURDATE()";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setInt(1, userId);
    //           ResultSet rs = statement.executeQuery();
    //           if (rs.next()) {
    //               return rs.getInt("count");
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error counting entries today: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return 0;
    //   }
    // ============================================================
    @Override
    public int countEntriesTodayByUserId(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM entries e JOIN topics t ON e.topic_id = t.id WHERE t.user_id = ? AND DATE(e.created_at) = CURDATE()";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting entries today: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }
}
