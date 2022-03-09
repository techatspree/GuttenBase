package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;

import java.util.Comparator;

public class RandomTableOrderHint extends TableOrderHint {
  @Override
  public TableOrderComparatorFactory getValue() {
    return () -> Comparator.comparingInt(System::identityHashCode);
  }
}
