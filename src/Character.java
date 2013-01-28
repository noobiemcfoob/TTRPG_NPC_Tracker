import java.util.*;

public class Character {
	private String name;
	
	//Hash table of all skills for a character. "Skill Name" -> Skill Value
	private HashMap<String,Integer> skills = new HashMap<String,Integer>();
	
	//Hash table of all stunts for a character. "Stunt Name" -> "Stund Description"
	private HashMap<String,ArrayList<String>> stunts = new HashMap<String,ArrayList<String>>();
	
	//Vector of all aspects of a character.
	private HashMap<String,ArrayList<String>> aspects = new HashMap<String,ArrayList<String>>();
	
	public Character(String name)
	{
		this.name = name;
		
		for(String skill : Viewer.allSkills){
			skills.put(skill, 0);
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Integer getSkill(String skillName){
		//Integer skillValue = 0;
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
		aspects.put(aspect, new ArrayList<String>());
		
		return true;
	}
	
	public boolean addAspectNote(String aspect, String description){
		try{
			aspects.get(aspect).add(description);
		}catch (Exception e){
			System.out.println("Aspect does not exist");
			return false;
		}
		
		return true;
	}
	
	public ArrayList<String> getAspectNote(String aspect){
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
	
	public boolean addStunt(String stunt, String description){
		try{
			stunts.put(stunt,new ArrayList<String>( Arrays.asList( new String[] {description})));
			return true;
		}catch (Exception e){
			return false;
		}
	}
}
