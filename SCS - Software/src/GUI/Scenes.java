package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import application.AppControl;
import application.Main.Tangibles;
import software.SelfCheckoutSoftware;
import store.Store;

public class Scenes {
	
	public static final int SC_OVERVIEW = 0;
	public static final int AS_TOUCH = 1;
	public static final int SCS_OVERVIEW = 2;
	public static final int SCS_TOUCH = 3;
	public static final int SCS_CARDREADER = 4;
	public static final int SCS_MAINTENANCE = 5;
	
	// arbitrary application window size
	// Note: this resolution size must be at least the
	//  size of all scenes that have hard coded sizes
	private static final int xResolution = 1280;
	private static final int yResolution = 720;
	
	private final GUI gui;
	private JFrame filterFrame;
	
	// n self-checkout stations and 1 attendant station
	private final int totalNumberOfStations = Tangibles.SUPERVISION_STATION.supervisedStationCount() + 1;
	
	// reference to the latest station we interact with
	private int currentStation;
	
	private Color defaultBackground = new Color(220, 227, 230);

	public Scenes(GUI gui) {
		this.gui = gui;
	}
	
	public int getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(int currentStation) {
		this.currentStation = currentStation;
	}
	
	/**
	 * First applies a dimming filter to hide interaction with background
	 * scenes, then constructs the scene requested.
	 * @param scene - one of the following scenes:
	 * 		SC_OVERVIEW, AS_TOUCH, SCS_OVERVIEW, SCS_TOUCH, SCS_CARDREADER, SCS_MAINTENANCE
	 * @return
	 */
	public JFrame getScene(int scene) {
		addDimmingFilter();
		
		if (scene == SC_OVERVIEW) {
			
			return new SC_Overview_Scene().getScene();
			
		} else if (scene == AS_TOUCH) {
			
			return new AS_Touchscreen_Scene().getScene();
			
		} else if (scene == SCS_OVERVIEW) {
			
			return new SCS_Overview_Scene().getScene();
		} else if (scene == SCS_TOUCH) {
			
			return null;
		} else if (scene == SCS_CARDREADER) {
			
			return new SCS_Cardreader_Scene().getScene();
		} else if (scene == SCS_MAINTENANCE) {
			
			return null;
		}
		
		return null;
	}
	
	// Self-Checkout Overview Scene
	private class SC_Overview_Scene extends JFrame  implements ActionListener {
	
		// Self-Checkout Overview interactable componenets
		JButton[] sbn; // station buttons
		JButton abn; // attendant button
		
		public JFrame getScene() {
			// init the scene and retrieve the JPanel canvas in which to build on
			JPanel scene = preprocessScene(this, xResolution, yResolution);			

			// include a banner for navigation
			JPanel banner = generateBanner(scene);
			
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
			content.setBackground(defaultBackground);
			content.setLayout(null);
			
			// create panels to mark where the self checkout stations
			// will be in our scene.  Accompany them with buttons to 
			// provide a way to select the station.
			List<SelfCheckoutSoftware> scssList = Store.getSelfCheckoutSoftwareList();
			int numOfSCS = scssList.size();
			JPanel[] scs = new JPanel[numOfSCS];
			sbn = new JButton[numOfSCS];
			
			for (int i = 0; i < numOfSCS; i++) {
				// create an individual station panel
				scs[i] = new JPanel();
				scs[i].setLayout(null);
				scs[i].setBounds((175 * (i+1)) + 50, (60 * i) + 25, 150, 150);
				scs[i].setBackground(new Color((20 * i) + 100, (10 * i) + 120, 180));
				
				// create a button to select a particular station
				sbn[i] = new JButton();
				sbn[i].setText("Station " + (i+1));
				sbn[i].setBounds(45, 35, 60, 25);
				sbn[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				sbn[i].addActionListener(this);


				// We can actually set client attribute and pass it to action listener
				// Eg, pass in the self-checkout software object, so action listener can
				// directly do operation on the software object. Think this would make be
				// helpful and make a lot of logic easier to follow. -Yunfan FIXME:
				sbn[i].putClientProperty("station-id", i+1); // add one to store station not index
				sbn[i].putClientProperty("station-scss", scssList.get(i));

				// add button to the stations panel
				scs[i].add(sbn[i]);

				// add station panel to the main content panel
				content.add(scs[i]);
			}

			// create a panel for the attendant station
			JPanel as = new JPanel();
			as.setLayout(null);
			as.setBounds(125, 450, 220, 125);
			as.setBackground(new Color(190, 200, 180));

			// create a button to select the attendant station
			abn = new JButton();
			abn.setText("Attendant Station");
			abn.setBounds(40, 35, 140, 25);
			abn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			abn.addActionListener(this);
			as.add(abn); // add attendant selection button to it's panel

			// add the swing components to the main content panel
			content.add(as);

			// add the visual content to the scene
			scene.add(content);

			// to display frame, make visible after adding all components
			this.setVisible(true);

			// prompt the user to reply with what type of user they are
			int newUserType = (promptForUserType() == 0) ? AppControl.CUSTOMER : AppControl.ATTENDANT;
			gui.newUser(newUserType);

			return this;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == abn) { // attendant station
				setCurrentStation(0);
				gui.userApproachesStation(0);
			} else {
				// self-checkout stations
				
				// since we are accessing Void data and casting it to expected types
				// there is a possibility that this gets bad data if the ActionEvent
				// is not a JButton.  Something to keep in mind if an error occurs.
				int station = (int) ((JButton) e.getSource()).getClientProperty("station-id");

				setCurrentStation(station);
				gui.userApproachesStation(station);
			}
		}
	}

	
	// Self-Checkout Station Overview Scene
	private class SCS_Overview_Scene extends JFrame  implements ActionListener {

		// actionable components to add to listeners
		JButton bagScale;
		JButton bnInSlot;
		JButton bnOutSlot;
		JButton maintenance;
		JButton coinInSlot;
		JButton coinTray;
		JButton weighScale;
		JButton scanner;
		JButton handScanner;
		JButton cardReader;
		JButton printer;
		JButton touchscreen;
		
		public JFrame getScene() {
			// init the window
			JPanel scene = preprocessScene(this, 900, 600);

			// include a banner for navigation
			JPanel banner = generateBanner(scene);
			
			// Closing this scene means this user is no 
			// longer using the self-checkout station
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					gui.userLeavesStation(getCurrentStation());
					removeDimmingFilter();
				}
			});
			
			JPanel content = new JPanel();
			content.setBackground(defaultBackground);
			content.setLayout(null); 
			
			// bagging scale
			bagScale = new JButton();
			bagScale.setBounds(70, 350, 300, 140);
			bagScale.setText("Bagging Area");
			bagScale.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			bagScale.addActionListener(this);
			bagScale.setFocusable(false);
			content.add(bagScale);
			
			// banknote input slot
			bnInSlot = new JButton();
			bnInSlot.setBounds(400, 370, 110, 30);
			bnInSlot.setText("Banknote In");
			bnInSlot.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			bnInSlot.addActionListener(this);
			bnInSlot.setFocusable(false);
			content.add(bnInSlot);
			
			// banknote output slot
			bnOutSlot = new JButton();
			bnOutSlot.setBounds(540, 370, 110, 30);
			bnOutSlot.setText("Banknote Out");
			bnOutSlot.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			bnOutSlot.addActionListener(this);
			bnOutSlot.setFocusable(false);
			content.add(bnOutSlot);
			
			// maintenance hatch
			maintenance = new JButton();
			maintenance.setBounds(400, 420, 250, 100);
			maintenance.setText("Maintenance Hatch");
			maintenance.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
			maintenance.addActionListener(this);
			maintenance.setFocusable(false);
			content.add(maintenance);
			
			// coin input slot
			coinInSlot = new JButton();
			coinInSlot.setBounds(720, 370, 50, 70);
			coinInSlot.setText("Coin In");
			coinInSlot.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			coinInSlot.addActionListener(this);
			coinInSlot.setFocusable(false);
			content.add(coinInSlot);
			
			// coin tray
			coinTray = new JButton();
			coinTray.setBounds(695, 470, 100, 50);
			coinTray.setText("Coin Tray");
			coinTray.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			coinTray.addActionListener(this);
			coinTray.setFocusable(false);
			content.add(coinTray);
			
			// item weigh scale
			weighScale = new JButton();
			weighScale.setBounds(435, 250, 215, 90);
			weighScale.setText("Item Weigh Scale");
			weighScale.setBorder(BorderFactory.createLineBorder(Color.black, 3, true));
			weighScale.addActionListener(this);
			weighScale.setFocusable(false);
			content.add(weighScale);
			
			// stationary barcode scanner
			scanner = new JButton();
			scanner.setBounds(670, 250, 150, 90);
			scanner.setText("Barcode Scanner");
			scanner.setBorder(BorderFactory.createLineBorder(Color.black, 3, true));
			scanner.addActionListener(this);
			scanner.setFocusable(false);
			content.add(scanner);
			
			// handheld barcode scanner
			handScanner = new JButton();
			handScanner.setBounds(750, 100, 60, 90);
			handScanner.setText("<html>Handheld<br>Scanner</html>");
			handScanner.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			handScanner.addActionListener(this);
			handScanner.setFocusable(false);
			content.add(handScanner);
			
			// card reader
			cardReader = new JButton();
			cardReader.setBounds(670, 50, 60, 90);
			cardReader.setText("<html>Card<br>Reader</html>");
			cardReader.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			cardReader.addActionListener(this);
			cardReader.setFocusable(false);
			content.add(cardReader);
			
			// receipt printer
			printer = new JButton();
			printer.setBounds(670, 170, 60, 40);
			printer.setText("Receipt");
			printer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			printer.addActionListener(this);
			printer.setFocusable(false);
			content.add(printer);
			
			// touchscreen
			touchscreen = new JButton();
			touchscreen.setBounds(450, 50, 200, 140);
			touchscreen.setText("Touchscreen");
			touchscreen.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			touchscreen.addActionListener(this);
			touchscreen.setFocusable(false);
			content.add(touchscreen);
			
			// import self-checkout station model
			content.add(new BackgroundImage("src/GUI/images/selfcheckoutstation_model.png"));
			
			scene.add(content);
			
			this.setVisible(true);
			
			return this;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == bagScale) {
				gui.userBagsItem(currentStation);
			} else if (e.getSource() == bnInSlot) {
				gui.userInsertsBanknote(currentStation);
			} else if (e.getSource() == bnOutSlot) {
				gui.userRemovesBanknote(currentStation);
			} else if (e.getSource() == maintenance) {
				gui.userServicesStation(currentStation);
			} else if (e.getSource() == coinInSlot) {
				gui.userInsertsCoin(currentStation);
			} else if (e.getSource() == coinTray) {
				gui.userRemovesCoins(currentStation);
			} else if (e.getSource() == weighScale) {
				gui.userPlacesItemOnWeighScale(currentStation);
			} else if (e.getSource() == scanner) {
				gui.userScansItem(currentStation);
			} else if (e.getSource() == handScanner) {
				gui.userScansItem(currentStation);
			} else if (e.getSource() == cardReader) {
				gui.userAccessCardReader(currentStation);
			} else if (e.getSource() == printer) {
				gui.userRemovesReceipt(currentStation);
			} else if (e.getSource() == touchscreen) {
				gui.userAccessTouchscreen(currentStation);
			}
		}
	}
	
	// Attendant Station Touch Screen Scene
	private class AS_Touchscreen_Scene extends JFrame  implements ActionListener {
		
		// cosmetic panel colors
		Color tint_one = new Color(220, 230, 234);
		Color tint_two = new Color(205, 220, 230);
		
		JLabel[] station_status = new JLabel[Tangibles.SUPERVISION_STATION.supervisedStationCount()];
		JLabel[] station_light = new JLabel[Tangibles.SUPERVISION_STATION.supervisedStationCount()];
		JButton[] station_block  = new JButton[Tangibles.SUPERVISION_STATION.supervisedStationCount()];
		JButton[] station_approve = new JButton[Tangibles.SUPERVISION_STATION.supervisedStationCount()];

		public JFrame getScene() {
			JPanel scene = preprocessScene(this, 800, 650);

			JPanel banner = generateBanner(scene);
			
			// Closing this scene should not log out the attendant
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					removeDimmingFilter();
				}
			});

			JPanel content = new JPanel();
			content.setBackground(defaultBackground);
			content.setLayout(null); 
			
			Border border = BorderFactory.createLineBorder(Color.black, 2, true);
			
			// separate each station on it's own pseudo-banner for organization
			for (int i = 0; i < Tangibles.SUPERVISION_STATION.supervisedStationCount(); i++) {
			
				JPanel station = new JPanel();
				station.setLayout(null);
				station.setBounds(0, i * 100, 800, 100);
				station.setBackground((i % 2 == 0) ? tint_one : tint_two);
				
				// display the stations relavent status to the attendant
				station_status[i] = new JLabel();
				station_status[i].setFont(new Font("Lucida Grande", Font.BOLD, 16));
				station_status[i].setText(gui.stationStatus(i));
				station_status[i].setBounds(30, 10, 200, 80);
				station_status[i].setHorizontalAlignment(JLabel.CENTER);
				station.add(station_status[i]);
				
				Color station_light_color = checkStationAttention(i);
				
				// station light draws attention to erogenous status'
				station_light[i] = new JLabel();
				station_light[i].setBounds(260, 30, 40, 40);
				station_light[i].setBackground(station_light_color);
				station_light[i].setOpaque(true);
				station.add(station_light[i]);
				
				// display which station this banner is associated with
				JLabel station_label = new JLabel();
				station_label.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
				station_label.setText("STATION " + (i+1));
				station_label.setBounds(335, 10, 130, 80);
				station_label.setHorizontalAlignment(JLabel.CENTER);
				station_label.setBorder(border);
				station_label.setBackground((i % 2 == 0) ? tint_two : tint_one);
				station_label.setOpaque(true);
				station.add(station_label);
				
				// create a button for the attendant to block/unblock a station
				station_block[i] = new JButton();
				station_block[i].setBounds(485, 25, 130, 50);
				station_block[i].setFont(new Font("Lucida Grande", Font.BOLD, 12));
				station_block[i].setText(checkBlockStatus(i));
				station_block[i].addActionListener(this);
				station_block[i].setFocusable(false);
				station.add(station_block[i]);
				
				// create a button for the attendant to approve relevant erogenous states
				station_approve[i] = new JButton();
				station_approve[i].setBounds(635, 25, 130, 50);
				station_approve[i].setFont(new Font("Lucida Grande", Font.BOLD, 12));
				station_approve[i].setText("APPROVE");
				station_approve[i].addActionListener(this);
				station_approve[i].setFocusable(false);
				station.add(station_approve[i]);
				
				content.add(station);
			}
			
			scene.add(content);
			
			this.setVisible(true);
			
			addDimmingFilter();
			promptAttendantForLogIn();
			
			return this;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: time permitting - use setClientProperty to assign 
			// 		button station id's instead of looping through all stations
			for (int i = 0; i < Tangibles.SUPERVISION_STATION.supervisedStationCount(); i++) {
				if (e.getSource() == station_block[i]) {
					gui.attendantBlockToggle(i);
					station_block[i].setText(checkBlockStatus(i));
					station_status[i].setText(gui.stationStatus(i)); 
					station_light[i].setBackground(checkStationAttention(i));
				} else if (e.getSource() == station_approve[i]) {
					gui.attendantApproveStation(i);
					station_status[i].setText(gui.stationStatus(i)); 
					station_light[i].setBackground(checkStationAttention(i));
				}
			}
		}
	}
	
	// Self-Checkout Station Touch Screen Scene
	
	// Self-Checkout Station Card Reader Scene
	private class SCS_Cardreader_Scene extends JFrame  implements MouseListener {
		
		JLabel tap;
		JLabel swipe;
		JLabel insert;
		
		public JFrame getScene() {
			JPanel scene = preprocessScene(this, 250, 350);

			JPanel banner = generateBanner(scene);
			
			// Closing this scene means this user does not  
			// wish to use a card 
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					removeDimmingFilter();
				}
			});
			
			JPanel content = new JPanel();
			content.setBackground(defaultBackground);
			content.setLayout(null); 
		
			// card tap 
			tap = new JLabel();
			tap.setBounds(40, 40, 130, 110);
			tap.setText("TAP");
			tap.setOpaque(true);
			tap.setHorizontalAlignment(JLabel.CENTER);
			tap.addMouseListener(this);
			content.add(tap);
			
			// card swipe
			swipe = new JLabel();
			swipe.setBounds(190, 40, 20, 220);
			swipe.setText("<html>S<br>W<br>I<br>P<br>E</html>");
			swipe.setOpaque(true);
			swipe.setHorizontalAlignment(JLabel.CENTER);
			swipe.addMouseListener(this);
			content.add(swipe);
			
			// card insert
			insert = new JLabel();
			insert.setBounds(40, 190, 130, 70);
			insert.setText("INSERT");
			insert.setOpaque(true);
			insert.setHorizontalAlignment(JLabel.CENTER);
			insert.addMouseListener(this);
			content.add(insert);
			
			scene.add(content);
			
			this.setVisible(true);
			
			return this;
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == tap) {
				gui.userTapsCard(promptCustomerForCard());
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} else if (e.getSource() == swipe) {
				gui.userSwipesCard(promptCustomerForCard());
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} else if (e.getSource() == insert) {
				gui.userInsertCard(promptCustomerForCard());
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
	// Self-Checkout Station Maintenance Scene
	
	// cosmetic status indicator colors
	Color red_light = new Color(235, 80, 70);
	Color green_light = new Color(80, 225, 80);
	
	/**
	 * 
	 * @param station
	 * @return
	 */
	private Color checkStationAttention(int station) {
		return (gui.stationStatus(station) != "BLOCKED" && gui.stationStatus(station) != "WEIGHT DISCREPANCY" &&
				gui.stationStatus(station) != "MISSING ITEM") ? green_light : red_light;
	}
	
	/**
	 * 
	 * @param station
	 * @return
	 */
	private String checkBlockStatus(int station) {
		return (gui.stationStatus(station) == "BLOCKED") 
				? "UNBLOCK" : "BLOCK";
	}
	
	/**
	 * 
	 * @return
	 */
	private static int promptForUserType() {
		String[] userTypes = {"Customer", "Attendant" };
		return JOptionPane.showOptionDialog(null, "Are you a Customer or Attendant?", 
				"User?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, userTypes, 0); 
	}
	
	/**
	 * 
	 * @return
	 */
	private int promptCustomerForCard() {
		String[] cardTypes = {"CREDIT", "DEBIT", "MEMBERSHIP", "GIFT CARD" };
		return JOptionPane.showOptionDialog(null, "Which card will you use?", 
				"Card?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cardTypes, 0); 
	}
	
	/**
	 * Prompts the attendant for a log in number.
	 * Accepts literally any sequence of numbers.
	 */
	private void promptAttendantForLogIn() {
		// TODO: change to accept username + password
		// 		check if attendant is already logged in first
		new Keypad("ENTER LOG IN NUMBER");
	}
	
	private void promptCustomerForMemebership() {
		new Keypad("ENTER MEMBERSHIP NUMBER");
	}
	
	/**
	 * Initializes the scenes window size and attaches
	 * a JPanel with a Border Layout to use as a default
	 * canvas for the scenes components.
	 * @param f - the window to initilize
	 * @param x - the windows x-axis size
	 * @param y - the windows y-axis size
	 * @return JPanel attached to provided window
	 */
	private JPanel preprocessScene(JFrame f, int x, int y) {
		f.setSize(x, y);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		
		// Create a panel to take up the window and act as our canvas
		JPanel scene = new JPanel();
		scene.setLayout(new BorderLayout());
		
		// add the canvas to the frame
		f.add(scene);
		
		return scene;
	}
	
	/**
	 * 
	 * 
	 * @param p - panel with BorderLayout
	 * @return the banner created for any further customizations
	 */
	private JPanel generateBanner(JPanel p) {
		// top banner
		JPanel banner = new JPanel();
		banner.setPreferredSize(new Dimension(100, 50));
		banner.setLayout(new BorderLayout());
		banner.setBackground(Color.lightGray);
		
		JFrame window = (JFrame) SwingUtilities.getWindowAncestor(p);
		
		JButton exit = new JButton();
		exit.addActionListener(e -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)));
		exit.setFont(new Font("Arial", Font.BOLD, 20));
		exit.setText("X");
		exit.setFocusable(false);
		
		banner.add(exit, BorderLayout.EAST);
		
		p.add(banner, BorderLayout.NORTH);
		return banner;
	}
	
	/**
	 * 
	 */
	private void addDimmingFilter() {
		if (filterFrame != null) {
			removeDimmingFilter();
		}
		
		filterFrame = new JFrame();
		JPanel filter = preprocessScene(filterFrame, xResolution, yResolution);
		
		filter.setBackground(Color.black);
		
		filterFrame.setOpacity((float) 0.75);
		filterFrame.setFocusableWindowState(false);
		filterFrame.setVisible(true);
	}
	
	private void removeDimmingFilter() {
		filterFrame.removeAll();
	}
	
	/**
	 * Creates a JPanel that attachs a provided background image to it.
	 * Code copied from: https://mathbits.com/JavaBitsNotebook/Graphics/InsertBackground.html
	 * 
	 * @author joshuaplosz
	 *
	 */
	private class BackgroundImage extends JPanel {
		Image img;
		
		public BackgroundImage(String imgPath) {
			ImageIcon icon = new ImageIcon(imgPath);
			img = icon.getImage();
			this.setBounds(0, 0, img.getWidth(null), img.getHeight(null));
			this.setOpaque(false);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, null);
		}
	}
}
