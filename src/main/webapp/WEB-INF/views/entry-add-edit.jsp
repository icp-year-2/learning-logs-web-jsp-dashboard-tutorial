<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log | Entry" />
    <jsp:param name="cssFile" value="entry-add" />
  </jsp:include>

  <body>
    <div class="page">

      <jsp:include page="/WEB-INF/templates/header.jsp" />

      <jsp:include page="/WEB-INF/templates/nav.jsp">
        <jsp:param name="back_title" value="Entries" />
        <jsp:param name="back_href" value="${pageContext.request.contextPath}/entry?topicid=${param.topicid}" />
        <jsp:param name="page_title" value="${empty entry ? 'Add Entry' : 'Edit Entry'}" />
        <jsp:param name="add_title" value="" />
        <jsp:param name="add_href" value="" />
      </jsp:include>

      <main class="content">
        <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

        <div class="form-card">
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

          <form action="${pageContext.request.contextPath}/entry" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${empty entry ? 'add' : 'edit'}" />
            <input type="hidden" name="topicid" value="${param.topicid}" />
            <c:if test="${not empty entry}">
              <input type="hidden" name="entryid" value="${entry.id}" />
            </c:if>

            <div class="form-row">
              <label for="title">Title:</label>
              <input type="text" id="title" name="title" placeholder="Title"
                     value="<c:out value='${entry.title}' default='' />" />
            </div>

            <div class="form-row">
              <label for="text">Description:</label>
              <textarea id="text" name="text" rows="4"
                placeholder="Description"><c:out value="${entry.text}" default="" /></textarea>
            </div>

            <div class="form-row">
              <label for="link">Link:</label>
              <input type="text" id="link" name="link" placeholder="Link URL"
                     value="<c:out value='${entry.link}' default='' />" />
            </div>

            <c:if test="${not empty entry and not empty entry.image and entry.image ne 'static/images/book.png'}">
              <div class="form-row">
                <label>Current:</label>
                <div class="image-preview">
                  <img src="${pageContext.request.contextPath}/uploads/${entry.image}"
                       alt="Current image" />
                  <p>Upload a new image below to replace, or leave
                     empty to keep the current image.</p>
                </div>
              </div>
            </c:if>
            <div class="form-row">
              <label for="image">Image:</label>
              <input type="file" id="image" name="image" accept=".jpg,.jpeg,.png" />
            </div>

            <div class="form-actions">
              <button type="submit">Save</button>
            </div>
          </form>
        </div>
      </main>

      <%@ include file="/WEB-INF/templates/footer.html" %>

    </div>
  </body>
</html>
