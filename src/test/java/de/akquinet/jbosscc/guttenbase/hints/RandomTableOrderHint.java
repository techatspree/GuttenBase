package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class RandomTableOrderHint extends TableOrderHint
{
  @Override
  public TableOrderComparatorFactory getValue()
  {
    return new TableOrderComparatorFactory()
    {
      @Override
      public Comparator<TableMetaData> createComparator()
      {
        return new Comparator<TableMetaData>()
        {
          @Override
          public int compare(final TableMetaData o1, final TableMetaData o2)
          {
            return System.identityHashCode(o1) - System.identityHashCode(o2);
          }
        };
      }
    };
  }
}
