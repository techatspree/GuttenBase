package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Created by mfehler on 26.04.17.
 */
public class TestTableNameFilterHint extends RepositoryTableFilterHint {
    @Override
    public RepositoryTableFilter getValue() {
        return table -> {
            final String tableCase = table.getTableName();
            return tableCase.startsWith("TAB")||(tableCase.startsWith("P"))||(tableCase.startsWith("O"))||(tableCase.startsWith("C"))
                    ||(tableCase.startsWith("FOO"))||(tableCase.startsWith("E"));
        };
    }
}
