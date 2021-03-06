package article.team.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.common.ArticleNotFoundException;
import article.team.model.*;
import jdbc.JdbcUtil;

public class TeamArticleDao {

	public TeamArticle insert(Connection conn, TeamArticle article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into teamboard"
					+ "(fileNo, teamNo, writeId, title, regDate, modDate, downCnt) "
					+ "values (?,?,?,?,?,?,0)");
			pstmt.setString(1, article.getFileNo());
			pstmt.setString(2, article.getWriter().getTeamNo());
			pstmt.setString(3, article.getWriter().getWriterId());
			pstmt.setString(4, article.getTitle());
			pstmt.setTimestamp(5, toTimestamp(article.getRegDate()));
			pstmt.setTimestamp(6, toTimestamp(article.getModifiedDate()));
			int insertedCount = pstmt.executeUpdate();		//리턴값은 성골했을 때 : 성공한 행의 개수, 실패시 : 0
			
			if(insertedCount>0) {
				//stmt = conn.createStatement();
				//rs = stmt.executeQuery("SELECT MAX(fileNo) FROM teamboard;");
				//if (rs.next()) {
					//String newNo = rs.getString(1);
					return new TeamArticle(article.getFileNo(),
							article.getTitle(),
							article.getWriter(),
							article.getRegDate(),
							article.getModifiedDate(),
							0);
				//}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}

	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) from teamboard");
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	public List<TeamArticle> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from teamboard " +
					"order by regDate desc limit ?, ?");
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			List<TeamArticle> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertArticle(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	/*이부분 문제 있을 수 있음*/
	private TeamArticle convertArticle(ResultSet rs) throws SQLException {
		return new TeamArticle(rs.getString("fileNo"),
				rs.getString("title"),
				new TeamArticleWriter(
						rs.getString("teamNo"),
						rs.getString("writeId")),
				toDate(rs.getTimestamp("regDate")),
				toDate(rs.getTimestamp("modDate")),
				rs.getInt("downCnt"));
	}

	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	public TeamArticle selectById(Connection conn, String fileNo) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(
					"select * from teamboard where fileNo = ?");
			pstmt.setString(1, fileNo);
			rs = pstmt.executeQuery();
			TeamArticle article = null;
			if (rs.next()) {		//이거 if 문 뭐지? 굳이 필요한가
				article = convertArticle(rs);
			}
			return article;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	public void increaseDownCount(Connection conn, String fileNo) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update teamboard set downCnt = downCnt + 1 "+
						"where fileNo = ?")) {
			pstmt.setString(1, fileNo);
			pstmt.executeUpdate();
		}
	}
	
	public int update(Connection conn, String fileNo, String title) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update teamboard set title = ?, modDate = now() "+
						"where fileNo = ?")) {
			pstmt.setString(1, title);
			pstmt.setString(2, fileNo);
			return pstmt.executeUpdate();
		}
	}
	
	public TeamArticle deleteByFileNo(Connection conn, String fileNo) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			TeamArticle teamarticle = selectById(conn, fileNo);
			if(teamarticle == null) {
				throw new ArticleNotFoundException();
			}
			
			pstmt = conn.prepareStatement("delete from teamboard where fileNo = ? ");
			pstmt.setString(1, fileNo);
			int insertedCount = pstmt.executeUpdate();		//리턴값은 성골했을 때 : 성공한 행의 개수, 실패시 : 0
			
			if(insertedCount>0) {
					return teamarticle;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}
}
