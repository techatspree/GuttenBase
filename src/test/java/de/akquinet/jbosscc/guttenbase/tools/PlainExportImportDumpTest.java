package de.akquinet.jbosscc.guttenbase.tools;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.export.ExporterFactory;
import de.akquinet.jbosscc.guttenbase.export.ImporterFactory;
import de.akquinet.jbosscc.guttenbase.export.plain.PlainGzipExporter;
import de.akquinet.jbosscc.guttenbase.export.plain.PlainGzipImporter;
import de.akquinet.jbosscc.guttenbase.hints.ExporterFactoryHint;
import de.akquinet.jbosscc.guttenbase.hints.ImporterFactoryHint;

public class PlainExportImportDumpTest extends AbstractExportImportDumpTest {
	@Before
	public final void setupExport() {
		_connectorRepository.addConnectorHint(EXPORT, new ExporterFactoryHint() {
			@Override
			public ExporterFactory getValue() {
				return PlainGzipExporter::new;
			}
		});

		_connectorRepository.addConnectorHint(IMPORT, new ImporterFactoryHint() {
			@Override
			public ImporterFactory getValue() {
				return PlainGzipImporter::new;
			}
		});
	}
}
