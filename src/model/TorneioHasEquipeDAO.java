package model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

//Início da classe de conexão//
/**
 *
 *
 */
public class TorneioHasEquipeDAO {

    /**
     * *************** CONEXÃO COM O BANCO DE DADOS ***********************
     */
    // objeto responsável pela conexão com o servidor do banco de dados
    Connection con;
    // objeto responsável por preparar as consultas dinâmicas
    PreparedStatement pst;
    // objeto responsável por executar as consultas estáticas
    Statement st;
    // objeto responsável por referenciar a tabela resultante da busca
    ResultSet rs;

    // NOME DO BANCO DE DADOS
    String database = "gerenciadorCompeticoes";
    // URL: VERIFICAR QUAL A PORTA
    String url = "jdbc:Mysql://localhost:3306/" + database + "?useTimezone=true&serverTimezone=UTC&useSSL=false";
    // USUÁRIO
    String user = "root";
    // SENHA
    String password = "gustavo16";

    boolean sucesso = false;

    // Conectar ao banco de dados
    public void connectToDb() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Mensagem de Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Esta função insere uma equipe ao torneio selecionado.
     */
    public boolean inserirEquipeTorneio(String nomeEquipe, String nomeTorneio) {
        connectToDb();

        //Aqui capturamos os ids da equipe e do torneio que vamos trabalhar
        EquipeDAO daoEquipe = new EquipeDAO();
        TorneioDAO daoTorneio = new TorneioDAO();
        int idEquipe = daoEquipe.getIdEquipe(nomeEquipe);
        int idTorneio = daoTorneio.getIdTorneio(nomeTorneio);

        String sql = "INSERT INTO torneio_has_equipe (torneio_idtorneio,equipe_idequipe) values (?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idTorneio);
            pst.setInt(2, idEquipe);

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {

            if (ex.getErrorCode() == 1062) { //Código de erro para entradas duplicadas
                JOptionPane.showMessageDialog(null, "Esta equipe já esta inserida neste torneio!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
            sucesso = false;
        } finally {
            try {   //Encerra a conexão
                con.close();
                pst.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
        }
        return sucesso;
    }

    /**
     * Esta função encontra as equipes do torneio selecionado
     */
    public ArrayList<String> pesquisaEquipeTorneio(String nomeTorneio) {
        connectToDb();

        ArrayList<Integer> listaIdsEquipe = new ArrayList<>();
        ArrayList<String> listaNomesEquipe = new ArrayList<>();

        TorneioDAO daoTorneio = new TorneioDAO();

        int idTorneio = daoTorneio.getIdTorneio(nomeTorneio);

        listaIdsEquipe = this.getIdEquipeTorneio(idTorneio);

        listaNomesEquipe = this.pesquisaNomeEquipe(listaIdsEquipe);

        return listaNomesEquipe;

    }

    /**
     * Esta função retorna um ArrayList com os Ids de equipes do torneio
     * selecionado
     */
    public ArrayList<Integer> getIdEquipeTorneio(int idTorneio) {

        ArrayList<Integer> listaIdsEquipe = new ArrayList<>();

        String sqlEquipeTorneio = "SELECT equipe_idequipe FROM torneio_has_equipe WHERE torneio_idtorneio = ? ";
        try {
            pst = con.prepareStatement(sqlEquipeTorneio);
            pst.setInt(1, idTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                listaIdsEquipe.add(rs.getInt("equipe_idequipe"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }
        return listaIdsEquipe;
    }

    /**
     * Esta função retorna um ArrayList com os nomes das equipes do torneio
     * selecionado
     */
    public ArrayList<String> pesquisaNomeEquipe(ArrayList<Integer> listaIdsEquipe) {

        ArrayList<String> listaNomesEquipe = new ArrayList<>();

        for (int i : listaIdsEquipe) {

            String sqlGetNomeEquipe = "SELECT * FROM equipe WHERE idequipe = ? ";
            try {
                pst = con.prepareStatement(sqlGetNomeEquipe);
                pst.setInt(1, i);
                rs = pst.executeQuery();

                while (rs.next()) {
                    listaNomesEquipe.add(rs.getString("nome"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }

        }

        return listaNomesEquipe;
    }

    /**
     * Esta função retorna um ArrayList com os capitães das equipes do torneio
     * selecionado.
     */
    public ArrayList<String> pesquisaCapitaoEquipe(String nomeTorneio) {
        connectToDb(); //Conecta ao banco de dados
        ArrayList<Integer> listaIdsEquipe = new ArrayList<>();
        ArrayList<String> listaCapitaes = new ArrayList<>();

        TorneioDAO daoTorneio = new TorneioDAO();

        int idTorneio = daoTorneio.getIdTorneio(nomeTorneio);

        listaIdsEquipe = this.getIdEquipeTorneio(idTorneio);

        for (int i : listaIdsEquipe) {

            String sqlGetIdCapitao = "SELECT * FROM equipe WHERE idequipe = ? ";
            try {
                pst = con.prepareStatement(sqlGetIdCapitao);
                pst.setInt(1, i);
                rs = pst.executeQuery();

                while (rs.next()) {
                    listaCapitaes.add(this.getNomeCapitao(rs.getInt("competidor_idcompetidor")));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
        }

        try { //encerrando a conexão
            con.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        return listaCapitaes;
    }

    /**
     * Esta função é um complemento de pesquisaCapitaoEquipe(), ela retorna o do
     * competidor cujo id é passado no parâmetro.
     */
    public String getNomeCapitao(int idCapitao) {

        String nomeCapitao = "";

        if (idCapitao != 0) {

            String sqlcapturanomecapitao = "SELECT * FROM competidor WHERE idcompetidor = ? ";

            try {
                pst = con.prepareStatement(sqlcapturanomecapitao);
                pst.setInt(1, idCapitao);
                rs = pst.executeQuery();

                while (rs.next()) {
                    nomeCapitao = rs.getString("nome");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }

        } else {
            nomeCapitao = "Sem Capitão";
        }

        return nomeCapitao;
    }
}
