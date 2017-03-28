package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class RandomTableOrderHint extends TableOrderHint
{
  @Override
  public TableOrderComparatorFactory getValue()
  {
    return () -> Comparator.comparingInt(System::identityHashCode);
  }
}
