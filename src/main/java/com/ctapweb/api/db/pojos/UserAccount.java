/**
 * 
 */
package com.ctapweb.api.db.pojos;

import java.sql.Timestamp;
import java.util.Date;

/**
 * A POJO for user account.
 * @author xiaobin
 *
 */
public class UserAccount extends TablePojo {
	private String firstName;
	private String lastName;
	private String institution;
	private String email;
	private String passwd;
	private Timestamp createDate;
	private Timestamp lastLogin;

	public UserAccount() {
	}

	public UserAccount(long id, String firstName, String lastName, String institution, String email, String passwd,
			Timestamp createDate, Timestamp lastLogin) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.institution = institution;
		this.email = email;
		this.passwd = passwd;
		this.createDate = createDate;
		this.lastLogin = lastLogin;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	

}
