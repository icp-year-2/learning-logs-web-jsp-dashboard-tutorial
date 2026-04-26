<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log | Topics" />
    <jsp:param name="cssFile" value="topic-list" />
  </jsp:include>

  <body>
    <div class="page">

      <jsp:include page="/WEB-INF/templates/header.jsp" />

      <jsp:include page="/WEB-INF/templates/nav.jsp">
        <jsp:param name="back_title" value="" />
        <jsp:param name="back_href" value="" />
        <jsp:param name="page_title" value="Topic Lists (${topics.size()})" />
        <jsp:param name="add_title" value="+New Topic" />
        <jsp:param name="add_href" value="${pageContext.request.contextPath}/topic?action=new" />
      </jsp:include>

      <main class="content">
        <c:if test="${not empty success}">
          <p style="color: green; text-align: center; padding: 5px;">
            <c:out value="${success}" />
          </p>
        </c:if>
        <div class="search">
          <form action="${pageContext.request.contextPath}/topic" method="get">
            <input type="hidden" name="action" value="search" />
            <label for="search">Topic: </label>
            <input type="text" name="search" placeholder="Search..." value="<c:out value='${searchKeyword}' default='' />" />
            <button type="submit">SEARCH</button>
          </form>
        </div>
        <div class="topicContainer">
          <ul>
            <c:forEach var="topic" items="${topics}" varStatus="status">
              <li class="topicItem">
                <a class="topic" href="${pageContext.request.contextPath}/entry?topicid=${topic.id}">${status.count}. <c:out value="${topic.name}" /></a>
                <span class="date">
                  <fmt:formatDate value="${topic.createdAt}" pattern="MMM d, yyyy" />
                </span>
                <a class="edit" href="${pageContext.request.contextPath}/topic?action=edit&topicid=${topic.id}">Edit</a>
                <form action="${pageContext.request.contextPath}/topic" method="post">
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="topicid" value="${topic.id}" />
                  <button class="submit" type="submit"
                    onclick="return confirm('Are you sure you want to delete?');">
                    Delete
                  </button>
                </form>
              </li>
            </c:forEach>
          </ul>
        </div>
      </main>

      <%@ include file="/WEB-INF/templates/footer.html" %>

    </div>
  </body>
</html>
