package Cliente;

public class Sala {
	
	private String nombreDeLaSala;
	private String codigoDeSala;
	private String password;
	
	
	public Sala(String nombreDeLaSala) {
		this.nombreDeLaSala=nombreDeLaSala;
	}
	
	public String getNombreDeLaSala() {
		return nombreDeLaSala;
	}


	public void setNombreDeLaSala(String nombreDeLaSala) {
		this.nombreDeLaSala = nombreDeLaSala;
	}


	public String getCodigoDeSala() {
		return codigoDeSala;
	}


	public void setCodigoDeSala(String codigoDeSala) {
		this.codigoDeSala = codigoDeSala;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	

}
