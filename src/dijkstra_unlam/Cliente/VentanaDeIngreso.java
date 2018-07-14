package dijkstra_unlam.Cliente;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import dijkstra_paqueteEnvio.Pedido;

public class VentanaDeIngreso extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField textUsuario;
	private JPasswordField textPass;
	private JLabel usuarioLabel;
	private JLabel passLabel;
	private JButton ingresar;
	private JButton nuevo;
	private String ip = "192.168.100.5";
	private int puerto = 9890;
	private int puertoParaValidacionDeUsuarios = 9894;
	private String nombre;
	private ServerSocket serv;
	private JFrame ventana = this;

	public VentanaDeIngreso() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel imagenDeFondo = new JLabel();
		imagenDeFondo.setBounds(0, 0, this.getWidth(), 250);
		ImageIcon icon;
		Image img;
		try {
			icon = new ImageIcon("Utilitarias//icono.jpg");

			img = icon.getImage().getScaledInstance(imagenDeFondo.getWidth(), imagenDeFondo.getHeight(),
					Image.SCALE_SMOOTH);
			imagenDeFondo.setIcon(new ImageIcon(img));

			ImageIcon iconApp = new ImageIcon("Utilitarias//chat.png");

			Image imgApp = iconApp.getImage();
			this.setIconImage(imgApp);

		} catch (Exception e) {

		}

		textUsuario = new JTextField();
		textUsuario.setBounds(110, 100, 300, 20);
		contentPane.add(textUsuario);
		textUsuario.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				send();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});

		textPass = new JPasswordField();
		textPass.setBounds(110, 130, 300, 20);
		contentPane.add(textPass);
		textPass.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				send();
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});

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
		

		this.setLocationRelativeTo(null);
		nuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					serv.close();
				} catch (Exception e) {

				}
				VentanaNuevoUsuario v = new VentanaNuevoUsuario(ventana);
				v.setLocationRelativeTo(null);
				v.setVisible(true);
				ventana.setVisible(false);

			}
		});
		
		
		
		
		

		ingresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}

		});

	
	
	}


	public void send() {
		nombre = textUsuario.getText();
		String pass = String.valueOf(textPass.getPassword());

		if (nombre.equals("") || pass.equals("")) {
			JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Pedido paquete;
		try {
			paquete = new Pedido(nombre, pass, InetAddress.getLocalHost().getHostAddress(), Pedido.ingresar);

		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "Error con el host local", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Gson gson = new Gson();
		String json = gson.toJson(paquete);
		try {
			Socket s = new Socket(ip, puerto);
			DataOutputStream d = new DataOutputStream(s.getOutputStream());
			d.writeUTF(json);
			s.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No hay conexion con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		esperarRespuesta();
	}
	
	public void esperarRespuesta() {
		try {
			serv = new ServerSocket(puertoParaValidacionDeUsuarios);
			Socket s;

			s = serv.accept();
			DataInputStream d = new DataInputStream(s.getInputStream());
			String resp = d.readUTF();
			serv.close();
			d.close();
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
				JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
				break;

			default:
				break;

			}

		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "No puedes tener dos ventanas de login abiertas al mismo tiempo",
					"Error", JOptionPane.ERROR_MESSAGE);
			ventana.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
