<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
  <ul>
    <li>
      <c:if test="${not empty param.back_title}">
        <a href="${param.back_href}">&lt; Back (${param.back_title})</a>
      </c:if>
    </li>
    <li><p>${param.page_title}</p></li>
    <li>
      <c:if test="${not empty param.add_title}">
        <a href="${param.add_href}">${param.add_title}</a>
      </c:if>
    </li>
  </ul>
</nav>
