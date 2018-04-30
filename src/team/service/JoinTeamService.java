package team.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import team.dao.*;
import member.service.DuplicateIdException;
import team.model.*;


public class JoinTeamService {

   private TeamDao teamDao = new TeamDao();
   
   public void JoinTeam(String teamNo, String stuId) {
      Connection conn = null;
      try {
         conn = ConnectionProvider.getConnection();
         conn.setAutoCommit(false);
         teamDao.update_team(conn, stuId, teamNo);
         conn.commit();
      } catch (SQLException e) {
         JdbcUtil.rollback(conn);
         throw new RuntimeException(e);
      } finally {
         JdbcUtil.close(conn);
      }
   }
   
   public boolean searchTeam(String teamNo) {
	   Connection conn = null;
	      try {
	         conn = ConnectionProvider.getConnection();
	         conn.setAutoCommit(false);
	         Team team = teamDao.selectByteam(conn, teamNo);
			 if (team == null) {				
				JdbcUtil.rollback(conn);
				return false;
			 }else {
					return true;
				}	         
	      } catch (SQLException e) {
	          JdbcUtil.rollback(conn);
	          throw new RuntimeException(e);
	      } finally {
	          JdbcUtil.close(conn);
	      }
   }
}