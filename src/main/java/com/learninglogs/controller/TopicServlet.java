package com.learninglogs.controller;

import com.learninglogs.dao.EntryDao;
import com.learninglogs.dao.EntryDaoImpl;
import com.learninglogs.dao.TopicDao;
import com.learninglogs.dao.TopicDaoImpl;
import com.learninglogs.entity.Entry;
import com.learninglogs.entity.Topic;
import com.learninglogs.entity.User;
import com.learninglogs.utils.ImageUtil;
import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/topic")
public class TopicServlet extends HttpServlet {

    private final TopicDao topicDao = new TopicDaoImpl();
    private final EntryDao entryDao = new EntryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        User user = (User) SessionUtil.getAttribute(request, "user");

        String success = (String) SessionUtil.getAttribute(request, "success");
        if (success != null) {
            request.setAttribute("success", success);
            SessionUtil.removeAttribute(request, "success");
        }

        if (action == null) {
            ArrayList<Topic> topics = topicDao.fetchAllTopicsByUserId(user.getId());
            request.setAttribute("topics", topics);
            request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
                   .forward(request, response);
        }
        else if ("new".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("edit".equals(action)) {
            int topicId;
            try {
                topicId = Integer.parseInt(request.getParameter("topicid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            if (!topicDao.checkUserForTopic(user.getId(), topicId)) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("search".equals(action)) {
            String keyword = request.getParameter("search");
            ArrayList<Topic> topics;
            if (keyword == null || keyword.trim().isEmpty()) {
                topics = topicDao.fetchAllTopicsByUserId(user.getId());
            } else {
                topics = topicDao.searchTopicsByUserId(user.getId(), keyword.trim());
            }
            request.setAttribute("topics", topics);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String topicName = request.getParameter("topic");

            if (topicName == null || topicName.trim().isEmpty()) {
                request.setAttribute("error", "Topic name cannot be empty.");
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Topic newTopic = new Topic(topicName.trim());
            User user = (User) SessionUtil.getAttribute(request, "user");
            newTopic.setUserId(user.getId());
            boolean success = topicDao.insertTopic(newTopic);

            if (!success) {
                request.setAttribute("error", "Topic already exists.");
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            SessionUtil.setAttribute(request, "success", "Topic added successfully!");
            response.sendRedirect(request.getContextPath() + "/topic");
        }
        else if ("edit".equals(action)) {
            int topicId;
            try {
                topicId = Integer.parseInt(request.getParameter("topicid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (!topicDao.checkUserForTopic(user.getId(), topicId)) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            String topicName = request.getParameter("topic");

            if (topicName == null || topicName.trim().isEmpty()) {
                request.setAttribute("error", "Topic name cannot be empty.");
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Topic topic = new Topic(topicName.trim());
            topic.setId(topicId);
            topicDao.updateTopic(topic);
            SessionUtil.setAttribute(request, "success", "Topic updated successfully!");
            response.sendRedirect(request.getContextPath() + "/topic");
        }
        else if ("delete".equals(action)) {
            int topicId;
            try {
                topicId = Integer.parseInt(request.getParameter("topicid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (!topicDao.checkUserForTopic(user.getId(), topicId)) {
                response.sendRedirect(request.getContextPath() + "/topic");
                return;
            }
            ArrayList<Entry> entries = entryDao.fetchEntriesByTopicId(topicId);
            for (Entry entry : entries) {
                ImageUtil.deleteImage(entry.getImage());
            }
            topicDao.deleteTopic(topicId);
            SessionUtil.setAttribute(request, "success", "Topic deleted successfully!");
            response.sendRedirect(request.getContextPath() + "/topic");
        }
    }
}
