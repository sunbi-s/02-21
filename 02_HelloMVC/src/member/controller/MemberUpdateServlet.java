package member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.service.MemberService;
import member.model.vo.Member;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private MemberService memberService = new MemberService();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1인코딩
		request.setCharacterEncoding("UTF-8");
		//2전송값 꺼내기
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		String memberName = request.getParameter("memberName");
		String birthDay = request.getParameter("birthDay");
		String gender = request.getParameter("gender");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String[] hobbies = request.getParameterValues("hobby");
		
		//데이터 형변환
		String hobby = "";
		if(hobbies != null) hobby = String.join(",", hobbies);
		
		//날짜타입으로 변경 : 1990-09-09
		Date birthDay_ = null;
		if(birthDay != null && !"".equals(birthDay))
			birthDay_ = Date.valueOf(birthDay);
		
		//memberRole은 U, enrollDate는 null로 처리
		
		//values=""은 null이 아님 nullpointException이 안남 그냥 그대로 db에 넣기로함
		Member member = 
				new Member(memberId, password, memberName, "U", 
						   gender, birthDay_, email, phone, 
						   address, hobby, null);
		
		int result = memberService.updateMember(member);
		
		//4.view단 처리
				String msg = "";
				//DML, login등 요청후 반드시 url을 변경해서 새로고침 사고를 방지한다.

				String view = "/member/memberView";
				
				if(result>0)
					msg = "정보가 정상 변경 되었습니다.";
				else 
					msg = "정보변경에 실패했습니다.";	
				
				request.setAttribute("msg", msg);

				
				RequestDispatcher reqDispatcher = request.getRequestDispatcher(view);
				reqDispatcher.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
