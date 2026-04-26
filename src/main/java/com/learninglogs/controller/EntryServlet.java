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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/entry")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize       = 1024 * 1024 * 10,
    maxRequestSize    = 1024 * 1024 * 50
)
public class EntryServlet extends HttpServlet {

    private final EntryDao entryDao = new EntryDaoImpl();
    private final TopicDao topicDao = new TopicDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

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

        String success = (String) SessionUtil.getAttribute(request, "success");
        if (success != null) {
            request.setAttribute("success", success);
            SessionUtil.removeAttribute(request, "success");
        }

        String action = request.getParameter("action");

        if (action == null) {
            ArrayList<Entry> entries = entryDao.fetchEntriesByTopicId(topicId);
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entries", entries);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-list.jsp")
                   .forward(request, response);
        }

        else if ("new".equals(action)) {
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("edit".equals(action)) {
            int entryId;
            try {
                entryId = Integer.parseInt(request.getParameter("entryid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            Entry entry = entryDao.findEntryById(entryId);
            if (entry == null || entry.getTopicId() != topicId) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entry", entry);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                   .forward(request, response);
        }

        else if ("search".equals(action)) {
            String keyword = request.getParameter("search");
            ArrayList<Entry> entries;
            if (keyword == null || keyword.trim().isEmpty()) {
                entries = entryDao.fetchEntriesByTopicId(topicId);
            } else {
                entries = entryDao.searchEntries(topicId, keyword.trim());
            }
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entries", entries);
            request.setAttribute("topic", topic);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/WEB-INF/views/entry-list.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

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

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            String link = request.getParameter("link");

            if (title == null || title.trim().isEmpty()
                    || text == null || text.trim().isEmpty()) {
                request.setAttribute("error", "Title and description are required.");
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Part imagePart = request.getPart("image");
            String imagePath = ImageUtil.uploadImage(imagePart);
            if (imagePath == null) {
                imagePath = "static/images/book.png";
            }

            Entry entry = new Entry(title.trim(), text.trim(), topicId);
            entry.setLink(link);
            entry.setImage(imagePath);
            entryDao.insertEntry(entry);
            SessionUtil.setAttribute(request, "success", "Entry added successfully!");
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }

        else if ("edit".equals(action)) {
            int entryId;
            try {
                entryId = Integer.parseInt(request.getParameter("entryid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            Entry existing = entryDao.findEntryById(entryId);
            if (existing == null || existing.getTopicId() != topicId) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            String link = request.getParameter("link");
            if (title == null || title.trim().isEmpty()
                    || text == null || text.trim().isEmpty()) {
                request.setAttribute("error", "Title and description are required.");
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("entry", existing);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Part imagePart = request.getPart("image");
            String imagePath = ImageUtil.uploadImage(imagePart);

            if (imagePath != null) {
                ImageUtil.deleteImage(existing.getImage());
            } else {
                imagePath = existing.getImage();
            }

            Entry entry = new Entry(title.trim(), text.trim(), topicId);
            entry.setId(entryId);
            entry.setLink(link);
            entry.setImage(imagePath);
            entryDao.updateEntry(entry);
            SessionUtil.setAttribute(request, "success", "Entry updated successfully!");
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }

        else if ("delete".equals(action)) {
            int entryId;
            try {
                entryId = Integer.parseInt(request.getParameter("entryid"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            Entry existing = entryDao.findEntryById(entryId);
            if (existing == null || existing.getTopicId() != topicId) {
                response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
                return;
            }
            ImageUtil.deleteImage(existing.getImage());
            entryDao.deleteEntry(entryId);
            SessionUtil.setAttribute(request, "success", "Entry deleted successfully!");
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }
    }
}
