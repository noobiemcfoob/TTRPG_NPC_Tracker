import java.util.*;
import java.util.Map.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

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
	static public HashMap<Character,JPanel> chars;

	
	public Viewer(){
		readSkills();
		loadCharacters();
		buildViewer();
		buildAddCharWindow();
		//Separate into thread
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
			addCharWindow.setVisible(true);
			addCharWindow.requestFocus();
		}else if(ae.getSource() == addChar_aspectAdd){
			addAspect_AddChar();
		}
		else if(ae.getSource() == addChar_stuntAdd){
			addStunt_AddChar();
		}else if(ae.getSource() == createChar){
			captureCharacter();
			addCharWindow.setVisible(false);
		}
	}
	
	private void captureCharacter() {
		Character newChar = new Character();
		
		//Set General Info
		newChar.setName(addChar_nameField.getText());
		newChar.setAllegiance(addChar_allyField.getText());
		try{
			newChar.setSkillCap(Integer.parseInt(addChar_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillCap(5);
		}
		try{
			newChar.setSkillPoints(Integer.parseInt(addChar_skillCapField.getText()));
		}catch (Exception e){
			newChar.setSkillPoints(0);
		}
		
		try{
			newChar.setPC((addChar_typeGroup.getSelection().getActionCommand()=="PC")?true:false);
		}catch (Exception e){
			newChar.setPC(false);
		}
		try{
			newChar.setRefresh(Integer.parseInt(addChar_refreshField.getText()));
		}catch (Exception e){
			newChar.setRefresh(7);
		}
		newChar.setRevisit(addChar_revisitBox.isSelected());

		
		//Set Stunts
		for(int i = 0;i<addChar_stuntManager.size();i+=2){
			try{
				newChar.addStunt(addChar_stuntManager.get(i).getText(), "", Integer.parseInt(addChar_stuntManager.get(i+1).getText()));
			}catch (Exception e){
				newChar.addStunt(addChar_stuntManager.get(i).getText(), "", 0);
			}
		}
		
		//Set Stunts
		for(int i = 0;i<addChar_aspectFieldManager.size();i++){
			newChar.addAspect(addChar_aspectFieldManager.get(i).getText(), addChar_aspectAreaManager.get(i).getText());
		}
		
		//Set Skills
		for(String skill : allSkills){
			try{
				newChar.setSkill(skill, Integer.parseInt(addChar_SkillFields.get(skill).getText()));
			}catch (Exception e){
				newChar.setSkill(skill, 0);
			}
		}
		
		//Set Inventory
		newChar.setInventory(addChar_inventory.getText());
		
		chars.put(newChar, new JPanel());
		createCombatCharacterPanel(newChar);
		addCharacterPanel(newChar);
		saveCharacters();
	}

	private void saveCharacters() {
		try{
			ObjectOutputStream saveOOS = new ObjectOutputStream(new FileOutputStream("characters.ser"));
			saveOOS.writeObject(chars);
			saveOOS.close();
		}catch (Exception e){
			System.out.println("Problem saving characters.ser: " + e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadCharacters() {
		try{
			ObjectInputStream OIS = new ObjectInputStream(new FileInputStream("characters.ser"));
			if(OIS.readObject() instanceof Character){
				chars = (HashMap<Character,JPanel>) OIS.readObject();
				OIS.close();
			}else{
				OIS.close();
				throw new IOException();
			}
		}catch (Exception e){
			System.out.println("Problem loading characters.ser: " + e);
			System.out.println("Generating new list");
			chars = new HashMap<Character,JPanel>();
		}
	}

	private void createCombatCharacterPanel(Character newChar){
		chars.remove(newChar);
		JPanel newPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
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
		
		newPanel.add(new JLabel(newChar.getAttackSkill()+": "),c);
		c.gridx++;
		newPanel.add(new JLabel(newChar.getSkill(newChar.getAttackSkill().toUpperCase()).toString()),c);
		c.gridx++;
		newPanel.add(new JLabel("  "),c);
		c.gridx++;
		newPanel.add(new JTextField(2),c);
		c.gridx=0;
		c.gridy++;
		
		c.weightx = 1;
		c.weighty = 1;
		newPanel.add(new JLabel(newChar.getDefenseSkill()+": "),c);
		c.gridx++;
		newPanel.add(new JLabel(newChar.getSkill(newChar.getDefenseSkill().toUpperCase()).toString()),c);
		c.gridx++;
		c.ipadx=20;
		newPanel.add(new JTextField(2),c);
		c.gridx=0;
		c.gridy++;
		
		chars.put(newChar, newPanel);
	}
	
	private void addCharacterPanel(Character newChar){
		charPanel.add(chars.get(newChar));
		mainWindow.setVisible(true);
	}
	
	private void buildAddCharWindow(){
		JPanel tempPanel;
		
		addCharWindow.setTitle("Add New Character");
		addCharWindow.setBackground(Color.GRAY);
		
		//Setting the border for the different areas
		addChar_skillPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_stuntPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_aspectPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_westPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_inventoryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
	
		//Build skillPanel
		addChar_skillPanel.setLayout(new BoxLayout(addChar_skillPanel, BoxLayout.Y_AXIS));
		
		addChar_SkillFields = new HashMap<String, JTextField>();
		for(String skill : allSkills){
			addChar_SkillFields.put(skill, new JTextField(3));
			
			tempPanel = new JPanel();
			
			tempPanel.add(new JLabel(skill));
			tempPanel.add(addChar_SkillFields.get(skill));
			
			addChar_skillPanel.add(tempPanel);
		}
		addCharWindow.getContentPane().add(addChar_skillScroll,"East");
		
		//build aspectPanel
		addChar_aspectFieldManager 	= new ArrayList<JTextField>();
		addChar_aspectAreaManager	= new ArrayList<JTextArea>();
		JTextField aspectField = new JTextField(30);
		aspectField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		JTextArea aspectArea = new JTextArea(10,50);
		aspectArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_aspectPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
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
		addChar_aspectAdd.addActionListener(this);
		addChar_aspectPanel.add(addChar_aspectAdd,c);
		
		addChar_aspectFieldManager.add(aspectField);
		addChar_aspectAreaManager.add(aspectArea);
		
		addCharWindow.getContentPane().add(addChar_aspectScroll);
		
		//build northPanel
		addChar_northPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		addChar_northPanel.add(new JLabel("Character:  "),c);
		c.gridx = 1;
		addChar_northPanel.add(addChar_nameField,c);
		c.gridx = 2;
		addChar_northPanel.add(new JLabel("   Allegiance:  "),c);
		c.gridx = 3;
		addChar_northPanel.add(addChar_allyField,c);
		c.gridx = 4;
		addChar_northPanel.add(new JLabel("  Skill Cap:  "),c);
		c.gridx = 5;
		addChar_northPanel.add(addChar_skillCapField,c);
		c.gridx = 6;
		addChar_northPanel.add(new JLabel("  Skill Points:  "),c);
		c.gridx = 7;
		addChar_northPanel.add(addChar_skillPointsField,c);
		c.gridy = 1;
		c.gridx = 0;
		addChar_typeGroup.add(addChar_pcRButton);
		addChar_northPanel.add(addChar_pcRButton,c);
		addChar_pcRButton.addActionListener(this);
		addChar_pcRButton.setActionCommand("PC");
		c.gridx = 1;
		addChar_typeGroup.add(addChar_npcRButton);
		addChar_northPanel.add(addChar_npcRButton,c);
		addChar_npcRButton.addActionListener(this);
		addChar_npcRButton.setActionCommand("NPC");
		c.gridx = 2;
		addChar_northPanel.add(new JLabel("  Concept:  "),c);
		c.gridx = 3;
		addChar_northPanel.add(addChar_conceptField,c);
		c.gridx = 4;
		addChar_northPanel.add(new JLabel("  Refresh:  "),c);
		c.gridx = 5;
		addChar_northPanel.add(addChar_refreshField,c);
		c.gridx = 6;
		addChar_northPanel.add(addChar_revisitBox,c);
		addCharWindow.getContentPane().add(addChar_northPanel,"North");
		
		/*
		 * Build West Panel
		 */
		//Build stuntPanel
		addChar_stuntManager = new ArrayList<JTextField>();
		addChar_stuntPanel.setLayout(new GridBagLayout());
		JTextField temp = new JTextField(20);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		addChar_stuntPanel.add(new JLabel("Stunt Name"),c);
		c.gridx++;
		addChar_stuntPanel.add(new JLabel("   "),c);
		c.gridx++;
		addChar_stuntPanel.add(new JLabel("Cost"),c);
		c.gridy++;
		for(int i=0;i<6;i++){
			c.gridx = 0;
			addChar_stuntManager.add(temp);
			addChar_stuntPanel.add(temp,c);
			c.gridx += 2;
			temp = new JTextField(3);
			addChar_stuntManager.add(temp);
			addChar_stuntPanel.add(temp,c);
			c.gridx=0;
			temp = new JTextField(20);
			c.gridy++;
		}
		addChar_stuntGridY = c.gridy;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		addChar_stuntAdd.addActionListener(this);
		addChar_stuntPanel.add(addChar_stuntAdd,c);
		addChar_westPanel.add(addChar_stuntPanel);
		
		//Build inventoryPanel
		addChar_inventoryPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		addChar_inventoryPanel.add(new JLabel("INVENTORY"),c);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		addChar_inventory.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createLoweredBevelBorder()));
		addChar_inventoryPanel.add(addChar_inventory,c);
		addChar_westPanel.add(addChar_inventoryPanel);
		addCharWindow.getContentPane().add(addChar_westScroll, "West");
		
		createChar.setPreferredSize(new Dimension(100,30));
		createChar.addActionListener(this);
		addCharWindow.getContentPane().add(createChar, "South");
		
		//Set size of window and hide
		addCharWindow.setSize(800, 500);
		addCharWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		addCharWindow.setVisible(false);
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
		newChar.addActionListener(this);
		modeGroup.add(comRButton);
		menuBar.add(comRButton);
		comRButton.addActionListener(this);
		modeGroup.add(socRButton);
		menuBar.add(socRButton);
		socRButton.addActionListener(this);
		mainWindow.setJMenuBar(menuBar);
		
		//Setting up rightPanel
		rightPanel.add(testButton);
		mainWindow.getContentPane().add(rightPanel, "East");
		
		//Setting up charPanel
		charPanel.setBackground(Color.WHITE);
		mainWindow.getContentPane().add(charPanel);
		
		//Set size of window and display
		mainWindow.setSize(800,800);
		mainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainWindow.setVisible(true);
		
		for(Character loadedChar : chars.keySet()){
			addCharacterPanel(loadedChar);
			System.out.println("Creating panel for: " + loadedChar.getName());
			System.out.println("\t"+loadedChar.getRefresh());
		}
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
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent me) {
		JPanel source = (JPanel) me.getSource();
		Character charSource = new Character();
		for(Entry<Character,JPanel> entry : chars.entrySet()){
			if(source.equals(entry.getValue())){
				charSource = entry.getKey();
			}
		}
		System.out.println(charSource.getName() + " was clicked!");
	}
}
