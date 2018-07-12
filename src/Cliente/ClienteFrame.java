package Cliente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.google.gson.Gson;

import dijkstra_paqueteEnvio.Usuario;
import edsger_dijkstra_unlam.asistente.asistente.Asistente;
//import dijkstra_paqueteEnvio;
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ClienteFrame extends JFrame implements Runnable {

	public static int est = 0;
	private JPanel contentPane;
	private JTextField textField;

	// private JTextField textField2;
	Usuario destinatario;
	Socket cliente; // Prepara un puente para conectarse a un serverSocket
	ServerSocket servidor_cliente; // Prepara un cliente para que se conecten otros Sockets(en este caso solo se va
									// a conectar el servidor cuando le retransmita el mensaje)
	int puetroClienteAServidor = 9998; // Puerto para que se conecten
	int puertoServidorACliente = 9996;
	int puertoParaConexionesActivas = 9994;
	String ip = "192.168.100.5"; // Ip del servidor
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

	public ClienteFrame(String nick) {

		// System.setProperty("java.net.useSystemProxies", "true");
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

		this.nick = nick;

		jenkins = new Asistente("Jenkins");
		jenkins.setUsuario(nick);

		new Thread() {
			public void run() {
				Gson gson = new Gson();
				while (true) {

					try {
						Thread.sleep(2000);
						Socket s = new Socket(ip, puertoParaConexionesActivas);
						Usuario paq = new Usuario(nick, InetAddress.getLocalHost().getHostAddress());
						DataOutputStream dat = new DataOutputStream(s.getOutputStream());

						String json = gson.toJson(paq);
						dat.writeUTF(json);
						s.close();

					} catch (IOException | InterruptedException e) {

					}

				}
			}
		}.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Chat-" + nick);
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

		// *****************************************************************//
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(10, 143, 410, 108);
		this.add(scrollPane);
		// *****************************************************************//

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

	public void send() { 

		StyleConstants.setForeground(sas, Color.red);
		String cadena = textField.getText();

		if (cadena.toLowerCase().contains("@jenkins ") || cadena.toLowerCase().contains(" @jenkins")) {

			cadena = cadena.toLowerCase().replace("@jenkins ", "");
			cadena = cadena.toLowerCase().replace(" @jenkins", "");
			try {
				String resp = jenkins.escuchar(cadena);
				StyleConstants.setForeground(sas, Color.RED);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						("\n" + nick + ": "), sas);
				StyleConstants.setForeground(sas, Color.black);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						(textField.getText() + "\n\n"), sas);

				StyleConstants.setForeground(sas, Color.green);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "Jenkins: ", sas);
				StyleConstants.setForeground(sas, Color.black);

				ManejadorDeAsistente.manejar(resp, textPane, sas, nick);

				textField.setText("");
				return;

			} catch (BadLocationException e) {
			}

		}

		else {

			try {

				cliente = new Socket(ip, puetroClienteAServidor);// Trata de conctarse al servidor

				destinatario = new Usuario(nick, InetAddress.getLocalHost().getHostAddress());
				destinatario.setMensaje(cadena);

				Gson gson = new Gson();
				String json = gson.toJson(destinatario);

				DataOutputStream envia = new DataOutputStream(cliente.getOutputStream());
				envia.writeUTF(json);
		
				try {

					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), nick + ": ",
							sas);

					StyleConstants.setForeground(sas, Color.black);
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							textField.getText() + "\n", sas);
					textPane.setCaretPosition(textPane.getStyledDocument().getLength());
				} catch (BadLocationException e) {

					e.printStackTrace();
				}

				
				textField.setText(""); // Se vacia el campo de mensaje
				cliente.close();

			} catch (IOException e) {
				try {
					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							"\nNO SE PUEDE ESTABLECER CONEXION\n", sas);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

			
			}
		}
	}

	@Override
	public void run() { // Metodo que estara a la escucha para ver si se reciben mensajes. Es el que
						// hace que un cliente se comporte como un servidor

		try {

			servidor_cliente = new ServerSocket(puertoServidorACliente); // Crea el puente por el que se va a recibir la informacion
			Gson gson = new Gson(); 
			Socket cliente;
			while (true) {

				cliente = servidor_cliente.accept();// espera que alguien intente establecer coneccion y la acepta.
													// En
													// el caso de esta clase el unico que intentara establecer
													// coneccion
													// es el servidor Principal
				DataInputStream flujo_entrada = new DataInputStream(cliente.getInputStream());// Guarda en
																								// flujo_entrada
				String json = flujo_entrada.readUTF();
			
				Usuario entrada = gson.fromJson(json, Usuario.class);// Lee el mensaje

				StyleConstants.setForeground(sas, Color.BLUE);

				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						"@" + entrada.getNick() + ": ", sas);

				StyleConstants.setForeground(sas, Color.black);
				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
						entrada.getMensaje() + "\n", sas);
				textPane.setCaretPosition(textPane.getStyledDocument().getLength());
				flujo_entrada.close();

				cliente.close();
			}

		} catch (Exception e) {

		}
	}



}
