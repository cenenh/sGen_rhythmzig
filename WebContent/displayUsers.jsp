<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.sgen.DAO.*"%>
<%@ page import="com.sgen.DTO.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style type="text/css">

@import url(http://fonts.googleapis.com/earlyaccess/nanumgothic.css);
@import url(http://fonts.googleapis.com/earlyaccess/nanumbrushscript.css);
@import url(http://fonts.googleapis.com/earlyaccess/nanumgothiccoding.css);
@import url(http://fonts.googleapis.com/earlyaccess/nanummyeongjo.css);
@import url(http://fonts.googleapis.com/earlyaccess/nanumpenscript.css);

table {
	width: 70%;
	border: 1px solid #727272;
	border-collapse: collapse;

}
font-case {
	font-family: 'Nanum Gothic';
}

th {
	background-color: #d0d0d0;
}

tr {
	background-color: ;
}

td {
	font-family : 'Nanum Gothic';
	border: 1px solid #727272;
	padding: 5px 10px;
}

body{
	background-image: url("/imgs/bg.jpg");
	background-repeat: no-repeat;
	background-size : 500px 250px;
	background-color: #D7BD97;
}


</style>
 <!-- /AgetUsers -->
<title>TEST</title>
</head>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<script>
	function getLogData(){
		
		$.ajax({
			type : "GET",
			url : "/agetLogs?deviceCode=123412341234",
			dataType : "json",
			cache : false,
			async : false,
			ifModified : true,
			success : function(json) {
				var loglist = json;
				var loglistSize = loglist.length;
				$("#logData").empty();
				var log_first_html = "<tr align='center'>"
					+ "<td><b>사용자 번호</b></td>"
					+ "<td><b>사용자 이름</b></td>"
					+ "<td><b>기기번호</b></td>"
					+ "<td><b>도어락 Mode</b></td>"
					+ "<td><b>출입 시간</b></td>";
				$("#logData").append(log_first_html);
				for (var idx = 0; idx < loglistSize; idx++) 
				{
					var log_html = "";
					log_html = "<tr align='center'>" + "<td>"
							+ loglist[idx].userCode + "</td>" + "<td>"
							+ loglist[idx].userName + "</td>" + "<td>"
							+ loglist[idx].deviceCode + "</td>" + "<td>"
							+ loglist[idx].deviceStatus + "</td>" + "<td>"
							+ loglist[idx].enterTime + "</td>" + "</tr>";
					$("#logData").append(log_html);
				}
			}
		})
	}

	function getUserData() {
		$.ajax({
			type : "GET",
			url : "/agetUsers?deviceCode=123412341234",
			dataType : "json",
			cache : false,
			async : false,
			ifModified : true,
			success : function(json) {
				var list = json;
				var listSize = list.length;
				$("#userData").empty();
				var first_html = "<tr align='center'>"
					+ "<td><b>사용자 번호</b></td>"
					+ "<td><b>사용자 이름</b></td>"
					+ "<td><b>기기번호</b></td>"
					+ "<td><b>관리자</b></td>";
				$("#userData").append(first_html);
				for (var idx = 0; idx < listSize; idx++) {
					var html = "";
					html = "<tr align='center'>" + "<td>"
							+ list[idx].userCode + "</td>" + "<td>"
							+ list[idx].userName + "</td>" + "<td>"
							+ list[idx].deviceCode + "</td>" + "<td>"
							+ list[idx].isAdmin + "</td>" + "</tr>";
					$("#userData").append(html);
				}
			}
		})
	}

	$(document).ready(
			function() {
				$.ajax({
					type : "GET",
					url : "/agetUsers?deviceCode=123412341234",
					dataType : "json",
					cache : false,
					async : false,
					ifModified : true,
					success : function(json) {
						getUserData();
						getLogData();
						setInterval(getUserData,10000);
						setInterval(getLogData,10000);
					}
				})
			
				
			});
</script>

<body class="font-case">
	
	<br><br><br><br>
	
<center>
	<TABLE id="userData">
		
	</TABLE>
	<br>
	<TABLE id="logData">
		
	</TABLE>		
</center>

</body>
</html>