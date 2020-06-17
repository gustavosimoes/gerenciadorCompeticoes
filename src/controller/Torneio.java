package controller;

public class Torneio {
    
    private String nomeTorneio;
    private String localizacao;
    public Torneio(String nomeCompeticao, String localizacao) {
        this.nomeTorneio = nomeCompeticao;
        this.localizacao = localizacao;
    }

    public String getNomeTorneio() {
        return nomeTorneio;
    }

    public void setNomeTorneio(String nomeTorneio) {
        this.nomeTorneio = nomeTorneio;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    
}
