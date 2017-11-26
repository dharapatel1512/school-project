package org.njit.cs602.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class DbInteraction {
	ArrayList<Member> members;
	DbInteraction(ArrayList<Member> members){
		this.members = members;
	}

	public ArrayList<Member> buildQuery() throws SQLException {

		DbConnection dbConn = new DbConnection();
		Connection conn = dbConn.connection();


		switch(members.get(0).getMessage()) {
		case "login" :
			PreparedStatement ps=conn.prepareStatement("SELECT username, password, type From member_details.members WHERE username=? AND password=?");
			ps.setString(1, this.members.get(0).getUsername());
			ps.setString(2, this.members.get(0).getPassword());

			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				//System.out.println("Got it" + rs.getString("username") + rs.getString("password") +rs.getString("type"));
				this.members.get(0).setType(rs.getString("type"));
				this.members.get(0).setMessage("success");
				//response="success";
			}
			else {
				System.out.println("Not Exists in DB");
				this.members.get(0).setMessage("failed");

				//response="faliled";

			}
			rs.close();
			conn.close();

			break; // optional

		case "load" :
			PreparedStatement ps1=conn.prepareStatement("SELECT * From member_details.members");

			ResultSet rs1 = ps1.executeQuery();

			while(rs1.next()){
				Member member = new Member();
				member.setFullname(rs1.getString("username"));
				member.setPassword(rs1.getString("password"));
				member.setEmail(rs1.getString("email"));
				if(rs1.getString("phone_num")!=null){
					member.setPhoneNo(rs1.getString("phone_num"));
				}
				if(rs1.getString("birth_date")!=null){
					member.setDob(rs1.getString("birth_date"));
				}
				member.setType(rs1.getString("type"));
				this.members.add(member);
			}

			rs1.close();
			conn.close();
			break; 

		case "add" :
			Calendar calendar = Calendar.getInstance();
		      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

			PreparedStatement ps2=conn.prepareStatement("INSERT INTO member_details.members (member_id, username, password, email, phone_num, birth_date, type) VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps2.setInt(1, this.members.get(0).getMemberId());
			ps2.setString(2, this.members.get(0).getFullname());
			ps2.setString(3, "123456");
			ps2.setString(4, this.members.get(0).getEmail());
			ps2.setString(5, this.members.get(0).getPhoneNo());
			ps2.setDate(6, startDate);
			ps2.setString(7, "member");
			int rs2 = ps2.executeUpdate();
			if(rs2!=0) {
				System.out.println("Number of rows afftected:"+ rs2);
			}else {
				System.out.println("Number of rows afftected:"+ rs2 +"Please try again");
			}
			
			conn.close();
			break; // optional

		case "update" :/*
			PreparedStatement ps3=conn.prepareStatement("UPDATE member_details.members SET username = 'manish' WHERE member_id='2'");
			ps3.setString(1, this.members.get(0).getFullname());
			ps3.setString(2, this.members.get(0).getEmail());

			int rs3 = ps3.executeUpdate();

			if(rs3!=0) {
				System.out.println("Number of rows afftected:"+ rs3);
			}else {
				System.out.println("Number of rows afftected:"+ rs3 +"Please try again");
			}
			
			conn.close();

			break; // optional*/
			
		case "delete" :
			PreparedStatement ps4=conn.prepareStatement("DELETE FROM member_details.members WHERE member_id=?");
			ps4.setInt(1, this.members.get(0).getMemberId());
			int rs4 = ps4.executeUpdate();
			if(rs4!=0) {
				System.out.println("Number of rows afftected:"+ rs4);
			}else {
				System.out.println("Number of rows afftected:"+ rs4 +"Please try again");
			}
			
			conn.close();
			break; // optional
		default : 
			System.out.println("Action code does not match any");
		}
		return this.members;
	}
}
