package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
public class DbInteraction {
	ArrayList<Member> members;
	DbInteraction(ArrayList<Member> members){
		this.members = members;
	}

	public ArrayList<Member> buildQuery() throws SQLException {

		DbConnection dbConn = new DbConnection();
		Connection conn = dbConn.connection();
		PreparedStatement ps;
		ResultSet rs;
		switch(members.get(0).getMessage()) {
		case "login" :
			//ps=conn.prepareStatement("SELECT member_id, username, password, type From member_details.Members WHERE username=? AND password=?");
			ps=conn.prepareStatement("SELECT member_id, username, password, type From Members WHERE username=? AND password=?");
			ps.setString(1, this.members.get(0).getUsername());
			ps.setString(2, this.members.get(0).getPassword());

			rs = ps.executeQuery();

			if(rs.next()) {
				this.members.get(0).setMemberId(rs.getInt("member_id"));
				this.members.get(0).setType(rs.getString("type"));
				this.members.get(0).setMessage("success");
			}
			else {
				System.out.println("Not Exists in DB");
				this.members.get(0).setMessage("failed");
			}

			ps.close();
			rs.close();
			conn.close();
			break; 

		case "load" :
			//ps=conn.prepareStatement("SELECT * From member_details.members");
			ps=conn.prepareStatement("SELECT * From Members");
			rs = ps.executeQuery();

			while(rs.next()){
				Member member = new Member();
				member.setMemberId(rs.getInt("member_id"));
				member.setFullname(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setEmail(rs.getString("email"));
				if(rs.getString("phone_num")!=null){
					member.setPhoneNo(rs.getString("phone_num"));
				}
				if(rs.getString("birth_date")!=null){
					member.setDob(rs.getString("birth_date"));
				}
				member.setType(rs.getString("type"));
				this.members.add(member);
			}

			ps.close();
			rs.close();
			conn.close();
			break; 

		case "add" :
			Calendar calendar = Calendar.getInstance();
			java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

			//ps=conn.prepareStatement("INSERT INTO member_details.members (member_id, username, password, email, phone_num, birth_date, type) VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps=conn.prepareStatement("INSERT INTO Members (member_id, username, password, email, phone_num, birth_date, type) VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, this.members.get(0).getMemberId());
			ps.setString(2, this.members.get(0).getFullname());
			ps.setString(3, "123456");
			ps.setString(4, this.members.get(0).getEmail());
			ps.setString(5, this.members.get(0).getPhoneNo());
			ps.setDate(6, startDate);
			ps.setString(7, "member");

			int rs2 = ps.executeUpdate();
			if(rs2!=0) {
				System.out.println("Number of rows afftected:"+ rs2);
			}else {
				System.out.println("Number of rows afftected:"+ rs2 +"Please try again");
			}

			ps.close();
			conn.close();
			break;

		case "update" :
			//ps=conn.prepareStatement("UPDATE member_details.members SET username =?, email=?, phone_num=? WHERE member_id=?");
			ps=conn.prepareStatement("UPDATE Members SET username =?, email=?, phone_num=? WHERE member_id=?");
			ps.setString(1, this.members.get(0).getFullname());
			ps.setString(2, this.members.get(0).getEmail());
			ps.setString(3, this.members.get(0).getPhoneNo());
			ps.setInt(4, this.members.get(0).getMemberId());

			int rs3 = ps.executeUpdate();
			if(rs3!=0) {
				System.out.println("Number of rows afftected:"+ rs3);
			}else {
				System.out.println("Number of rows afftected:"+ rs3 +"Please try again");
			}

			ps.close();
			conn.close();
			break;

		case "delete" :
			//ps=conn.prepareStatement("DELETE FROM member_details.members WHERE member_id=?");
			ps=conn.prepareStatement("DELETE FROM Members WHERE member_id=?");
			ps.setInt(1, this.members.get(0).getMemberId());

			int rs4 = ps.executeUpdate();
			if(rs4!=0) {
				System.out.println("Number of rows afftected:"+ rs4);
			}else {
				System.out.println("Number of rows afftected:"+ rs4 +"Please try again");
			}

			ps.close();
			conn.close();
			break;
			
		default : 
			System.out.println("Action Code Does not match");
		}
		
		return this.members;
	}
}
