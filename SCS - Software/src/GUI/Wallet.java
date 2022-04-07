package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class Wallet extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Wallet dialog = new Wallet();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Wallet() {
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel nickelLabel = new JLabel("nickel");
			ImageIcon nickel = new ImageIcon("src/GUI/images/nickel.png");
			nickelLabel.setIcon(nickel);
			nickelLabel.setBounds(35, 162, 65, 65);
			contentPanel.add(nickelLabel);
		}
		{
			JLabel dimeLabel = new JLabel("dime");
			ImageIcon dime = new ImageIcon("src/GUI/images/dime.png");
			dimeLabel.setIcon(dime);
			dimeLabel.setBounds(119, 162, 65, 65);
			contentPanel.add(dimeLabel);
		}
		{
			JLabel lblQuarter = new JLabel("quarter");
			ImageIcon quarter = new ImageIcon("src/GUI/images/quarter.png");
			lblQuarter.setIcon(quarter);
			lblQuarter.setBounds(196, 162, 65, 65);
			contentPanel.add(lblQuarter);
		}
		{
			JLabel lblLoonie = new JLabel("loonie");
			ImageIcon loonie = new ImageIcon("src/GUI/images/loonie.png");
			lblLoonie.setIcon(loonie);
			lblLoonie.setBounds(273, 162, 65, 65);
			contentPanel.add(lblLoonie);
		}
		{
			JLabel lblToonie = new JLabel("toonie");
			ImageIcon toonie = new ImageIcon("src/GUI/images/toonie.png");
			lblToonie.setIcon(toonie);
			lblToonie.setBounds(350, 162, 65, 65);
			contentPanel.add(lblToonie);
		}
		{
			JLabel lblNewLabel = new JLabel("Wallet");
			lblNewLabel.setBounds(200, 6, 61, 16);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel fiveDollar = new JLabel("fiveDollar");
			ImageIcon five = new ImageIcon("src/GUI/images/fiveDollar.jpg");
			fiveDollar.setIcon(five);
			fiveDollar.setBounds(35, 35, 102, 47);
			contentPanel.add(fiveDollar);
		}
		{
			JLabel tenDollar = new JLabel("tenDollar");
			ImageIcon ten = new ImageIcon("src/GUI/images/tenDollar.jpeg");
			tenDollar.setIcon(ten);
			tenDollar.setBounds(174, 34, 102, 47);
			contentPanel.add(tenDollar);
		}
		{
			JLabel twentyDollar = new JLabel("twentyDollar");
			ImageIcon twenty = new ImageIcon("src/GUI/images/twentyDollar.jpg");
			twentyDollar.setIcon(twenty);
			twentyDollar.setBounds(313, 35, 102, 47);
			contentPanel.add(twentyDollar);
		}
		{
			JLabel fiftyDollar = new JLabel("fiftyDollar");
			ImageIcon fifty = new ImageIcon("src/GUI/images/fiftyDollar.jpg");
			fiftyDollar.setIcon(fifty);
			fiftyDollar.setBounds(111, 94, 102, 47);
			contentPanel.add(fiftyDollar);
		}
		{
			JLabel hundredDollar = new JLabel("hundredDollar");
			ImageIcon hundred = new ImageIcon("src/GUI/images/hundredDollar.jpg");
			hundredDollar.setIcon(hundred);
			hundredDollar.setBounds(250, 93, 102, 47);
			contentPanel.add(hundredDollar);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
