package Cliente;

import java.io.Serializable;

public class PaqueteEnvio implements Serializable{
	private static final long serialVersionUID = 1L;
	private String ip, mensaje;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
