package team.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import team.dao.*;
import member.dao.*;
import team.model.*;
import team.service.MakeTeamRequest;;


public class MakeTeamService {

   private TeamDao teamDao = new TeamDao();
   private StudentDao studentDao = new StudentDao();
   
   public void MakeTeam(MakeTeamRequest mtReq) {
      Connection conn = null;
      try {
         conn = ConnectionProvider.getConnection();
         conn.setAutoCommit(false);
         
         ArrayList<String> students = mtReq.getStuIds();
         
         for(int i = 0 ; i < students.size() ; i++) {
            Team team = teamDao.selectByteam(conn, students.get(i));
            if(true) {
               /* 이부분은 만약 추가하려는 팀원이 이미 다른 팀에 있는 경우 오류 처리하기 위해 작성 */
            }
         }
         
         teamDao.insert(conn, 
               new Team(
                     mtReq.getTeamNo(), 
                     mtReq.getTeamName(), 
                     mtReq.getTeamSubject(),
                     mtReq.getAdvisor(),
                     mtReq.isState(),
                     mtReq.getGroupNo(),
                     new Date())
               );
         conn.commit();
      } catch (SQLException e) {
         JdbcUtil.rollback(conn);
         throw new RuntimeException(e);
      } finally {
         JdbcUtil.close(conn);
      }
   }
}