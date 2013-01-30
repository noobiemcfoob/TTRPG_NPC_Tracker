import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


abstract public class GUIElem {
	/*
	 * Processing variables
	 */
	static public ArrayList<String> allSkills;	//Holds array of all Skill names
	//Holds array of JTextFields for capturing stunts.
	//Every 2 items is a captured stunt: 1->Stunt Name;2->Stunt Cost
	static public ArrayList<JTextField> addChar_stuntManager; 
	
	//Holds array of JTextFields for capturing aspects.
	//Every 2 items is a captured stunt: 1->Aspect Name;2->Stunt Aspect Notes
	static public ArrayList<JTextField> addChar_aspectFieldManager; 
	static public ArrayList<JTextArea>	addChar_aspectAreaManager;
	
	/*
	 * GUI ELEMENTS for mainWindow
	 */
	protected JFrame mainWindow = new JFrame();
	protected JPanel charPanel = new JPanel();
	protected JPanel southPanel = new JPanel();
	protected JPanel rightPanel = new JPanel();
	protected JMenuBar menuBar = new JMenuBar();
	
	//menuBar itmes
	protected JButton newChar = new JButton("+Char");
	protected JMenu testMenu = new JMenu("File");
	protected JMenuItem testMenuItem = new JMenuItem("Test");
	
	//southPanel Items
	protected ButtonGroup modeGroup = new ButtonGroup();
	protected JRadioButton comRButton = new JRadioButton("Combat");
	protected JRadioButton socRButton = new JRadioButton("Social");
	
	//rightPanel items
	protected JButton testButton = new JButton("Test");
	
	/*
	 * GUI Items for add Character window
	 */
	protected JFrame addCharWindow = new JFrame();
	protected JPanel addChar_skillPanel = new JPanel();
	protected JScrollPane addChar_skillScroll = new JScrollPane(addChar_skillPanel);
	protected JPanel addChar_aspectPanel = new JPanel();
	protected JScrollPane addChar_aspectScroll = new JScrollPane(addChar_aspectPanel);
	protected JPanel addChar_northPanel = new JPanel(new GridLayout(2,2));
	protected JButton createChar = new JButton("Create Character");
	protected JPanel addChar_westPanel = new JPanel(new GridLayout(2,1));
	protected JPanel addChar_stuntPanel = new JPanel();
	protected JPanel addChar_inventoryPanel = new JPanel();
	protected JScrollPane addChar_westScroll = new JScrollPane(addChar_westPanel);
	
	//northPanel items
	protected JTextField addChar_nameField = new JTextField(25);
	protected JTextField addChar_allyField = new JTextField(25);
	protected JTextField addChar_skillCapField = new JTextField(3);
	protected JTextField addChar_skillPointsField = new JTextField(3);
	protected JTextField addChar_refreshField = new JTextField(3);
	protected JTextField addChar_conceptField = new JTextField(25);
	protected ButtonGroup addChar_typeGroup = new ButtonGroup();
	protected JRadioButton addChar_pcRButton = new JRadioButton("PC");
	protected JRadioButton addChar_npcRButton = new JRadioButton("NPC");
	protected JCheckBox	addChar_revisitBox = new JCheckBox("Revisit?");
	
	//westPanel items
	protected Integer addChar_stuntGridY = 0;
	protected JButton addChar_stuntAdd = new JButton("Add Additional Stunt");
	protected JTextArea addChar_inventory = new JTextArea(10,26);
	
	//aspectPanel items
	protected Integer addChar_aspectGridY = 0;
	protected JButton addChar_aspectAdd = new JButton("Add Additional Aspect");
	
	//skillPanel items
	protected HashMap<String, JTextField> addChar_SkillFields;
	
	protected void addStunt_AddChar() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = addChar_stuntGridY;
		c.gridx = 0;
		JTextField temp = new JTextField(20);
		
		addChar_stuntManager.add(temp);
		addChar_stuntPanel.add(temp,c);
		c.gridx+=2;
		temp = new JTextField(3);
		addChar_stuntManager.add(temp);
		addChar_stuntPanel.add(temp,c);
		c.gridx=0;
		temp = new JTextField(20);
		c.gridy++;
		
		addChar_stuntGridY = c.gridy;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		addChar_stuntPanel.add(addChar_stuntAdd,c);
		addCharWindow.setVisible(true);
	}

	protected void addAspect_AddChar() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = addChar_aspectGridY;
		
		JTextField aspectField = new JTextField(30);
		aspectField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		JTextArea aspectArea = new JTextArea(10,50);
		aspectArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		addChar_aspectPanel.add(new JLabel("Aspect Name:  "),c);
		c.gridx++;
		aspectField.setMinimumSize(aspectField.getPreferredSize());
		addChar_aspectPanel.add(aspectField, c);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		aspectArea.setMinimumSize(aspectArea.getPreferredSize());
		addChar_aspectPanel.add(aspectArea, c);
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NONE;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		addChar_aspectGridY = c.gridy;
		c.gridx++;
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		addChar_aspectPanel.add(addChar_aspectAdd,c);
		
		addChar_aspectFieldManager.add(aspectField);
		addChar_aspectAreaManager.add(aspectArea);
		
		addCharWindow.setVisible(true);
	}
}
