<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="header">
  <div class="logo">
    <a href="${pageContext.request.contextPath}/dashboard" style="text-decoration: none">
      <img src="${pageContext.request.contextPath}/static/images/book.png" alt="LL" />
    </a>
    <h3>Learning Log</h3>
  </div>
  <div class="usersession">
    <%-- ============================================================
         TODO 9: Link Username to Dashboard
         ============================================================
         Wrap the username h3 in an anchor tag that links to the
         dashboard page. This gives users a quick way to return to
         the dashboard from any page.

         Steps:
           1. Add an a tag around the existing h3
           2. Set href to: ${pageContext.request.contextPath}/dashboard
           3. Add style="text-decoration: none" to keep it looking
              like plain text (no underline)
           4. Keep the c:out inside the h3 for XSS safety

         The complete code:
           (a href="${pageContext.request.contextPath}/dashboard"
              style="text-decoration: none")
             (h3)(c:out value="${sessionScope.user.username}" /)(/h3)
           (/a)
         ============================================================ --%>
    <h3><c:out value="${sessionScope.user.username}" /></h3>
    <a href="${pageContext.request.contextPath}/logout" class="logout"
       onclick="return confirm('Are you sure you want to logout?');">Logout</a>
  </div>
</header>
