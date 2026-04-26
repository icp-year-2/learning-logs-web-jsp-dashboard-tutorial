# Learning Logs — Week 10 Dashboard Tutorial

## User Analytics Dashboard

> **This is the final week.** You've built a complete web application from scratch — entities, DAOs, servlets, JSPs, authentication, templates, and file upload. Now you'll add a dashboard that aggregates your data into KPI cards and a recent topics list, using SQL aggregate functions and a read-only servlet pattern.

---

## What's Already Done

Everything from Weeks 4-9 is provided complete:

| Category | Files | Status |
|----------|-------|--------|
| CRUD operations | TopicServlet, EntryServlet, DAOs | Provided |
| Authentication | LoginServlet, RegisterServlet, AuthenticationFilter | Provided |
| File upload | ImageUtil, ImageServlet, @MultipartConfig | Provided |
| JSP templates | head.jsp, header.jsp, nav.jsp, footer.html | Provided |
| Views | topic-list, topic-add-edit, entry-list, entry-add-edit | Provided |
| Utilities | SessionUtil, CookieUtil, PasswordUtil, ValidationUtil | Provided |
| CSS | main.css + page-specific stylesheets | Provided |
| Dashboard CSS | dashboard.css (KPI grid, cards, recent topics) | Provided |

---

## What You'll Build

9 TODOs across 6 files — building a user analytics dashboard:

| # | File | What You'll Build |
|---|------|-------------------|
| 1 | `TopicDao.java` | Add 3 dashboard method signatures (count, countThisWeek, fetchRecent) |
| 2 | `TopicDaoImpl.java` | Implement countTopicsByUserId — COUNT(*) query |
| 3 | `TopicDaoImpl.java` | Implement countTopicsThisWeekByUserId — WEEK() + YEAR() query |
| 4 | `TopicDaoImpl.java` | Implement fetchRecentTopicsByUserId — ORDER BY + LIMIT query |
| 5 | `EntryDao.java` + `EntryDaoImpl.java` | Add + implement countEntriesByUserId — JOIN aggregate |
| 6 | `EntryDao.java` + `EntryDaoImpl.java` | Add + implement countEntriesTodayByUserId — JOIN + DATE() |
| 7 | `DashboardServlet.java` | Get user, call 5 DAO methods, set attributes, forward to JSP |
| 8 | `dashboard.jsp` | Build 4 KPI cards + recent topics list with EL and JSTL |
| 9 | `header.jsp` | Wrap username in a link to the dashboard |

---

## Key Learning Points

- **Aggregate SQL functions** — COUNT(*) for totals, with WHERE for filtering
- **JOIN for cross-table counting** — entries don't have user_id, so JOIN through topics
- **MySQL date functions** — WEEK(), YEAR(), CURDATE(), DATE() for time-based queries
- **Dashboard servlet pattern** — read-only servlet that aggregates data from multiple DAOs
- **fmt:formatDate** — JSTL formatting tag for displaying dates in JSPs

---

## Architecture

```
Browser GET /dashboard
  |
  v
DashboardServlet.doGet()
  |
  +-- topicDao.countTopicsByUserId()        --> int totalTopics
  +-- topicDao.countTopicsThisWeekByUserId() --> int topicsThisWeek
  +-- topicDao.fetchRecentTopicsByUserId()   --> ArrayList<Topic> recentTopics
  +-- entryDao.countEntriesByUserId()        --> int totalEntries
  +-- entryDao.countEntriesTodayByUserId()   --> int entriesToday
  |
  v
request.setAttribute(...)  -->  forward to dashboard.jsp
  |
  v
dashboard.jsp renders:
  +-- 4 KPI cards (EL: ${totalTopics}, ${totalEntries}, etc.)
  +-- Recent topics list (c:forEach + fmt:formatDate)
```

---

## Project Structure

```
learning-logs-web-jsp-dashboard-tutorial/
├── .gitignore
├── README.md
├── pom.xml
├── references/
│   ├── 01-aggregate-queries.md
│   └── 02-dashboard-servlet-pattern.md
├── sql/
│   ├── learninglog.sql
│   └── seed.sql
└── src/main/
    ├── java/com/learninglogs/
    │   ├── controller/
    │   │   ├── DashboardServlet.java    <-- TODO 7: aggregate + forward
    │   │   ├── EntryServlet.java
    │   │   ├── ImageServlet.java
    │   │   ├── LoginServlet.java
    │   │   ├── LogoutServlet.java
    │   │   ├── RegisterServlet.java
    │   │   ├── TopicServlet.java
    │   │   └── filter/
    │   │       └── AuthenticationFilter.java
    │   ├── dao/
    │   │   ├── EntryDao.java            <-- TODOs 5-6: new signatures
    │   │   ├── EntryDaoImpl.java        <-- TODOs 5-6: implementations
    │   │   ├── TopicDao.java            <-- TODO 1: new signatures
    │   │   ├── TopicDaoImpl.java        <-- TODOs 2-4: implementations
    │   │   ├── UserDao.java
    │   │   └── UserDaoImpl.java
    │   ├── entity/
    │   │   ├── Entry.java
    │   │   ├── Topic.java
    │   │   └── User.java
    │   └── utils/
    │       ├── CookieUtil.java
    │       ├── DatabaseConnection.java
    │       ├── ImageUtil.java
    │       ├── PasswordUtil.java
    │       ├── SessionUtil.java
    │       └── ValidationUtil.java
    └── webapp/
        ├── error404.jsp
        ├── static/
        │   ├── css/
        │   │   ├── auth.css
        │   │   ├── dashboard.css
        │   │   ├── entry-add.css
        │   │   ├── entry-list.css
        │   │   ├── error.css
        │   │   ├── main.css
        │   │   ├── topic-add.css
        │   │   └── topic-list.css
        │   ├── images/book.png
        │   └── js/.gitkeep
        └── WEB-INF/
            ├── templates/
            │   ├── footer.html
            │   ├── head.jsp
            │   ├── header.jsp           <-- TODO 9: username link
            │   └── nav.jsp
            ├── views/
            │   ├── dashboard.jsp        <-- TODO 8: KPI cards + list
            │   ├── entry-add-edit.jsp
            │   ├── entry-list.jsp
            │   ├── login.jsp
            │   ├── register.jsp
            │   ├── topic-add-edit.jsp
            │   └── topic-list.jsp
            └── web.xml
```

---

## Getting Started

### 1. Database Setup

Run these files in phpMyAdmin (in order):

```
sql/learninglog.sql    -- creates tables
sql/seed.sql           -- adds sample data with spread dates
```

The seed data uses `DATE_SUB(NOW(), INTERVAL n DAY)` so that dashboard analytics show meaningful values on first run.

> **Note on "Entries Today":** The `countEntriesTodayByUserId` query compares `DATE(e.created_at)` against `CURDATE()`. Both use the **MySQL server's timezone**. If your MySQL timezone differs from your local time (e.g., MySQL is set to UTC while you're in NPT/UTC+5:45), entries seeded with `DATE_SUB(NOW(), INTERVAL 1 DAY)` might still fall on "today" from MySQL's perspective. This is expected — the query logic is correct, but the results depend on when you import the seed relative to your server's clock.

### 2. Build and Run

```bash
mvn clean compile cargo:run
```

Then open: `http://localhost:9090/learning-logs/dashboard`

### 3. Test Users

| Username | Password | Topics | Entries |
|----------|----------|--------|---------|
| testuser | Test@123 | 5 | 10 |
| demouser | Test@123 | 3 | 6 |

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| All KPI cards show 0 | Run seed.sql to add sample data |
| "Topics This Week" shows 0 | Seed data uses DATE_SUB — add a new topic manually to see a non-zero value for the current week |
| "Entries Today" shows 0 | No seed entries have today's date — add a new entry manually, or check MySQL timezone (see note above) |
| "Entries Today" shows unexpected value | MySQL timezone may differ from local time — `DATE_SUB(NOW(), INTERVAL 1 DAY)` can land on "today" if timezones are offset |
| Blank dashboard page | Check DashboardServlet.doGet() forwards to the correct JSP path |
| 404 on /dashboard | Verify @WebServlet("/dashboard") annotation on DashboardServlet |
| fmt:formatDate error | Check that the fmt taglib import is at the top of dashboard.jsp |
| Recent topics list empty | Verify fetchRecentTopicsByUserId returns topics (check SQL and limit parameter) |
