package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;

public class RandomTableOrderHint extends TableOrderHint
{
  @Override
  public TableOrderComparatorFactory getValue()
  {
    return () -> Comparator.comparingInt(System::identityHashCode);
  }
}
