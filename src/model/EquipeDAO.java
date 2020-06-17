//Nome do nosso pacote //                
package model;

//Classes necessárias para uso de Banco de dados //
import controller.Equipe;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

//Início da classe de conexão//
public class EquipeDAO {

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
     * Esta função insere uma equipe no banco de dados.
     */
    public boolean inserirEquipe(Equipe novaEquipe) {
        connectToDb(); //Conecta ao banco de dados

        String sqlInsereEquipe = "INSERT INTO equipe (nome) values (?)";
        try {
            pst = con.prepareStatement(sqlInsereEquipe);
            pst.setString(1, novaEquipe.getNome());

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { //codigo do erro para nomes duplicados.
                JOptionPane.showMessageDialog(null, "Equipe com este nome ja cadastrada!\nTente Novamente com um nome diferente");
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
     * Esta função adiciona um capitão à equipe selecionada.
     */
    public boolean addCapitao() {
        connectToDb(); //Conecta ao banco de dados

        int idEquipe = 0;
        int idCompetidor = 0;
        int idCapitao = 0;

        //Aqui capturamos os ids da equipe e do competidor que vamos trabalhar
        String sqlGetIdUltimoComp = "SELECT * FROM competidor WHERE idcompetidor = (SELECT MAX(idcompetidor) FROM competidor)";
        String sqlVerificaCapitao = "SELECT competidor_idcompetidor FROM equipe WHERE idequipe = ?";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sqlGetIdUltimoComp);

            while (rs.next()) {
                idCompetidor = rs.getInt("idcompetidor");
                idEquipe = rs.getInt("equipe_idequipe");
            }

            pst = con.prepareStatement(sqlVerificaCapitao);
            pst.setInt(1, idEquipe);
            rs = pst.executeQuery();
            while (rs.next()) {
                idCapitao = rs.getInt("competidor_idcompetidor");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            sucesso = false;
        }

        if (idCapitao == 0) {
            this.insereCapitao(idCompetidor, idEquipe);
        } else {
            int ans = JOptionPane.showConfirmDialog(
                    null, "Esta equipe já possui um capitão,"
                    + "\ncaso prossiga o capitão atual será alterado para este. "
                    + "\nDeseja Continuar?", "", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (ans == 0) {
                this.insereCapitao(idCompetidor, idEquipe);
            }
        }
        return sucesso;
    }

    /**
     * Função que complementa a addCapitão, faz a inserção no banco de dados.
     */
    public void insereCapitao(int idCompetidor, int idEquipe) {

        connectToDb();

        String sqlSetCapitao = "UPDATE equipe SET competidor_idcompetidor = ? WHERE idequipe = ?";
        try {
            pst = con.prepareStatement(sqlSetCapitao);
            pst.setInt(1, idCompetidor);
            pst.setInt(2, idEquipe);

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
    }

    /**
     * Esta função retorna uma lista das equipes cadastradas.
     */
    public ArrayList<String> listaEquipes() {
        ArrayList<String> listaEquipes = new ArrayList<>();
        connectToDb();

        String sql = "SELECT * FROM equipe";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql); //ref. a tabela resultante da busca
            while (rs.next()) {
                listaEquipes.add(rs.getString("nome"));
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
        return listaEquipes;
    }

    /**
     * Esta função atualiza o nome da equipe selecionada.
     */
    public boolean atualizaNomeEquipe(String nomeEquipe, String novoNomeEquipe) {

        connectToDb();
        int idEquipe = this.getIdEquipe(nomeEquipe);

        String sql = "UPDATE equipe SET nome = ? WHERE idequipe = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novoNomeEquipe);
            pst.setInt(2, idEquipe);

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
     * Esta função exclui a equipe selecionada.
     */
    public boolean deletarEquipe(String nomeEquipe) {
        connectToDb();

        int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir?", "Excluir Equipe", JOptionPane.YES_NO_OPTION);

        if (opcao == 0) {

            int idEquipe = this.getIdEquipe(nomeEquipe);

            String sqlDeleteEquipe = "DELETE FROM equipe WHERE idequipe=?";
            try {
                pst = con.prepareStatement(sqlDeleteEquipe);
                pst.setInt(1, idEquipe);
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
     * Esta função retorna o id da equipe selecionada.
     */
    public Integer getIdEquipe(String nomeEquipe) {

        connectToDb();

        int idEquipe = 0;

        String sqlequipe = "SELECT * FROM equipe WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqlequipe);
            pst.setString(1, nomeEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                idEquipe = rs.getInt("idequipe");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }
        return idEquipe;
    }

}
