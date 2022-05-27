import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



import util.GameObject;



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
 
 * Credits: Kelly Charles (2020)
 * Modified by Jiaheng Guo£¬
   20210848
 */ 
public class Viewer extends JPanel {
	private long CurrentAnimationTime= 0;
	
	
	Model gameworld =new Model(); 
	 
	public Viewer(Model World) {
		this.gameworld=World;
		// TODO Auto-generated constructor stub
	}

	public Viewer(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public Viewer(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public Viewer(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public void updateview() {
		
		this.repaint();
		// TODO Auto-generated method stub
		
	}
	
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		CurrentAnimationTime++; // runs animation time step 
		
		
	
		
		int health01 = gameworld.getPlayer01().getHealth();
		
		
		int health02 = gameworld.getPlayer02().getHealth();
		
		//Draw background 
		drawBackground(g);
		
		//Draw player
		gameworld.getPlayers().forEach((temp) -> 
		{ 
			drawPlayer((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),temp.getDirection(),g);	 
		}); 
		
		//Draw Bullets 
		// change back 
		gameworld.getBullets().forEach((temp) -> 
		{ 
			drawBullet((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),temp.getDirection(),g);	 
		}); 
		
		//Draw Enemies   
		gameworld.getEnemies().forEach((temp) -> 
		{
			drawEnemies((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),temp.getDirection(),g);	 
		 
	    }); 
		gameworld.getExplosions().forEach((temp) -> 
		{
			drawExplosion(temp,(int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),g);	 
		 
	    }); 
		gameworld.gethealthDrops().forEach((temp) -> 
		{
			drawhealthDrops(temp,(int) temp.getCentre().getX(), (int) temp.getCentre().getY(), (int) temp.getWidth(), (int) temp.getHeight(), temp.getTexture(),g);	 
		 
	    }); 
		
		//Draw Health
		drawHealth01(health01,g);
		drawHealth02(health02,g);
        //Draw Stage
		drawStage(gameworld.getStage(),g);
	}
	
	private void drawStage(int stage, Graphics g) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				File TextureToLoad = new File("res/numbers.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
				try {
					Image myImage = ImageIO.read(TextureToLoad);
					if(stage==6||stage==7) {
						g.drawImage(myImage, 1230,20, 1280, 75, 0 , 372, 331, 744, null);
					}
					else {
						g.drawImage(myImage, 1230,20, 1280, 75, (stage-1)*331 , 0, (stage)*331, 372, null);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}



	private void drawhealthDrops(GameObject temp, int x, int y, int width, int height, String texture, Graphics g) {
		File TextureToLoad = new File(texture);
		try {
			
            //Delete the object after the explosion animation ends			
			
			Image myImage = ImageIO.read(TextureToLoad);
			
			
			g.drawImage(myImage, x,y, x+width, y+height, 0 , 0, 519, 463, null);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void drawHealth01(int health,Graphics g)
	{
		File TextureToLoad = new File("res/health.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			int i = 0;
			while(i<health) {
				g.drawImage(myImage,20+i*50,20,40,40,null);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawHealth02(int health,Graphics g)
	{
		File TextureToLoad = new File("res/health.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			int i = 0;
			while(i<health) {
				g.drawImage(myImage,1180-i*50,20,40,40,null);
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawEnemies(int x, int y, int width, int height, String texture,double degree, Graphics g) {
		
		try {
			File TextureToLoad = new File(texture);
			Image myImage = ImageIO.read(TextureToLoad);
			BufferedImage subImage = ((BufferedImage) myImage).getSubimage(50,80,230,120);
			
			
			
			g.drawImage(rotate (subImage,degree), x,y, width, height, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private void drawBackground(Graphics g)
	{
		File TextureToLoad = new File("res/back.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
		try {
			Image myImage = ImageIO.read(TextureToLoad);
			if((0+CurrentAnimationTime)%1980<(520+CurrentAnimationTime)%1980) {
			 g.drawImage(myImage, 0,0, 1300, 800,(int)(0+CurrentAnimationTime)%1980 , 500, (int)(520+CurrentAnimationTime)%1980, 820, null); 
			}
			else {
				g.drawImage(myImage, 0,0, 1300, 800,(int)(520+CurrentAnimationTime)%1980 , 500, (int)(1040+CurrentAnimationTime)%1980, 820, null); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawBullet(int x, int y, int width, int height, String texture,double degree,Graphics g)
	{
		File TextureToLoad = new File(texture);  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE 
		try {
			Image myImage = ImageIO.read(TextureToLoad); 
			//64 by 128 
			BufferedImage subImage = ((BufferedImage) myImage).getSubimage(70,80,30,120);
			
			double sin = Math.abs(Math.sin(Math.toRadians(degree%360))),
				       cos = Math.abs(Math.cos(Math.toRadians(degree%360)));
				
			    int neww = (int) Math.floor(width*cos + height*sin),
			        newh = (int) Math.floor(height*cos + width*sin);
			 g.drawImage(rotate(subImage,degree), x,y, neww, newh, null); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawExplosion(GameObject t,int x, int y, int width, int height, String texture,Graphics g)
	{
		File TextureToLoad = new File(texture);
		try {
			
            //Delete the object after the explosion animation ends			
			
			Image myImage = ImageIO.read(TextureToLoad);
			
			
			g.drawImage(myImage, x,y, x+width, y+height, t.getSurvivalTime()/2%6*32 , 0, t.getSurvivalTime()/2*32+31, 32, null);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private void drawPlayer(int x, int y, int width, int height, String texture,double degree,Graphics g) { 
		
		try {
			File TextureToLoad = new File(texture);
			Image myImage = ImageIO.read(TextureToLoad);
			BufferedImage subImage = ((BufferedImage) myImage).getSubimage(30,30,200,210);
			
			
			double sin = Math.abs(Math.sin(Math.toRadians(degree%360))),
			       cos = Math.abs(Math.cos(Math.toRadians(degree%360)));
			
		    int neww = (int) Math.floor(width*cos + height*sin),
		        newh = (int) Math.floor(height*cos + width*sin);
			g.drawImage(rotate (subImage,degree), x,y, neww, newh, null); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		//g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer));
		//Lighnting Png from https://opengameart.org/content/animated-spaceships  its 32x32 thats why I know to increament by 32 each time 
		// Bullets from https://opengameart.org/forumtopic/tatermands-art 
		// background image from https://www.needpix.com/photo/download/677346/space-stars-nebula-background-galaxy-universe-free-pictures-free-photos-free-images
		
	}
	public static BufferedImage rotate(BufferedImage bimg, Double angle) {
	    double sin = Math.abs(Math.sin(Math.toRadians(angle))),
	           cos = Math.abs(Math.cos(Math.toRadians(angle)));
	    int w = bimg.getWidth();
	    int h = bimg.getHeight();
	    int neww = (int) Math.floor(w*cos + h*sin),
	        newh = (int) Math.floor(h*cos + w*sin);
	    BufferedImage rotated = new BufferedImage(neww, newh, bimg.getType());
	    Graphics2D graphic = rotated.createGraphics();
	    graphic.translate((neww-w)/2, (newh-h)/2);
	    graphic.rotate(Math.toRadians(angle), w/2, h/2);
	    graphic.drawRenderedImage(bimg, null);
	    graphic.dispose();
	    return rotated;
	}
		 
	 

}


/*
 * 
 * 
 *              VIEWER HMD into the world                                                             
                                                                                
                                      .                                         
                                         .                                      
                                             .  ..                              
                               .........~++++.. .  .                            
                 .   . ....,++??+++?+??+++?++?7ZZ7..   .                        
         .   . . .+?+???++++???D7I????Z8Z8N8MD7I?=+O$..                         
      .. ........ZOZZ$7ZZNZZDNODDOMMMMND8$$77I??I?+?+=O .     .                 
      .. ...7$OZZ?788DDNDDDDD8ZZ7$$$7I7III7??I?????+++=+~.                      
       ...8OZII?III7II77777I$I7II???7I??+?I?I?+?+IDNN8??++=...                  
     ....OOIIIII????II?I??II?I????I?????=?+Z88O77ZZO8888OO?++,......            
      ..OZI7III??II??I??I?7ODM8NN8O8OZO8DDDDDDDDD8DDDDDDDDNNNOZ= ......   ..    
     ..OZI?II7I?????+????+IIO8O8DDDDD8DNMMNNNNNDDNNDDDNDDNNNNNNDD$,.........    
      ,ZII77II?III??????DO8DDD8DNNNNNDDMDDDDDNNDDDNNNDNNNNDNNNNDDNDD+.......   .
      7Z??II7??II??I??IOMDDNMNNNNNDDDDDMDDDDNDDNNNNNDNNNNDNNDMNNNNNDDD,......   
 .  ..IZ??IIIII777?I?8NNNNNNNNNDDDDDDDDNDDDDDNNMMMDNDMMNNDNNDMNNNNNNDDDD.....   
      .$???I7IIIIIIINNNNNNNNNNNDDNDDDDDD8DDDDNM888888888DNNNNNNDNNNNNNDDO.....  
       $+??IIII?II?NNNNNMMMMMDN8DNNNDDDDZDDNN?D88I==INNDDDNNDNMNNMNNNNND8:..... 
   ....$+??III??I+NNNNNMMM88D88D88888DDDZDDMND88==+=NNNNMDDNNNNNNMMNNNNND8......
.......8=+????III8NNNNMMMDD8I=~+ONN8D8NDODNMN8DNDNNNNNNNM8DNNNNNNMNNNNDDD8..... 
. ......O=??IIIIIMNNNMMMDDD?+=?ONNNN888NMDDM88MNNNNNNNNNMDDNNNMNNNMMNDNND8......
........,+++???IINNNNNMMDDMDNMNDNMNNM8ONMDDM88NNNNNN+==ND8NNNDMNMNNNNNDDD8......
......,,,:++??I?ONNNNNMDDDMNNNNNNNNMM88NMDDNN88MNDN==~MD8DNNNNNMNMNNNDND8O......
....,,,,:::+??IIONNNNNNNDDMNNNNNO+?MN88DN8DDD888DNMMM888DNDNNNNMMMNNDDDD8,.... .
...,,,,::::~+?+?NNNNNNNMD8DNNN++++MNO8D88NNMODD8O88888DDDDDDNNMMMNNNDDD8........
..,,,,:::~~~=+??MNNNNNNNND88MNMMMD888NNNNNNNMODDDDDDDDND8DDDNNNNNNDDD8,.........
..,,,,:::~~~=++?NMNNNNNNND8888888O8DNNNNNNMMMNDDDDDDNMMNDDDOO+~~::,,,.......... 
..,,,:::~~~~==+?NNNDDNDNDDNDDDDDDDDNNND88OOZZ$8DDMNDZNZDZ7I?++~::,,,............
..,,,::::~~~~==7DDNNDDD8DDDDDDDD8DD888OOOZZ$$$7777OOZZZ$7I?++=~~:,,,.........   
..,,,,::::~~~~=+8NNNNNDDDMMMNNNNNDOOOOZZZ$$$77777777777II?++==~::,,,......  . ..
...,,,,::::~~~~=I8DNNN8DDNZOM$ZDOOZZZZ$$$7777IIIIIIIII???++==~~::,,........  .  
....,,,,:::::~~~~+=++?I$$ZZOZZZZZ$$$$$777IIII?????????+++==~~:::,,,...... ..    
.....,,,,:::::~~~~~==+?II777$$$$77777IIII????+++++++=====~~~:::,,,........      
......,,,,,:::::~~~~==++??IIIIIIIII?????++++=======~~~~~~:::,,,,,,.......       
.......,,,,,,,::::~~~~==+++???????+++++=====~~~~~~::::::::,,,,,..........       
.........,,,,,,,,::::~~~======+======~~~~~~:::::::::,,,,,,,,............        
  .........,.,,,,,,,,::::~~~~~~~~~~:::::::::,,,,,,,,,,,...............          
   ..........,..,,,,,,,,,,::::::::::,,,,,,,,,.,....................             
     .................,,,,,,,,,,,,,,,,.......................                   
       .................................................                        
           ....................................                                 
               ....................   .                                         
                                                                                
                                                                                
                                                                 GlassGiant.com
                                                                 */
