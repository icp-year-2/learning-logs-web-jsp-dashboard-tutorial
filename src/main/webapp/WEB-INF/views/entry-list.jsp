<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log | Entries" />
    <jsp:param name="cssFile" value="entry-list" />
  </jsp:include>

  <body>
    <div class="page">

      <jsp:include page="/WEB-INF/templates/header.jsp" />

      <jsp:include page="/WEB-INF/templates/nav.jsp">
        <jsp:param name="back_title" value="Topics" />
        <jsp:param name="back_href" value="${pageContext.request.contextPath}/topic" />
        <jsp:param name="page_title" value="Entries List (${entries.size()})" />
        <jsp:param name="add_title" value="+New Entry" />
        <jsp:param name="add_href" value="${pageContext.request.contextPath}/entry?action=new&topicid=${topic.id}" />
      </jsp:include>

      <main class="content">
        <c:if test="${not empty success}">
          <p style="color: green; text-align: center; padding: 5px;">
            <c:out value="${success}" />
          </p>
        </c:if>

        <div class="search-bar">
          <form action="${pageContext.request.contextPath}/entry" method="get">
            <input type="hidden" name="action" value="search" />
            <input type="hidden" name="topicid" value="${topic.id}" />
            <label for="search">Entry: </label>
            <input type="text" name="search" placeholder="Search..." value="<c:out value='${searchKeyword}' default='' />" />
            <button type="submit">SEARCH</button>
          </form>
        </div>

        <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

        <div class="entry-grid">
          <c:forEach var="entry" items="${entries}">
            <div class="entry-card">
              <div class="entry-header">
                <div>
                  <h3><c:out value="${entry.title}" /></h3>
                  <p class="date">Date:
                    <fmt:formatDate value="${entry.createdAt}"
                      pattern="MMM d, yyyy" />
                  </p>
                </div>

                <c:choose>
                    <c:when test="${not empty entry.image and entry.image ne 'static/images/book.png'}">
                        <img class="photo"
                             src="${pageContext.request.contextPath}/uploads/${entry.image}"
                             alt="Entry photo" />
                    </c:when>
                    <c:otherwise>
                        <img class="photo"
                             src="${pageContext.request.contextPath}/static/images/book.png"
                             alt="Default photo" />
                    </c:otherwise>
                </c:choose>
              </div>

              <p class="entry-text"><c:out value="${entry.text}" /></p>

              <c:if test="${not empty entry.link}">
                <p class="link">
                  Link:
                  <c:choose>
                    <c:when test="${fn:startsWith(entry.link, 'http://') || fn:startsWith(entry.link, 'https://')}">
                      <a href="${fn:escapeXml(entry.link)}" target="_blank"><c:out value="${entry.link}" /></a>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${entry.link}" />
                    </c:otherwise>
                  </c:choose>
                </p>
              </c:if>

              <div class="entry-actions">
                <a href="${pageContext.request.contextPath}/entry?action=edit&entryid=${entry.id}&topicid=${topic.id}">
                  <button>Edit</button>
                </a>
                <form action="${pageContext.request.contextPath}/entry" method="post">
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="entryid" value="${entry.id}" />
                  <input type="hidden" name="topicid" value="${topic.id}" />
                  <button class="danger" type="submit"
                    onclick="return confirm('Are you sure you want to delete?');">
                    Delete
                  </button>
                </form>
              </div>
            </div>
          </c:forEach>
        </div>
      </main>

      <%@ include file="/WEB-INF/templates/footer.html" %>

    </div>
  </body>
</html>
