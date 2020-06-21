//Nome do nosso pacote //                
package model;

//Classes necessárias para uso de Banco de dados //
import controller.Competidor;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import view.TelaInicial;

//Início da classe de conexão//
public class CompetidorDAO {

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
     * Esta função insere o novo competidor na equipe selecionada.
     */
    public boolean inserirCompetidor(Competidor novoCompetidor, String nomeEquipe) {

        connectToDb();

        EquipeDAO daoEquipe = new EquipeDAO();
        int idEquipe = daoEquipe.getIdEquipe(nomeEquipe); //Função que busca o id da Equipe

        connectToDb();

        String sqlInserirCompetidor = "INSERT INTO competidor (nome,idade,sexo,equipe_idequipe) values (?,?,?,?)"; //Inserindo o competidor na equipe
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sqlInserirCompetidor);
            pst.setString(1, novoCompetidor.getNome());
            pst.setInt(2, novoCompetidor.getIdade());
            pst.setString(3, novoCompetidor.getSexo());
            pst.setInt(4, idEquipe);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Inserção feita com sucesso!");
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
     * Esta função retorna uma lista com os competidores da equipe selecionada.
     */
    public ArrayList<Competidor> listaCompetidores(String nomeEquipe) {
        ArrayList<Competidor> listaCompetidores = new ArrayList<>();
        connectToDb();

        EquipeDAO daoEquipe = new EquipeDAO();

        int idEquipe = daoEquipe.getIdEquipe(nomeEquipe);
        int idCapitao = 0;

        String sqlCapitao = "SELECT * FROM equipe WHERE idequipe = ?";
        String sqlCompetidores = "SELECT * FROM competidor WHERE equipe_idequipe = ? ";
        try {
            pst = con.prepareStatement(sqlCapitao);
            pst.setInt(1, idEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                idCapitao = rs.getInt("competidor_idcompetidor");
            }

            pst = con.prepareStatement(sqlCompetidores);
            pst.setInt(1, idEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                Competidor competidorTemp = new Competidor(rs.getString("nome"), rs.getInt("idade"), rs.getString("sexo"));

                if (rs.getInt("idcompetidor") == idCapitao) { //caso o competidor atual seja o capitão da equipe, atualizamos a variavel dele para true.
                    competidorTemp.setCapitao(true);
                }
                listaCompetidores.add(competidorTemp);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
        }

        return listaCompetidores;
    }

    /**
     * Esta função exclui o competidor selecionado.
     */
    public boolean deletarCompetidor(String nomeCompetidor, int idEquipe) {

        connectToDb(); //Conecta ao banco de dados
        //Comando em SQL:

        int idCompetidor = 0;

        String sqlIdCompetidor = "SELECT * FROM competidor WHERE nome = (?) AND equipe_idequipe = (?)";
        String sqlExcluirCompetidor = "DELETE FROM competidor WHERE idcompetidor = (?)";
        try {
            pst = con.prepareStatement(sqlIdCompetidor);
            pst.setString(1, nomeCompetidor);
            pst.setInt(2, idEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                idCompetidor = rs.getInt("idcompetidor");
            }

            pst = con.prepareStatement(sqlExcluirCompetidor);
            pst.setInt(1, idCompetidor);
            
            
            System.out.println(idEquipe);
            

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }
        }

        return sucesso;
    }

}
