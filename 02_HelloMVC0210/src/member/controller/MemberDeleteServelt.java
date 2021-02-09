package member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.service.MemberService;

/**
 * Servlet implementation class MemberDeleteServelt
 */
@WebServlet("/member/deleted")
public class MemberDeleteServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MemberService memberService = new MemberService();
 
    public MemberDeleteServelt() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//인코딩
		request.setCharacterEncoding("UTF-8");
		//색인자
		String memberId = request.getParameter("memberId");
		
		
		System.out.println(memberId);
		
		String msg = "";
		//DML, login등 요청후 반드시 url을 변경해서 새로고침 사고를 방지한다.
		String loc = request.getContextPath();
		String view = "/index.jsp";
		
		
		int result = memberService.deleteMember(memberId);
		
		if(result>0)
				msg = "계정이 정상 삭제 되었습니다.";
		else 
			msg = "계정 삭제에 실패했습니다.";	
		
	
		
		//존재하지 않는다면, 새로 만들지 않고 null리턴 
		//세션 삭제
		HttpSession session = request.getSession(false);
		
		if(session != null)
			session.invalidate();
		
		request.setAttribute("msg", msg);
		request.setAttribute("loc", loc);
		
//		System.out.println(result);
		
		RequestDispatcher reqDispatcher = request.getRequestDispatcher(view);
		reqDispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
