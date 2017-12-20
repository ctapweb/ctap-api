package com.ctapweb.api.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.junit.Test;

import com.ctapweb.api.servlets.exceptions.CTAPException;

public class TestRunAnalysis {

	@Test
	public void testRunAnalysis() throws ClassNotFoundException, IOException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CTAPException {
		RunAnalysis runAnalysis = new RunAnalysis(4);
		runAnalysis.run();
	}
}
