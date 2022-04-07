package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class BanknoteWallet extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BanknoteWallet dialog = new BanknoteWallet();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BanknoteWallet() {
		setResizable(false);
		setBounds(100, 100, 268, 510);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Banknote Wallet");
			lblNewLabel.setBounds(82, 6, 105, 16);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel fiveDollar = new JLabel("five");
			ImageIcon five = new ImageIcon("src/GUI/images/fiveDollar.jpg");
			fiveDollar.setIcon(five);
			fiveDollar.setBounds(46, 44, 179, 64);
			contentPanel.add(fiveDollar);
		}
		{
			JLabel tenDollar = new JLabel("ten");
			ImageIcon ten = new ImageIcon("src/GUI/images/tenDollar.jpg");
			tenDollar.setIcon(ten);
			tenDollar.setBounds(46, 120, 179, 64);
			contentPanel.add(tenDollar);
		}
		{
			JLabel twentyDollar = new JLabel("twenty");
			ImageIcon twenty = new ImageIcon("src/GUI/images/twentyDollar.jpg");
			twentyDollar.setIcon(twenty);
			twentyDollar.setBounds(46, 196, 179, 64);
			contentPanel.add(twentyDollar);
		}
		{
			JLabel fiftyDollar = new JLabel("fifty");
			ImageIcon fifty = new ImageIcon("src/GUI/images/fiftyDollar.jpg");
			fiftyDollar.setIcon(fifty);
			fiftyDollar.setBounds(46, 272, 179, 64);
			contentPanel.add(fiftyDollar);
		}
		{
			JLabel hundredDollar = new JLabel("hundred");
			ImageIcon hundred = new ImageIcon("src/GUI/images/hundredDollar.jpg");
			hundredDollar.setIcon(hundred);
			hundredDollar.setBounds(46, 348, 179, 64);
			contentPanel.add(hundredDollar);
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
		}
	}

}
