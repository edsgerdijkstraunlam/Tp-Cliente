package Cliente;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

public class VentanaNuevoUsuario extends JFrame {

	private static final long serialVersionUID = 8655757883614209786L;
	private JPanel contentPane;
	private JTextField textUsuario;
	private JPasswordField textPass;
	private JPasswordField textRepetirPass;
	private JLabel lblusuario;
	private JLabel lblpass;
	private JLabel lblrepetirPass;
	private JButton btnaceptar;
	private JButton btncancelar;
	private JLabel errorDeContraseña;
	private String ip = "192.168.100.5";
	private int puerto = 9890;
	private int puertoParaValidacionDeUsuarios = 9894;
	private String usuario;
	private ServerSocket serv;
	private JFrame ventana = this;

	public VentanaNuevoUsuario(JFrame f) {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textUsuario = new JTextField();
		textUsuario.setBounds(130, 70, 250, 20);
		contentPane.add(textUsuario);

		textPass = new JPasswordField();
		textPass.setBounds(130, 100, 250, 20);
		contentPane.add(textPass);

		textRepetirPass = new JPasswordField();
		textRepetirPass.setBounds(130, 130, 250, 20);
		contentPane.add(textRepetirPass);

		lblusuario = new JLabel("Usuario:");
		lblusuario.setBounds(20, 70, 60, 20);
		contentPane.add(lblusuario);

		lblpass = new JLabel("Contraseña:");
		lblpass.setBounds(20, 100, 100, 20);
		contentPane.add(lblpass);

		lblrepetirPass = new JLabel("Repetir contraseña:");
		lblrepetirPass.setBounds(20, 130, 100, 20);
		contentPane.add(lblrepetirPass);

		btnaceptar = new JButton("Aceptar");
		btnaceptar.setBounds(40, 200, 110, 30);
		contentPane.add(btnaceptar);

		btncancelar = new JButton("Cancelar");
		btncancelar.setBounds(240, 200, 110, 30);
		contentPane.add(btncancelar);

		errorDeContraseña = new JLabel("*");
		errorDeContraseña.setForeground(Color.RED);
		errorDeContraseña.setFont(new Font(Font.DIALOG, Font.ITALIC, 20));
		errorDeContraseña.setBounds(390, 130, 250, 20);
		contentPane.add(errorDeContraseña);
		errorDeContraseña.setVisible(false);

		ImageIcon iconApp = new ImageIcon("Utilitarias//chat.png");
		Image imgApp = iconApp.getImage();
		this.setIconImage(imgApp);

		JFrame ventana = this;
		btncancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				f.setVisible(true);
				ventana.setVisible(false);

			}
		});

		textRepetirPass.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (!String.valueOf(textPass.getPassword()).equals(String.valueOf(textRepetirPass.getPassword())))
					errorDeContraseña.setVisible(true);
				else
					errorDeContraseña.setVisible(false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		textPass.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (!String.valueOf(textPass.getPassword()).equals(String.valueOf(textRepetirPass.getPassword())))
					errorDeContraseña.setVisible(true);
				else
					errorDeContraseña.setVisible(false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		btnaceptar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Socket s;
				Gson gson = new Gson();

				usuario = textUsuario.getText();
				String contraseña = String.valueOf(textPass.getPassword());
				String repetirContraseña = String.valueOf(textRepetirPass.getPassword());

				if (usuario.equals("") || contraseña.equals("") || repetirContraseña.equals("")) {
					JOptionPane.showMessageDialog(null, "Los campos de usuario y contraseña no pueden estar vacios",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!contraseña.equals(String.valueOf(repetirContraseña))) {
					JOptionPane.showMessageDialog(null, "Los campos de usuario y contraseña no coinciden", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					Pedido paq = new Pedido();
					paq.setUsuario(usuario);
					paq.setPedido(Pedido.nuevo);
					paq.setPassword(contraseña);
					paq.setIp(InetAddress.getLocalHost().getHostAddress());
					s = new Socket(ip, puerto);
					DataOutputStream d = new DataOutputStream(s.getOutputStream());
					String json = gson.toJson(paq);
					d.writeUTF(json);
					s.close();
					d.close();
					esperarRespuesta();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	public void esperarRespuesta() {

		try {
			serv = new ServerSocket(puertoParaValidacionDeUsuarios);
			Socket s = serv.accept();
			DataInputStream d = new DataInputStream(s.getInputStream());
			String resp = d.readUTF();
			serv.close();
			switch (resp) {

			case "EXIST":
				JOptionPane.showMessageDialog(null, "El nick seleccionado ya existe", "Error",
						JOptionPane.ERROR_MESSAGE);
				break;
			case "ADD":
				ClienteFrame f = new ClienteFrame(usuario);
				f.setLocationRelativeTo(null);
				f.setVisible(true);
				ventana.setVisible(false);
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
