package org.njit.cs602.server;


public class Member extends DataObject implements Comparable<Member>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String fullname;
	private String email;
	private String phoneNo;
	private String dob;

	public Member(String fullname, String email, String phoneNo, String dob) {
		this.fullname = fullname;
		this.email = email;
		this.phoneNo = phoneNo;
		this.dob = dob;
	}
	public Member() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String name) {
		this.fullname = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNum) {
		this.phoneNo = phoneNum;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	@Override
	public int compareTo(Member oldMember){
		if (this.fullname.compareToIgnoreCase(oldMember.fullname) != 0){
			return this.fullname.compareToIgnoreCase(oldMember.fullname);
		}
		else if (this.email.compareToIgnoreCase(oldMember.email) != 0){
			return this.email.compareToIgnoreCase(oldMember.email);
		}
		else return 0;

	}
}