package de.akquinet.jbosscc.guttenbase.utils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ScriptExecutorProgressIndicatorPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private final JTextField _totalTime;
  private final JTextField _scriptTime;
  private final JProgressBar _totalProgress;
  private final JTextArea _messages;

  /**
   * Create the panel.
   */
  public ScriptExecutorProgressIndicatorPanel() {
    final GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);

    final JLabel lblTotalTimeElapsed = new JLabel("Total time elapsed");
    lblTotalTimeElapsed.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblTotalTimeElapsed = new GridBagConstraints();
    gbc_lblTotalTimeElapsed.anchor = GridBagConstraints.WEST;
    gbc_lblTotalTimeElapsed.insets = new Insets(5, 5, 5, 5);
    gbc_lblTotalTimeElapsed.gridx = 0;
    gbc_lblTotalTimeElapsed.gridy = 1;
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
    gbc_totalTime.gridy = 1;
    add(_totalTime, gbc_totalTime);

    final JLabel lblTableTimeElapsed = new JLabel("Statement time elapsed");
    lblTableTimeElapsed.setFont(new Font("Tahoma", Font.BOLD, 12));
    final GridBagConstraints gbc_lblTableTimeElapsed = new GridBagConstraints();
    gbc_lblTableTimeElapsed.anchor = GridBagConstraints.WEST;
    gbc_lblTableTimeElapsed.insets = new Insets(5, 5, 5, 5);
    gbc_lblTableTimeElapsed.gridx = 3;
    gbc_lblTableTimeElapsed.gridy = 1;
    add(lblTableTimeElapsed, gbc_lblTableTimeElapsed);

    _scriptTime = new JTextField();
    _scriptTime.setEditable(false);
    _scriptTime.setColumns(10);
    final GridBagConstraints gbc_tableTime = new GridBagConstraints();
    gbc_tableTime.insets = new Insets(0, 0, 5, 5);
    gbc_tableTime.weightx = 1.0;
    gbc_tableTime.gridwidth = 1;
    gbc_tableTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_tableTime.anchor = GridBagConstraints.WEST;
    gbc_tableTime.gridx = 4;
    gbc_tableTime.gridy = 1;
    add(_scriptTime, gbc_tableTime);

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
    gbc_panel_1.gridy = 2;
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
    gbc_panel_2.gridy = 3;
    add(panel_2, gbc_panel_2);
    panel_2.setLayout(new BorderLayout(0, 0));

    final JScrollPane scrollPane = new JScrollPane();
    panel_2.add(scrollPane, BorderLayout.CENTER);

    _messages = new JTextArea();
    _messages.setRows(20);
    scrollPane.setViewportView(_messages);
  }

  public final JTextField getTotalTime() {
    return _totalTime;
  }

  public final JTextField getScriptTime() {
    return _scriptTime;
  }

  public final JProgressBar getTotalProgress() {
    return _totalProgress;
  }

  public final JTextArea getMessages() {
    return _messages;
  }
}
