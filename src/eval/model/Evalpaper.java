package eval.model;

import java.util.Date;

import member.model.Professor;
import team.model.Team;

/* 팀별 개별 교수님 평가서 */
public class Evalpaper {
	
	final private int DEFAULT_TOTAL = 0;
	final private int DEFAULT_STATE = 0;
	
	private String evalNo;
	private Questions qs;
	private String pro;
	private String team;
	private String paperNo;
	private Date evalDate;
	private int total;
	private int resultState;
	
	public Evalpaper() {
		evalNo = null;
		qs = new Questions();
		pro = null;
		team = null;
		paperNo = null;
		evalDate = null;
		total = DEFAULT_TOTAL;
		resultState = DEFAULT_STATE;
	}
	
	public Evalpaper(String e, Questions q, String p, String t, Date d, int tt, int r) {
		evalNo = e;
		qs = q;
		pro = p;
		team = t;
		paperNo = makePaperNo(e, t, p);
		evalDate = d;
		total = tt;
		resultState = r;
	}
	
	public Evalpaper(String e, Questions q, String p, String t, String pfno, Date d, int tt, int r) {
		evalNo = e;
		qs = q;
		pro = p;
		team = t;
		paperNo = pfno;
		evalDate = d;
		total = tt;
		resultState = r;
	}
	
	public Evalpaper(Questions q, String pno, Date d, int tt, int r) {
		evalNo = null;
		qs = q;
		pro = null;
		team = null;
		paperNo = pno;
		evalDate = d;
		total = tt;
		resultState = r;
	}
	
	/* 개별 교수님 평가서 번호 */
	public String makePaperNo(String eNo, String tNo, String pId) {
		return eNo+"-"+tNo+"-"+pId;
	}
	
	public String getEvalNo() {
		return evalNo;
	}

	public void setEvalNo(String evalNo) {
		this.evalNo = evalNo;
	}

	public Questions getQs() {
		return qs;
	}

	public void setQs(Questions qs) {
		this.qs = qs;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Date getEvalDate() {
		return evalDate;
	}

	public void setEvalDate(Date evalDate) {
		this.evalDate = evalDate;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getResultState() {
		return resultState;
	}

	public void setResultState(int resultState) {
		this.resultState = resultState;
	}

	public String getPaperNo() {
		return paperNo;
	}

	public void setPaperNo(String paperNo) {
		this.paperNo = paperNo;
	}
	
	
}
