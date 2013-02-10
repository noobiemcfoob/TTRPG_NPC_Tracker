import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


abstract public class GUIElem {
	/*
	 * Processing variables
	 */
	static public ArrayList<String> allSkills;	//Holds array of all Skill names
	//Holds array of JTextFields for capturing stunts.
	//Every 2 items is a captured stunt: 1->Stunt Name;2->Stunt Cost
	static public ArrayList<JTextField> char_stuntManager; 
	
	//Holds array of JTextFields for capturing aspects.
	//Every 2 items is a captured stunt: 1->Aspect Name;2->Stunt Aspect Notes
	static public ArrayList<JTextField> char_aspectFieldManager; 
	static public ArrayList<JTextArea>	char_aspectAreaManager;
	
	/*
	 * GUI ELEMENTS for mainWindow
	 */
	protected JFrame mainWindow = new JFrame();
	protected JPanel pcPanel = new JPanel();
	protected JPanel npcPanel = new JPanel();
	protected JPanel southPanel = new JPanel();
	protected JPanel rightPanel = new JPanel();
	protected JMenuBar menuBar = new JMenuBar();
	
	//menuBar items
	protected JButton newChar = new JButton("Char+");
	protected JMenu testMenu = new JMenu("File");
	protected JMenuItem testMenuItem = new JMenuItem("Test");
	
	//southPanel Items
	protected ButtonGroup modeGroup = new ButtonGroup();
	protected JRadioButton comRButton = new JRadioButton("Combat");
	protected JRadioButton socRButton = new JRadioButton("Social");
	protected ButtonGroup listGroup = new ButtonGroup();
	protected JRadioButton pcRButton = new JRadioButton("PCs Only");
	protected JRadioButton npcRButton = new JRadioButton("NPCs Only");
	protected JRadioButton allRButton = new JRadioButton("Show All");
	
	//rightPanel items
	protected JButton testButton = new JButton("Test");
	
	/*
	 * GUI Items for add Character window
	 */
	protected JMenuBar charMenuBar;
	protected JMenu charTemplates;
	protected ArrayList<JMenuItem> charTempItems; 
	protected JFrame charWindow;
	protected JPanel char_skillPanel;
	protected JScrollPane char_skillScroll;
	protected JScrollPane char_aspectScroll;
	protected JPanel char_northPanel;
	protected JPanel char_centerPanel;
	protected JButton createChar = new JButton("Create Character");
	protected JButton tempChar = new JButton("Create Template");
	protected JButton updateChar = new JButton("Update Character");
	protected JButton deleteChar = new JButton("Delete");
	protected JPanel char_westPanel;
	protected JPanel char_stuntPanel;
	protected JPanel char_notesPanel;
	protected JPanel char_inventoryPanel;
	protected JScrollPane char_westScroll;
	
	//northPanel items
	protected JTextField char_nameField = new JTextField("Name",25);
	protected JTextField char_allyField = new JTextField("None",25);
	protected JTextField char_skillCapField = new JTextField("5",3);
	protected JTextField char_skillPointsField = new JTextField("30",3);
	protected JTextField char_refreshField = new JTextField("7",3);
	protected JTextField char_conceptField = new JTextField("None",25);
	protected ButtonGroup char_typeGroup = new ButtonGroup();
	protected JRadioButton char_pcRButton = new JRadioButton("PC");
	protected JRadioButton char_npcRButton = new JRadioButton("NPC");
	protected JCheckBox	char_revisitBox = new JCheckBox("Revisit?");
	
	//westPanel items
	protected Integer char_stuntGridY = 0;
	protected JButton char_stuntAdd = new JButton("Add Additional Stunt");
	protected JPanel char_conPanel = new JPanel();
	protected ArrayList<JTextField> consequences = new ArrayList<JTextField>(4);
	protected JPanel char_stressPanel = new JPanel();
	protected ArrayList<JTextField> stresses = new ArrayList<JTextField>();
	static protected ArrayList<String> stressNames = new ArrayList<String>(Arrays.asList(
			"PHY",
			"MEN",
			"SOC",
			"ARM",
			"MSC"
		));
	
	//centerPanel items
	protected JPanel char_aspectPanel;
	protected Integer char_aspectGridY = 0;
	protected JButton char_aspectAdd = new JButton("Add Additional Aspect");
	protected JTextArea char_inventory = new JTextArea(10,13);
	protected JTextArea char_notes = new JTextArea(10,13);
	
	//skillPanel items
	protected HashMap<String, JTextField> char_SkillFields;
	
	protected void addStunt_AddChar() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = char_stuntGridY;
		c.gridx = 0;
		JTextField temp = new JTextField(20);
		
		char_stuntManager.add(temp);
		char_stuntPanel.add(temp,c);
		c.gridx+=2;
		temp = new JTextField(3);
		char_stuntManager.add(temp);
		char_stuntPanel.add(temp,c);
		c.gridx=0;
		temp = new JTextField(20);
		c.gridy++;
		
		char_stuntGridY = c.gridy;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		char_stuntPanel.add(char_stuntAdd,c);
		charWindow.setVisible(true);
	}

	protected void addAspect_AddChar() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = char_aspectGridY;
		
		JTextField aspectField = new JTextField(30);
		aspectField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		JTextArea aspectArea = new JTextArea(10,50);
		aspectArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		char_aspectPanel.add(new JLabel("Aspect Name:  "),c);
		c.gridx++;
		aspectField.setMinimumSize(aspectField.getPreferredSize());
		char_aspectPanel.add(aspectField, c);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		aspectArea.setMinimumSize(aspectArea.getPreferredSize());
		char_aspectPanel.add(aspectArea, c);
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NONE;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		char_aspectGridY = c.gridy;
		c.gridx++;
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		char_aspectPanel.add(char_aspectAdd,c);
		
		char_aspectFieldManager.add(aspectField);
		char_aspectAreaManager.add(aspectArea);
		
		charWindow.setVisible(true);
	}
}
