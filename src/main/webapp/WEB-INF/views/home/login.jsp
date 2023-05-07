<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <div class="container">
        <div class="d-flex justify-content-center align-items-center" style="min-height: 700px;">
            <div class="card border-0 rounded-2 shadow-lg my-4" style="width: 50%; height: 60%; min-width: 300px;">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h2>로그인</h2>
                </div>
                <div class="card-body d-flex justify-content-center align-items-center">
                    <div style="width: 80%; min-width: 220px;">
                        <form name="loginForm" action="/login" method="post">
                            <div class="form-group my-2">
                                <label for="userId" class="form-label">아이디</label>
                                <input type="text" id="userId" name="userId" class="form-control" autofocus />
                            </div>
                            <div class="form-group my-2">
                                <label for="userPw" class="form-label">비밀번호</label>
                                <input type="password" id="userPw" name="userPw" class="form-control" />
                            </div>
                            <div class="my-2">
                                <span class="text-danger"><c:if test="${param.error != null}">아이디 또는 비밀번호가 틀렸습니다.</c:if></span>
                            </div>
                            <div class="text-end mt-4">
                                <button type="submit" class="btn btn-primary">로그인</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
</script>
</html>