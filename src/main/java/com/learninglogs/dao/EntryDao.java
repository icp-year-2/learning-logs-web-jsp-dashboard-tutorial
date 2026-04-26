package com.learninglogs.dao;

import com.learninglogs.entity.Entry;
import java.util.ArrayList;

public interface EntryDao {
    boolean insertEntry(Entry entry);
    ArrayList<Entry> fetchAllEntries();
    ArrayList<Entry> fetchEntriesByTopicId(int topicId);

    Entry findEntryById(int id);
    boolean updateEntry(Entry entry);
    boolean deleteEntry(int id);
    ArrayList<Entry> searchEntries(int topicId, String keyword);

    // ============================================================
    // TODO 5: Add countEntriesByUserId Signature
    // ============================================================
    // Add a method to count ALL entries across all of a user's
    // topics. Since entries don't have a user_id column directly,
    // the implementation will need a JOIN through the topics table.
    //
    //   int countEntriesByUserId(int userId)
    //
    // See: references/01-aggregate-queries.md (JOIN Aggregates)
    //
    // The complete code:
    //
    //   int countEntriesByUserId(int userId);
    // ============================================================
    int countEntriesByUserId(int userId);

    // ============================================================
    // TODO 6: Add countEntriesTodayByUserId Signature
    // ============================================================
    // Add a method to count entries created TODAY for a user.
    // Similar JOIN as TODO 5, but with an additional date filter.
    //
    //   int countEntriesTodayByUserId(int userId)
    //
    // See: references/01-aggregate-queries.md (CURDATE)
    //
    // The complete code:
    //
    //   int countEntriesTodayByUserId(int userId);
    // ============================================================
    int countEntriesTodayByUserId(int userId);
}
