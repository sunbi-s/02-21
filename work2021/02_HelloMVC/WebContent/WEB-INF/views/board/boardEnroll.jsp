<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>    
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<script>
$(function(){
	$("[name=boardEnrollFrm]").submit(boardValidate);
});

/**
* boardEnrollFrm 유효성 검사
*/
function boardValidate(){
	//제목을 작성하지 않은 경우 폼제출할 수 없음.
	var $boardTitle = $("[name=boardTitle]");
	//아무글자 1개이상
	if(/^.{1,}$/.test($boardTitle.val()) == false){
		alert("제목을 입력하세요.");
		return false;
	}
					   
	//내용을 작성하지 않은 경우 폼제출할 수 없음.
	var $boardContent = $("[name=boardContent]");
	//아무글자 또는 개행문자가 1개이상
	if(/^(.|\n){1,}$/.test($boardContent.val()) == false){
		alert("내용을 입력하세요.");
		return false;
	}

	return true;
}
</script>
<section id="board-container">
<h2>게시판 작성</h2>
<form
	name="boardEnrollFrm"
	action="<%=request.getContextPath() %>/board/boardEnroll" 
	method="post"
	enctype="multipart/form-data">
	<!-- 파일업로드가 포함된 폼 전송시 필수 속성  enctype="multipart/form-data" -->
	<table id="tbl-board-view">
	<tr>
		<th>제 목</th>
		<td><input type="text" name="boardTitle" required></td>
	</tr>
	<tr>
		<th>작성자</th>
		<td>
			<input 
				type="text" name="boardWriter" 
				value="<%= memberLoggedIn.getMemberId() %>" readonly/>
		</td>
	</tr>
	<tr>
		<th>첨부파일</th>
		<td>			
			<input type="file" name="upFile">
		</td>
	</tr>
	<tr>
		<th>내 용</th>
		<td><textarea rows="5" cols="40" name="boardContent"></textarea></td>
	</tr>
	<tr>
		<th colspan="2">
			<input type="submit" value="등록하기">
		</th>
	</tr>
</table>
</form>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
