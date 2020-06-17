//Nome do nosso pacote //                
package model;

//Classes necessárias para uso de Banco de dados //
import controller.Torneio;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

//Início da classe de conexão//
public class TorneioDAO {

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
     * Esta função insere um novo torneio no banco de dados.
     */
    public boolean inserirTorneio(Torneio novoTorneio) {
        connectToDb(); //Conecta ao banco de dados
        String sql = "INSERT INTO torneio (nome,localizacao) values (?,?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novoTorneio.getNomeTorneio());
            pst.setString(2, novoTorneio.getLocalizacao());

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { //Código de erro para entradas duplicadas
                JOptionPane.showMessageDialog(null, "Torneio com este nome ja cadastrada!\nTente Novamente com um nome diferente!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
                sucesso = false;
            }
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
     * Esta função retorna a lista dos torneios cadastrados.
     */
    public ArrayList<String> listaTorneios() {
        ArrayList<String> listaTorneios = new ArrayList<>();
        connectToDb();

        String sql = "SELECT * FROM torneio";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql); //ref. a tabela resultante da busca
            while (rs.next()) {
                listaTorneios.add(rs.getString("nome"));
            }
            sucesso = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                st.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
        }
        return listaTorneios;
    }

    /**
     * Esta função atualiza o nome do torneio selecionado.
     */
    public boolean atualizaNomeTorneio(String nomeTorneio, String novoNomeTorneio) {

        connectToDb();
        int idTorneio = this.getIdTorneio(nomeTorneio);

        String sql = "UPDATE torneio SET nome = ? WHERE idtorneio = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novoNomeTorneio);
            pst.setInt(2, idTorneio);

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
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
     * Esta função atualiza a localização do torneio selecionado.
     */
    public boolean atualizaLocalizacaoTorneio(String nomeTorneio, String novaLocalizacao) {

        connectToDb();
        int idTorneio = this.getIdTorneio(nomeTorneio);

        String sql = "UPDATE torneio SET localizacao = ? WHERE idtorneio = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novaLocalizacao);
            pst.setInt(2, idTorneio);

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
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
     * Esta função apaga o torneio selecionado.
     */
    public boolean deletarTorneio(String nomeTorneio) {
        connectToDb();

        int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir?", "Excluir Torneio", JOptionPane.YES_NO_OPTION);
        if (opcao == 0) {

            int idTorneio = this.getIdTorneio(nomeTorneio);

            String sqlDeleteEquipe = "DELETE FROM torneio WHERE idtorneio=?";
            try {
                pst = con.prepareStatement(sqlDeleteEquipe);
                pst.setInt(1, idTorneio);
                pst.execute();
                sucesso = true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
                sucesso = false;
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
                }
            }
        } else {
            sucesso = false;
        }
        return sucesso;
    }

    /**
     * Esta função retorna o local do torneio.
     */
    public String getLocalTorneio(String nomeTorneio) {

        connectToDb();

        String localTorneio = "";

        int idTorneio = this.getIdTorneio(nomeTorneio);
        
        String sqlLocalTorneio = "SELECT * FROM torneio WHERE idtorneio = ?";
        try {
            pst = con.prepareStatement(sqlLocalTorneio);
            pst.setInt(1, idTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                localTorneio = rs.getString("localizacao");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        return localTorneio;
    }



    /**
     * Esta função retorna o id do torneio selecionado.
     */
    
    public Integer getIdTorneio(String nomeTorneio) {

        connectToDb();

        int idTorneio = 0;

        String sqlTorneio = "SELECT * FROM torneio WHERE nome = ?";
        try {
            pst = con.prepareStatement(sqlTorneio);
            pst.setString(1, nomeTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idTorneio = rs.getInt("idtorneio");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        return idTorneio;
    }
}
