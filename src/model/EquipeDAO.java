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
            //JOptionPane.showMessageDialog(null, "Conexão feita com sucesso!");
            //System.out.println("Conexão feita com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Mensagem de Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * ********************** INSERIR DADOS
     *
     ********************************
     * @param novaEquipe
     * @return sucesso
     */
    public boolean inserirEquipe(Equipe novaEquipe) {
        connectToDb(); //Conecta ao banco de dados
        //Comando em SQL:
        String sql = "INSERT INTO equipe (nome) values (?)";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novaEquipe.getNome());

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
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
                System.out.println("Erro = " + ex.getMessage());
            }
        }
        return sucesso;
    }

    public boolean addCapitao() {
        connectToDb(); //Conecta ao banco de dados

        int idEquipe = 0;
        int idCompetidor = 0;
        int idCapitao = 0;

        //Aqui capturamos os ids da equipe e do competidor que vamos trabalhar
        //Comando em SQL:
        String sqlcompetidor = "SELECT * FROM competidor WHERE idcompetidor = (SELECT MAX(idcompetidor) FROM competidor)";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            st = con.createStatement();
            rs = st.executeQuery(sqlcompetidor);

            while (rs.next()) {
                idCompetidor = rs.getInt("idcompetidor");
                idEquipe = rs.getInt("equipe_idequipe");
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
            sucesso = false;
        }

        String sqlVerificaCapitao = "SELECT competidor_idcompetidor FROM equipe WHERE idequipe = ?";
        try {
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
            //Comando em SQL:
            String sql = "UPDATE equipe SET competidor_idcompetidor = ? WHERE idequipe = ?";
            //O comando recebe paramêtros -> consulta dinâmica (pst)
            try {
                pst = con.prepareStatement(sql);
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
                    System.out.println("Erro = " + ex.getMessage());
                }
            }
        } else {
            int ans = JOptionPane.showConfirmDialog(null, "Esta equipe já possui um capitão,\ncaso prossiga o capitão atual será alterado para este. \nDeseja Continuar?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ans == 0) {
                String sql = "UPDATE equipe SET competidor_idcompetidor = ? WHERE idequipe = ?";
                //O comando recebe paramêtros -> consulta dinâmica (pst)
                try {
                    pst = con.prepareStatement(sql);
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
                        System.out.println("Erro = " + ex.getMessage());
                    }
                }
            }
        }
        return sucesso;
    }

    /**
     *
     * @return Lista de Equipes Cadastradas
     */
    public ArrayList<String> listaEquipes() {
        ArrayList<String> listaEquipes = new ArrayList<>();
        connectToDb();
        //Comando em SQL:
        String sql = "SELECT * FROM equipe";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql); //ref. a tabela resultante da busca
            while (rs.next()) {
                //System.out.println(rs.getString("nome"));

                listaEquipes.add(rs.getString("nome"));
            }
            sucesso = true;
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Erro = " + ex.getMessage());
            }
        }
        return listaEquipes;
    }

    public boolean atualizaNomeEquipe(String nomeEquipe, String novoNomeEquipe) {

        connectToDb();
        int idEquipe = 0;

        idEquipe = this.getIdEquipe(nomeEquipe);

        //Comando em SQL:
        String sql = "UPDATE equipe SET nome = ? WHERE idequipe = ?";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
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
                System.out.println("Erro = " + ex.getMessage());
            }
        }

        return sucesso;
    }

    /**
     * ********************** DELETAR REGISTROS ******************************
     */
    public boolean deletarEquipe(String nomeEquipe) {
        connectToDb();
        //Comando em SQL:
        int idEquipe = 0;

        int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir?", "Excluir Equipe", JOptionPane.YES_NO_OPTION);

        if (opcao == 0) {

            idEquipe = this.getIdEquipe(nomeEquipe);

            String sqlDeleteEquipe = "DELETE FROM equipe WHERE idequipe=?";
            try {
                pst = con.prepareStatement(sqlDeleteEquipe);
                pst.setInt(1, idEquipe);
                pst.execute();
                sucesso = true;
            } catch (SQLException ex) {
                System.out.println("Erro = " + ex.getMessage());
                sucesso = false;
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex) {
                    System.out.println("Erro = " + ex.getMessage());
                }
            }
        } else {
            sucesso = false;
        }
        return sucesso;
    }

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
            System.out.println("Erro = " + ex.getMessage());
        }
        return idEquipe;
    }

}
