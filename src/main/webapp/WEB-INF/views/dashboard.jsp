<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log | Dashboard" />
    <jsp:param name="cssFile" value="dashboard" />
  </jsp:include>

  <body>
    <div class="page">

      <jsp:include page="/WEB-INF/templates/header.jsp" />

      <jsp:include page="/WEB-INF/templates/nav.jsp">
        <jsp:param name="back_title" value="" />
        <jsp:param name="back_href" value="" />
        <jsp:param name="page_title" value="Dashboard" />
        <jsp:param name="add_title" value="" />
        <jsp:param name="add_href" value="" />
      </jsp:include>

      <main class="content">

        <%-- ============================================================
             TODO 8: Build the Dashboard View
             ============================================================
             Create the KPI cards and recent topics list using EL
             expressions to display the data from DashboardServlet.

             Step 1 — KPI Cards Grid:
               Create a div with class "kpi-grid" containing 4 cards.
               Each card is a div with class "kpi-card" containing:
                 - An h3 with the label (e.g., "Total Topics")
                 - A p with class "metric" showing the value via EL

               The 4 cards:
                 a) "Total Topics"     -> ${totalTopics}
                 b) "Total Entries"    -> ${totalEntries}
                 c) "Entries Today"    -> ${entriesToday}
                 d) "Topics This Week" -> ${topicsThisWeek}

             Step 2 — Recent Topics List:
               Create a div with class "recent-topics" containing:
                 - An h3 title: "Recent Topics"
                 - A ul with c:forEach iterating over ${recentTopics}
                 - Each li contains:
                   - An a tag linking to the topic's entries page:
                     href="${pageContext.request.contextPath}/entry?topicid=${topic.id}"
                     with c:out for the topic name
                   - A span showing the creation date using fmt:formatDate:
                     (pattern "MMM d, yyyy")

             WHY c:out: Prevents XSS — user-entered topic names are
             safely escaped.
             WHY fmt:formatDate: Formats java.sql.Timestamp into a
             human-readable date string.

             The complete code:

               <div class="kpi-grid">
                 <div class="kpi-card">
                   <h3>Total Topics</h3>
                   <p class="metric">${totalTopics}</p>
                 </div>
                 <div class="kpi-card">
                   <h3>Total Entries</h3>
                   <p class="metric">${totalEntries}</p>
                 </div>
                 <div class="kpi-card">
                   <h3>Entries Today</h3>
                   <p class="metric">${entriesToday}</p>
                 </div>
                 <div class="kpi-card">
                   <h3>Topics This Week</h3>
                   <p class="metric">${topicsThisWeek}</p>
                 </div>
               </div>

               <div class="recent-topics">
                 <h3>Recent Topics</h3>
                 <ul>
                   <c:forEach var="topic" items="${recentTopics}">
                     <li>
                       <a href="${pageContext.request.contextPath}/entry?topicid=${topic.id}"><c:out value="${topic.name}" /></a>
                       <span><fmt:formatDate value="${topic.createdAt}" pattern="MMM d, yyyy" /></span>
                     </li>
                   </c:forEach>
                 </ul>
               </div>
             ============================================================ --%>

        <div class="kpi-grid">
          <div class="kpi-card">
            <h3>Total Topics</h3>
            <p class="metric">${totalTopics}</p>
          </div>
          <div class="kpi-card">
            <h3>Total Entries</h3>
            <p class="metric">${totalEntries}</p>
          </div>
          <div class="kpi-card">
            <h3>Entries Today</h3>
            <p class="metric">${entriesToday}</p>
          </div>
          <div class="kpi-card">
            <h3>Topics This Week</h3>
            <p class="metric">${topicsThisWeek}</p>
          </div>
        </div>

        <div class="recent-topics">
          <h3>Recent Topics</h3>
          <ul>
            <c:forEach var="topic" items="${recentTopics}">
              <li>
                <a href="${pageContext.request.contextPath}/entry?topicid=${topic.id}"><c:out value="${topic.name}" /></a>
                <span><fmt:formatDate value="${topic.createdAt}" pattern="MMM d, yyyy" /></span>
              </li>
            </c:forEach>
          </ul>
        </div>

      </main>

      <%@ include file="/WEB-INF/templates/footer.html" %>

    </div>
  </body>
</html>
