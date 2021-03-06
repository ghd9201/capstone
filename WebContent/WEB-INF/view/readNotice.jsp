<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<title>게시글 읽기</title>
<link href="css/background.css?ver=2" rel="stylesheet" type="text/css" >
</head>
<body>
<header class="title">
        <h1>Capstone Design Management System</h1>
</header>

<table border="1" width="100%">
<tr>
	<td>번호</td>
	<td>${noticeData.notice.postNo}</td>
</tr>
<tr>
	<td>작성자</td>
	<td>${noticeData.notice.writer.name}</td>
</tr>
<tr>
	<td>제목</td>
	<td><c:out value='${noticeData.notice.title}' /></td>
</tr>
<tr>
	<td>내용</td>
	<td><u:pre value='${noticeData.content}'/></td>
</tr>
<tr>
	<td>파일</td>
	<td><c:out value='${noticeData.file}'/></td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="pageNo" value="${empty param.pageNo ? '1' : param.pageNo}" />
		<a href="noticelist.do?pageNo=${pageNo}">[목록]</a>
		<c:if test="${authUser.id == noticeData.notice.writer.id}">
		<a href="noticemodify.do?no=${noticeData.notice.postNo}">[게시글수정]</a>
		<a href="noticedelete.do?no=${noticeData.notice.postNo}">[게시글삭제]</a>
		</c:if>
	</td>
</tr>
</table>

</body>
</html>