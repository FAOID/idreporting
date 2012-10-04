package test.idnfi.reporting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Result;
import org.jooq.SelectLimitStep;
import org.jooq.SelectOnStep;
import org.jooq.impl.Factory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.RecordSummarySortField;
import org.openforis.collect.persistence.RecordDao;
import org.openforis.collect.persistence.RecordPersistenceException;
import org.openforis.collect.persistence.SurveyDao;
import org.openforis.collect.persistence.jooq.JooqDaoSupport;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.Schema;
import org.openforis.idm.metamodel.SurveyContext;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.CodeAttribute;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Field;
import org.openforis.idm.model.IntegerAttribute;
import org.openforis.idm.model.Node;
import org.openforis.idm.model.RealAttribute;
import org.openforis.idm.model.RealValue;
import org.openforis.idm.model.Record;
import org.openforis.idm.model.expression.AbsoluteModelPathExpression;
import org.openforis.idm.model.expression.ExpressionFactory;
import org.openforis.idm.model.expression.InvalidExpressionException;
import org.openforis.idm.model.expression.ModelPathExpression;
import org.openforis.idm.model.expression.internal.MissingValueException;
import org.openforis.idm.transform.DataTransformation;
import org.openforis.idm.transform.csv.ModelCsvWriter;
import org.openforis.idreporting.core.DialectAwareJooqFactory;
import org.openforis.idreporting.core.FactoryDao;
import static org.openforis.idreporting.persistence.jooq.tables.ListProvince.LIST_PROVINCE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.openforis.idreporting.persistence.jooq.tables.SchemaCluster.SCHEMA_CLUSTER;
import static org.openforis.idreporting.persistence.jooq.tables.SchemaClusterPermanentplota.SCHEMA_CLUSTER_PERMANENTPLOTA;
import static org.openforis.idreporting.persistence.jooq.tables.SchemaClusterPermanentplotaPlotaenum.SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM;
import static org.openforis.idreporting.persistence.jooq.Sequences.SCHEMA_CLUSTER_SEQ_ID;
import static org.openforis.idreporting.persistence.jooq.Sequences.SCHEMA_CLUSTER_PERMANENTPLOTA_SEQ_ID;
import static org.openforis.idreporting.persistence.jooq.Sequences.SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_SEQ_ID;
import static org.openforis.idreporting.persistence.jooq.Sequences.SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREEHIGHER20CM_SEQ_ID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/idreporting-context.xml" })
@TransactionConfiguration(defaultRollback = false)
/**
 * @author Wibowo, Eko
 */
@Transactional
public class TspPspProcessingTest {

	@Autowired
	protected SurveyDao surveyDao;
	
	@Autowired
	protected RecordDao recordDao;
	
	@Autowired
	protected RecordManager recordManager;

	@Autowired
	protected FactoryDao factoryDao;
	
	//@Test
	public void testJooq()
	{
		DialectAwareJooqFactory jf = factoryDao.getJooqFactory();
		Assert.assertNotNull(jf);
	}
	
	/*
	//@Test
	public void testReportingNvUsingAnalysis() throws RecordPersistenceException, InvalidExpressionException
	{
		DialectAwareJooqFactory jf = factoryDao.getJooqFactory();
		Result<org.jooq.Record> resultProvince = jf.select(LIST_PROVINCE.CODE, LIST_PROVINCE.LABEL).from(LIST_PROVINCE).orderBy(LIST_PROVINCE.CODE).fetch();
		
		// 1. Province
		for(org.jooq.Record province : resultProvince)
		{
			Integer provinceCode = province.getValueAsInteger(LIST_PROVINCE.CODE);
			
			Result<org.jooq.Record> resultClusterYear = jf.selectDistinct(SCHEMA_CLUSTER.KEY1, SCHEMA_CLUSTER.KEY2, SCHEMA_CLUSTER.KEY3, SCHEMA_CLUSTER.KEY4).
					from(SCHEMA_CLUSTER_PERMANENTPLOTA).
					join(SCHEMA_CLUSTER).onKey().
					join(LIST_PROVINCE).onKey().
					where(SCHEMA_CLUSTER_PERMANENTPLOTA.PROVINCE_CODE.equal(provinceCode)).
					and(SCHEMA_CLUSTER.KEY1.equal("49")).
					and(SCHEMA_CLUSTER.KEY2.equal("750")).
					and(SCHEMA_CLUSTER.KEY3.equal("9840")).
					orderBy(SCHEMA_CLUSTER.KEY1, SCHEMA_CLUSTER.KEY2, SCHEMA_CLUSTER.KEY3, SCHEMA_CLUSTER.KEY4).				
					fetch();
			
			System.out.println(provinceCode);
 
			//2. ClusterYear
			for(org.jooq.Record clusterYear : resultClusterYear)
			{
				Integer utmZone = clusterYear.getValueAsInteger(SCHEMA_CLUSTER.KEY1);
				Integer easting = clusterYear.getValueAsInteger(SCHEMA_CLUSTER.KEY2);
				String northing = String.format("%04d", clusterYear.getValueAsInteger(SCHEMA_CLUSTER.KEY3));
				String clusterKey = utmZone + "" +  easting  + "" + northing;
				Integer year = clusterYear.getValueAsInteger(SCHEMA_CLUSTER.KEY4);
				
				System.out.println("\t"+clusterKey);
				//3. Diameter & Bole Height
				Result<org.jooq.Record> resultClusterYearDbb = jf.
				select(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.DBB_OR_B, SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM.BOLE_HEIGHT).
				from(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM).
				join(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM).onKey().
				join(SCHEMA_CLUSTER_PERMANENTPLOTA).onKey().
				join(SCHEMA_CLUSTER).onKey().
				join(LIST_PROVINCE).onKey().
				where(SCHEMA_CLUSTER_PERMANENTPLOTA.PROVINCE_CODE.equal(provinceCode)).
				and(SCHEMA_CLUSTER.KEY1.equal(utmZone + "")).
				and(SCHEMA_CLUSTER.KEY2.equal(easting + "")).
				and(SCHEMA_CLUSTER.KEY3.equal(northing + "")).
				and(SCHEMA_CLUSTER.KEY4.equal(year+"")).
				and(SCHEMA_CLUSTER.KEY1.equal("49")).
				and(SCHEMA_CLUSTER.KEY2.equal("750")).		
				and(SCHEMA_CLUSTER.KEY3.equal("9840")).
				orderBy(SCHEMA_CLUSTER.KEY4).				
				fetch();
				System.out.println("\t\t" + year);
				
				for(org.jooq.Record clusterYearDbb : resultClusterYearDbb)
				{
					Double diameter = clusterYearDbb.getValueAsDouble(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.DBB_OR_B);
					Double bole_height = clusterYearDbb.getValueAsDouble(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM.BOLE_HEIGHT);
					
					System.out.println("\t\t\t" + diameter + ";" + bole_height);
					
					//20
					//30
					//40
					//50
					//60
					//70
					//80
					
				}
			}
		}
	}
	*/
	
	//@Test
	public void testClearDb()
	{
		DialectAwareJooqFactory jf = factoryDao.getJooqFactory();
		jf.delete(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM).execute();
		jf.delete(SCHEMA_CLUSTER_PERMANENTPLOTA).execute();
		jf.delete(SCHEMA_CLUSTER).execute();
	}

	//@Test
	public void testSynchDb() throws RecordPersistenceException, InvalidExpressionException
	{
		DialectAwareJooqFactory jf = factoryDao.getJooqFactory();
		jf.delete(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM).execute();
		jf.delete(SCHEMA_CLUSTER_PERMANENTPLOTA).execute();
		jf.delete(SCHEMA_CLUSTER).execute();
		
		CollectSurvey survey = surveyDao.load("idnfi");
		List<CollectRecord> records = recordManager.loadSummaries(survey, "cluster", 0, Integer.MAX_VALUE, (List<RecordSummarySortField>) null, (String) null);
		for (CollectRecord s : records) {
			CollectRecord record = recordManager.load(survey, s.getId(), 1);			
			
			ExpressionFactory expressionFactory = s.getSurveyContext().getExpressionFactory();
			String rootEntityName = "/cluster";
			AbsoluteModelPathExpression expression = expressionFactory.createAbsoluteModelPathExpression(rootEntityName);
			List<Node<?>> rowNodes = null;
			try {
				rowNodes = expression.iterate(record);
			}catch(MissingValueException ex)
			{				
			}
			
			int icluster = 0;
			for(Node<?> n :rowNodes)
			{
				++icluster;
				String utm_zone = extractValues(n, "utm_zone").get(0);
				String easting = extractValues(n, "easting").get(0);
				String northing = extractValues(n, "northing").get(0);
				String year = extractValues(n, "year").get(0);
				String description = extractValues(n, "description").get(0);				
				String clusterKey = utm_zone + easting + String.format("%04d",Integer.parseInt(northing)); //TOFIX: easting as integer is better
				
				if(!utm_zone.equals("49") && !easting.equals("750")) 
				{
					System.out.println("SKIPPING : " + utm_zone+":" + easting + ":" + northing + ":" + year +":" + description);
					continue;
				}
				
				System.out.println(utm_zone+":" + easting + ":" + northing + ":" + year +":" + description);
				int schemaClusterId = jf.nextval(SCHEMA_CLUSTER_SEQ_ID).intValue();
				jf.insertInto(SCHEMA_CLUSTER)
					.set(SCHEMA_CLUSTER.ID, schemaClusterId)
					.set(SCHEMA_CLUSTER.CLUSTERKEY, clusterKey)
					.set(SCHEMA_CLUSTER.KEY1, utm_zone)
					.set(SCHEMA_CLUSTER.KEY2, easting)
					.set(SCHEMA_CLUSTER.KEY3, northing)
					.set(SCHEMA_CLUSTER.KEY4, year)					
					.set(SCHEMA_CLUSTER.KEY5, description)
					.execute();
				
				AbsoluteModelPathExpression expression1 = expressionFactory.createAbsoluteModelPathExpression("/cluster/permanent_plot_a[" + icluster + "]");
				List<Node<?>> rowNodes1 = null;
				try {
					rowNodes1 = expression1.iterate(record);
					int ipermanentPlotA = 0;
					for(Node<?> n1 :rowNodes1)
					{
						++ipermanentPlotA;
						int province = Integer.parseInt(extractValues(n1, "province").get(0));
						
						int schemaClusterPermanentPlotAId = jf.nextval(SCHEMA_CLUSTER_PERMANENTPLOTA_SEQ_ID).intValue();
						jf.insertInto(SCHEMA_CLUSTER_PERMANENTPLOTA)
							.set(SCHEMA_CLUSTER_PERMANENTPLOTA.ID, schemaClusterPermanentPlotAId)
							.set(SCHEMA_CLUSTER_PERMANENTPLOTA.PARENT_ID, schemaClusterId)
							.set(SCHEMA_CLUSTER_PERMANENTPLOTA.PROVINCE_CODE, province)
							.execute();
						
						AbsoluteModelPathExpression expression2 = expressionFactory.createAbsoluteModelPathExpression("/cluster/permanent_plot_a[" + icluster + "]/plota_enum[" + ipermanentPlotA + "]");
						List<Node<?>> rowNodes2 = null;					
						rowNodes2 = expression2.iterate(record);
						int ipermanentPlotA_plotaenum = 0;
						for(Node<?> n2 :rowNodes2)
						{
							++ipermanentPlotA_plotaenum;
							String sdbb_or_b = extractValues(n2, "dbb_or_b").get(0);
							if(sdbb_or_b.equals("")) sdbb_or_b="0";//TOFIX: what should be the empty value??/
							float dbb_or_b = Float.parseFloat(sdbb_or_b);//TOFIX : should this be double???
							
							int schemaClusterPermanentPlotA_plotaEnumId = jf.nextval(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_SEQ_ID).intValue();
							jf.insertInto(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM)
								.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.PROVINCE_CODE, province)
								.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.CLUSTER_ID, schemaClusterId)
								.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.ID, schemaClusterPermanentPlotA_plotaEnumId)
								.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.PARENT_ID, schemaClusterPermanentPlotAId)
								.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.DBB_OR_B, dbb_or_b)
								.execute();
							
							AbsoluteModelPathExpression expression3 = expressionFactory.createAbsoluteModelPathExpression("/cluster/permanent_plot_a[" + icluster + "]/plota_enum[" + ipermanentPlotA + "]/trees_higher_than_20cm[" + ipermanentPlotA_plotaenum + "]");
							List<Node<?>> rowNodes3 = null;					
							rowNodes2 = expression3.iterate(record);
							int ipermanentPlotA_plotaenum_treehigher20cm = 0;
							for(Node<?> n3 :rowNodes2)
							{
								++ipermanentPlotA_plotaenum_treehigher20cm;
								String sbole_height = extractValues(n3, "bole_height").get(0);
								if(sbole_height.equals("")||sbole_height==null) sbole_height ="0.0";//TOFIX: what should be the empty value??/
								float bole_height = Float.parseFloat(sbole_height);//TOFIX : should this be double???
								float volume = (float) ((0.25 * 3.14 * dbb_or_b * dbb_or_b * bole_height * 0.8)/10000); //V = (0.25 * 3.14 * d^2 * bole_height * 0.56) / 10.000
								System.out.println("V=" + volume + "," + "B=" + bole_height);
								//flattenning up into plota_enum
								jf.update(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM)
									.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.VOLUME, volume)
									.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.BOLE_HEIGHT,bole_height)
									.where(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM.ID.equal(schemaClusterPermanentPlotA_plotaEnumId))		
									.execute();
								
							}
						}
						
					}
				}catch(MissingValueException ex)
				{				
				}				
			}
		}
	}

	//@Test
	public void testExcelOutput() throws IOException, URISyntaxException
	{
		URI uriOutput = new URI("file:///C:/Users/User/Documents/ReportNV-SE-output.xlsx");
		FileOutputStream fileOutputStream = new FileOutputStream(uriOutput.getPath());
		XSSFWorkbook workbook = new XSSFWorkbook();		
		XSSFSheet worksheet = workbook.createSheet("Report NV");
		workbook.write(fileOutputStream);
	}
	
	@Test
	public void testReportingNvUsingCollectSTEP1() throws InvalidExpressionException, RecordPersistenceException, URISyntaxException, IOException
	{	
		
		URI uriOutput = new URI("file:///C:/Users/User/Documents/ReportNV-SE.xlsx");
		FileOutputStream fileOutputStream = new FileOutputStream(uriOutput.getPath());
		XSSFWorkbook workbook = new XSSFWorkbook();		
		XSSFSheet worksheet = workbook.createSheet("Report NV");
		
		
		
		String rootEntityName = "/cluster/permanent_plot_a/plota_enum";
		CollectSurvey survey = surveyDao.load("idnfi");
		List<CollectRecord> records = recordManager.loadSummaries(survey, "cluster", 0, Integer.MAX_VALUE, (List<RecordSummarySortField>) null, (String) null);
		
		
		HashMap<Integer, ProvinceN> hashProvince = new HashMap<Integer, ProvinceN>();
		int i=0;	
		hashProvince.put(1, new ProvinceN(1,"Daerah Istimewa Aceh"));
		hashProvince.put(2, new ProvinceN(2,"Sumatera Utara"));
		hashProvince.put(3, new ProvinceN(3,"Sumatera Barat"));
		hashProvince.put(4, new ProvinceN(4,"Riau"));
		hashProvince.put(5, new ProvinceN(5,"Jambi"));
		hashProvince.put(6, new ProvinceN(6,"Sumatera Selatan"));
		hashProvince.put(7, new ProvinceN(7,"Lampung"));
		hashProvince.put(8, new ProvinceN(8,"Bengkulu"));
		hashProvince.put(9, new ProvinceN(9,"Banten"));
		hashProvince.put(10, new ProvinceN(10,"Jawa Barat"));
		hashProvince.put(11, new ProvinceN(11,"Jawa Tengah"));
		hashProvince.put(12, new ProvinceN(12,"DIY"));
		hashProvince.put(13, new ProvinceN(13,"Jawa Timur"));
		hashProvince.put(14, new ProvinceN(14,"Bali"));
		hashProvince.put(15, new ProvinceN(15,"Nusa Tenggara Barat"));
		hashProvince.put(16, new ProvinceN(16,"Nusa Tenggara Timur"));
		hashProvince.put(17, new ProvinceN(17,"Timor Timur"));
		hashProvince.put(18, new ProvinceN(18,"Kalimantan Barat"));
		hashProvince.put(19, new ProvinceN(19,"Kalimantan Tengah"));
		hashProvince.put(20, new ProvinceN(20,"Kalimantan Selatan"));
		hashProvince.put(21, new ProvinceN(21,"Kalimantan Timur"));
		hashProvince.put(22, new ProvinceN(22,"Sulawesi Utara"));
		hashProvince.put(23, new ProvinceN(23,"Sulawesi Tengah"));
		hashProvince.put(24, new ProvinceN(24,"Sulawesi Tenggara"));
		hashProvince.put(25, new ProvinceN(25,"Sulawesi Selatan"));
		hashProvince.put(26, new ProvinceN(26,"Maluku"));
		hashProvince.put(27, new ProvinceN(27,"Irian Jaya"));
		hashProvince.put(28, new ProvinceN(28,"Kepulauan Riau"));
		hashProvince.put(29, new ProvinceN(29,"Bangka Belitung"));
		hashProvince.put(30, new ProvinceN(30,"Gorontalo"));
		hashProvince.put(31, new ProvinceN(31,"Sulawesi Barat"));
		hashProvince.put(32, new ProvinceN(32,"Maluku Utara"));
		hashProvince.put(33, new ProvinceN(33,"Irian Barat"));
		
		ModelPathExpression relativeExpression;
		for (CollectRecord s : records) {
			//clear data
			
			ExpressionFactory expressionFactory = s.getSurveyContext().getExpressionFactory();
			AbsoluteModelPathExpression expression = expressionFactory.createAbsoluteModelPathExpression(rootEntityName);
			CollectRecord record = recordManager.load(survey, s.getId(), 1);
			List<Node<?>> rowNodes = null;
			try {
				rowNodes = expression.iterate(record);
			}catch(MissingValueException ex)
			{				
			}
			
			if(rowNodes == null) continue;
			
			for(Node<?> n :rowNodes)
			{	
				String strD = extractValues(n, "dbb_or_b").get(0);
				double d;
				int provinceCode=-1;
				
				
				if(!"".equals(strD))
				{	
					//utm zone
					relativeExpression = expressionFactory.createModelPathExpression("parent()/parent()/utm_zone");
					IntegerAttribute utmZoneAttr = (IntegerAttribute) relativeExpression.evaluate(n, null);
					Integer utmZone = utmZoneAttr.getValue().getValue();
					
					
					//easting
					relativeExpression = expressionFactory.createModelPathExpression("parent()/parent()/easting");
					IntegerAttribute eastingAttr = (IntegerAttribute) relativeExpression.evaluate(n, null);
					Integer easting = eastingAttr.getValue().getValue();
					
					
					//northing
					relativeExpression = expressionFactory.createModelPathExpression("parent()/parent()/northing");
					IntegerAttribute northingAttr = (IntegerAttribute) relativeExpression.evaluate(n, null);
					Integer northing = northingAttr.getValue().getValue();
					
					
					String clusterKey = utmZone + "" + easting + "" + String.format("%04d", northing)  + "";
					//System.out.println(utmZone + ":" + easting + ":" + String.format("%04d", northing)  + "");
					
					//year
					relativeExpression = expressionFactory.createModelPathExpression("parent()/year");
					IntegerAttribute yearAttr = (IntegerAttribute) relativeExpression.evaluate(n, null);
					Integer year = yearAttr.getValue().getValue();
					
					
					//bole_height
					relativeExpression = expressionFactory.createModelPathExpression("trees_higher_than_20cm/bole_height");
					RealAttribute bole_heightAttr=null;
					try {
						bole_heightAttr = (RealAttribute) relativeExpression.evaluate(n, null);
					}catch(MissingValueException ex)
					{
						
					}
					double bole_height = 0;
					if(bole_heightAttr!=null)
					{
						RealValue x = bole_heightAttr.getValue();
						if(x!=null) {
							try {
								bole_height = x.getValue();
							}catch(NullPointerException ex)
							{	
							}
						}
					}
					
					relativeExpression = expressionFactory.createModelPathExpression("parent()/province");
					CodeAttribute code = null;
					try {
						code = (CodeAttribute ) relativeExpression.evaluate(n, null);
						provinceCode = Integer.parseInt(code.getValue().getCode());
					}catch(MissingValueException ex)
					{
						System.out.println("Province Error on Record = " + record.getId());						
					}
					
					
					
					d = Double.parseDouble(strD);
					if(d>=20){
						i++;
						hashProvince.get(provinceCode).getN(clusterKey, year, "20").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "20", d, bole_height);
					}
					if(d>=30){
						hashProvince.get(provinceCode).getN(clusterKey, year, "30").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "30", d, bole_height);
					}
					if(d>=40){
						hashProvince.get(provinceCode).getN(clusterKey, year, "40").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "40", d, bole_height);
					}
					if(d>=50){
						hashProvince.get(provinceCode).getN(clusterKey, year, "50").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "50", d, bole_height);
					}
					if(d>=60){
						hashProvince.get(provinceCode).getN(clusterKey, year, "60").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "60", d, bole_height);
					}
					if(d>=70){
						hashProvince.get(provinceCode).getN(clusterKey, year, "70").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "70", d, bole_height);
					}
					if(d>=80){
						hashProvince.get(provinceCode).getN(clusterKey, year, "80").add(d);
						hashProvince.get(provinceCode).addV(clusterKey, year, "80", d, bole_height);
					}
				}
			}				
					
		}
		
		
		
		//System.out.println("i =" + i);
		//System.out.println("Provinsi;Cluster;Tahun;N20;V20;N30;V30;N40;V40;N50;V50;N60;V60;N70;V70;N80;V80");
		XSSFRow rowHeader;
		XSSFCell cellHeader;
		
		rowHeader = worksheet.createRow(0);
		cellHeader = rowHeader.createCell(0);
		cellHeader.setCellValue("Provinsi");
		cellHeader = rowHeader.createCell(1);
		cellHeader.setCellValue("Klaster");
		cellHeader = rowHeader.createCell(2);
		cellHeader.setCellValue("Tahun");
		cellHeader = rowHeader.createCell(3);
		cellHeader.setCellValue("N20");
		cellHeader = rowHeader.createCell(4);
		cellHeader.setCellValue("V20");
		cellHeader = rowHeader.createCell(5);
		cellHeader.setCellValue("N30");
		cellHeader = rowHeader.createCell(6);
		cellHeader.setCellValue("V30");
		cellHeader = rowHeader.createCell(7);
		cellHeader.setCellValue("N40");
		cellHeader = rowHeader.createCell(8);
		cellHeader.setCellValue("V40");
		cellHeader = rowHeader.createCell(9);
		cellHeader.setCellValue("N50");
		cellHeader = rowHeader.createCell(10);
		cellHeader.setCellValue("V50");
		cellHeader = rowHeader.createCell(11);
		cellHeader.setCellValue("N60");
		cellHeader = rowHeader.createCell(12);
		cellHeader.setCellValue("V60");
		cellHeader = rowHeader.createCell(13);
		cellHeader.setCellValue("N70");
		cellHeader = rowHeader.createCell(14);
		cellHeader.setCellValue("V70");
		cellHeader = rowHeader.createCell(15);
		cellHeader.setCellValue("N80");
		cellHeader = rowHeader.createCell(16);
		cellHeader.setCellValue("V80");
		cellHeader = rowHeader.createCell(17);
		cellHeader.setCellValue("Standar Deviasi");
		
		int iRow = 1; 
		XSSFRow rowData = null;
		XSSFCell cellValue = null;
		for(int p : hashProvince.keySet())
		{
			ProvinceN prov = hashProvince.get(p);
			if(prov.getHashClusterN().keySet().size()>0) 
			{
				System.out.print(prov.getTitle());
				rowData = worksheet.createRow(iRow);
				cellValue = rowData.createCell(0);
				cellValue.setCellValue(prov.getTitle());
			}
			
			for(String clusterKey : prov.getHashClusterN().keySet())//province
			{				
				if(prov.getHashClusterN().get(clusterKey).size()>0)//cluster
				{					
					for(Integer year : prov.getHashClusterN().get(clusterKey).get(20).keySet())
					{
						cellValue = rowData.createCell(1);
						cellValue.setCellValue(clusterKey);
						
						cellValue = rowData.createCell(2);
						cellValue.setCellValue(year);
						
						VolumeStatistic vstats20 = prov.getVolume(clusterKey, year, "20");
						int n20 = prov.getHashN(clusterKey, year, "20").size();
						double v20  = prov.getHashV(clusterKey, year, "20").size()==0? 0: vstats20.getTotalV();
						
						VolumeStatistic vstats30 = prov.getVolume(clusterKey, year, "30");
						int n30 = prov.getHashN(clusterKey, year, "30").size();
						double v30  = prov.getHashV(clusterKey, year, "30").size()==0? 0: vstats30.getTotalV();
						
						VolumeStatistic vstats40 = prov.getVolume(clusterKey, year, "40");
						int n40 = prov.getHashN(clusterKey, year, "40").size();
						double v40  = prov.getHashV(clusterKey, year, "40").size()==0? 0: vstats40.getTotalV();
						
						VolumeStatistic vstats50 = prov.getVolume(clusterKey, year, "50");
						int n50 = prov.getHashN(clusterKey, year, "50").size();
						double v50  = prov.getHashV(clusterKey, year, "50").size()==0? 0: vstats50.getTotalV();
						
						VolumeStatistic vstats60 = prov.getVolume(clusterKey, year, "60");
						int n60 = prov.getHashN(clusterKey, year, "60").size();
						double v60  = prov.getHashV(clusterKey, year, "60").size()==0? 0: vstats60.getTotalV();
						
						VolumeStatistic vstats70 = prov.getVolume(clusterKey, year, "70");
						int n70 = prov.getHashN(clusterKey, year, "70").size();
						double v70  = prov.getHashV(clusterKey, year, "70").size()==0? 0: vstats70.getTotalV();
						
						VolumeStatistic vstats80 = prov.getVolume(clusterKey, year, "80");
						int n80 = prov.getHashN(clusterKey, year, "80").size();
						double v80  = prov.getHashV(clusterKey, year, "80").size()==0? 0: vstats80.getTotalV();
						
						VolumeStatistic vstats = prov.getStandarDeviation(clusterKey, year);
						
						cellValue = rowData.createCell(3);
						cellValue.setCellValue(n20);
						cellValue = rowData.createCell(4);
						cellValue.setCellValue(v20);
						
						cellValue = rowData.createCell(5);
						cellValue.setCellValue(n30);
						cellValue = rowData.createCell(6);
						cellValue.setCellValue(v30);
						
						cellValue = rowData.createCell(7);
						cellValue.setCellValue(n40);
						cellValue = rowData.createCell(8);
						cellValue.setCellValue(v40);
						
						cellValue = rowData.createCell(9);
						cellValue.setCellValue(n50);
						cellValue = rowData.createCell(10);
						cellValue.setCellValue(v50);
						
						cellValue = rowData.createCell(11);
						cellValue.setCellValue(n60);
						cellValue = rowData.createCell(12);
						cellValue.setCellValue(v60);
						
						cellValue = rowData.createCell(13);
						cellValue.setCellValue(n70);
						cellValue = rowData.createCell(14);
						cellValue.setCellValue(v70);
						
						cellValue = rowData.createCell(15);
						cellValue.setCellValue(n80);
						cellValue = rowData.createCell(16);
						cellValue.setCellValue(v80);						
						
						cellValue = rowData.createCell(17);
						cellValue.setCellValue(vstats.getStandardDeviation());
						
						//prepare new row
						iRow++;
						rowData = worksheet.createRow(iRow);
						cellValue = rowData.createCell(0);
						cellValue.setCellValue(prov.getTitle());
					}
				}
			}
		}
		workbook.write(fileOutputStream);
	}
	
	
	//@Test
	public void testTahun()
	{
		int tahun;
		Assert.assertEquals(1990, findTahun(1));
		Assert.assertEquals(1990, findTahun(14));
		Assert.assertEquals(1991, findTahun(15));
		Assert.assertEquals(1991, findTahun(28));
		Assert.assertEquals(1992, findTahun(29));
		Assert.assertEquals(1992, findTahun(42));
		
	}
	
	private int findTahun(int kolom) {
		if(kolom > 14){
			return 1990 + mapKolom(kolom);	
		}		
		return 1990;
	}

	private int mapKolom(int kolom) {
		return (kolom/14) +  (kolom % 14) - 1;
	}

	@Test
	public void testReportingNvUsingCollectSTEP2() throws URISyntaxException
	{	
		try {
			URI uri = new URI("file:///C:/Users/User/Documents/ReportNV-SE.xlsx");
			URI uriOutput = new URI("file:///C:/Users/User/Documents/ReportNV-SE.xlsx");
			FileInputStream fileInputStream = new FileInputStream(uri.getPath());
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet worksheet = workbook.getSheet("Report NV Per Year");
			int rowNum = 0;
			Iterator<Row> iter = worksheet.rowIterator();
			

			
			while(iter.hasNext())
			{	
				rowNum++;
				Row row = iter.next();
				
				if(rowNum==3)
				{
					short iheaderKolom;
					Cell cellHeader;
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N20");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V20");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N30");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V30");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N40");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V40");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N50");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V50");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N60");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V60");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N70");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V70");
					
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("N80");
					iheaderKolom = row.getLastCellNum();
					cellHeader = row.createCell(iheaderKolom); 
					cellHeader.setCellType(Cell.CELL_TYPE_STRING);
					cellHeader.setCellValue("V80");
				}
				//clusterkey
				String clusterKey = null;
				int intValue = 0;
				
				Cell cell = row.getCell(0);
				if(cell!=null) cell.setCellType(Cell.CELL_TYPE_STRING);
				try
				{
					clusterKey = cell.getStringCellValue();
					intValue = Integer.parseInt(clusterKey);
				}catch(Exception ex)
				{
					continue;
				}				
				System.out.println(clusterKey);
				
				
				//N & V value
				
				//menyimpan nilai terkini dari N & V
				double latestN20 = -1, latestV20 = -1, latestN30 = -1, latestV30 = -1, latestN40 = -1, latestV40 = -1;
				double latestN50 = -1, latestV50 = -1, latestN60 = -1, latestV60 = -1, latestN70 = -1, latestV70 = -1;
				double latestN80 = -1, latestV80 = -1;
				int latestYear = -1;
				
				//menyimpan nilai sebelumnya dari latest 
				double prevN20 = -1, prevV20 = -1, prevN30 = -1, prevV30 = -1, prevN40 = -1, prevV40 = -1;
				double prevN50 = -1, prevV50 = -1, prevN60 = -1, prevV60 = -1, prevN70 = -1, prevV70 = -1;
				double prevN80 = -1, prevV80 = -1;
				int prevYear = -1;
				
				
				
				for(int iyear = 1990; iyear <= 2012; iyear++)
				{
					int startKolom;
					if(iyear == 1990) {
						startKolom = 1;
					}else {
						startKolom = (14 * (iyear - 1990)) + 1;
					}
					
					String strValue = "";
					Cell cellValue = row.getCell(startKolom);
					if(cellValue!=null) 
					{
						cellValue.setCellType(Cell.CELL_TYPE_STRING);
						strValue = cellValue.getStringCellValue();
					}
					
					
					if(strValue.equals(""))
					{
						//next year
						System.out.println("\t" + iyear + " KOSONG, CHECKT NEXT YEAR");
						continue;
					}else{
						
						if(latestN20!=-1)
						{
							//save latest to prev
							prevYear = latestYear;
							prevN20 = latestN20;
							prevV20 = latestV20;
							
							prevN30 = latestN30;
							prevV30 = latestV30;
							
							prevN40 = latestN40;
							prevV40 = latestV40;
							
							prevN50 = latestN50;
							prevV50 = latestV50;
							
							prevN60 = latestN60;
							prevV60 = latestV60;
							
							prevN70 = latestN70;
							prevV70 = latestV70;
							
							prevN80 = latestN80;
							prevV80 = latestV80;
						} 
						
						// new value for latest
						Cell cellNV;
						double nv;  
						
						latestYear = iyear;
						cellNV = row.getCell(startKolom); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN20 = nv;
						cellNV = row.getCell(startKolom+1);
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV20 = nv;
						
						cellNV = row.getCell(startKolom+2);
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN30 = nv;
						cellNV = row.getCell(startKolom+3);
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV30 = nv;
						
						cellNV = row.getCell(startKolom+4); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN40 = nv;
						cellNV = row.getCell(startKolom+5); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV40 = nv;
						
						cellNV = row.getCell(startKolom+6); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN50 = nv;
						cellNV = row.getCell(startKolom+7); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV50 = nv;
						
						cellNV = row.getCell(startKolom+8); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN60 = nv;
						cellNV = row.getCell(startKolom+9); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV60 = nv;
						
						cellNV = row.getCell(startKolom+10); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN70 = nv;
						cellNV = row.getCell(startKolom+11); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV70 = nv;
						
						cellNV = row.getCell(startKolom+12); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestN80 = nv;
						cellNV = row.getCell(startKolom+13); 
						cellNV.setCellType(Cell.CELL_TYPE_NUMERIC);
						nv = cellNV.getNumericCellValue();
						latestV80 = nv;						
					}
				}
				
				
				
				//output the delta
				double deltaN20 = -1, deltaV20 = -1, deltaN30 = -1, deltaV30 = -1, deltaN40 = -1, deltaV40 = -1;
				double deltaN50 = -1, deltaV50 = -1, deltaN60 = -1, deltaV60 = -1, deltaN70 = -1, deltaV70 = -1;
				double deltaN80 = -1, deltaV80 = -1;
				
				
				if(latestYear == 2012 || prevYear == -1){
					// pakai data 2012
					deltaN20 = latestN20;
					deltaV20 = latestV20;
					deltaN30 = latestN30;
					deltaV30 = latestV30;
					deltaN40 = latestN40;
					deltaV40 = latestV40;
					deltaN50 = latestN50;
					deltaV50 = latestV50;
					deltaN60 = latestN60;
					deltaV60 = latestV60;
					deltaN70 = latestN70;
					deltaV70 = latestV70;
					deltaN80 = latestN80;
					deltaV80 = latestV80;
				}else{
					deltaN20 = (latestN20 - prevN20) / (latestYear - prevYear);
					deltaV20 = (latestV20 - prevV20) / (latestYear - prevYear);
					deltaN30 = (latestN30 - prevN30) / (latestYear - prevYear);
					deltaV30 = (latestV30 - prevV30) / (latestYear - prevYear);
					deltaN40 = (latestN40 - prevN40) / (latestYear - prevYear);
					deltaV40 = (latestV40 - prevV40) / (latestYear - prevYear);
					deltaN50 = (latestN50 - prevN50) / (latestYear - prevYear);
					deltaV50 = (latestV50 - prevV50) / (latestYear - prevYear);
					deltaN60 = (latestN60 - prevN60) / (latestYear - prevYear);
					deltaV60 = (latestV60 - prevV60) / (latestYear - prevYear);
					deltaN70 = (latestN70 - prevN70) / (latestYear - prevYear);
					deltaV70 = (latestV70 - prevV70) / (latestYear - prevYear);
					deltaN80 = (latestN80 - prevN80) / (latestYear - prevYear);
					deltaV80 = (latestV80 - prevV80) / (latestYear - prevYear);
				}
				
				int outputKolom;
				Cell cellOutput;
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN20);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV20);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN30);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV30);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN40);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV40);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN50);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV50);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN60);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV60);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN70);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV70);
				
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaN80);
				outputKolom = row.getLastCellNum();
				cellOutput = row.createCell(outputKolom); 
				cellOutput.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellOutput.setCellValue(deltaV80);
				
				System.out.println("\tprev year = " + prevYear + ", prev N70 " + prevN70 + " prev V70 " + prevV70);
				System.out.println("\tlatest year = " + latestYear + ", latest N70 " + latestN70 + " latest V70 " + latestV70);
			}
			
			FileOutputStream fileOutputStream = new FileOutputStream(uriOutput.getPath());
			workbook.write(fileOutputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String formatTwoDigits(double d) {  
		DecimalFormat fmt = new DecimalFormat("0.00");  
		String string = fmt.format(d);  
		return string;  
	}  
	
	public List<String> extractValues(Node<?> axis,String attributeName) {
		if ( axis == null ) {
			throw new NullPointerException("Axis must be non-null");
		} else if ( axis instanceof Entity ) {
			Attribute<?,?> attr = (Attribute<?, ?>) ((Entity) axis).get(attributeName, 0);
			if ( attr == null ) {
				return Arrays.asList(""); 
			} else {
				Field<?> fld = attr.getField(0);
				Object v = fld.getValue();
				return Arrays.asList(v == null ? "" : v.toString());
			}
		} else {
			throw new UnsupportedOperationException("Axis must be an Entity");
		}
	}
	
	
}
