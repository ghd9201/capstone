package eval.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.service.User;
import eval.service.AllEvalStatusValue;
import eval.service.EvalplanStatuService;
import eval.service.ShowTeam;
import eval.service.TerminateEvalplanService;
import mvc.command.CommandHandler;

public class TerminateEvalHandler implements CommandHandler {
	
	TerminateEvalplanService terminateEvalplanService = new TerminateEvalplanService();
	
	private static final String FORM_VIEW = "/WEB-INF/view/FinalList.jsp";
	private static final String LIST_VIEW = "/WEB-INF/view/EvalTeamList.jsp";
	
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		User user = (User)req.getSession(false).getAttribute("authProUser");
		List<ShowTeam> restlist = null;
		
		String strYear = AllEvalStatusValue.togetStrYear();
		String twodigit= strYear.substring(2);
		if(terminateEvalplanService.NotComplete(twodigit, restlist)) {
			req.setAttribute("remain", "yes");
			return FORM_VIEW;
		}else {	
			terminateEvalplanService.Terminate(user.getId());
			return LIST_VIEW;
		}
	}
}