package aprimorar.pessoas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Endereco {

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "cep", nullable = false, length = 8)
    private String cep;

    @Column(name = "complemento")
    private String complemento;

    protected Endereco() {}

    public Endereco(String rua, String numero, String bairro, String cidade, String estado, String cep, String complemento) {
        this.rua = validateRequired(rua, "Rua é obrigatória");
        this.numero = validateRequired(numero, "Número é obrigatório");
        this.bairro = validateRequired(bairro, "Bairro é obrigatório");
        this.cidade = validateRequired(cidade, "Cidade é obrigatória");
        this.estado = validateRequired(estado, "Estado é obrigatório");
        this.cep = validateRequired(cep, "CEP é obrigatório").replaceAll("\\D", "");
        this.complemento = complemento;
    }

    private String validateRequired(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}






}
