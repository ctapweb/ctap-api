/**
 * 
 */
package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.TableNames;
import com.ctapweb.api.db.operations.AnalysisTableOperations;
import com.ctapweb.api.db.operations.CorpusTableOperations;
import com.ctapweb.api.db.operations.FS_METableOperations;
import com.ctapweb.api.db.operations.FeatureSetTableOperations;
import com.ctapweb.api.db.operations.MeasureCategoryTableOperations;
import com.ctapweb.api.db.operations.MeasureTableOperations;
import com.ctapweb.api.db.operations.ResultTableOperations;
import com.ctapweb.api.db.operations.TagTableOperations;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.UserAccount;
import com.ctapweb.api.measures.annotations.Measure;
import com.ctapweb.api.measures.annotations.MeasureCategory;

/**
 * Administrative operations: initialize database, get user account list, etc.
 * @author xiaobin
 *
 */
public class AdminOperations {

	private Logger logger = LogManager.getLogger();

	private TableNames tableNames;
	private UserTableOperations userTableOperations;
	private CorpusTableOperations corpusTableOperations;
	private TagTableOperations tagTableOperations;
	private TextTableOperations textTableOperations;
	private MeasureCategoryTableOperations categoryTableOperations;
	private MeasureTableOperations measureTableOperations;
	private ResultTableOperations resultTableOperations;
	private AnalysisTableOperations analysisTableOperations;
	private FeatureSetTableOperations featureSetTableOperations;
	private FS_METableOperations fs_METableOperations;

	public AdminOperations() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getDataSource();

		tableNames = new TableNames();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);
		resultTableOperations = new ResultTableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
		fs_METableOperations = new FS_METableOperations(dataSource);

	}

	public List<String> getAllTableNames() {
		return tableNames.getAllTableNames();
	}

	/**
	 * For initializing database. Create all the tables necessary for the system.
	 * Pay attention to table dependencies.
	 * @throws SQLException 
	 */
	public void initDB() throws SQLException {
		logger.info("Initializing database and creating tables...");
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		tagTableOperations.createTable();
		textTableOperations.createTable();
		categoryTableOperations.createTable();
		measureTableOperations.createTable();
		featureSetTableOperations.createTable();
		fs_METableOperations.createTable();
		analysisTableOperations.createTable();
		resultTableOperations.createTable();
	}

	/**
	 * Reinitialize database, dropping tables that are already created.
	 * Be careful with the operation because it will delete all the data in the tables.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void reInitDB() throws SQLException, ClassNotFoundException, IOException {
		logger.warn("Reinitializing database to recreate the tables...");
		dropAllTables();
		initDB();
	}

	/**
	 * Drop all database tables. Be careful of dependency.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void dropAllTables() throws SQLException, ClassNotFoundException, IOException {
		logger.warn("Dropping all database tables...");
		resultTableOperations.dropTable();
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		fs_METableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		analysisTableOperations.dropTable();
		corpusTableOperations.dropTable();
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
	}

	/**
	 * Imports measures by using info from the @Measure annotation.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void importMeasures() throws IllegalArgumentException, IllegalAccessException, IOException, SQLException {

		//gets all methods that are annotated with @Measure.
		//each method extracts one measure.
		for(com.ctapweb.api.db.pojos.Measure measure: getMeasuresFromAnnotation()) {
			String measureName = measure.getName();
			long categoryId = measure.getCategoryId();
			String description = measure.getDescription();
			String language = categoryTableOperations.getEntry(measure.getCategoryId()).getLanguage();
			if(measureTableOperations.getEntryByCategoryAndName(categoryId, measureName) == null)  {
				//if not exists, insert new measure
				com.ctapweb.api.db.pojos.Measure measureEntry = 
						new com.ctapweb.api.db.pojos.Measure(1, categoryId, measureName, description);
				measureTableOperations.addEntry(measureEntry);
			} else {
				//if measure already exists, update description
				long measureId = measureTableOperations.getEntryByCategoryAndName(categoryId, measureName).getId(); 
				measureTableOperations.updateDescription(measureId, measure.getDescription());

			}


		}
	}


	//gets a set of classes annotated with @MeasureCategory
	private Set<Class<?>> getCategoryClasses() {
		return getReflections().getTypesAnnotatedWith(MeasureCategory.class);
	}

	private Reflections getReflections() {
		return new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forClassLoader())
				.filterInputsBy(new FilterBuilder().include("com.ctapweb.api.*"))
				.setScanners(
						new SubTypesScanner(false),
						new TypeAnnotationsScanner(),
						new MethodAnnotationsScanner()
						));
	}

	//Gets the category's id either from inserting the category as new or by
	//retrieving it from db if it already exists.
	private long getCategoryId(Class categoryClass) throws SQLException {
		long categoryId;
		//gets annotation of the class
		MeasureCategory categoryAnnotation = 
				(MeasureCategory) categoryClass.getAnnotation(MeasureCategory.class);
		String cName = categoryAnnotation.name();
		String cDescription = categoryAnnotation.description();
		String cLanguage = categoryAnnotation.language();
		String cRequiredPipe = categoryAnnotation.requiredPipeline();
		String cClassName = categoryClass.getName();

		//check if category already in db
		if(!categoryTableOperations.isCategoryExist(cName, cLanguage)) {
			com.ctapweb.api.db.pojos.MeasureCategory category = 
					new com.ctapweb.api.db.pojos.MeasureCategory();
			category.setName(cName);
			category.setDescription(cDescription);
			category.setLanguage(cLanguage);
			category.setRequiredPipeline(cRequiredPipe);
			category.setClassName(cClassName);

			categoryId = categoryTableOperations.addEntry(category);
		} else {
			categoryId = categoryTableOperations.getEntry(cName, cLanguage).getId();
		}

		return categoryId;
	}

	/**
	 * Check that all measures in DB have corresponding annotations from measure class, 
	 * or else remove them.
	 * Remove measure categories as well.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void cleanMeasures() throws SQLException, IllegalArgumentException, IllegalAccessException, IOException {

		//cleans measure categories
		//gets all categories from annotation
		Set<com.ctapweb.api.db.pojos.MeasureCategory> categoriesFromAnnotation = 
				getCategoriesFromAnnotation();
		//gets all categories from db and check that each exists in annotation, otherwise delete the category
		for(com.ctapweb.api.db.pojos.MeasureCategory c: categoryTableOperations.getAllEntries()) {
			if(!categoriesFromAnnotation.contains(c)) {
				categoryTableOperations.deleteEntry(c.getId());
			}
		}


		//cleans measures
		//gets all measures from method annotation
		Set<com.ctapweb.api.db.pojos.Measure> measuresFromAnnotation = getMeasuresFromAnnotation();

		//gets all measures from DB and check that each one exists in annotation, otherwise delete it.
		for(com.ctapweb.api.db.pojos.Measure m: measureTableOperations.getAllEntries()) {
			if(!measuresFromAnnotation.contains(m)) {
				measureTableOperations.deleteEntry(m.getId());
			}
		}

		//reimport the measures from annotation
		importMeasures();
	}

	private Set<com.ctapweb.api.db.pojos.Measure> getMeasuresFromAnnotation() throws SQLException {
		Set<com.ctapweb.api.db.pojos.Measure> measuresFromAnnotation = new HashSet<>();

		Set<Method> annotatedMethods = getReflections().getMethodsAnnotatedWith(Measure.class);
		for(Method method: annotatedMethods) {
			Measure measure = method.getAnnotation(Measure.class);
			long categoryId = getCategoryId(method.getDeclaringClass());

			measuresFromAnnotation.add(new com.ctapweb.api.db.pojos.Measure(1, categoryId, measure.name(), measure.description()));
		}
		return measuresFromAnnotation;
	}

	private Set<com.ctapweb.api.db.pojos.MeasureCategory> getCategoriesFromAnnotation() throws SQLException {
		Set<com.ctapweb.api.db.pojos.MeasureCategory> categoriesFromAnnotation = new HashSet<>();

		Set<Class<?>> annotatedClasses = getReflections().getTypesAnnotatedWith(MeasureCategory.class);

		for(Class clazz: annotatedClasses) {
			MeasureCategory measureCategory = (MeasureCategory) clazz.getAnnotation(MeasureCategory.class);
			categoriesFromAnnotation.add(
					categoryTableOperations.getEntryByNameAndLanguage(
							measureCategory.name(), measureCategory.language()));
		}
		return categoriesFromAnnotation;
	}

	public List<UserAccount> getAllUserAccounts() throws SQLException {
		return userTableOperations.getAllEntries();
	}

}






