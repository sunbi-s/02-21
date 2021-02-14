package common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.service.MemberService;
import member.model.vo.Member;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    public AdminFilter() {
    }
    
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)request;
		HttpSession session = httpReq.getSession();
		Member memberLoggedIn = (Member)session.getAttribute("memberLoggedIn");
		
		String UriId = httpReq.getParameter("memberId");

		
		if(memberLoggedIn == null || !memberLoggedIn.getMemberRole().equals(MemberService.ADMIN_MEMBER_ROLE)) {
			HttpServletResponse httpResp = (HttpServletResponse)response;
			session.setAttribute("msg",  "잘못된 경로로 접근하셨습니다.");
			httpResp.sendRedirect(httpReq.getContextPath());
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
