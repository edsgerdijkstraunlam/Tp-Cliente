package Cliente;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;


public class ManejadorDeAsistente {

	public static void manejar(String resp, JTextPane textPane, SimpleAttributeSet sas, String usuario) {

		System.setProperty("java.net.useSystemProxies", "true");
		if (resp.length() > 5 && resp.substring(0, 6).equals("&yout&")) {

			String enlace = resp.substring(6);

			JPanel jPanel1 = new JPanel();
			// JFXPanel jPanel1= new JFXPanel();
			// jPanel1.setBounds(0, 0, 50, 50);
			SwingBrowser browser = new SwingBrowser();

			// browser.loadURL("https://www.youtube.com/embed/YKevgUmzEp4");
			browser.loadURL(enlace);

			// browser.setBounds(1, 1, 10, 10);
			jPanel1.add(browser);
			textPane.setCaretPosition(textPane.getStyledDocument().getLength());
			textPane.insertComponent(browser);
			textPane.setCaretPosition(textPane.getStyledDocument().getLength());

			return;
		}

		try {
			if (resp.contains("&wiki&")) {

				boolean wiki = false;

				String direccion;

				JLabel enlace = new JLabel();
				int tamDigDir;
				int tamDir;
				enlace.setBackground(Color.white);
				enlace.setForeground(Color.blue);

				if (resp.substring(6, 11).equals("&not&")) {

					tamDigDir = resp.split("&")[4].length();
					tamDir = Integer.parseInt(resp.split("&")[4]);
					direccion = resp.substring(12 + tamDigDir, 12 + tamDigDir + tamDir);
					// enlace.setText("Google");
				}

				else {

					tamDigDir = resp.split("&")[2].length();
					tamDir = Integer.parseInt(resp.split("&")[2]);
					direccion = resp.substring(7 + tamDigDir, 7 + tamDigDir + tamDir);
					wiki = true;
					// enlace.setText("Wikipedia");

				}

				// enlace.setText("<html><a
				// href=\"http://www.google.com/\">Wikipedia</a></html>");

				// enlace.setToolTipText(direccion);
				enlace.setText(direccion);

				enlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
				enlace.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {

						try {
							if (Desktop.isDesktopSupported()) {
								Desktop desktop = Desktop.getDesktop();
								if (desktop.isSupported(Desktop.Action.BROWSE))
									desktop.browse(new URI(direccion));

							}
						} catch (Exception ee) {
						}
						;
					}

					@Override
					public void mouseEntered(MouseEvent e) {

						enlace.setForeground(Color.red);
					}

					@Override
					public void mouseExited(MouseEvent e) {

						enlace.setForeground(Color.blue);

					}

					@Override
					public void mousePressed(MouseEvent e) {

					}

					@Override
					public void mouseReleased(MouseEvent e) {

					}

				});

				textPane.insertComponent(enlace);
				textPane.setCaretPosition(textPane.getStyledDocument().getLength());

				if (wiki) {
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							"\n\n" + resp.substring(7 + tamDigDir + tamDir) + "\n\n", sas);

					wiki = false;
				} else {
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\nLo siento "
							+ usuario
							+ ", no encontre resultados en wikipedia, aqui tienes un enlace a Google que puede ayudarte\n\n",
							sas);

				}

				return;

			}

			if (resp.contains("&9gag&:") || resp.contains("&gif_&:")) {

				String http = resp.substring(7);

				if (http.equals("&not&")) {

					try {
						textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
								"@" + usuario + " lo siento, no pude encontrar el elemento solicitado\n", sas);
					} catch (BadLocationException e) {
					}
					return;
				}

				try {

					URL url = new URL(http);

					ImageIcon mem = new ImageIcon(url);
					if (resp.contains("&9gag&:"))
						mem = (new ImageIcon(mem.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH)));
					textPane.setCaretPosition(textPane.getStyledDocument().getLength());
					JLabel elemento = new JLabel();
					elemento.setSize(300, 200);
					elemento.setIcon(mem);
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\n", sas);
					textPane.insertComponent(elemento);
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\n", sas);

					return;
				} catch (MalformedURLException me) {

					return;
				}

			}

			if (resp.contains("&meme:")) {
				String meme = resp.split(":")[1];

				ImageIcon mem = new ImageIcon("Utilitarias//Imagenes//" + meme);

				Image im = mem.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
				mem = (new ImageIcon(im));
				textPane.setCaretPosition(textPane.getStyledDocument().getLength());

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\n", sas);

				textPane.insertIcon(mem);
				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\n\n", sas);

				return;

			}

			textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), resp + "\n", sas);

		} catch (BadLocationException e) {
		}
	}
}
