package test.idnfi.reporting;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

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
import static org.openforis.idreporting.persistence.jooq.tables.SchemaClusterPermanentplotaPlotaenumTreeshigherthan20cm.SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM;
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
	
	@Test
	public void testSynchDb() throws RecordPersistenceException, InvalidExpressionException
	{
		DialectAwareJooqFactory jf = factoryDao.getJooqFactory();
		jf.delete(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM).execute();
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
								if(sbole_height.equals("")) sbole_height ="0";//TOFIX: what should be the empty value??/
								float bole_height = Float.parseFloat(sbole_height);//TOFIX : should this be double???
								
								int schemaClusterPermanentPlotA_plotaEnum_treehigher20cmId = jf.nextval(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREEHIGHER20CM_SEQ_ID).intValue();
								jf.insertInto(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM)
									.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM.ID, schemaClusterPermanentPlotA_plotaEnum_treehigher20cmId)
									.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM.PARENT_ID, schemaClusterPermanentPlotA_plotaEnumId)
									.set(SCHEMA_CLUSTER_PERMANENTPLOTA_PLOTAENUM_TREESHIGHERTHAN20CM.BOLE_HEIGHT, bole_height)
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
	public void testReportingNvUsingCollect() throws InvalidExpressionException, RecordPersistenceException
	{
		/*CollectSurvey survey = surveyDao.load("idnfi");
		Schema schema = survey.getSchema();
		String rootEntityName = "/cluster/permanent_plot_a/plota_enum";
		EntityDefinition defn = (EntityDefinition) schema.getByPath(rootEntityName);
		
		CollectRecord records = recordDao.load(survey, survey.getId(), 1);
		
		List<Node<?>> nodes = iterateExpression(rootEntityName,records );
		System.out.println(nodes.size());*/
		
		String rootEntityName = "/cluster/permanent_plot_a/plota_enum";
		CollectSurvey survey = surveyDao.load("idnfi");
		List<CollectRecord> records = recordManager.loadSummaries(survey, "cluster", 0, Integer.MAX_VALUE, (List<RecordSummarySortField>) null, (String) null);
		
		// mencari berapa cluster yg sudah diinputkan pada masing2 tahun
		// V = (0.25 * 3.14 * d^2 * bole_height * 0.56) / 10.000
		
		HashMap<Integer, ProvinceN> hashProvince = new HashMap<Integer, ProvinceN>();
		int i=0;
		//System.out.println("collectRecord size = " + records.size());
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
		
		System.out.println("i =" + i);
		System.out.println("Provinsi;Cluster;Tahun;N20;V20;N30;V30;N40;V40;N50;V50;N60;V60;N70;V70;N80;V80");
		for(int p : hashProvince.keySet())
		{
			ProvinceN prov = hashProvince.get(p);
			if(prov.getHashClusterN().keySet().size()>0) 
			{
				System.out.print(prov.getTitle());
			}
			
			for(String clusterKey : prov.getHashClusterN().keySet())//province
			{				
				if(prov.getHashClusterN().get(clusterKey).size()>0)//cluster
				{					
					for(Integer year : prov.getHashClusterN().get(clusterKey).get(20).keySet())
					{
						System.out.println(";" + clusterKey + ";" + year + ";" + 
							prov.getHashN(clusterKey, year, "20").size() + ";" + (prov.getHashV(clusterKey, year, "20").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "20").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "30").size() + ";" + (prov.getHashV(clusterKey, year, "30").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "30").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "40").size() + ";" + (prov.getHashV(clusterKey, year, "40").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "40").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "50").size() + ";" + (prov.getHashV(clusterKey, year, "50").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "50").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "60").size() + ";" + (prov.getHashV(clusterKey, year, "60").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "60").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "70").size() + ";" + (prov.getHashV(clusterKey, year, "70").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "70").get(0))) + ";" +
							prov.getHashN(clusterKey, year, "80").size() + ";" + (prov.getHashV(clusterKey, year, "80").size()==0?"0": formatTwoDigits(prov.getHashV(clusterKey, year, "80").get(0)))
						);
					}
				}
			}
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
