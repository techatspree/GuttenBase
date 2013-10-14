package de.akquinet.jbosscc.guttenbase.utils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TableCopyProgressIndicatorPanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  private final JTextField _sourceTable;
  private final JTextField _targetTable;
  private final JTextField _totalTime;
  private final JTextField _tableTime;
  private final JProgressBar _totalProgress;
  private final JProgressBar _tableProgress;
  private final JTextArea _messages;

  /**
   * Create the panel.
   */
  public TableCopyProgressIndicatorPanel()
  {
    final GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    final JLabel lblNewLabel = new JLabel("Source table");
    lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
    gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 1;
    add(lblNewLabel, gbc_lblNewLabel);

    _sourceTable = new JTextField();
    _sourceTable.setEditable(false);
    final GridBagConstraints gbc_sourceTable = new GridBagConstraints();
    gbc_sourceTable.insets = new Insets(0, 0, 5, 5);
    gbc_sourceTable.gridwidth = 2;
    gbc_sourceTable.fill = GridBagConstraints.HORIZONTAL;
    gbc_sourceTable.anchor = GridBagConstraints.WEST;
    gbc_sourceTable.gridx = 1;
    gbc_sourceTable.gridy = 1;
    add(_sourceTable, gbc_sourceTable);
    _sourceTable.setColumns(40);

    final JLabel lblTargetTable = new JLabel("Target table");
    lblTargetTable.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblTargetTable = new GridBagConstraints();
    gbc_lblTargetTable.anchor = GridBagConstraints.WEST;
    gbc_lblTargetTable.insets = new Insets(5, 5, 5, 5);
    gbc_lblTargetTable.gridx = 3;
    gbc_lblTargetTable.gridy = 1;
    add(lblTargetTable, gbc_lblTargetTable);

    _targetTable = new JTextField();
    _targetTable.setEditable(false);
    _targetTable.setColumns(40);
    final GridBagConstraints gbc_targetTable = new GridBagConstraints();
    gbc_targetTable.insets = new Insets(0, 0, 5, 0);
    gbc_targetTable.weightx = 1.0;
    gbc_targetTable.anchor = GridBagConstraints.WEST;
    gbc_targetTable.gridwidth = 2;
    gbc_targetTable.fill = GridBagConstraints.HORIZONTAL;
    gbc_targetTable.gridx = 4;
    gbc_targetTable.gridy = 1;
    add(_targetTable, gbc_targetTable);

    final JLabel lblTotalTimeElapsed = new JLabel("Total time elapsed");
    lblTotalTimeElapsed.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblTotalTimeElapsed = new GridBagConstraints();
    gbc_lblTotalTimeElapsed.anchor = GridBagConstraints.WEST;
    gbc_lblTotalTimeElapsed.insets = new Insets(5, 5, 5, 5);
    gbc_lblTotalTimeElapsed.gridx = 0;
    gbc_lblTotalTimeElapsed.gridy = 2;
    add(lblTotalTimeElapsed, gbc_lblTotalTimeElapsed);

    _totalTime = new JTextField();
    _totalTime.setEditable(false);
    _totalTime.setColumns(10);
    final GridBagConstraints gbc_totalTime = new GridBagConstraints();
    gbc_totalTime.weightx = 1.0;
    gbc_totalTime.insets = new Insets(0, 0, 5, 5);
    gbc_totalTime.anchor = GridBagConstraints.WEST;
    gbc_totalTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_totalTime.gridx = 1;
    gbc_totalTime.gridy = 2;
    add(_totalTime, gbc_totalTime);

    final JLabel lblTableTimeElapsed = new JLabel("Table time elapsed");
    lblTableTimeElapsed.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblTableTimeElapsed = new GridBagConstraints();
    gbc_lblTableTimeElapsed.anchor = GridBagConstraints.WEST;
    gbc_lblTableTimeElapsed.insets = new Insets(5, 5, 5, 5);
    gbc_lblTableTimeElapsed.gridx = 3;
    gbc_lblTableTimeElapsed.gridy = 2;
    add(lblTableTimeElapsed, gbc_lblTableTimeElapsed);

    _tableTime = new JTextField();
    _tableTime.setEditable(false);
    _tableTime.setColumns(10);
    final GridBagConstraints gbc_tableTime = new GridBagConstraints();
    gbc_tableTime.insets = new Insets(0, 0, 5, 5);
    gbc_tableTime.weightx = 1.0;
    gbc_tableTime.gridwidth = 1;
    gbc_tableTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_tableTime.anchor = GridBagConstraints.WEST;
    gbc_tableTime.gridx = 4;
    gbc_tableTime.gridy = 2;
    add(_tableTime, gbc_tableTime);

    final JPanel panel = new JPanel();
    panel.setBorder(new TitledBorder(new EtchedBorder(), "Table rows", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    final GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.weighty = 0.2;
    gbc_panel.gridwidth = 6;
    gbc_panel.anchor = GridBagConstraints.WEST;
    gbc_panel.insets = new Insets(5, 5, 5, 0);
    gbc_panel.fill = GridBagConstraints.BOTH;
    gbc_panel.gridx = 0;
    gbc_panel.gridy = 3;
    add(panel, gbc_panel);
    panel.setLayout(new BorderLayout(0, 0));

    _tableProgress = new JProgressBar();
    _tableProgress.setStringPainted(true);
    panel.add(_tableProgress, BorderLayout.CENTER);

    final JPanel panel_1 = new JPanel();
    panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Total progress",
        TitledBorder.LEADING, TitledBorder.TOP, null, null));
    final GridBagConstraints gbc_panel_1 = new GridBagConstraints();
    gbc_panel_1.weighty = 0.2;
    gbc_panel_1.anchor = GridBagConstraints.WEST;
    gbc_panel_1.gridwidth = 6;
    gbc_panel_1.insets = new Insets(5, 5, 5, 0);
    gbc_panel_1.fill = GridBagConstraints.BOTH;
    gbc_panel_1.gridx = 0;
    gbc_panel_1.gridy = 4;
    add(panel_1, gbc_panel_1);
    panel_1.setLayout(new BorderLayout(0, 0));

    _totalProgress = new JProgressBar();
    _totalProgress.setStringPainted(true);
    panel_1.add(_totalProgress, BorderLayout.CENTER);

    final JPanel panel_2 = new JPanel();
    panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Messages", TitledBorder.LEADING,
        TitledBorder.TOP, null, null));
    final GridBagConstraints gbc_panel_2 = new GridBagConstraints();
    gbc_panel_2.weighty = 1.0;
    gbc_panel_2.weightx = 1.0;
    gbc_panel_2.gridheight = 2;
    gbc_panel_2.gridwidth = 6;
    gbc_panel_2.insets = new Insets(5, 5, 5, 5);
    gbc_panel_2.fill = GridBagConstraints.BOTH;
    gbc_panel_2.gridx = 0;
    gbc_panel_2.gridy = 5;
    add(panel_2, gbc_panel_2);
    panel_2.setLayout(new BorderLayout(0, 0));

    final JScrollPane scrollPane = new JScrollPane();
    panel_2.add(scrollPane, BorderLayout.CENTER);

    _messages = new JTextArea();
    _messages.setRows(20);
    scrollPane.setViewportView(_messages);
  }

  public final JTextField getSourceTable()
  {
    return _sourceTable;
  }

  public final JTextField getTargetTable()
  {
    return _targetTable;
  }

  public final JTextField getTotalTime()
  {
    return _totalTime;
  }

  public final JTextField getTableTime()
  {
    return _tableTime;
  }

  public final JProgressBar getTotalProgress()
  {
    return _totalProgress;
  }

  public final JProgressBar getTableProgress()
  {
    return _tableProgress;
  }

  public final JTextArea getMessages()
  {
    return _messages;
  }
}
