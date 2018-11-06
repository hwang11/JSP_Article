<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글쓰기</title>
</head>
<body>

<form action="<c:url value='/wirte.jsp'/>"method="post">
제목: <input type = "text" name = "title" size="20"/> <br/>
작성자: <input type = "text" name = "wirterName" size="20"/> <br/>
글암호: <input type = "text" name = "password" size="20"/> <br/>
글내용: <br />
<textarea name = "content" cols = "40" rows ="5"></textarea> 
<br />
<input type="submit" value = "전송"/>
</form>

</body>
</html>