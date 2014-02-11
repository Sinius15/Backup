package program;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Gui extends JFrame {

	private static final long serialVersionUID = -6307933136193394753L;
	private ArrayList<JCheckBox> boxes = new ArrayList<>();
	private JPanel contentPane;
	public JButton button;
	private JPanel checkPanel;
	private JPanel infoPanel;
	private JProgressBar progressBar;
	private JTextArea status;

	public Gui(ActionListener action) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		setTitle("Backup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 304, 204);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmOpenConfig = new JMenuItem("Open Config");
		mntmOpenConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(Backup.configFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmOpenConfig);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{276, 0};
		gbl_contentPane.rowHeights = new int[] {0, 15, 140};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		checkPanel = new JPanel();
		GridBagConstraints gbc_checkPanel = new GridBagConstraints();
		gbc_checkPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_checkPanel.insets = new Insets(0, 0, 5, 0);
		gbc_checkPanel.gridx = 0;
		gbc_checkPanel.gridy = 1;
		contentPane.add(checkPanel, gbc_checkPanel);
		checkPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		infoPanel = new JPanel();
		GridBagConstraints gbc_infoPanel = new GridBagConstraints();
		gbc_infoPanel.fill = GridBagConstraints.BOTH;
		gbc_infoPanel.gridx = 0;
		gbc_infoPanel.gridy = 2;
		contentPane.add(infoPanel, gbc_infoPanel);
		infoPanel.setLayout(null);
		
		button = new JButton("Backup");
		button.setBounds(0, 0, 274, 27);
		infoPanel.add(button);
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		progressBar.setBounds(0, 29, 274, 15);
		infoPanel.add(progressBar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 46, 274, 88);
		infoPanel.add(scrollPane);
		
		status = new JTextArea();
		status.setEditable(false);
		scrollPane.setViewportView(status);
		button.addActionListener(action);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public boolean isCheckboxSelected(String txt){
		for(JCheckBox b : boxes)
			if(b.getText().startsWith(txt))
				return b.isSelected();
		return false;
	}
	
	public void addCheckBox(String txt){
		JCheckBox b = new JCheckBox(txt);
		boxes.add(b);
		checkPanel.add(b);
	}
	public JPanel getCheckPanel() {
		return checkPanel;
	}
	public JPanel getInfoPanel() {
		return infoPanel;
	}
	public void setStatus(String text) {
		status.setText(text);;
	}
	public JButton getBtnBackup() {
		return button;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
