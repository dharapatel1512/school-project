package org.njit.cs602.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbInteraction {
	Member member;
	DbInteraction(Member member){
		this.member = member;
	}

	public String buildQuery() throws SQLException {

		DbConnection dbConn = new DbConnection();
		Connection conn = dbConn.connection();
		PreparedStatement ps=conn.prepareStatement("SELECT username, password From member_details.members WHERE username=? AND password=?");
		ps.setString(1, this.member.getUsername());
		ps.setString(2, this.member.getPassword());
		System.out.println("Creating statement...");
		String response="failed";
		ResultSet rs = ps.executeQuery();

        if(rs.next()){
			System.out.println("Got it" + rs.getString("username") + rs.getString("password"));
			member.setMessage("success");
			//response="success";
		}
		else {
			System.out.println("NAAAAAAAAAA");
			member.setMessage("faliled");

			//response="faliled";

		}
		rs.close();
		conn.close();
		//System.out.println(response);
		return response;
	}
}
