package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class CoinWallet extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CoinWallet dialog = new CoinWallet();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CoinWallet() {
		setBounds(100, 100, 135, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel nickelLabel = new JLabel("nickel");
			ImageIcon nickel = new ImageIcon("src/GUI/images/nickel.png");
			nickelLabel.setIcon(nickel);
			nickelLabel.setBounds(25, 33, 84, 84);
			contentPanel.add(nickelLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Coin Wallet");
			lblNewLabel_1.setBounds(31, 6, 91, 16);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblDime = new JLabel("dime");
			ImageIcon dime = new ImageIcon("src/GUI/images/dime.png");
			lblDime.setIcon(dime);
			lblDime.setBounds(25, 126, 84, 84);
			contentPanel.add(lblDime);
		}
		{
			JLabel lblQuarter = new JLabel("quarter");
			ImageIcon quarter = new ImageIcon("src/GUI/images/quarter.png");
			lblQuarter.setIcon(quarter);
			lblQuarter.setBounds(25, 219, 84, 84);
			contentPanel.add(lblQuarter);
		}
		{
			JLabel lblLoonie = new JLabel("loonie");
			ImageIcon loonie = new ImageIcon("src/GUI/images/loonie.png");
			lblLoonie.setIcon(loonie);
			lblLoonie.setBounds(25, 317, 84, 84);
			contentPanel.add(lblLoonie);
		}
		{
			JLabel lblToonie = new JLabel("toonie");
			ImageIcon toonie = new ImageIcon("src/GUI/images/toonie.png");
			lblToonie.setIcon(toonie);
			lblToonie.setBounds(25, 422, 84, 84);
			contentPanel.add(lblToonie);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Insert");
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
