package test.idnfi.reporting;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.RecordSummarySortField;
import org.openforis.collect.persistence.RecordDao;
import org.openforis.collect.persistence.RecordPersistenceException;
import org.openforis.collect.persistence.SurveyDao;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.Schema;
import org.openforis.idm.metamodel.SurveyContext;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.CodeAttribute;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Field;
import org.openforis.idm.model.Node;
import org.openforis.idm.model.RealValue;
import org.openforis.idm.model.Record;
import org.openforis.idm.model.expression.AbsoluteModelPathExpression;
import org.openforis.idm.model.expression.ExpressionFactory;
import org.openforis.idm.model.expression.InvalidExpressionException;
import org.openforis.idm.model.expression.ModelPathExpression;
import org.openforis.idm.model.expression.internal.MissingValueException;
import org.openforis.idm.transform.DataTransformation;
import org.openforis.idm.transform.csv.ModelCsvWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/ImportIdm-context.xml" })
@TransactionConfiguration(defaultRollback = false)
/**
 * @author Wibowo, Eko
 */
@Transactional
public class ReportingNV {

	@Autowired
	protected SurveyDao surveyDao;
	
	@Autowired
	protected RecordDao recordDao;
	
	@Autowired
	protected RecordManager recordManager;

	@Test
	public void testReportingNV() throws InvalidExpressionException, RecordPersistenceException
	{
		CollectSurvey survey = surveyDao.load("idnfi");
		Schema schema = survey.getSchema();
		String rootEntityName = "/cluster/permanent_plot_a/plota_enum";
		EntityDefinition defn = (EntityDefinition) schema.getByPath(rootEntityName);
		
		CollectRecord records = recordDao.load(survey, survey.getId(), 1);
		
		List<Node<?>> nodes = iterateExpression(rootEntityName,records );
		System.out.println(nodes.size());
		
		
		
		List<CollectRecord> summaries = recordManager.loadSummaries(survey, "cluster", 0, Integer.MAX_VALUE, (List<RecordSummarySortField>) null, (String) null);
		for (CollectRecord s : summaries) {
			ExpressionFactory expressionFactory = s.getSurveyContext().getExpressionFactory();
			AbsoluteModelPathExpression expression = expressionFactory.createAbsoluteModelPathExpression(rootEntityName);
			CollectRecord record = recordManager.load(survey, s.getId(), 1);
			try 
			{
				List<Node<?>> rowNodes = expression.iterate(record);
				for(Node<? extends NodeDefinition> n :rowNodes)
				{	
					String strD = extractValues(n, "dbb_or_b").get(0);
					double d;
					if(!"".equals(strD))
					{
						d = Double.parseDouble(strD);
						System.out.println(n.getName() + " = " + d);
					}
				}
			}
			catch(MissingValueException ex)
			{
				ex.printStackTrace();
			}
			
		}
	}
	
	private List<Node<?>> iterateExpression(String expr, Record record) throws InvalidExpressionException {
		ExpressionFactory expressionFactory = record.getSurveyContext().getExpressionFactory();
		AbsoluteModelPathExpression expression = expressionFactory.createAbsoluteModelPathExpression(expr);
		List<Node<?>> l = expression.iterate(record);
		return l;
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
