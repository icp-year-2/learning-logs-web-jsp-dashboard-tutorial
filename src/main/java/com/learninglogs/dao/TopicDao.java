package com.learninglogs.dao;

import com.learninglogs.entity.Topic;
import java.util.ArrayList;

public interface TopicDao {
    boolean insertTopic(Topic topic);
    ArrayList<Topic> fetchAllTopics();
    Topic findTopicByNameAndUserId(String name, int userId);

    Topic findTopicById(int id);
    boolean updateTopic(Topic topic);
    boolean deleteTopic(int id);
    ArrayList<Topic> searchTopics(String keyword);

    ArrayList<Topic> fetchAllTopicsByUserId(int userId);
    ArrayList<Topic> searchTopicsByUserId(int userId, String keyword);

    boolean checkUserForTopic(int userId, int topicId);

    // ============================================================
    // TODO 1: Add Dashboard DAO Method Signatures
    // ============================================================
    // The dashboard needs three pieces of topic data for the
    // logged-in user. Add these method signatures:
    //
    //   1. int countTopicsByUserId(int userId)
    //      - Returns the total number of topics for this user
    //
    //   2. int countTopicsThisWeekByUserId(int userId)
    //      - Returns the count of topics created THIS WEEK
    //
    //   3. ArrayList<Topic> fetchRecentTopicsByUserId(int userId, int limit)
    //      - Returns the most recent topics (newest first),
    //        limited to `limit` results
    //
    // WHY: Interfaces define the contract — the implementation
    // class (TopicDaoImpl) must provide the actual SQL queries.
    // ============================================================

}
