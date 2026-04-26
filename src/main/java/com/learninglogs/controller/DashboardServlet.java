package com.learninglogs.controller;

import com.learninglogs.dao.EntryDao;
import com.learninglogs.dao.EntryDaoImpl;
import com.learninglogs.dao.TopicDao;
import com.learninglogs.dao.TopicDaoImpl;
import com.learninglogs.entity.Topic;
import com.learninglogs.entity.User;
import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final TopicDao topicDao = new TopicDaoImpl();
    private final EntryDao entryDao = new EntryDaoImpl();

    // ============================================================
    // TODO 7: Implement doGet — Aggregate Data and Forward
    // ============================================================
    // The dashboard shows KPI cards and recent topics for the
    // logged-in user. This servlet gathers all the data and
    // forwards it to dashboard.jsp.
    //
    // Steps:
    //   1. Get the user from the session:
    //      User user = (User) SessionUtil.getAttribute(request, "user");
    //
    //   2. Call the 5 DAO methods you implemented in TODOs 2-6:
    //      - topicDao.countTopicsByUserId(user.getId())
    //      - topicDao.countTopicsThisWeekByUserId(user.getId())
    //      - topicDao.fetchRecentTopicsByUserId(user.getId(), 5)
    //      - entryDao.countEntriesByUserId(user.getId())
    //      - entryDao.countEntriesTodayByUserId(user.getId())
    //
    //   3. Set each result as a request attribute:
    //      - "totalTopics", "topicsThisWeek", "recentTopics",
    //        "totalEntries", "entriesToday"
    //
    //   4. Forward to the dashboard view:
    //      request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
    //             .forward(request, response);
    //
    // WHY this pattern: The servlet acts as a controller — it
    // gathers data from multiple DAOs and passes it to the view.
    // The JSP only handles presentation, never database access.
    //
    // See: references/02-dashboard-servlet-pattern.md
    //
    // The complete code:
    //
    //   User user = (User) SessionUtil.getAttribute(request, "user");
    //
    //   int totalTopics = topicDao.countTopicsByUserId(user.getId());
    //   int topicsThisWeek = topicDao.countTopicsThisWeekByUserId(user.getId());
    //   ArrayList<Topic> recentTopics = topicDao.fetchRecentTopicsByUserId(user.getId(), 5);
    //   int totalEntries = entryDao.countEntriesByUserId(user.getId());
    //   int entriesToday = entryDao.countEntriesTodayByUserId(user.getId());
    //
    //   request.setAttribute("totalTopics", totalTopics);
    //   request.setAttribute("topicsThisWeek", topicsThisWeek);
    //   request.setAttribute("recentTopics", recentTopics);
    //   request.setAttribute("totalEntries", totalEntries);
    //   request.setAttribute("entriesToday", entriesToday);
    //
    //   request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
    //          .forward(request, response);
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

    }
}
