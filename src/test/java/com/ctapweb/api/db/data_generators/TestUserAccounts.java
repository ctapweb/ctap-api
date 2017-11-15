package com.ctapweb.api.db.data_generators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ctapweb.api.db.pojos.UserAccount;

/**
 * For generating user accounts for testing.
 * @author xiaobin
 *
 */
public class TestUserAccounts {
	public UserAccount generateAccount() {
		return generateAccounts(1).get(0);
	}
	
	public List<UserAccount> generateAccounts(int size) {
		List<UserAccount> accountList = new ArrayList<>();
		
		for(int i = 1; i <= size; i++) {
			UserAccount account = new UserAccount(i, 
					"firstName" + i, 
					"lastName" + i, 
					"institution" + i, 
					"email@email.com" + i, 
					"passwd" + i, 
					new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));

			accountList.add(account);
			
		}
		
		return accountList;
		
	}
	
	

}
