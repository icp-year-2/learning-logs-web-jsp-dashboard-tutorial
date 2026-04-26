<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log | Topic" />
    <jsp:param name="cssFile" value="topic-add" />
  </jsp:include>

  <body>
    <div class="page">

      <jsp:include page="/WEB-INF/templates/header.jsp" />

      <jsp:include page="/WEB-INF/templates/nav.jsp">
        <jsp:param name="back_title" value="Topics" />
        <jsp:param name="back_href" value="${pageContext.request.contextPath}/topic" />
        <jsp:param name="page_title" value="${empty topic ? 'Add Topic' : 'Edit Topic'}" />
        <jsp:param name="add_title" value="" />
        <jsp:param name="add_href" value="" />
      </jsp:include>

      <main class="content">
        <div class="topicContainer">
          <c:choose>
            <c:when test="${not empty error}">
              <p style="color: red; text-align: center; padding: 5px;">
                <c:out value="${error}" />
              </p>
            </c:when>
            <c:when test="${not empty success}">
              <p style="color: green; text-align: center; padding: 5px;">
                <c:out value="${success}" />
              </p>
            </c:when>
          </c:choose>
          <form action="${pageContext.request.contextPath}/topic" method="post">
            <input type="hidden" name="action" value="${empty topic ? 'add' : 'edit'}" />
            <c:if test="${not empty topic}">
              <input type="hidden" name="topicid" value="${topic.id}" />
            </c:if>
            <div class="topicItem">
              <label for="topic">Topic: </label>
              <input type="text" placeholder="Topic Name" name="topic"
                     id="topic" value="<c:out value='${topic.name}' default='' />" />
            </div>
            <div class="submit">
              <button type="submit">Save</button>
            </div>
          </form>
        </div>
      </main>

      <%@ include file="/WEB-INF/templates/footer.html" %>

    </div>
  </body>
</html>
