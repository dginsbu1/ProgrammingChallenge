package edu.yu.intro.objects.myinheritance;

public class Machine{
	private String power, position;

	public Machine(){
		power = "off";
		position = null;
	}
	public void turnOn(){
		power = "on";
		position = "pause";
	}
	public void turnOff(){
		power = "off";
		position = null;
	}
	public void start(){
		if(power.equals("on")){
			position = "start";
		}
	}
	public void pause(){
		if(power.equals("on")){
			position = "pause";
		}
	}
	//getters
	public String getPosition(){
		return position;
	}
	public String getPower(){
		return power;
	}

}
