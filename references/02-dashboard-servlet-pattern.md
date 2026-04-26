# Dashboard Servlet Pattern

## Overview

A dashboard servlet is a **read-only** servlet that aggregates data from multiple DAOs and forwards it to a view. Unlike CRUD servlets, it only implements `doGet()` — there's no `doPost()` because dashboards display data, they don't modify it.

## Pattern

```
Browser GET /dashboard
  -> DashboardServlet.doGet()
  -> Get user from session
  -> Call multiple DAO methods (counts, lists)
  -> Set each result as a request attribute
  -> Forward to dashboard.jsp
  -> JSP renders KPI cards + lists using EL
```

## Key Differences from CRUD Servlets

| CRUD Servlet | Dashboard Servlet |
|---|---|
| doGet + doPost | doGet only |
| One DAO | Multiple DAOs |
| CRUD actions (add/edit/delete) | Read-only aggregation |
| Single entity type | Cross-entity statistics |
| Action parameter routing | No action parameter |

## Implementation

```java
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final TopicDao topicDao = new TopicDaoImpl();
    private final EntryDao entryDao = new EntryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get user from session
        User user = (User) SessionUtil.getAttribute(request, "user");

        // 2. Call DAO methods
        int totalTopics = topicDao.countTopicsByUserId(user.getId());
        // ... more DAO calls ...

        // 3. Set request attributes
        request.setAttribute("totalTopics", totalTopics);
        // ... more attributes ...

        // 4. Forward to view
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
               .forward(request, response);
    }
}
```

## Request Attributes

Each DAO result becomes a request attribute that the JSP accesses via EL:

| Java (Servlet) | EL (JSP) |
|---|---|
| `request.setAttribute("totalTopics", 5)` | `${totalTopics}` |
| `request.setAttribute("recentTopics", list)` | `${recentTopics}` with `c:forEach` |

## Why No doPost?

Dashboards are informational. They show aggregated data but don't accept form submissions. If a dashboard had interactive elements (like filters), those would use GET parameters — still `doGet()`.
