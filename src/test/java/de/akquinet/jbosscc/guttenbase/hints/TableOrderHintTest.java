package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Test a schema migration where table ordering is customized...
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TableOrderHintTest extends AbstractHintTest {
	public TableOrderHintTest() {
		super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
	}

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectorHint(TARGET, new TableOrderHint() {
			@Override
			public TableOrderComparatorFactory getValue() {
				return new TableOrderComparatorFactory() {
					@Override
					public Comparator<TableMetaData> createComparator() {
						return new Comparator<TableMetaData>() {
							@Override
							public int compare(final TableMetaData o1, final TableMetaData o2) {
								return o1.hashCode() - o2.hashCode(); // Just for demonstration purposes...
							}
						};
					}
				};
			}
		});
	}
}
