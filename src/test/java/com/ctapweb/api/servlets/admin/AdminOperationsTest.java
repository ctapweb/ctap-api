package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class AdminOperationsTest {

	@Test
	public void testImportCategories() 
			throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {

		AdminOperations adminOperations  = new AdminOperations();
		
		adminOperations.reInitDB();
		adminOperations.importMeasures();
		adminOperations.cleanMeasures();

		
	}
}
