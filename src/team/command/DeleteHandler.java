package team.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.*;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import team.service.*;
import mvc.command.CommandHandler;
import team.teamnum;
import team.command.*;


public class DeleteHandler implements CommandHandler {	
	private static final String FORM_VIEW = "/WEB-INF/view/deleteTeamForm.jsp";
	private RemoveTeamService removeTeamSvc = new RemoveTeamService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}


	private String processSubmit(HttpServletRequest req, HttpServletResponse res)
	throws Exception {
		Member member = (Member)req.getSession().getAttribute("authTeam");	
		StudentUser user = (StudentUser)req.getSession(false).getAttribute("authStdUser");
		
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		String curPwd = req.getParameter("curPwd");
		System.out.println("팀넘버입니다." + member.getTeamNo());
		String curTeam = member.getTeamNo();
		
		if (curPwd == null || curPwd.isEmpty()) {
			errors.put("curPwd", Boolean.TRUE);
		}		
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		try {
			removeTeamSvc.delete_team(curPwd, curTeam); // 현재 로그인상태의 teamNo값 불러오기
			removeTeamSvc.delete_teamNo(user.getId());
			return "/WEB-INF/view/DeleteTeamSuccess.jsp";
			
		} catch (InvalidPasswordException e) {
			errors.put("badCurPwd", Boolean.TRUE);
			return FORM_VIEW;
		} catch (MemberNotFoundException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

}