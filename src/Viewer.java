import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.yaml.snakeyaml.Yaml;

public class Viewer extends GUIElem implements Runnable, ActionListener, MouseListener{

	/**
	 * Main Method
	 * ===========
	 * The main method really doesn't do anything for our program.  It's simply here to be able to run it from
	 * the command line.  All it does is check to see that there are no arguments and starts the constructor.
	 * 
	 * @param args none, Main takes no arguments.
	 */
	public static void main(String[] args) {
		try{
			if(args.length > 0)
				throw new Exception("mainulator accepts no arguments");
			
			new Viewer();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	/*
	 * Class Variables
	 */
	private ArrayList<Character> charArray;
	private Character charPasser;
	Yaml yaml = new Yaml();

	
	public Viewer(){
		readSkills();
		System.out.println("Loading Characters...");
		loadCharacters();
		System.out.println("Building GUI...");
		buildViewer();
		
		//Separate into thread
		System.out.println("Starting thread...");
		new Thread(this).start();
	}
	
	/**
	 * Run Method
	 * ==========
	 * All of our processing is actually done in the actionlistener method, since user input is required before anything happens.
	 */
	public void run() {
		while(true){}	//Run forever
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == newChar){
			buildAddCharWindow();
			charWindow.setVisible(true);
			charWindow.requestFocus();
		}else if(ae.getSource() == char_aspectAdd){
			addAspect_AddChar();
		}else if(ae.getSource() == char_stuntAdd){
			addStunt_AddChar();
		}else if(ae.getSource() == createChar){
			captureCharacter();
			charWindow.setVisible(false);
			charPasser = null;
		}else if(ae.getSource() == updateChar){
			delCharacterPanel(charPasser);
			captureCharacter(charPasser);
			charWindow.setVisible(false);
			charPasser = null;
		}else if(ae.getSource() == deleteChar){
			delCharacterPanel(charPasser);
			charWindow.setVisible(false);
			charArray.remove(charPasser);
			charPasser = null;
			saveCharacters();
		}else if(ae.getSource() == comRButton){
			charPanel = new JPanel();
			charPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
			charPanel.setBackground(Color.WHITE);
			mainWindow.getContentPane().add(charPanel,"Center");
			
			for(Character loadedChar : charArray){
				createCombatCharacterPanel(loadedChar);
				addCharacterPanel(loadedChar);
			}
			mainWindow.setVisible(true);
		}else if(ae.getSource() == socRButton){
			charPanel = new JPanel();
			charPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
			charPanel.setBackground(Color.WHITE);
			mainWindow.getContentPane().add(charPanel,"Center");
			
			for(Character loadedChar : charArray){
				createSocialCharacterPanel(loadedChar);
				addCharacterPanel(loadedChar);
			}
			mainWindow.setVisible(true);
		}
	}
	
	private void captureCharacter() {
		System.out.println("Capturing Character");
		Character newChar = new Character();
		
		//Set General Info
		newChar.setName(char_nameField.getText());
		newChar.setAllegiance(char_allyField.getText());
		newChar.setConcept(char_conceptField.getText());
		try{
			newChar.setSkillCap(Integer.parseInt(char_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillCap(5);
		}
		try{
			newChar.setSkillPoints(Integer.parseInt(char_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillPoints(0);
		}
		
		try{
			newChar.setPC((char_typeGroup.getSelection().getActionCommand()=="PC")?true:false);
		}catch (Exception e){
			newChar.setPC(false);
		}
		try{
			newChar.setRefresh(Integer.parseInt(char_refreshField.getText()));
		}catch (Exception e){
			newChar.setRefresh(7);
		}
		newChar.setRevisit(char_revisitBox.isSelected());

		
		//Set Stunts
		for(int i = 0;i<char_stuntManager.size();i+=2){
			if(!char_stuntManager.get(i).getText().equals("")){
				try{
					newChar.addStunt(char_stuntManager.get(i).getText(), "", Integer.parseInt(char_stuntManager.get(i+1).getText()));
				}catch (Exception e){
					newChar.addStunt(char_stuntManager.get(i).getText(), "", 0);
				}
			}
		}
		
		//Set Aspects
		for(int i = 0;i<char_aspectFieldManager.size();i++){
			if(!char_aspectFieldManager.get(i).getText().equals("")){
				newChar.addAspect(char_aspectFieldManager.get(i).getText(), char_aspectAreaManager.get(i).getText());
			}
		}
		
		//Set Skills
		for(String skill : allSkills){
			try{
				newChar.setSkill(skill, Integer.parseInt(char_SkillFields.get(skill).getText()));
			}catch (Exception e){
				newChar.setSkill(skill, 0);
			}
		}
		
		//Set Inventory
		newChar.setInventory(char_inventory.getText());
		
		newChar.panel = new JPanel();
		
		charArray.add(newChar);
		createCombatCharacterPanel(newChar);
		addCharacterPanel(newChar);
		saveCharacters();
	}

	private void captureCharacter(Character newChar) {
		charArray.remove(newChar);
		
		//Set General Info
		newChar.setName(char_nameField.getText());
		newChar.setAllegiance(char_allyField.getText());
		newChar.setConcept(char_conceptField.getText());
		try{
			newChar.setSkillCap(Integer.parseInt(char_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillCap(5);
		}
		try{
			newChar.setSkillPoints(Integer.parseInt(char_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillPoints(0);
		}
		
		try{
			newChar.setPC((char_typeGroup.getSelection().getActionCommand()=="PC")?true:false);
		}catch (Exception e){
			newChar.setPC(false);
		}
		try{
			newChar.setRefresh(Integer.parseInt(char_refreshField.getText()));
		}catch (Exception e){
			newChar.setRefresh(7);
		}
		newChar.setRevisit(char_revisitBox.isSelected());

		
		//Set Stunts
		for(int i = 0;i<char_stuntManager.size();i+=2){
			if(!char_stuntManager.get(i).getText().equals("")){
				System.out.println("New stunt: "+char_stuntManager.get(i).getText());
				try{
					newChar.addStunt(char_stuntManager.get(i).getText(), "", Integer.parseInt(char_stuntManager.get(i+1).getText()));
				}catch (Exception e){
					newChar.addStunt(char_stuntManager.get(i).getText(), "", 0);
				}
			}
		}
		
		//Set Stunts
		for(int i = 0;i<char_aspectFieldManager.size();i++){
			if(!char_aspectFieldManager.get(i).getText().equals("")){
				newChar.addAspect(char_aspectFieldManager.get(i).getText(), char_aspectAreaManager.get(i).getText());
			}
		}
		
		//Set Skills
		for(String skill : allSkills){
			try{
				newChar.setSkill(skill, Integer.parseInt(char_SkillFields.get(skill).getText()));
			}catch (Exception e){
				newChar.setSkill(skill, 0);
			}
		}
		
		//Set Inventory
		newChar.setInventory(char_inventory.getText());
		
		newChar.panel = new JPanel();
		
		charArray.add(newChar);
		createCombatCharacterPanel(newChar);
		addCharacterPanel(newChar);
		saveCharacters();
	}
	
	private void saveCharacters() {
		System.out.println("\nSaving Characters");
		try{
			ObjectOutputStream saveOOS = new ObjectOutputStream(new FileOutputStream("characters.ser"));
			saveOOS.writeObject(charArray);
			saveOOS.close();
			System.out.println("Characters saved");
		}catch (Exception e){
			System.out.println("Problem saving characters.ser: " + e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadCharacters() {
		try{
			ObjectInputStream OIS = new ObjectInputStream(new FileInputStream("characters.ser"));
			ArrayList<Character> loadedChars = (ArrayList<Character>)OIS.readObject();
			charArray = new ArrayList<Character>();
			for(Character loadedChar : loadedChars){
				charArray.add(loadedChar);
			}
			OIS.close();
			if(charArray.size() == 0){
				throw new IOException();
			}
		}catch (Exception e){
			System.out.println("Problem loading characters.ser: " + e);
			System.out.println("Generating new list");
			charArray = new ArrayList<Character>();
		}
	}

	private void createCombatCharacterPanel(Character newChar){
		System.out.println("\nBuilding Combat panel for: " + newChar.getName());
		JPanel newPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		if(newPanel.getMouseListeners().length == 0)
			newPanel.addMouseListener(this);
		
		newPanel.setLayout(new GridBagLayout());
		newPanel.add(new JLabel(newChar.getName()),c);
		c.gridy++;
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		newPanel.add(new JLabel("Refresh:  "),c);
		c.gridx++;
		newPanel.add(new JLabel(newChar.getRefresh().toString()),c);
		c.gridx = 0;
		c.gridy++;
		
		ArrayList<String> attackSkills = newChar.getAttackSkills();
		
		for(String skill : attackSkills){
			newPanel.add(new JLabel(skill+": "),c);
			c.gridx++;
			newPanel.add(new JLabel(newChar.getSkill(skill.toUpperCase()).toString()),c);
			c.gridx++;
			newPanel.add(new JLabel("  "),c);
			c.gridx++;
			newPanel.add(new JTextField(2),c);
			c.gridx=0;
			c.gridy++;
		}
		
		ArrayList<String> defenseSkills = newChar.getDefenseSkills();
		
		for(String skill : defenseSkills){
			newPanel.add(new JLabel(skill+": "),c);
			c.gridx++;
			newPanel.add(new JLabel(newChar.getSkill(skill.toUpperCase()).toString()),c);
			c.gridx++;
			newPanel.add(new JLabel("  "),c);
			c.gridx++;
			newPanel.add(new JTextField(2),c);
			c.gridx=0;
			c.gridy++;
		}
		
		newChar.panel = newPanel;
		System.out.println("Combat Panel Built...");
	}
	
	private void createSocialCharacterPanel(Character newChar){
		JPanel newPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		if(newPanel.getMouseListeners().length == 0)
			newPanel.addMouseListener(this);
		
		newPanel.setLayout(new GridBagLayout());
		newPanel.add(new JLabel(newChar.getName()),c);
		c.gridy++;
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		newPanel.add(new JLabel("Refresh:  "),c);
		c.gridx++;
		newPanel.add(new JLabel(newChar.getRefresh().toString()),c);
		c.gridx = 0;
		c.gridy++;
		
		ArrayList<String> socialSkills = newChar.getSocialSkills();
		
		for(String skill : socialSkills){
			newPanel.add(new JLabel(skill+": "),c);
			c.gridx++;
			newPanel.add(new JLabel(newChar.getSkill(skill.toUpperCase()).toString()),c);
			c.gridx++;
			newPanel.add(new JLabel("  "),c);
			c.gridx++;
			newPanel.add(new JTextField(2),c);
			c.gridx=0;
			c.gridy++;
		}

		newChar.panel = newPanel;
	}
	
	private void addCharacterPanel(Character newChar){
		System.out.println("\nAdding character panel for " + newChar.getName());
		charPanel.add(newChar.panel);

		if(newChar.panel.getMouseListeners().length == 0)
			newChar.panel.addMouseListener(this);
		mainWindow.setVisible(true);
		saveCharacters();

	}
	
	private void delCharacterPanel(Character delChar){
		charPanel.remove(delChar.panel);
		mainWindow.repaint();
	}
	
	private void buildAddCharWindow(){
		JPanel tempPanel;
		
		charWindow = new JFrame();
		char_skillPanel = new JPanel();
		char_skillScroll = new JScrollPane(char_skillPanel);
		char_aspectPanel = new JPanel();
		char_aspectScroll = new JScrollPane(char_aspectPanel);
		char_northPanel = new JPanel(new GridLayout(2,2));
		char_westPanel = new JPanel(new GridLayout(3,1));
		char_stuntPanel = new JPanel();
		char_inventoryPanel = new JPanel();
		char_notesPanel = new JPanel();
		char_centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		char_westScroll = new JScrollPane(char_westPanel);
		
		//NorthPanel items
		char_nameField = new JTextField(25);
		char_allyField = new JTextField(25);
		char_skillCapField = new JTextField(3);
		char_skillPointsField = new JTextField(3);
		char_refreshField = new JTextField(3);
		char_conceptField = new JTextField(25);
		char_typeGroup = new ButtonGroup();
		char_pcRButton = new JRadioButton("PC");
		char_npcRButton = new JRadioButton("NPC");
		char_revisitBox = new JCheckBox("Revisit?");
		
		//westPanel items
		char_stuntGridY = 0;
		char_inventory = new JTextArea(10,26);
		
		//aspectPanel items
		char_aspectGridY = 0;
		
		charWindow.setTitle("Add New Character");
		charWindow.setBackground(Color.GRAY);
		
		//Setting the border for the different areas
		char_centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_skillPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_stuntPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_aspectPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_westPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_inventoryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_notesPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
	
		//Build skillPanel
		char_skillPanel.setLayout(new BoxLayout(char_skillPanel, BoxLayout.Y_AXIS));
		
		char_SkillFields = new HashMap<String, JTextField>();
		for(String skill : allSkills){
			char_SkillFields.put(skill, new JTextField(3));
			
			tempPanel = new JPanel();
			
			tempPanel.add(new JLabel(skill));
			tempPanel.add(char_SkillFields.get(skill));
			
			char_skillPanel.add(tempPanel);
		}
		charWindow.getContentPane().add(char_skillScroll,"East");
		
		/*
		 * Build centerPanel
		 */
		//build aspectPanel
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 1;
		c2.weighty = 1;
		c2.gridx = 0;
		c2.gridy = 0;
		c2.anchor = GridBagConstraints.FIRST_LINE_START;
		c2.gridheight = 2;
		char_aspectFieldManager 	= new ArrayList<JTextField>();
		char_aspectAreaManager	= new ArrayList<JTextArea>();
		JTextField aspectField = new JTextField(30);
		aspectField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		JTextArea aspectArea = new JTextArea(10,50);
		aspectArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_aspectPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
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
		if(char_aspectAdd.getActionListeners().length == 0){
			char_aspectAdd.addActionListener(this);
		}
		char_aspectPanel.add(char_aspectAdd,c);
		char_aspectFieldManager.add(aspectField);
		char_aspectAreaManager.add(aspectArea);
		char_centerPanel.add(char_aspectScroll,c2);
		
		//Build Notes and Inventory panels
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 0.1;
		c2.weighty = 0.1;
		c2.gridx = 0;
		c2.gridy = 2;
		c2.anchor = GridBagConstraints.FIRST_LINE_START;
		c2.gridheight = 1;
		JPanel southCenter = new JPanel(new GridLayout(1,2));
		//Build notesPanel
		char_notesPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		char_notesPanel.add(new JLabel("NOTES"),c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		char_notes= new JTextArea(10,13);
		char_notes.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_notesPanel.add(char_notes,c);
		southCenter.add(char_notesPanel);
		
		//Build inventoryPanel
		char_inventoryPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		char_inventoryPanel.add(new JLabel("INVENTORY"),c);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		char_inventory.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_inventoryPanel.add(char_inventory,c);
		southCenter.add(char_inventoryPanel);
		
		char_centerPanel.add(southCenter,c2);
		
		charWindow.getContentPane().add(char_centerPanel);
		
		//build northPanel
		char_northPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		char_northPanel.add(new JLabel("Character:  "),c);
		c.gridx = 1;
		char_northPanel.add(char_nameField,c);
		c.gridx = 2;
		char_northPanel.add(new JLabel("   Allegiance:  "),c);
		c.gridx = 3;
		char_northPanel.add(char_allyField,c);
		c.gridx = 4;
		char_northPanel.add(new JLabel("  Skill Cap:  "),c);
		c.gridx = 5;
		char_northPanel.add(char_skillCapField,c);
		c.gridx = 6;
		char_northPanel.add(new JLabel("  Skill Points:  "),c);
		c.gridx = 7;
		char_northPanel.add(char_skillPointsField,c);
		c.gridy = 1;
		c.gridx = 0;
		char_typeGroup.add(char_pcRButton);
		char_northPanel.add(char_pcRButton,c);
		
		if(char_pcRButton.getActionListeners().length == 0){
			char_pcRButton.addActionListener(this);
		}
		
		char_pcRButton.setActionCommand("PC");
		c.gridx = 1;
		char_typeGroup.add(char_npcRButton);
		char_northPanel.add(char_npcRButton,c);
		
		if(char_npcRButton.getActionListeners().length == 0){
			char_npcRButton.addActionListener(this);
		}
		
		char_npcRButton.setActionCommand("NPC");
		c.gridx = 2;
		char_northPanel.add(new JLabel("  Concept:  "),c);
		c.gridx = 3;
		char_northPanel.add(char_conceptField,c);
		c.gridx = 4;
		char_northPanel.add(new JLabel("  Refresh:  "),c);
		c.gridx = 5;
		char_northPanel.add(char_refreshField,c);
		c.gridx = 6;
		char_northPanel.add(char_revisitBox,c);
		charWindow.getContentPane().add(char_northPanel,"North");
		
		/*
		 * Build West Panel
		 */
		//Build stuntPanel
		char_stuntManager = new ArrayList<JTextField>();
		char_stuntPanel.setLayout(new GridBagLayout());
		JTextField temp = new JTextField(20);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		char_stuntPanel.add(new JLabel("Stunt Name"),c);
		c.gridx++;
		char_stuntPanel.add(new JLabel("   "),c);
		c.gridx++;
		char_stuntPanel.add(new JLabel("Cost"),c);
		c.gridy++;
		for(int i=0;i<6;i++){
			c.gridx = 0;
			char_stuntManager.add(temp);
			char_stuntPanel.add(temp,c);
			c.gridx += 2;
			temp = new JTextField(3);
			char_stuntManager.add(temp);
			char_stuntPanel.add(temp,c);
			c.gridx=0;
			temp = new JTextField(20);
			c.gridy++;
		}
		char_stuntGridY = c.gridy;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		if(char_stuntAdd.getActionListeners().length == 0){
			char_stuntAdd.addActionListener(this);
		}
		char_stuntPanel.add(char_stuntAdd,c);
		char_westPanel.add(char_stuntPanel);
		
		charWindow.getContentPane().add(char_westScroll, "West");
		
		createChar.setPreferredSize(new Dimension(100,30));
		if(createChar.getActionListeners().length == 0){
			createChar.addActionListener(this);
		}
		charWindow.getContentPane().add(createChar, "South");
		
		//Set size of window and hide
		charWindow.setSize(800, 500);
		charWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		charWindow.setVisible(false);
	}
	
	private void buildViewCharWindow(Character viewChar){
		charWindow = new JFrame();
		char_skillPanel = new JPanel();
		char_skillScroll = new JScrollPane(char_skillPanel);
		char_aspectPanel = new JPanel();
		char_aspectScroll = new JScrollPane(char_aspectPanel);
		char_northPanel = new JPanel(new GridLayout(2,2));
		char_westPanel = new JPanel(new GridLayout(3,1));
		char_stuntPanel = new JPanel();
		char_notesPanel = new JPanel();
		char_inventoryPanel = new JPanel();
		char_centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		char_westScroll = new JScrollPane(char_westPanel);
		charPasser = viewChar;
		
		JPanel tempPanel;
		
		charWindow.setTitle("View Character: " + viewChar.getName());
		charWindow.setBackground(Color.GRAY);
		
		//Setting the border for the different areas
		char_centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_skillPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_stuntPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_aspectPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_westPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_inventoryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_notesPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));

		
		//Build skillPanel
		char_skillPanel.setLayout(new BoxLayout(char_skillPanel, BoxLayout.Y_AXIS));
		
		char_SkillFields = new HashMap<String, JTextField>();
		for(String skill : allSkills){
			char_SkillFields.put(skill, new JTextField(viewChar.getSkill(skill).toString(),3));
			
			tempPanel = new JPanel();
			
			tempPanel.add(new JLabel(skill));
			tempPanel.add(char_SkillFields.get(skill));
			
			char_skillPanel.add(tempPanel);
		}
		charWindow.getContentPane().add(char_skillScroll,"East");
		
		/*
		 * Build centerPanel
		 */
		//build aspectPanel
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 1;
		c2.weighty = 1;
		c2.gridx = 0;
		c2.gridy = 0;
		c2.anchor = GridBagConstraints.FIRST_LINE_START;
		c2.gridheight = 2;
		char_aspectFieldManager 	= new ArrayList<JTextField>();
		char_aspectAreaManager	= new ArrayList<JTextArea>();
		JTextField aspectField = new JTextField(30);
		aspectField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		JTextArea aspectArea = new JTextArea(10,50);
		aspectArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_aspectPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
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
		if(char_aspectAdd.getActionListeners().length == 0){
			char_aspectAdd.addActionListener(this);
		}
		char_aspectPanel.add(char_aspectAdd,c);
		char_aspectFieldManager.add(aspectField);
		char_aspectAreaManager.add(aspectArea);
		char_centerPanel.add(char_aspectScroll,c2);
		
		//Build Notes and Inventory panels
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 0.1;
		c2.weighty = 0.1;
		c2.gridx = 0;
		c2.gridy = 2;
		c2.anchor = GridBagConstraints.FIRST_LINE_START;
		c2.gridheight = 1;
		JPanel southCenter = new JPanel(new GridLayout(1,2));
		//Build notesPanel
		char_notesPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		char_notesPanel.add(new JLabel("NOTES"),c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		char_notes= new JTextArea(10,13);
		char_notes.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_notesPanel.add(char_notes,c);
		southCenter.add(char_notesPanel);
		
		//Build inventoryPanel
		char_inventoryPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		char_inventoryPanel.add(new JLabel("INVENTORY"),c);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		char_inventory.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		char_inventoryPanel.add(char_inventory,c);
		southCenter.add(char_inventoryPanel);
		
		char_centerPanel.add(southCenter,c2);
		
		charWindow.getContentPane().add(char_centerPanel);
		
		//build northPanel
		char_northPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		char_northPanel.add(new JLabel("Character:  "),c);
		c.gridx = 1;
		char_nameField = new JTextField(viewChar.getName(),25);
		char_northPanel.add(char_nameField,c);
		c.gridx = 2;
		char_northPanel.add(new JLabel("   Allegiance:  "),c);
		c.gridx = 3;
		char_allyField = new JTextField(viewChar.getAllegiance(),25);
		char_northPanel.add(char_allyField,c);
		c.gridx = 4;
		char_northPanel.add(new JLabel("  Skill Cap:  "),c);
		c.gridx = 5;
		char_skillCapField = new JTextField(viewChar.getSkillCap().toString(),3);
		char_northPanel.add(char_skillCapField,c);
		c.gridx = 6;
		char_northPanel.add(new JLabel("  Skill Points:  "),c);
		c.gridx = 7;
		c.weightx = 1;
		char_skillPointsField = new JTextField(viewChar.getSkillPoints().toString(),3);
		char_northPanel.add(char_skillPointsField,c);
		c.gridx++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		char_northPanel.add(deleteChar,c);
		if(deleteChar.getActionListeners().length == 0){
			deleteChar.addActionListener(this);
		}
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 1;
		c.gridx = 0;
		char_typeGroup.add(char_pcRButton);
		char_pcRButton.setSelected(viewChar.getPC());
		char_northPanel.add(char_pcRButton,c);
		if(char_pcRButton.getActionListeners().length == 0){
			char_pcRButton.addActionListener(this);
		}
		char_pcRButton.setActionCommand("PC");
		c.gridx = 1;
		char_typeGroup.add(char_npcRButton);
		char_npcRButton.setSelected(!viewChar.getPC());
		char_northPanel.add(char_npcRButton,c);
		if(char_npcRButton.getActionListeners().length == 0){
			char_npcRButton.addActionListener(this);
		}
		char_npcRButton.setActionCommand("NPC");
		c.gridx = 2;
		char_northPanel.add(new JLabel("  Concept:  "),c);
		c.gridx = 3;
		char_conceptField = new JTextField(viewChar.getConcept(),25);
		char_northPanel.add(char_conceptField,c);
		c.gridx = 4;
		char_northPanel.add(new JLabel("  Refresh:  "),c);
		c.gridx = 5;
		char_refreshField = new JTextField(viewChar.getRefresh().toString(),3);
		char_northPanel.add(char_refreshField,c);
		c.gridx = 6;
		c.weighty = 1;
		char_revisitBox.setSelected(viewChar.getRevisit());
		char_northPanel.add(char_revisitBox,c);
		charWindow.getContentPane().add(char_northPanel,"North");
		
		/*
		 * Build West Panel
		 */
		//Build stuntPanel
		char_stuntManager = new ArrayList<JTextField>();
		char_stuntPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		char_stuntPanel.add(new JLabel("Stunt Name"),c);
		c.gridx++;
		char_stuntPanel.add(new JLabel("   "),c);
		c.gridx++;
		char_stuntPanel.add(new JLabel("Cost"),c);
		c.gridy++;
		
		for(String stunt : viewChar.getStunts()){
			JTextField temp = new JTextField(stunt,20);
			c.gridx = 0;
			char_stuntManager.add(temp);
			char_stuntPanel.add(temp,c);
			c.gridx += 2;
			temp = new JTextField(viewChar.getStuntCost(stunt).toString(),3);
			char_stuntManager.add(temp);
			char_stuntPanel.add(temp,c);
			c.gridx=0;
			c.gridy++;
		}
		char_stuntGridY = c.gridy;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		if(char_stuntAdd.getActionListeners().length == 0){
			char_stuntAdd.addActionListener(this);
		}
		char_stuntPanel.add(char_stuntAdd,c);
		char_westPanel.add(char_stuntPanel);
		
		charWindow.getContentPane().add(char_westScroll, "West");
		
		updateChar.setPreferredSize(new Dimension(100,30));
		if(updateChar.getActionListeners().length == 0){
			updateChar.addActionListener(this);
		}
		charWindow.getContentPane().add(updateChar, "South");
		
		//Set size of window and hide
		charWindow.setSize(1100, 500);
		charWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		charWindow.setVisible(true);
	}
	
	private void buildViewer(){
		//Setting up the window
		mainWindow.setTitle("FATE NPC Tracker");
		mainWindow.setBackground(Color.GRAY);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Setting the border for the different areas
		charPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		rightPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
	
		//Setting up menuBar
		menuBar.add(newChar);
		if(newChar.getActionListeners().length == 0){
			newChar.addActionListener(this);
		}
		modeGroup.add(comRButton);
		menuBar.add(comRButton);
		if(comRButton.getActionListeners().length == 0){
			comRButton.addActionListener(this);
		}
		modeGroup.add(socRButton);
		menuBar.add(socRButton);
		if(socRButton.getActionListeners().length == 0){
			socRButton.addActionListener(this);
		}
		mainWindow.setJMenuBar(menuBar);
		
		//Setting up rightPanel
		JPanel adjectives = new JPanel();
		adjectives.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		adjectives.setLayout(new BoxLayout(adjectives, BoxLayout.Y_AXIS));
		adjectives.add(new JLabel("ADJECTIVES"));
		adjectives.add(new JLabel("Legendary 7+"));
		adjectives.add(new JLabel("Epic 6"));
		adjectives.add(new JLabel("Superb 5"));
		adjectives.add(new JLabel("Great 4"));
		adjectives.add(new JLabel("Good 3"));
		adjectives.add(new JLabel("Fair 2"));
		adjectives.add(new JLabel("Average 1"));
		adjectives.add(new JLabel("Mediocre 0"));
		adjectives.add(new JLabel("Poor -1"));
		adjectives.add(new JLabel("Terrible -2"));
		rightPanel.add(adjectives);
		mainWindow.getContentPane().add(rightPanel, "East");
		
		//Setting up charPanel
		charPanel.setBackground(Color.WHITE);
		mainWindow.getContentPane().add(charPanel,"Center");
		
		//Set size of window and display
		mainWindow.setSize(800,800);
		mainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainWindow.setVisible(true);
		
		System.out.println("Building GUI...");
		for(Character loadedChar : charArray){
			createCombatCharacterPanel(loadedChar);
			addCharacterPanel(loadedChar);
		}

		System.out.println("GUI Built...");
	}
	
	private void readSkills(){
		try{
			allSkills = new ArrayList<String>(Files.readAllLines(Paths.get("Skills.txt"),StandardCharsets.UTF_8));
			for (int i = 0; i<allSkills.size(); i++){
				allSkills.set(i,allSkills.get(i).toUpperCase());
			}
		}catch (Exception e){
			return;
		}
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		JPanel source = (JPanel) me.getSource();
		Character charSource = new Character();
		for(Character entry : charArray){
			if(source.equals(entry.panel)){
				charSource = entry;
			}
		}
		buildViewCharWindow(charSource);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent me) {}
}
