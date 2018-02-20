<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>팀 게시글쓰기</title>
<link href="css/background.css?ver=2" rel="stylesheet" type="text/css" >
</head>
<body>
<header class="title">
        <h1>Capstone Design Management System</h1>
</header>
<form action="write.do" method="post">
<p>
	제목:<br/><input type="text" name="title" value="${param.title}">
	<c:if test="${errors.title}">제목을 입력하세요.</c:if>
</p>
<p>
	파일:<br/>
	<%--!!이부분을 파일업로드로 수정해야함. 자바스크립스 이용!! --%>
	<textarea name="content" rows="5" cols="30">${param.title}</textarea>
</p>
<input type="submit" value="새 글 등록">
</form>
</body>
</html>