import java.util.*;
import javax.swing.*;
import java.io.Serializable;

public class Character implements Serializable{
	public JPanel panel;
	public JButton next;
	public JButton back;
	private static final long serialVersionUID = 14L;
	
	static ArrayList<String> allSkills;
	
	private String name;
	private String allegiance;
	private String concept;
	private Integer skillCap;
	private Integer skillPoints;
	private boolean pc;
	private Integer refresh;
	private boolean revisit;
	private String inventory;
	private String notes;
	
	//Combat related variables
	private ArrayList<String> attackSkills = new ArrayList<String>(Arrays.asList(
			"ARCHERY",
			"EQUESTRIAN",
			"MIGHT",
			"STRIKE"
		));
	private ArrayList<String> defenseSkills = new ArrayList<String>(Arrays.asList(
			"ALERTNESS",
			"ATHLETICS",
			"DISCIPLINE",
			"CONVICTION",
			"PARRY"
		));
	
	//Social related variables
	private ArrayList<String> socialSkills = new ArrayList<String>(Arrays.asList(
		"Performance",
		"Deceit",
		"Empathy",
		"Intimidation",
		"Presence",
		"Rapport"
	));
	
	//Health Related Variables
	private ArrayList<Boolean> physicalStress = new ArrayList<Boolean>();
	private Integer maxPhyStress = 0;
	private ArrayList<Boolean> mentalStress = new ArrayList<Boolean>();
	private Integer maxMenStress = 0;
	private ArrayList<Boolean> socialStress = new ArrayList<Boolean>();
	private Integer maxSocStress = 0;
	private ArrayList<Boolean> armorStress = new ArrayList<Boolean>();
	private Integer maxArmStress = 0;
	private ArrayList<Boolean> miscStress = new ArrayList<Boolean>();
	private Integer maxMiscStress = 0;
	
	private ArrayList<String> consequences = new ArrayList<String>(Arrays.asList(
			" ",
			" ",
			" ",
			" "
		));
	
	//Hash table of all skills for a character. "Skill Name" -> Skill Value
	private HashMap<String,Integer> skills = new HashMap<String,Integer>();
	
	//Hash table of all stunts for a character. "Stunt Name" -> ["Stunt Description","Stunt Cost"]
	private HashMap<String,ArrayList<String>> stunts = new HashMap<String,ArrayList<String>>();
	
	//Vector of all aspects of a character.
	private HashMap<String,String> aspects = new HashMap<String,String>();
	
	public Character()
	{
		for(String skill : Viewer.allSkills){
			skills.put(skill, 0);
		}
	}
	
	public String getNotes(){
		return notes;
	}
	
	public void setNotes(String notes){
		this.notes = notes;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getAllegiance(){
		return this.allegiance;
	}
	
	public void setAllegiance(String allegiance){
		this.allegiance = allegiance;
	}
	
	public String getConcept(){
		return this.concept;
	}
	
	public void setConcept(String concept){
		this.concept = concept;
	}
	
	public Integer getSkillCap(){
		return this.skillCap;
	}
	
	public void setSkillCap(int skillCap){
		this.skillCap = skillCap;
	}
	
	public Integer getSkillPoints(){
		return this.skillPoints;
	}
	
	public void setSkillPoints(int skillPoints){
		this.skillPoints = skillPoints;
	}
	
	public boolean getPC(){
		return this.pc;
	}
	
	public void setPC(boolean pc){
		this.pc = pc;
	}
	
	public Integer getRefresh(){
		return this.refresh;
	}
	
	public void setRefresh(int refresh){
		this.refresh = refresh;
	}
	
	public boolean getRevisit(){
		return this.revisit;
	}
	
	public void setRevisit(boolean revisit){
		this.revisit = revisit;
	}
	
	public String getInventory(){
		return this.inventory;
	}
	
	public void setInventory(String inventory){
		this.inventory = inventory;
	}
	
	public void appendToInventory(String inventory){
		this.inventory = this.inventory + "\n" + inventory;
	}

	public ArrayList<String> getAttackSkills(){
		return attackSkills;
	}
	
	public void setAttackSkills(ArrayList<String> skills){
		this.attackSkills = skills;
	}
	
	public ArrayList<String> getDefenseSkills(){
		return defenseSkills;
	}
	
	public void setDefenseSkills(ArrayList<String> skills){
		this.defenseSkills = skills;
	}
	
	public ArrayList<String> getSocialSkills(){
		return socialSkills;
	}
	
	public void setSocialSkills(ArrayList<String> skills){
		this.socialSkills = skills;
	}
	
	public void addSocialSkill(String skill){
		this.socialSkills.add(skill);
	}
	
	public Integer getSkill(String skillName){
		//TODO Add error checking
		return skills.get(skillName);
	}
	
	public boolean setSkill(String skillName,Integer skillValue){
		if(Viewer.allSkills.contains(skillName)){
			skills.put(skillName,skillValue);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean setAllSkills(ArrayList<Integer> skillValues){
		if(skillValues.size() != Viewer.allSkills.size()){
			return false;
		}
		
		for(Integer index = 0; index<skillValues.size(); index++){
			skills.put(Viewer.allSkills.get(index), skillValues.get(index));
		}
		
		return true;
	}
	
	public Set<String> getAspects(){
		//Integer skillValue = 0;
		//TODO Add error checking
		
		return aspects.keySet();
	}
	
	public boolean addAspect(String aspect){
		aspects.put(aspect, "");
		
		return true;
	}
	
	public boolean addAspect(String aspect, String description){
		aspects.put(aspect, description);
		
		return true;
	}
	
	public boolean addAspectNote(String aspect, String description){
		try{
			aspects.put(aspect, aspects.get(aspect)+"\n"+description);
		}catch (Exception e){
			System.out.println("Aspect does not exist");
			return false;
		}
		
		return true;
	}
	
	public String getAspectNote(String aspect){
		try{
			return aspects.get(aspect);
		}catch (Exception e){
			System.out.println("Aspect does not exist");
			return null;
		}
	}
	
	public Set<String> getStunts(){
		//TODO Add error checking
		
		return stunts.keySet();
	}
	
	public String getStuntDescription(String stunt){
		try{
			return stunts.get(stunt).get(0);
		}catch (Exception e){
			System.out.println("Stunt does not exist");
			return null;
		}
	}
	
	public ArrayList<String> getStuntNotes(String stunt){
		try{
			return stunts.get(stunt);
		}catch (Exception e){
			System.out.println("Stunt does not exist");
			return null;
		}
	}
	
	public Integer getStuntCost(String stunt){
		try{
			return Integer.parseInt(stunts.get(stunt).get(1));
		}catch (Exception e){
			System.out.println("Stunt or cost does not exist");
			return null;
		}
	}
	
	public boolean addStunt(String stunt, String description, Integer cost){
		try{
			stunts.put(stunt,new ArrayList<String>( Arrays.asList( new String[] {description, cost.toString()})));
			return true;
		}catch (Exception e){
			return false;
		}
	}
	
	public boolean addConsequence(Integer level, String consequence){
		switch(level){
			case -2: 	consequences.set(0, consequence);
						return true;
			case -4: 	consequences.set(1, consequence); 
						return true;
			case -6: 	consequences.set(2, consequence);
						return true; 
			case -8: 	consequences.set(3, consequence);
						return true;
			default: return false;
		}
	}
	
	public String getConsequence(Integer index){
		return consequences.get(index);
	}
	
	public ArrayList<String> getConsequences(){
		return consequences;
	}
	
	public void setMaxMenStress(Integer maxStress){
		maxMenStress = maxStress;
		mentalStress.ensureCapacity(maxStress);
	}
	
	public void setMaxPhyStress(Integer maxStress){
		maxPhyStress = maxStress;
		physicalStress.ensureCapacity(maxStress);
	}
	
	public void setMaxSocStress(Integer maxStress){
		maxSocStress = maxStress;
		socialStress.ensureCapacity(maxStress);
	}
	
	public void setMaxMiscStress(Integer maxStress){
		maxMiscStress = maxStress;
		miscStress.ensureCapacity(maxStress);
	}
	
	public void setMaxArmStress(Integer maxStress){
		maxArmStress = maxStress;
		armorStress.ensureCapacity(maxStress);
	}
	
	public ArrayList<Boolean> getPhyStress(){
		return physicalStress;
	}
	
	public ArrayList<Boolean> getMenStress(){
		return mentalStress;
	}
	
	public ArrayList<Boolean> getSocStress(){
		return socialStress;
	}
	
	public ArrayList<Boolean> getArmStress(){
		return armorStress;
	}
	
	public ArrayList<Boolean> getMscStress(){
		return miscStress;
	}
	
	public Integer getMaxMenStress(){
		return maxMenStress;
	}
	
	public Integer getMaxPhyStress(){
		return maxPhyStress;
	}
	
	public Integer getMaxSocStress(){
		return maxSocStress;
	}
	
	public Integer getMaxMiscStress(){
		return maxMiscStress;
	}
	
	public Integer getMaxArmStress(){
		return maxArmStress;
	}
	
	public void setMentalStress(Integer level){
		if(level > maxMenStress){
			
		}else{
			mentalStress.set(level, true);
		}
	}
}
