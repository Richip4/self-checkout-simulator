package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import store.Store;

public class Scenes {
	
	public static final int SC_OVERVIEW = 0;
	public static final int AS_TOUCH = 1;
	public static final int SCS_OVERVIEW = 2;
	public static final int SCS_TOUCH = 3;
	public static final int SCS_KEYBOARD = 4;
	public static final int SCS_CARDREADER = 5;
	public static final int SCS_MAINTENANCE = 6;
	
	private GUI gui;
	private int currentStation;

	public Scenes(GUI gui) {
		this.gui = gui;
	}
	
	public int getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(int currentStation) {
		this.currentStation = currentStation;
	}
	
	public JFrame getScene(int scene) {
		if (scene == SC_OVERVIEW) {
			
			return new SC_Overview_Scene().getScene();
			
		} else if (scene == AS_TOUCH) {
			
			return null;
		} else if (scene == SCS_OVERVIEW) {
			
			return new SCS_Overview_Scene().getScene();
		} else if (scene == SCS_TOUCH) {
			
			return null;
		} else if (scene == SCS_KEYBOARD) {
			
			return null;
		} else if (scene == SCS_CARDREADER) {
			
			return null;
		} else if (scene == SCS_MAINTENANCE) {
			
			return null;
		}
		
		return null;
	}

	// Self-Checkout Overview interactable componenets
	JButton[] sbn; // station buttons
	JButton abn; // attendant button
	
	// Self-Checkout Overview Scene
	private class SC_Overview_Scene extends JFrame  implements ActionListener {
		
		public JFrame getScene() {
			// init the window
			preProcessScene(this, 1280, 720);

			// include a banner for navigation
			JPanel banner = generateBanner(this);
			
			// This overview scene should be the only scene to 
			// terminate the actual program.  Set a window
			// listener here to exit when scene is exitted.
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
			// main content panel of the scene
			// contains the visually interactable 
			// components in the scene.
			JPanel content = new JPanel();
			content.setSize(1280, 670);
			content.setBackground(new Color(220, 227, 230));
			content.setLayout(new BorderLayout());
			
			// create panels to mark where the self checkout stations
			// will be in our scene.  Accompany them with buttons to 
			// provide a way to select the station.
			int numOfSCS = 6;	// <---- get number from supervision station
			JPanel[] scs = new JPanel[numOfSCS];
			sbn = new JButton[numOfSCS];
			
			for (int i = 0; i < numOfSCS; i++) {
				// create an individual station panel
				scs[i] = new JPanel();
				scs[i].setLayout(null);
				scs[i].setBounds((175 * (i+1)) + 50, (60 * i) + 25, 150, 150);
				scs[i].setBackground(new Color((20 * i) + 100, (10 * i) + 120, 180));
				
				// create the button and add it to the stations panel
				sbn[i] = new JButton();
				sbn[i].setText("Station " + (i+1));
				sbn[i].setBounds(45, 35, 60, 25);
				sbn[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				sbn[i].addActionListener(this);

				// add button to the stations panel
				scs[i].add(sbn[i]);
				
				// add station panel to the content as a whole
				content.add(scs[i]);
			}
			
			// create a panel for the attendant station 
			// along with a button for accessing it
			JPanel as = new JPanel();
			as.setLayout(null);
			as.setBounds(125, 450, 220, 125);
			as.setBackground(new Color(190, 200, 180));
			
			abn = new JButton();
			abn.setText("Attendant Station");
			abn.setBounds(40, 35, 140, 25);
			abn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			abn.addActionListener(this);
			as.add(abn);
			
			content.add(as);
			
			// the last component in a panel will try to take
			// up the remaining space in the panel.  Adding
			// this empty label to essential fill the panels
			// remaining space with nothing.
			content.add(new JLabel());
			
			// add the visual content to the frame
			this.add(content);
			
			this.setVisible(true);
			
			return this;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == sbn[0]) {
				setCurrentStation(1);
				gui.userApproachesStation(1);
			} else if (e.getSource() == sbn[1]) {
				setCurrentStation(2);
				gui.userApproachesStation(2);
			} else if (e.getSource() == sbn[2]) {
				setCurrentStation(3);
				gui.userApproachesStation(3);
			} else if (e.getSource() == sbn[3]) {
				setCurrentStation(4);
				gui.userApproachesStation(4);
			} else if (e.getSource() == sbn[4]) {
				setCurrentStation(5);
				gui.userApproachesStation(5);
			} else if (e.getSource() == sbn[5]) {
				setCurrentStation(6);
				gui.userApproachesStation(6);
			} else if (e.getSource() == abn) {
				setCurrentStation(0);
				gui.userApproachesStation(0);
			}
		}
		
	}
	
	// Attendant Station Touch Screen Scene
	private class SCS_Overview_Scene extends JFrame  implements ActionListener {

		public JFrame getScene() {
			// init the window
			preProcessScene(this, 900, 600);

			// include a banner for navigation
			JPanel banner = generateBanner(this);
			
			// Closing this scene means this user is no 
			// longer using the self-checkout station
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					gui.userLeavesStation(getCurrentStation());
				}
			});
			
			this.setVisible(true);
			
			return this;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	// Self-Checkout Station Overview Scene
	
	// Self-Checkout Station Touch Screen Scene
	
	// Self-Checkout Station Keyboard Scene
	
	// Self-Checkout Station Card Reader Scene
	
	// Self-Checkout Station Maintenance Scene
	
	private void preProcessScene(JFrame f, int x, int y) {
		f.setSize(x, y);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
	}
	
	private JPanel generateBanner(JFrame f) {
		// top banner
		JPanel banner = new JPanel();
		banner.setPreferredSize(new Dimension(100, 50));
		banner.setLayout(new BorderLayout());
		banner.setBackground(Color.lightGray);
		
		JButton exit = new JButton();
		exit.addActionListener(e -> f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING)));
		exit.setFont(new Font("Arial", Font.BOLD, 20));
		exit.setText("X");
		exit.setFocusable(false);
		
		banner.add(exit, BorderLayout.EAST);
		
		f.add(banner, BorderLayout.NORTH);
		return banner;
	}
}
