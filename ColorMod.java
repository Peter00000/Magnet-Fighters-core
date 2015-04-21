package Magnet_Fighter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColorMod /*extends JPanel*/{

	public BufferedImage img;
	public ArrayList<Integer> colors = new ArrayList<Integer>();
	
	/**Default constructor
	 * 
	 * @param fileName
	 * @return retouched image
	 */
	public ColorMod(String fileName){
		
		/*Read the image file*/
		try		{img = ImageIO.read(new File("assets/"+fileName));} 
		catch (IOException e) 
		{e.printStackTrace();}
		
		/*Gather all unique colors*/
		gatherColors();
		
		/*Display the colors*/
//		for(Integer i:colors){
//			System.out.println(Integer.toHexString(i));
//			JButton button = new JButton(""+Integer.toHexString(i));
//			button.setBackground(new Color(i));
//			this.add(button);
//		}
//		
//		this.swapColor(this.getColors()[3], Color.orange);
//		JLabel picLabel = new JLabel(new ImageIcon(img));
//		this.add(picLabel);	
	}
	
	/**Alternative constructor
	 * 
	 * @param image
	 */
	
	public ColorMod(BufferedImage image){
		img = image;
		gatherColors();
	}
	
	/*Gathers all of the colors in the image*/
	public void gatherColors(){
		boolean match = false;
		for(int y=0; y<img.getHeight(); y++)
			for(int x=0; x<img.getWidth();x++){
				for(int c=0; c<colors.size(); c++)
					if(colors.get(c) == img.getRGB(x, y)) match = true;
				if(!match) colors.add(img.getRGB(x, y));
				match = false;
			}
	}
	
	/*Get the array of colors in the picture as integers*/
	public int[] getColors(){
		int[] temp = new int[colors.size()];
		for(int i=0; i<colors.size(); i++)
			temp[i] = (int)colors.get(i);
		return temp;
	}
	
	/*Get the image*/
	public BufferedImage getImg()
	{return img;}
	
	/*return monotone image*/
	public BufferedImage getImg(Color c){
		int rgb = c.getRGB();
		BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for(int y=0; y<img.getHeight(); y++)
			for(int x=0; x<img.getWidth();x++)
				if(img.getRGB(x, y) != 0x00ffffff)	temp.setRGB(x, y, rgb);
		return temp;
	}
	
	/*Swap the colors in the picture*/
	public void swapColor(int swapFrom, Color swapTo){
		int rgb = swapTo.getRGB();
		for(int y=0; y<img.getHeight(); y++)
			for(int x=0; x<img.getWidth();x++)
				if(img.getRGB(x, y) == swapFrom) img.setRGB(x, y, rgb);
	}
	
//	/*Main method to display the colors for debugging*/
//	public static void main(String[] args)
//	{
//		JFrame myframe = new JFrame("Color test");
//		ColorMod trial = new ColorMod("human.png");
//		myframe.add(trial);
//		myframe.setSize(1000, 700);
//		myframe.setVisible(true);
//		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
}
