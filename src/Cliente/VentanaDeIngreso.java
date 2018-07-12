package Cliente;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import dijkstra_paqueteEnvio.PaqueteEnvio;

public class VentanaDeIngreso extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField textUsuario;
	private JTextField textPass;
	private JLabel usuarioLabel;
	private JLabel passLabel;
	private JButton ingresar;
	private JButton nuevo;
	private String ip = "10.11.3.10";
	private int puerto = 9890;
	private int puertoParaValidacionDeUsuarios = 9894;
	private String nombre;

	public VentanaDeIngreso() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel imagenDeFondo= new JLabel();;
		imagenDeFondo.setBounds(0, 0, this.getWidth(),250);
		ImageIcon icon;
		Image img;
		try {
			icon= new ImageIcon("Utilitarias//icono.jpg");

			img = icon.getImage().getScaledInstance(imagenDeFondo.getWidth(), imagenDeFondo.getHeight(),
					Image.SCALE_SMOOTH);
			imagenDeFondo.setIcon(new ImageIcon(img));

			ImageIcon iconApp = new ImageIcon("Utilitarias//chat.png");

			Image imgApp = iconApp.getImage();
			this.setIconImage(imgApp);

		} catch (Exception e) {
			System.out.println("no");
		}

		
		textUsuario = new JTextField();
		textUsuario.setBounds(110, 100, 300, 20);
		contentPane.add(textUsuario);

		textPass = new JTextField();
		textPass.setBounds(110, 130, 300, 20);
		contentPane.add(textPass);

		usuarioLabel = new JLabel("USUARIO:");
		usuarioLabel.setBounds(15, 100, 100, 20);
		contentPane.add(usuarioLabel);

		passLabel = new JLabel("CONTRASEÑA:");
		passLabel.setBounds(15, 130, 100, 20);
		contentPane.add(passLabel);

		ingresar = new JButton("Ingresar");
		ingresar.setBounds(110, 190, 100, 30);
		contentPane.add(ingresar);

		nuevo = new JButton("Nuevo");
		nuevo.setBounds(255, 190, 100, 30);
		contentPane.add(nuevo);
		contentPane.add(imagenDeFondo);
	}

	public void validar() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		JFrame ventana = this;
		new Thread() {
			public void run() {

				ServerSocket serv;
				while (true) {
					try {
						serv = new ServerSocket(puertoParaValidacionDeUsuarios);
						Socket s = serv.accept();
						DataInputStream d = new DataInputStream(s.getInputStream());
						String resp = d.readUTF();
						serv.close();
						switch (resp) {

						case "NOTFOUND":
							JOptionPane.showMessageDialog(null, "No se ha encontrado tu usuario", "Error",
									JOptionPane.ERROR_MESSAGE);
							break;
						case "CONNECT":
							ClienteFrame f = new ClienteFrame(nombre);
							f.setLocationRelativeTo(null);
							f.setVisible(true);
							ventana.setVisible(false);
							break;
						case "NOTPASS":
							JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error",
									JOptionPane.ERROR_MESSAGE);
							break;

						default:
							break;

						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();

		ingresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nombre = textUsuario.getText();
				String pass = textPass.getText();

				if (nombre.equals("") || pass.equals("")) {
					JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				PaqueteEnvio paquete = new PaqueteEnvio();
				paquete.setNick(nombre);
				paquete.setContraseña(pass);
				try {
					paquete.setIp(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Gson gson = new Gson();
				String json = gson.toJson(paquete);
				try {
					Socket s = new Socket(ip, puerto);
					DataOutputStream d = new DataOutputStream(s.getOutputStream());
					d.writeUTF(json);
					s.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "No hay conexion con el servidor", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}

		});

	}

}
