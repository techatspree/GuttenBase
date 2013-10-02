package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Test a schema migration where column ordering is customized...
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ColumnOrderHintTest extends AbstractHintTest {
	public ColumnOrderHintTest() {
		super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
	}

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectorHint(SOURCE, new ColumnOrderHint() {
			@Override
			public ColumnOrderComparatorFactory getValue() {
				return new ColumnOrderComparatorFactory() {
					@Override
					public Comparator<ColumnMetaData> createComparator() {
						return new Comparator<ColumnMetaData>() {
							@Override
							public int compare(final ColumnMetaData o1, final ColumnMetaData o2) {
								return o1.hashCode() - o2.hashCode(); // Just for demonstration purposes...
							}
						};
					}
				};
			}
		});
	}
}
