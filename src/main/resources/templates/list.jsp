<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <title>List Products </title>
    </head>

    <body>

        <div class = "row">

            <c:forEach products="${listResult}" var="product">
                <div class="col-4">
                    <img src="${pageContext.request.contextPath}/image/${product.image}" width="100%" height="50%" />
                    <div class="row">
                        <h4>${product.name}</h4>
                    </div>
                </div>
            </c:forEach>

        </div>

    </body>
</html>