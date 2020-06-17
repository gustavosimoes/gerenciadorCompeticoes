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
     * ********************** INSERIR DADOS ********************************
     */
    public boolean inserirCompetidor(Competidor novoCompetidor, String nomeEquipe) {

        connectToDb(); //Conecta ao banco de dados
        //Comando em SQL:

        EquipeDAO daoEquipe = new EquipeDAO();

        int idEquipe = 0;

        idEquipe = daoEquipe.getIdEquipe(nomeEquipe);

        String sql = "INSERT INTO competidor (nome,idade,sexo,equipe_idequipe) values (?,?,?,?)";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novoCompetidor.getNome());
            pst.setInt(2, novoCompetidor.getIdade());
            pst.setString(3, novoCompetidor.getSexo());
            pst.setInt(4, idEquipe);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Inserção feita com sucesso!");
            sucesso = true;
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
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
     * ********************* Listagem de competidores por equipe
     *
     ***************
     * @return lista de competidores
     */
    public ArrayList<Competidor> listaCompetidores(String nomeEquipe) {
        ArrayList<Competidor> listaCompetidores = new ArrayList<>();
        connectToDb();

        EquipeDAO daoEquipe = new EquipeDAO();

        int idEquipe = 0, idCapitao = 0;

        idEquipe = daoEquipe.getIdEquipe(nomeEquipe);

        String sqlCapitao = "SELECT * FROM equipe WHERE idequipe = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqlCapitao);
            pst.setInt(1, idEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                idCapitao = rs.getInt("competidor_idcompetidor");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        //Comando em SQL:
        String sql = "SELECT * FROM competidor WHERE equipe_idequipe = ? ";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEquipe);
            rs = pst.executeQuery();

            while (rs.next()) {
                Competidor competidorTemp = new Competidor(rs.getString("nome"), rs.getInt("idade"), rs.getString("sexo"));

                if (rs.getInt("idcompetidor") == idCapitao) {
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

    public boolean deletarCompetidor(String nomeCompetidor) {

        connectToDb(); //Conecta ao banco de dados
        //Comando em SQL:

        int idCompetidor = 0;

        String sqlequipe = "SELECT * FROM competidor WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqlequipe);
            pst.setString(1, nomeCompetidor);
            rs = pst.executeQuery();

            while (rs.next()) {
                idCompetidor = rs.getInt("idcompetidor");
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }

        String sql = "DELETE FROM competidor WHERE idcompetidor = ?";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idCompetidor);

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
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

}
