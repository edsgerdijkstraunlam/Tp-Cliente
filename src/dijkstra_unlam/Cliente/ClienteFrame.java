package dijkstra_unlam.Cliente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dijkstra_paqueteEnvio.Pedido;
import dijkstra_paqueteEnvio.Usuarios;
import edsger_dijkstra_unlam.asistente.asistente.Asistente;

//import dijkstra_paqueteEnvio;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ClienteFrame extends JFrame implements Runnable {

	public static int est = 0;
	private JPanel contentPane;
	private JTextField textField;

	// private JTextField textField2;
	// Usuarios destinatario;
	Socket cliente; // Prepara un puente para conectarse a un serverSocket
	ServerSocket servidor_cliente; // Prepara un cliente para que se conecten otros Sockets(en este caso solo se va
									// a conectar el servidor cuando le retransmita el mensaje)
	int puetroClienteAServidor = 9998; // Puerto para que se conecten
	int puertoServidorACliente = 9996;
	int puertoParaConexionesActivas = 9994;
	int puertoParaPedidos = 9890;

	private int puertoParaControlDeSalas = 9895;
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
	JPanel panelSalas;
	JLabel salaActual;
	JComboBox<String> salasComboBox;
	JButton nuevaSala;
	ArrayList<String> listaSalas;
	JLabel notificacion;
	Thread notif;

	public ClienteFrame(String nick) {

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

		notif = new Thread() {
			public void run() {
				notificacion.setVisible(true);
				try {
					for (int i = 0; i < 5; i++) {
						Thread.sleep(300);
						notificacion.setVisible(true);
						Thread.sleep(300);
						notificacion.setVisible(false);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		//////////////////////////////////////////////////////////////

		this.nick = nick;
		listaSalas = new ArrayList<String>();
		jenkins = new Asistente("Jenkins");
		jenkins.setUsuario(nick);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Chat-" + nick);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(10, 220, 300, 25);
		textField.setBackground(new Color(230, 230, 230));
		contentPane.add(textField);

		textPane = new JTextPane();
		textPane.setBounds(10, 22, 410, 180);
		contentPane.add(textPane);
		textPane.setBackground(new Color(230, 230, 230));

		sas = new SimpleAttributeSet();
		StyleConstants.setBold(sas, true);
		StyleConstants.setItalic(sas, true);

		panelSalas = new JPanel();
		panelSalas.setBounds(this.getWidth() - 220, 22, this.getWidth() - textPane.getWidth() - 50,
				this.getHeight() - 130);
		panelSalas.setLayout(null);

		contentPane.add(panelSalas);

		salaActual = new JLabel("Sala Principal");
		salaActual.setForeground(Color.BLUE);
		salaActual.setBounds(10, 10, this.getWidth() - 20, 30);
		salaActual.setFont(new Font(Font.DIALOG, Font.ITALIC, 15));
		panelSalas.add(salaActual);

		notificacion = new JLabel();
		notificacion.setBounds(panelSalas.getWidth() - 40, 10, 25, 25);
		notificacion.setBackground(Color.red);
		try {
			ImageIcon not = new ImageIcon("Utilitarias//not.png");

			Image imagen = not.getImage().getScaledInstance(notificacion.getWidth(), notificacion.getHeight(),
					Image.SCALE_SMOOTH);
			notificacion.setIcon(new ImageIcon(imagen));

		} catch (Exception e) {
			//No se puede cargar la imagen, se cpntinua con la app sin el icono de notificacion
		}
		panelSalas.add(notificacion);
		notificacion.setVisible(false);

		new Thread() {
			public void run() {

				while (true) {
					try {
						Thread.sleep(2000);
						ServerSocket serv = new ServerSocket(puertoParaControlDeSalas);
						Socket s = serv.accept();
						serv.close();
						DataInputStream d = new DataInputStream(s.getInputStream());
						Gson gson = new Gson();
						java.lang.reflect.Type type = new TypeToken<ArrayList<String>>() {
						}.getType();
						ArrayList<String> lista = gson.fromJson(d.readUTF(), type);

						int cant1 = listaSalas.size();

						int cant2 = lista.size();

						for (int i = cant1; i < cant2; i++) {
							listaSalas.add(lista.get(i));
							salasComboBox.addItem(lista.get(i));
						}

					} catch (BindException e) {
						// excepcion causada por tener mas de una ventana abierta en la misma pc
						try {
							int rand = (int) (Math.random() * 10000) % 2000;
							Thread.sleep(rand);
							// Se trata de impedir que se ejecuten las actualizaciones de salas al mismo
							// tiempo
						} catch (InterruptedException e1) {
							//Excepcion causada por el cierre abrubto de un hilo
							e1.printStackTrace();
						}
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}

			}
		}.start();

		new Thread() {
			public void run() {
				Gson gson = new Gson();
				while (true) {

					try {
						Thread.sleep(2000);
						Socket s = new Socket(ip, puertoParaConexionesActivas);
						Usuarios paq = new Usuarios(nick, InetAddress.getLocalHost().getHostAddress());
						paq.setSala(salaActual.getText());
						DataOutputStream dat = new DataOutputStream(s.getOutputStream());

						String json = gson.toJson(paq);
						dat.writeUTF(json);
						s.close();

					} catch (IOException | InterruptedException e) {

					}

				}
			}
		}.start();

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				send();
			}

		});
		btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEnviar.setBounds(315, 220, 89, 23);
		contentPane.add(btnEnviar);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					send();

			}
		});

		salasComboBox = new JComboBox<String>();
		salasComboBox.setBounds(10, 70, panelSalas.getWidth() - 40, 20);
		panelSalas.add(salasComboBox);

		nuevaSala = new JButton("Nueva");
		nuevaSala.setBounds(10, 140, panelSalas.getWidth() / 2, 30);
		panelSalas.add(nuevaSala);

		// *****************************************************************//
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(10, 22, 410, 180);
		getContentPane().add(scrollPane);
		// *****************************************************************//

		salasComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String item = (String) salasComboBox.getSelectedItem();
				if (!item.equals(salaActual.getText())) {
					salaActual.setText(item);
					textPane.setText("");
				}
			}

		});

		nuevaSala.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre de la sala");
				if (nombre == null)
					return;
				Pedido pedido = new Pedido();
				try {
					pedido.setIp(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				pedido.setPedido(Pedido.crearSala);
				pedido.setUsuario(nick);
				pedido.setSala(nombre);
				Gson gson = new Gson();
				String json = gson.toJson(pedido);
				try {
					Socket s = new Socket(ip, puertoParaPedidos);
					DataOutputStream d = new DataOutputStream(s.getOutputStream());
					d.writeUTF(json);
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				salaActual.setText(nombre);
				textPane.setText("");
			}
		});

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

				textPane.setSize(ventana.width - 240, ventana.height - 130);
				scrollPane.setSize(ventana.width - 240, ventana.height - 130);

				textField.setLocation(10, (int) ventana.getHeight() - 90);
				textField.setSize(ventana.width - 150, textField.getHeight());

				btnEnviar.setLocation((int) ventana.getWidth() - 125, textField.getY());

				panelSalas.setLocation((int) ventana.getWidth() - 220, 22);
				panelSalas.setSize((int) ventana.getWidth() - textPane.getWidth() - 50,
						(int) ventana.getHeight() - 130);

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

				Usuarios destinatario = new Usuarios(nick, InetAddress.getLocalHost().getHostAddress());
				destinatario.setMensaje(cadena);
				destinatario.setSala(salaActual.getText());

				String prueba = cadena.replaceAll("@[^@]+", "FIND");

				if (prueba.contains("FIND")) {
					String l = cadena.substring(cadena.indexOf("@"));
					l = l.substring(0, l.indexOf(" ") == -1 ? l.length() : l.indexOf(" "));
					destinatario.setNotificarA(l.replace("@", ""));
				}

				// if(cadena.contains("@[a-z]+"))
				// System.out.println();
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
	public void run() { 
		try {

			servidor_cliente = new ServerSocket(puertoServidorACliente); 
			Gson gson = new Gson();
			Socket cliente;
			while (true) {

				cliente = servidor_cliente.accept();
				DataInputStream flujo_entrada = new DataInputStream(cliente.getInputStream());
				String json = flujo_entrada.readUTF();

				Usuarios entrada = gson.fromJson(json, Usuarios.class);

				if (entrada.getNotificarA() != null && entrada.getNotificarA().equals(nick)) {
					StyleConstants.setForeground(sas, new Color(129, 63, 154));

					textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),
							"\nNotificacion(Sala: \"" + entrada.getSala() + "\"): ", sas);

					Toolkit.getDefaultToolkit().beep();
					notif.start();
				}

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
