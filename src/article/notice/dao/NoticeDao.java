package article.notice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.notice.model.Notice;
import article.notice.model.Writer;
import jdbc.JdbcUtil;

public class NoticeDao {

	public Notice insert(Connection conn, Notice notice) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into notice "
					+ "(writerId, writerName, title, regdate, moddate, read_cnt) "
					+ "values (?,?,?,?,?,0)");
			pstmt.setString(1, notice.getWriter().getId());
			pstmt.setString(2, notice.getWriter().getName());
			pstmt.setString(3, notice.getTitle());
			pstmt.setTimestamp(4, toTimestamp(notice.getRegDate()));
			pstmt.setTimestamp(5, toTimestamp(notice.getModifiedDate()));
			int insertedCount = pstmt.executeUpdate();

			if (insertedCount > 0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select last_insert_id() from notice");
				if (rs.next()) {
					Integer newNo = rs.getInt(1);
					return new Notice(newNo,
							notice.getWriter(),
							notice.getTitle(),
							notice.getRegDate(),
							notice.getModifiedDate(),
							0);
				}
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
			rs = stmt.executeQuery("select count(*) from notice");
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	public List<Notice> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from notice " +
					"order by article_no desc limit ?, ?");
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			List<Notice> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertArticle(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	private Notice convertArticle(ResultSet rs) throws SQLException {
		return new Notice(rs.getInt("notice_no"),
				new Writer(
						rs.getString("writer_id"),
						rs.getString("writer_name")),
				rs.getString("title"),
				toDate(rs.getTimestamp("regdate")),
				toDate(rs.getTimestamp("moddate")),
				rs.getInt("read_cnt"));
	}

	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	public Notice selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(
					"select * from notice where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			Notice article = null;
			if (rs.next()) {
				article = convertArticle(rs);
			}
			return article;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update notice set read_cnt = read_cnt + 1 "+
						"where article_no = ?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	
	public int update(Connection conn, int no, String title) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update notice set title = ?, moddate = now() "+
						"where article_no = ?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
	
	public int update(Connection conn, int no, String content, String fileName) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update noticecontent set content = ?, fileName = ? "+
						"where article_no = ?")) {
			pstmt.setString(1, content);
			pstmt.setString(2, fileName);
			pstmt.setInt(3, no);
			return pstmt.executeUpdate();
		}
	}
}
