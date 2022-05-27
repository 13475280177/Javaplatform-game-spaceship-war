import java.util.ArrayList;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import util.GameObject;
import util.Point3f;
import util.Vector3f; 
import java.math.*;
/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
   Modified by Jiaheng Guo£¬
   20210848
 */ 
public class Model {
	
	 private  GameObject Player01;
	 private  GameObject Player02;
	 private Controller controller = Controller.getInstance();
	 private  CopyOnWriteArrayList<GameObject> EnemiesList  = new CopyOnWriteArrayList<GameObject>();
	 private  CopyOnWriteArrayList<GameObject> BulletList  = new CopyOnWriteArrayList<GameObject>();
	 private  CopyOnWriteArrayList<GameObject> ExplosionList  = new CopyOnWriteArrayList<GameObject>();
	 private  CopyOnWriteArrayList<GameObject> HealthDropList  = new CopyOnWriteArrayList<GameObject>();
	 private  CopyOnWriteArrayList<GameObject> PlayerList  = new CopyOnWriteArrayList<GameObject>();
	 private int Score=0;
	 private int defaultHealth = 2;
	 private boolean live = true;
	 private int stage = 1;
	 
	 private long t1=System.currentTimeMillis();

	public Model() {
		 //setup game world 
		//Player 
		
		Player01  = new GameObject("res/spaceship.png",50,50,new Point3f(625,500,0),3);
		Player02  = new GameObject("res/spaceship.png",50,50,new Point3f(625,600,0),3);
		PlayerList.add(Player01);
		PlayerList.add(Player02);
		
		//Enemies  starting with four 
		
		EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f(((float)Math.random()*50+400 ),0,0),defaultHealth)); 
		EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f(((float)Math.random()*50+200 ),0,0),defaultHealth));
		EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f(((float)Math.random()*100+600 ),0,0),defaultHealth));
		EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f(((float)Math.random()*100+800 ),0,0),defaultHealth));
		
		
	    
	}
	
	// This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly. 
	public void gamelogic() 
	{
		// Player Logic first 
		playerLogic(); 
		// Enemy Logic next
		enemyLogic();
		// Bullets move next 
		bulletLogic();
		explosionLogic();
		healthDropLogic();
		
		// interactions between objects
		gameLogic(); 
		
        //stageLogic	
		stageLogic();
		
		//game over	
		if((Player01.getHealth()<=0&&Player02.getHealth()<=0)||stage>7) 
		{
			this.live = false;
		} 	   
	}
	private void stageLogic() {
		 
		setStage(Score/20+1);
	}

	private void explosionLogic() {
		// TODO Auto-generated method stub
		for (GameObject temp : ExplosionList) 
		{
			temp.addSurvivalTime();
			if (temp.getSurvivalTime()==12) {
				ExplosionList.remove(temp);
			}
		}
		
	}
	
	private void healthDropLogic() {
		// TODO Auto-generated method stub
		for (GameObject temp : HealthDropList) 
		{
			temp.addSurvivalTime();
			if (temp.getSurvivalTime()==400) {
				HealthDropList.remove(temp);
			}
		}
		for (GameObject temp : HealthDropList) 
		{
			for (GameObject Player : PlayerList) {
				if(Math.abs(temp.getCentre().getX()- Player.getCentre().getX())<= Player.getWidth()&& Math.abs(temp.getCentre().getY()+0.5*temp.getHeight()- Player.getCentre().getY()-0.5*Player.getHeight())<= 0.5*(temp.getHeight()+Player.getHeight()) )
		    {
				HealthDropList.remove(temp);
				Player.setHealth(Player.getHealth()+1);
			}
				}
			
		
		}
		
	}

	private void gameLogic() { 
		
		
		// this is a way to increment across the array list data structure 

		
		//see if they hit anything 
		// using enhanced for-loop style as it makes it a lot easier both code wise and reading wise too 
		for (GameObject temp : EnemiesList) 
		{
		for (GameObject Bullet : BulletList) 
		{
			if ( Math.abs(temp.getCentre().getX()+0.5*temp.getWidth()- Bullet.getCentre().getX()-0.5*Bullet.getWidth())< 0.5*(temp.getWidth()+Bullet.getWidth())
				&& Math.abs(temp.getCentre().getY()+0.5*temp.getHeight()- Bullet.getCentre().getY()-0.5*Bullet.getHeight())<= 0.5*(temp.getHeight()+Bullet.getHeight()))
			{
				
				BulletList.remove(Bullet);

				temp.setHealth(temp.getHealth()-1);
				
			}  
		}
		}
        //collision detection	
		for (GameObject temp : EnemiesList) 
		{
			for (GameObject Player : PlayerList) {
			if(Math.abs(temp.getCentre().getX()- Player.getCentre().getX())<= Player.getWidth()
				&& Math.abs(temp.getCentre().getY()+0.5*temp.getHeight()- Player.getCentre().getY()-0.5*Player.getHeight())<= 0.5*(temp.getHeight()+Player.getHeight()) && stage<7 )
		    {
				EnemiesList.remove(temp);
				if(Math.random()>0.9) {
					HealthDropList.add(new GameObject("res/health.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				}
				Score+=1;
				ExplosionList.add(new GameObject("res/explosion.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				
				Player.setHealth(Player.getHealth()-1);
			}}
			
			
		
		}
		
		for (GameObject temp : EnemiesList) 
		{
			if(temp.getHealth()<=0&&stage<7) 
			{
				EnemiesList.remove(temp);
				ExplosionList.add(new GameObject("res/explosion.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				if(Math.random()>0.8) {
					HealthDropList.add(new GameObject("res/health.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				}
				Score+=1;
			}
			
			if(temp.getHealth()<=0&&stage==7) 
			{
				EnemiesList.remove(temp);
				ExplosionList.add(new GameObject("res/explosion.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				if(Math.random()>0.8) {
					HealthDropList.add(new GameObject("res/health.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				}
				Score+=20;
			}
		}
		
		
		
	}

	private void enemyLogic() {
		if (stage<=5) {
		// TODO Auto-generated method stub
		for (GameObject temp : EnemiesList) 
		{
			
		    // Move enemies,track players.
			float x1 = Player01.getCentre().getX()-temp.getCentre().getX();
			float y1 = Player01.getCentre().getY()-temp.getCentre().getY();
			float length1 = (float) Math.sqrt(x1*x1 +y1*y1);
			float x2 = Player02.getCentre().getX()-temp.getCentre().getX();
			float y2 = Player02.getCentre().getY()-temp.getCentre().getY();
			float length2 = (float) Math.sqrt(x2*x2 +y2*y2);
			if((length1<length2&&Player01.getHealth()>0)||Player02.getHealth()<=0) {
			temp.getCentre().ApplyLocateVector(new Vector3f ((float)0.5*(stage/3+1)*x1/length1,(float)0.5*(stage/3+1)*-1*y1/length1,0));}
			else{
				temp.getCentre().ApplyLocateVector(new Vector3f ((float)0.5*(stage/3+1)*x2/length2,(float)0.5*(stage/3+1)*-1*y2/length2,0));
			}
			
			
		}
		long t2=System.currentTimeMillis() -t1;
		if (EnemiesList.size()<2+t2/12000 || EnemiesList.size()<4)
		{
			while (EnemiesList.size()<stage*3)
			{
				EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f(Math.random()>0.5?0:1*1300,(float)Math.random()*800,0),(stage+2/3+1))); 
				EnemiesList.add(new GameObject("res/enemyship.png",50,30,new Point3f((float)Math.random()*1300,Math.random()>0.5?0:1*800,0),(stage+2/3+1))); 
			}
		}
		}
//		Boss fight
		if(stage==6) {
			for (GameObject temp : EnemiesList) 
			{
				EnemiesList.remove(temp);
				ExplosionList.add(new GameObject("res/explosion.png",32,32,new Point3f((float)(temp.getCentre().getX()),(float)(temp.getCentre().getY()),0)));
				
			}
			stage+=1;
			Score+=5;
			EnemiesList.add(new GameObject("res/enemyship.png",350,200,new Point3f(Math.random()>0.5?0:1*1300,(float)Math.random()*800,0),50));
		}
			
		if(stage == 7) {
			for (GameObject temp : EnemiesList) 
			{
			    // Move enemies,track players.
				float x1 = Player01.getCentre().getX()-temp.getCentre().getX();
				float y1 = Player01.getCentre().getY()-temp.getCentre().getY();
				float length1 = (float) Math.sqrt(x1*x1 +y1*y1);
				float x2 = Player02.getCentre().getX()-temp.getCentre().getX();
				float y2 = Player02.getCentre().getY()-temp.getCentre().getY();
				float length2 = (float) Math.sqrt(x2*x2 +y2*y2);
				if((length1<length2&&Player01.getHealth()>0)||Player02.getHealth()<=0) {
				temp.getCentre().ApplyLocateVector(new Vector3f ((float)0.5*(3/2+1)*x1/length1,(float)0.5*(3/2+1)*-1*y1/length1,0));}
				else {
					temp.getCentre().ApplyLocateVector(new Vector3f ((float)0.5*(3/2+1)*x2/length2,(float)0.5*(3/2+1)*-1*y2/length2,0));
				}
				
				
				
			}
			
		}
			
		
	}

	private void bulletLogic() {
		// TODO Auto-generated method stub
		// move bullets 
        
	  
		for (GameObject temp : BulletList) 
		{
		    //check to move them
			  
			temp.getCentre().ApplyVector(6,temp.getDirection());
			//see if they hit anything 
			
			//see if they get to the boundary of the screen 
			if (temp.getCentre().getY()<=0 || temp.getCentre().getY()>=750 || temp.getCentre().getX()<=0 || temp.getCentre().getX()>=1250||temp.getSurvivalTime()>100)
			{
			 	BulletList.remove(temp);
			} 
            //Maximum range of bullets	
			temp.addSurvivalTime();
		} 
		
	}

	private void playerLogic() {
		
		// smoother animation is possible if we make a target position  // done but may try to change things for students  
		 
		//check for movement and if you fired a bullet 
		  
        if(Player01.getHealth()>0) {
		if(Controller.getInstance().isKeyWPressed())
		{
			Player01.getCentre().ApplyVector( 4,Player01.getDirection());
		}
		if(Controller.getInstance().isKeyAPressed())
		{
			Player01.getCentre().ApplyTurn(-3.0);
			Player01.adjustDirection(-3);
		}
		if(Controller.getInstance().isKeyDPressed())
		{
			Player01.getCentre().ApplyTurn(3.0);
			Player01.adjustDirection(3);
		}
		
		if(Controller.getInstance().isKeySPressed()){Player01.getCentre().ApplyVector( -4,Player01.getDirection());}
		
		if(Controller.getInstance().isKeySpacePressed())
		{
			CreateBullet(Player01);
			Controller.getInstance().setKeySpacePressed(false);
		} }
      
		
//		player02
        if(Player02.getHealth()>0)
		if(Controller.getInstance().isKeyUpPressed())
		{
			Player02.getCentre().ApplyVector( 4,Player02.getDirection());
		}
		if(Controller.getInstance().isKeyLeftPressed())
		{
			Player02.getCentre().ApplyTurn(-3.0);
			Player02.adjustDirection(-3);
		}
		if(Controller.getInstance().isKeyRightPressed())
		{
			Player02.getCentre().ApplyTurn(3.0);
			Player02.adjustDirection(3);
		}
		
		if(Controller.getInstance().isKeyDownPressed()){Player02.getCentre().ApplyVector( -4,Player02.getDirection());}
		
		if(Controller.getInstance().isKeyEnterPressed())
		{
			CreateBullet(Player02);
			Controller.getInstance().setKeyEnterPressed(false);
		} 
		
		for (GameObject Player : PlayerList) {
			if(Player.getHealth()<=0) {
				PlayerList.remove(Player);
			}
		}
		
		
		
		
		
	}

	private void CreateBullet(GameObject Player) {
		
		if(Player.getHealth()<=3) {
		GameObject bullet = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
		bullet.setDirection(Player.getCentre().getDegree());
		BulletList.add(bullet);
		}
		else if (Player.getHealth()<=5)
		{
			GameObject bullet0 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet0.setDirection(Player.getCentre().getDegree());
			BulletList.add(bullet0);
			GameObject bullet1 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet1.setDirection(Player.getCentre().getDegree()-30);
			BulletList.add(bullet1);
			GameObject bullet2 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet2.setDirection(Player.getCentre().getDegree()+30);
			BulletList.add(bullet2);
		}
		else
		{
			GameObject bullet0 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet0.setDirection(Player.getCentre().getDegree());
			BulletList.add(bullet0);
			GameObject bullet1 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet1.setDirection(Player.getCentre().getDegree()-30);
			BulletList.add(bullet1);
			GameObject bullet2 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet2.setDirection(Player.getCentre().getDegree()+30);
			BulletList.add(bullet2);
			GameObject bullet3 = new GameObject("res/Bullet.png",15,60,new Point3f((float)(Player.getCentre().getX()),(float)(Player.getCentre().getY()),0.0f));
			bullet3.setDirection(Player.getCentre().getDegree()+180);
			BulletList.add(bullet3);
		}
		
	}


	public GameObject getPlayer01() {
		return Player01;
	}
	
	public GameObject getPlayer02() {
		return Player02;
	}

	public CopyOnWriteArrayList<GameObject> getEnemies() {
		return EnemiesList;
	}
	
	public CopyOnWriteArrayList<GameObject> getBullets() {
		return BulletList;
	}
	public CopyOnWriteArrayList<GameObject> getExplosions() {
		return ExplosionList;
	}
	public CopyOnWriteArrayList<GameObject> getPlayers() {
		return PlayerList;
	}
	public CopyOnWriteArrayList<GameObject> gethealthDrops() {
		return HealthDropList;
	}

	public int getScore() { 
		return Score;
	}
	

	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public boolean getLive() 
	{
		return live;
	}

	public int getStage() {
		// TODO Auto-generated method stub
		return stage;
	}
	
	
 

}


/* MODEL OF your GAME world 
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWNNNXXXKKK000000000000KKKXXXNNNWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWNXXK0OOkkxddddooooooolllllllloooooooddddxkkOO0KXXNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWNXK0OkxddooolllllllllllllllllllllllllllllllllllllllloooddxkO0KXNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXK0OkdooollllllllooddddxxxkkkOOOOOOOOOOOOOOOkkxxdddooolllllllllllooddxO0KXNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNK0kxdoollllllloddxkO0KKXNNNNWWWWWWMMMMMMMMMMMMMWWWWNNNXXK00Okkxdoollllllllloodxk0KNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXKOxdooolllllodxkO0KXNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWNXK0OkxdolllllolloodxOKXWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOxdoolllllodxO0KNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXKOkdolllllllloodxOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0kdolllllooxk0KNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNK0kdolllllllllodk0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0xdolllllodk0XNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWMMMMMMMMMMMWN0kdolllllllllodx0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0xoollllodxOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWMMMMMMMMMMWNXKOkkkk0WMMMMMMMMMMMMWNKkdolllloololodx0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWN0kdolllllox0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXK0kxk0KNWWWWNX0OkdoolllooONMMMMMMMMMMMMMMMWXOxolllllllollodk0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOdollllllllokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWN0xooollloodkOOkdoollllllllloxXWMMMMMMMMMMMMMMMWXkolllllllllllllodOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWN0koolllllllllllokNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxolllllllllllllllllllllllllllox0XWWMMMMMMMMMWNKOdoloooollllllllllllok0NWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0xoolllllllllllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxllolllllllllllllllllloollllllolodxO0KXNNNXK0kdoooxO0K0Odolllollllllllox0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOdolllllllllllllllllokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkolllllllllloolllllllllllllllllllolllloddddoolloxOKNWMMMWNKOxdolollllllllodOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMWXOdolllolllllllllllllloxKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxlllolllllloxkxolllllllllllllllllolllllllllllllxKWMWWWNNXXXKKOxoollllllllllodOXWMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMWXOdollllllllllllllllllllokNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOollllllllllxKNKOxooollolllllllllllllllllllolod0XX0OkxdddoooodoollollllllllllodOXWMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMN0xollllllllllllllllllllllld0NMMMMMMMMMMMMMMMMMMMMMMMWWNKKNMMMMMMMMMMMW0dlllllllllokXWMWNKkoloolllllllllllllllllllolokkxoolllllllllllllollllllllllllllox0NMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMWKxolllllllllllllllllllllllllloONMMMMMMMMMMMMMMMMMMMWNKOxdookNMMMMMMMMMWXkollllllodx0NWMMWWXkolooollllllllllllllllllllooollllllllllllllolllllllllllloooolloxKWMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMWXOdllllllllllllllooollllllllollld0WMMMMMMMMMMMMMMMMWXOxollllloOWMMMMMMMWNkollloodxk0KKXXK0OkdoollllllllllllllllllllllllllllllollllllllloollllllollllllllllllldOXWMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMN0xolllllllllllolllllllllllloodddddONMMMMMMMMMMMMMMMNOdolllllllokNMMMMMMWNkolllloddddddoooolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllox0NMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMWXkolllllllllllllllllllodxxkkO0KXNNXXXWMMMMMMMMMMMMMMNkolllllllllod0NMMMMMNOollllloollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllolllllllllllllokXWMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMWKxollllllllllllllllllox0NWWWWWMMMMMMMMMMMMMMMMMMMMMMW0dlllllllllllookKNWWNOolollloolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloxKWMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMN0dlllllllllllllllllllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkoloolllollllolloxO0Odllllllllllllllllllllllllllllllllllllllllllllollllllllllllllllllllllllllllllllllllllllllllllld0NWMMMMMMMMMMMMM
MMMMMMMMMMMMMXkolllllllllllllllllolllxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXOO0KKOdollllllllllooolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloONWMMMMMMMMMMMM
MMMMMMMMMMMWXkollllllllllllllllllllllxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWMMMMWNKOxoollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllokXWMMMMMMMMMMM
MMMMMMMMMMWKxollllllllllllllllllllllokNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWKxollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloxKWMMMMMMMMMM
MMMMMMMMMWKxollllllllllllodxkkkkkkkO0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMNKOkO0KK0OkdolllllloolllllllllllloooollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloxKWMMMMMMMMM
MMMMMMMMWKxllllllllllolodOXWWWWWWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxolloooollllllllllllllllloollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxKWMMMMMMMM
MMMMMMMWKxlllllllllollokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxololllllllooolloollllloolloooolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxKWMMMMMMM
MMMMMMWXxllllllllooodkKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMKdloollllllllllololodxxddddk0KK0kxxxdollolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxXWMMMMMM
MMMMMMXkolllllodk0KXNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMKdllollllllllllllodOXWWNXXNWMMMMWWWNX0xolollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllokNMMMMMM
MMMMMNOollllodONWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dooollllllllllllodOXNWWWWWWMMMMMMMMMWXOddxxddolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloONMMMMM
MMMMW0dllllodKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKKK0kdlllllllllllloodxxxxkkOOKNWMMMMMMWNNNNNXKOkdooooollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllld0WMMMM
MMMWKxllllloOWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkolllllollllllllllllllllodOKXWMMMMMMMMMMMMWNXKK0OOkkkxdooolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxKWMMM
MMMNkollllokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWXOdlllllolllllllllllloloolllooxKWMMMMMMMMMMMMMMMMMMMMWWWNXKOxoollllllllllllllllllllllllllllllllllllllllllllllllllllllllllolokNMMM
MMW0ollllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOOkxdollllllllllllllllllllllllllllox0NWMMMMWWNNXXKKXNWMMMMMMMMMWNKOxolllolllllllllllllllllllllllllllllllllllllllllllllllllllllllo0WMM
MMXxllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXkolllllllllllllllllllllllllllllllllllooxO000OkxdddoooodkKWMMMMMMMMMMWXxllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxXWM
MWOollllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXkollllllllllllllllllllllllllllllllllllllllllllllllllllllld0WMMMMMMMMMWKdlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloOWM
MXxllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXkollllllllllllllllllllllllllllllllllooollllllllllllllllllold0WMMMMMMWN0dolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxXM
W0ollllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNKkdolllllllllllllllllllllllllllllllllllllllllllllllllolllllllllokKXNWWNKkollllllllloxdollllllllolllllllllllllllllllllllllolllllllllllllolo0W
NkllllloxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllodxkkdoolollllllllxKOolllllllllllllllllllllllollooollllllloolllllloolllllkN
KxllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0doolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllkX0dlllllllllllllllllllloollloOKKOkxdddoollllllllllllllxK
Oolllllo0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxXXkollllooolllllllllllllllloONMMMWNNNXX0xolllllllllolloO
kolllllo0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllxXWXkollollllllllllllllllllodKMMMMMMMMMMWKxollllolollolok
kllllllo0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloolllllllllxXWWXkolllllllllllllllolllloONMMMMMMMMMMMW0dllllllllllllk
xollolld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxllllolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloollllllolloONMMN0xoolllllllolllllllloxXWMMMMMMMMMMMMXxollllllloollx
dollllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloollld0WMMWWXOdollollollllllloxXWMMMMMMMMMMMMMNOollllllokkold
olllllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNxlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllldONMMMMWXxollllolllllox0NWMMMMMMMMMMMMMMNOollllllxXOolo
llllllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXxllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloONMMMMMXxddxxxxkkO0XWMMMMMMMMMMMMMMMMMNOolllllxKW0olo
llllllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllldONWMMMWNXNNWWWMMMMMMMMMMMMMMMMMMMMMMMW0dllollOWW0oll
llllllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloxO0KXXXXKKKXNWMMMMMMMMMMMMMMMMMMMMMMMNOdolllkNWOolo
ollllllo0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllooooddooloodkKWMMMMMMMMMMMMMMMMMMMMMMWXOolldKNOooo
dollllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllloollllo0WMMMMMMMMMMMMMMMMMMMMMMMMXkold0Nkold
xollllloxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllollokNMMMMMMMMMMMMMMMMMMMMMMMMMWOookXXxolx
xolllllloONWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMN00XW0dlox
kollllllloxOKXXNNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOxollllllllllllllllllllllllllllllolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllolllllolo0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWOollk
OolllllllllloodddkKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOkkxddooooollllllllllooodxxdollolllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkoloO
KdllllllllllllllllxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWNXXXK0OOkkkkkkkkOKXXXNNX0xolllllllllllllllllllllllllllllllllllllllllllllllllllllllloollllllllloox0NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMKdlldK
NkllllollloolllllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWWWMMMMMMMMMWNOdlllllllllllllllllllllllllllllllllllllllllllllllllllllllollllllllodOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWOolokN
WOolllllllllllolllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxollllllllllllllllllllllllllllllllllllllllllllllllllllllllllllod0NWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXxolo0W
WXxllllllllllllllllox0NWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxollllllllllllllllllllllllllllllllllllllllllllllllllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOollxXM
MWOollllllllllllllooloxKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdllllllllllllllllllllllllllllllllllllllllllllllllllllloolld0NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdlloOWM
MWXxllolllllllllllllllldOXWWNNK00KXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllllllllllllllllllllllllllllllllllllllllllllllllllllllod0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxollxXWM
MMWOollllllllloollllllolodxkxdollodk0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOollllllllllllllllllllllllllllllllllllllllllllllllllodxOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWN0dlllo0WMM
MMMXxllolllllllllllllllllllllllllllloox0NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN0dooollllllllllllllllllllllllllllllllllllllllllllodOXNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKOkxxolllokNMMM
MMMW0dlllllllllllllllllllolllllllollollokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOdoolllllllllllllllllllllllllllllllllllllllllllxKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNOoollllllldKWMMM
MMMMNOollllllllllllllllllllllllllllllllloOWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXKOdolllllllllllllllllllllllllllllllllllllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOolllllllloOWMMMM
MMMMMXkollllllllllllllllllllllllllllllllokNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dlllllllllllllllllllllllllllllllllllllllld0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllolllllokNMMMMM
MMMMMWXxlllllllllllllllllllllllllllllllloxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0ollllllllllllllllllllllllllllllllllllllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWOollllllllxXWMMMMM
MMMMMMWKdlllllllllllllllllllllllllllllllokNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxolllllllllllllllllllllllllllllllllllllloONWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOolllllllxKWMMMMMM
MMMMMMMW0dlllllllllllllllllllllllllllllloOWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dlllloollllllllllllllllllllllllllllllllloxkOKKXXKKXNMMMMMMMMMMMMMMMMMMMMMMMMNOolllllldKWMMMMMMM
MMMMMMMMW0dllllllllllllllllllllllllllllldKMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdlllllllllllllllllllllllllllllllllllllllllllloooood0WMMMMMMMMMMMMMMMMMMMMMMMNOollolldKWMMMMMMMM
MMMMMMMMMW0dlllllllllllllllllllllllllllokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllllllllllllllllllllllllllllllolllllllllllllllllld0WMMMMMMMMMMMMMMMMMMMMMMWKxllllldKWMMMMMMMMM
MMMMMMMMMMW0dlllllllllllllllllllllllllloxXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkolllllllllllllllllllllllllllllllllllllllllllllllllxXMMMMMMMMMMMMMMMMMMMMMWXOdolllldKWMMMMMMMMMM
MMMMMMMMMMMWKxollllllllllllllllllllllllloOWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dlllllllllllllllllllllllllllllllloolllllllolllollllkNMMMMMMMMMMMMMMMMMMMWXOdolllloxKWMMMMMMMMMMM
MMMMMMMMMMMMWKxollllllllllllllllllllllllod0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkoloollllllllllllllllllllllllllllloddollllllllllllld0WMMMMMMMMMMMMMMMWWNKOdolllllokXWMMMMMMMMMMMM
MMMMMMMMMMMMMWXkollllllllllllllllllllllllldKMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllollllllllllllllllllllllllllllld0XOollllllllllllkNMMMMMMMMMMMMWNK0OkxollllllloONWMMMMMMMMMMMMM
MMMMMMMMMMMMMMMNOdlllllllllllllllllllllllokXMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN0dlllllllllllllllllllllllllolllld0NWN0dlllllloodxkKWMMMMMMMMMMMMNOollllllllllld0NMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMWKxolollllllllllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOolllllllllllllllllllllllllllldONMMMWKkdoooxOXNNWMMMMMMMMMMMMMNOollllllllllokXWMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMWXOdlllllllllllllllllloONWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdllllllllllllllllllllllllllld0NMMMMMWWXXXXNWMMMMMMMMMMMMMMMMW0dlllllllllod0NMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMWKxollolllllllllllloONMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMXxllllllllllllllllllllllllloxKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dlllllllllokXWMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMWNOdollllllloolllldKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkolllloollllooolllllllllodONWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNkllllllolox0NMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMWXkollllllolllllox0NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKdlllllollllllllllllllodkKWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW0dllllllodOXWMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMWKxoolllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN0dollllllllllooddxxk0KNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOdollllldOXWMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMN0xolllllllllllokXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKxolllllodk0KXNNWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKkdollolodkXWMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMNKxoolllllllllodOKNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWXOdolldOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0xoollllodkXWMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNKkolllollllllloxOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOx0WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOdolllllldOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKOdollllllllllodx0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOdoollllloxOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0xollollollollodxOXNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0kdooollllodk0NWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKkdooolllllllllooxOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOxdollllllloxOXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWN0kdllllllllollllodkOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWXKOkdoolllllloodOKNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0kdolllllllllllllodxO0XNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNX0OxdollloolllloxOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWX0kdoolllllllllllllooxkO0XNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWNX0OkxoololllllllooxOKNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNK0kdoolllllllllllloooodxkO0KXNWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWNXK0Okxdoolllllollllloxk0XWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNKOkdoollllllllloolllllloodxkkO00KXXNNWWWWWWMMMMMMMMMWWWWWWWNNXXK00Okxxdoolllllllllllloooxk0KNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNK0kxdoollllllllllllllllllllloodddxxxkkOOOOOOOOOOOkkkxxxdddoollllllllllllllllloodxO0XNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXK0OxdooollllllllllllooolllllllllllllllllllllllllllllllllllllllllllooodkO0KNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXK0OkxdooollllllllllllllllllllllllllllllllllllllllllloooddxkO0KXNWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNNXK0OOkkxdddoooooollllllllllllllllooooooddxxkOO0KKXNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWNNXXXKK00OOOOOOOOOOOOOOOO00KKXXXNNWWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 */

