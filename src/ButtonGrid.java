import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame; //imports JFrame library
import javax.swing.JButton; //imports JButton library

import java.awt.GridLayout; import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
//imports GridLayout library
import java.io.File;
import java.io.IOException;
 
public class ButtonGrid {
 
        JFrame frame=new JFrame(); //creates frame
        JButton[][] grid; //names the grid of buttons
        
        //BufferedImage buttonIcon = ImageIO.read(new File("buttonIconPath"));
        //button = new JButton(new ImageIcon(buttonIcon));
        
        
        
 
        public ButtonGrid(int width, int length, Board board){ //constructor
                frame.setLayout(new GridLayout(width,length)); //set layout
                grid=new JButton[width][length]; //allocate the size of grid
                for(int y=0; y<length; y++){
                        for(int x=0; x<width; x++){
                        	
                        	
                                //grid[x][y]=new JButton("("+x+","+y+")"); //creates new button 
                        		//grid[x][y]=new JButton("~"); //creates new button   
                        	if (board.boardArrayChar[y][x] == 'S'){
                        		grid[x][y] = new JButton();
	                        	  try {
	                        	    Image img = ImageIO.read(getClass().getResource("ship.jpg"));
	                        	    grid[x][y].setIcon(new ImageIcon(img));
	                        	  } catch (IOException ex) {
	                        	  }
                              frame.add(grid[x][y]); //adds button to grid
                        	} else if (board.boardArrayChar[y][x] == '~'){
                        		grid[x][y] = new JButton();
	                        	  try {
	                        	    Image img = ImageIO.read(getClass().getResource("blue.PNG"));
	                        	    grid[x][y].setIcon(new ImageIcon(img));
	                        	  } catch (IOException ex) {
	                        	  }
                              frame.add(grid[x][y]); //adds button to grid
                        	}
                        	
                        	grid[x][y].addActionListener(new ActionListener() {

                      		  public void actionPerformed(ActionEvent event) {
                      		    //do whatever should happen when the button is clicked...
                      			  System.out.println("WORKING!!!!!!");
                      		  }

                      		});
//                        	  grid[x][y] = new JButton();
//	                        	  try {
//	                        	    Image img = ImageIO.read(getClass().getResource("blue.PNG"));
//	                        	    grid[x][y].setIcon(new ImageIcon(img));
//	                        	  } catch (IOException ex) {
//	                        	  }
//                                frame.add(grid[x][y]); //adds button to grid
                        }
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800,800);
                //frame.pack(); //sets appropriate size for frame
                frame.setVisible(true); //makes frame visible
        }
//        public static void main(String[] args) {
//                new ButtonGrid(3,3, );//makes new ButtonGrid with 2 parameters
//        }
}