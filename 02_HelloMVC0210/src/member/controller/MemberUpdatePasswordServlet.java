package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.util.MvcUtils;
import member.model.service.MemberService;
import member.model.vo.Member;



/**
 * Servlet implementation class MemberUpdatePasswordServlet
 */
@WebServlet("/member/updatePassword")
public class MemberUpdatePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdatePasswordServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/views/member/updatePassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 request.setCharacterEncoding("utf-8");
    	 //세션정보 가져오기
    	 HttpSession session = request.getSession(false);
    	 Member member = (Member)session.getAttribute("memberLoggedIn");
    	 String dbPassword = member.getPassword();
    	 
    	 //사용자 요청 패스워드
    	 String reqPassword = request.getParameter("password");
    	 String newPassword = request.getParameter("newPassword");
    	 
    	 //기존 패스워드 암호화
    	 String EncryptedReqPassword = MvcUtils.getEncryptedPassword(reqPassword);
    	 
    	 //검사
    	 if(!EncryptedReqPassword.equals(dbPassword)) {
    		 session.setAttribute("msg", "비밀번호를 다시 입력하세요");
    		 String path = request.getContextPath()+"/member/updatePassword";
    		 response.sendRedirect(path);
    		 return;
    		 
    	 }else {
    		 
    		 //비밀번호 암호화
    		 String EncryptedNewPassword = MvcUtils.getEncryptedPassword(newPassword);
    		 
    		 //member에 비번넣기
    		 member.setPassword(EncryptedNewPassword);
    		 
    		 //DB에 비번 재설정
    		 int result = new MemberService().updatePassword(member);
    	     String msg = result > 0 ? "변경이 완료되었습니다." : "변경을 실패하였습니다.";
    		 System.out.println(result);
    		 
    		 //세션에 비밀번호 재설
    		 member.setPassword(EncryptedNewPassword);
    		 session.setAttribute("memberLoggedIn", member);
    		 
    		 session.setAttribute("msg", msg);
    	 }
    	
    	request.getRequestDispatcher("/index.jsp")
			   .forward(request, response);
	}

}
