package Cliente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import edsger_dijkstra_unlam.asistente.asistente.Asistente;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("serial")
public class ClienteFrame extends JFrame implements Runnable {

	public static int est = 0;
	private JPanel contentPane;
	private JTextField textField;

	private JTextField textField2;
	Socket cliente; // Prepara un puente para conectarse a un serverSocket
	ServerSocket servidor_cliente; // Prepara un cliente para que se conecten otros Sockets(en este caso solo se va
									// a conectar el servidor cuando le retransmita el mensaje)
	int puerto = 9998; // Puerto para que se conecten
	int puerto2 = 9996;
	String ip = "10.11.4.22"; // Ip del servidor
	ObjectOutputStream salida;
	BufferedReader entrada, teclado; // Flujo de datos de entrada
	String nick;
	Asistente jenkins;
	JButton btnEnviar;
	JRadioButton radioBtn;
	JLabel imagenDeFondo;
	ImageIcon icon;
	Image img;

	JTextPane textPane;
	SimpleAttributeSet sas;

	public void send() { // Metodo que se ejecuta al presionar el boton enviar (o enter en el campo de
							// mensaje)
		StyleConstants.setForeground(sas, Color.red);
		String ipCliente = textField2.getText(); // Se obtiene la ip de destino y se
		// guarda en ipCliente
		String cadena = ipCliente + "&" + textField.getText() + "&" + nick; // Se concatena la ip
		// con el mensaje separandolas con '&' y se guarda en cadena

		/*
		 * if(ipCliente.equals("meme")) { ImageIcon meme= new
		 * ImageIcon("Utilitarias//meme.jpg"); textPane.insertIcon(meme); return; }
		 */

		if (radioBtn.isSelected()) {

			try {
				
				
				
				
				
				
			
				
				
				
				
				
				
				
				
				
				
				
				String resp = jenkins.escuchar(textField.getText());
				StyleConstants.setForeground(sas, Color.RED);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						("\n" + nick + ": "), sas);
				StyleConstants.setForeground(sas, Color.black);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						(textField.getText() + "\n\n"), sas);

				StyleConstants.setForeground(sas, Color.green);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "Jenkins: ", sas);
				StyleConstants.setForeground(sas, Color.black);

				textField.setText("");

				
				
				
				
				
				
				
				
				
				
				
				
				
				
				if(resp.length()>5 &&resp.substring(0,6).equals("&yout&")) {

					String enlace=resp.substring(6);

					JPanel jPanel1= new JPanel();
					//JFXPanel jPanel1= new JFXPanel();
				//	jPanel1.setBounds(0, 0, 50, 50);
					SwingBrowser browser = new SwingBrowser();
					
					//browser.loadURL("https://www.youtube.com/embed/YKevgUmzEp4");
					browser.loadURL(enlace);
					
					//browser.setBounds(1, 1, 10, 10);
					jPanel1.add(browser);
					textPane.setCaretPosition(textPane.getStyledDocument().getLength());
					textPane.insertComponent(browser);
					textPane.setCaretPosition(textPane.getStyledDocument().getLength());
					
					
					return;
				}
				
				
				
				
				
				
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
						textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
								"\nLo siento " + jenkins.getUsuario()
										+ ", no encontre resultados en wikipedia, aqui tienes un enlace a Google que puede ayudarte\n\n",
								sas);

					}
					return;

				}

				if (resp.contains("&9gag&:") || resp.contains("&gif_&:")) {

					String http = resp.substring(7);

					if (http.equals("&not&")) {

						textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
								"@" + nick + " lo siento, no pude encontrar el elemento solicitado\n", sas);
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
					} catch (Exception e9) {
						System.out.println("no");
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

					textField.setText("");
					return;
				}
				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), resp + "\n", sas);

			} catch (BadLocationException e) {
			}

		}

		else {

			try {

				cliente = new Socket(ip, puerto);// Trata de conctarse al servidor

				DataOutputStream envio = new DataOutputStream(cliente.getOutputStream());
				envio.writeUTF(cadena);

				/*
				 * PaqueteEnvio p = new PaqueteEnvio(); p.setIp(textField2.getText());
				 * p.setMensaje(textField.getText());
				 * 
				 * salida= new ObjectOutputStream(cliente.getOutputStream());
				 * salida.writeObject(p);
				 * 
				 */
				try {

					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							"\n" + nick + ": ", sas);
					StyleConstants.setBackground(sas, Color.black);

					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							textField.getText() + "\n\n", sas);
				} catch (BadLocationException e) {

					e.printStackTrace();
				}

				// textArea.append(textField.getText() + "\n"); // Se escribe en el textarea el
				// mensaje enviado
				textField.setText(""); // Se vacia el campo de mensaje
				cliente.close();

			} catch (IOException e) {
				try {
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							"\nNO SE PUEDE ESTABLECER CONEXION\n", sas);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				// textArea.append("\nNO SE PUEDE ESTABLECER CONEXION\n");

			}
		}
	}

	public ClienteFrame() {

		System.setProperty("java.net.useSystemProxies", "true");
		System.setProperty("file.encoding", "UTF-8");
		Field charset;
		try {
			charset = Charset.class.getDeclaredField("defaultCharset");

			charset.setAccessible(true);
			charset.set(null, null);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {

		}
		// Se Inicia el Hilo que estara corriendo todo el tiempo para revisar si se
		// reciben mensajes

		//////////////////////////////////////////////////////////////

		Thread hilo = new Thread(this);
		hilo.start();

		//////////////////////////////////////////////////////////////

		nick = JOptionPane.showInputDialog("Nick:");
		if (nick == null || nick.length() < 3) {
			do {
				JOptionPane.showMessageDialog(null, "Tu usario debe tener al menos 3 caracteres", "Error",
						JOptionPane.ERROR_MESSAGE);

				nick = JOptionPane.showInputDialog("Nick:");
			} while (nick == null || nick.length() < 3);
		}

		jenkins = new Asistente("Jenkins");
		jenkins.setUsuario(nick);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(nick);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(10, 22, 414, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}

		});
		btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEnviar.setBounds(161, 106, 89, 23);
		contentPane.add(btnEnviar);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					send();

			}
		});

		textPane = new JTextPane();
		sas = new SimpleAttributeSet();
		StyleConstants.setBold(sas, true);
		StyleConstants.setItalic(sas, true);

		textPane.setBounds(10, 143, 410, 108);
		contentPane.add(textPane);

		/*
		 * textArea = new JTextArea(); textArea.setBounds(10, 143, 410, 108);
		 * contentPane.add(textArea);
		 */
		textField2 = new JTextField();
		textField2.setBounds(10, 50, 197, 20);
		contentPane.add(textField2);
		textField2.setColumns(10);

		// *****************************************************************//
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(10, 143, 410, 108);
		this.add(scrollPane);
		// *****************************************************************//

		radioBtn = new JRadioButton("Asistente");
		radioBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (radioBtn.isSelected()) {

					textField2.setBackground(Color.red);
					textField2.setText("Jenkins");
					textField2.setEnabled(false);

				} else {

					textField2.setBackground(Color.WHITE);
					textField2.setText("");
					textField2.setEnabled(true);

				}
			}
		});
		radioBtn.setBounds(247, 50, 80, 20);
		/////////////////////////////////////

		// PREPARADO PARA ASISTENTE//

		radioBtn.setSelected(true);
		textField2.setBackground(Color.red);
		textField2.setText("Jenkins");

		/////////////////////////////////////
		textField2.setEnabled(false);
		contentPane.add(radioBtn);

		imagenDeFondo = new JLabel();

		imagenDeFondo.setBounds(0, 0, 450, 300);

		try {
			icon = new ImageIcon("Utilitarias//fondo.jpg");

			img = icon.getImage().getScaledInstance(imagenDeFondo.getWidth(), imagenDeFondo.getHeight(),
					Image.SCALE_SMOOTH);
			imagenDeFondo.setIcon(new ImageIcon(img));

			ImageIcon iconApp = new ImageIcon("Utilitarias//chat.png");

			Image imgApp = iconApp.getImage();
			this.setIconImage(imgApp);

		} catch (Exception e) {
		}

		contentPane.add(imagenDeFondo);

		// *******************************************************************//

		JFrame f = this;
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(ComponentEvent e) {

				// Se obtienen las dimensiones en pixels de la pantalla.
				// Se obtienen las dimensiones en pixels de la ventana.
				Dimension ventana = f.getSize();

				textPane.setSize(ventana.width - 40, ventana.height - 200);
				scrollPane.setSize(ventana.width - 40, ventana.height - 200);
				btnEnviar.setLocation((int) ventana.getWidth() / 2 - 70, btnEnviar.getY());

				imagenDeFondo.setSize(ventana.width, ventana.height);

				img = icon.getImage().getScaledInstance(imagenDeFondo.getWidth(), imagenDeFondo.getHeight(),
						Image.SCALE_SMOOTH);
				imagenDeFondo.setIcon(new ImageIcon(img));
			}

		});

	}

	@Override
	public void run() { // Metodo que estara a la escucha para ver si se reciben mensajes. Es el que
						// hace que un cliente se comporte como un servidor

		try {

			servidor_cliente = new ServerSocket(puerto2); // Crea el puente por el que se va a recibir la informacion
			Socket cliente; // Prepara el puente de los que se conecten
			while (true) {

				cliente = servidor_cliente.accept();// espera que alguien intente establecer coneccion y la acepta. En
													// el caso de esta clase el unico que intentara establecer coneccion
													// es el servidor Principal
				DataInputStream flujo_entrada = new DataInputStream(cliente.getInputStream());// Guarda en flujo_entrada
																								// el dataImputStream
																								// del socket que se
																								// conecta
				String entrada = flujo_entrada.readUTF();// Lee el mensaje

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), entrada + "\n",
						sas);

				// textArea.append(entrada + "\n");// Lo escribe en la ventana
				cliente.close();
				flujo_entrada.close();

			}

		} catch (Exception e) {

		} finally {
			try {
				servidor_cliente.close();
			} catch (Exception e2) {

			}
		}
	}
}
