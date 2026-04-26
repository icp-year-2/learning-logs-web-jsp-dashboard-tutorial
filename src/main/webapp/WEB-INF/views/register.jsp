<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Learning Log — Register" />
    <jsp:param name="cssFile" value="auth" />
  </jsp:include>

  <body>
    <div class="auth-page">

      <div class="auth-header">
        <img src="${pageContext.request.contextPath}/static/images/book.png" alt="LL" />
        <h1>Learning Logs</h1>
      </div>

      <div class="auth-form">
        <form action="${pageContext.request.contextPath}/register" method="post">
          <h2>Register</h2>

          <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}" /></p>
          </c:if>

          <input type="text" name="username" placeholder="Username"
                 value="<c:out value='${param.username}' default='' />" required />
          <input type="email" name="email" placeholder="Email"
                 value="<c:out value='${param.email}' default='' />" required />
          <input type="password" name="password" placeholder="Password" required />
          <input type="password" name="cpassword" placeholder="Confirm Password" required />

          <button type="submit">Register</button>

          <p class="link">Already have an account?
            <a href="${pageContext.request.contextPath}/login">Log in</a>
          </p>
        </form>
      </div>

    </div>
  </body>
</html>
