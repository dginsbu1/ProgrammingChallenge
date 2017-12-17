package edu.yu.intro.objects.myinheritance;

public class PersonalAssistant extends Machine{
	String name;
	public PersonalAssistant(String name){
		super();
		if(name == null || name.equals("")){
			throw new IllegalArgumentException("You must name your Personal Assistant");
		}
		this.name = name;
	}

	@Override
	public void turnOn(){
		super.turnOn();
		System.out.println("Hi, I'm "+name+" your Personal Assistant.");
	}
	
	public void makeSchedule(){
		if(super.getPosition().equals("start")){
			System.out.println("Sorry, I can't do that yet. Please wait for the next update");
		}
	}

	public static void main(String[] args) {
		/*Machine machine = new Machine();
		machine.turnOn();
		machine.start();
		System.out.println(machine.getPower()+" "+machine.getPosition());*/
		PersonalAssistant mate = new PersonalAssistant("Siri");
		System.out.println("Power: "+mate.getPower()+" Position: "+mate.getPosition());
		mate.turnOn();
		System.out.println("Power: "+mate.getPower()+" Position: "+mate.getPosition());
		mate.makeSchedule();
		mate.start();
		System.out.println("Power: "+mate.getPower()+" Position: "+mate.getPosition());
		mate.makeSchedule();

	}	
	
}