//Nome do nosso pacote //                
package model;

//Classes necessárias para uso de Banco de dados //
import controller.Competidor;
import controller.Equipe;
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
     * ********************** INSERIR DADOS ********************************
     */
    public boolean inserirEquipeTorneio(String nomeEquipe, String nomeTorneio) {
        connectToDb(); //Conecta ao banco de dados

        //Aqui capturamos os ids da equipe e do torneio que vamos trabalhar
        //Comando em SQL:
        EquipeDAO daoEquipe = new EquipeDAO();
        int idEquipe = 0;

        idEquipe = daoEquipe.getIdEquipe(nomeEquipe);

        int idTorneio = 0;
        String sqltorneio = "SELECT * FROM torneio WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqltorneio);
            pst.setString(1, nomeTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idTorneio = rs.getInt("idtorneio");
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }

        //Comando em SQL:
        String sql = "INSERT INTO torneio_has_equipe (torneio_idtorneio,equipe_idequipe) values (?,?)";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idTorneio);
            pst.setInt(2, idEquipe);

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {

            if (ex.getErrorCode() == 1062) {
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

    public ArrayList<String> pesquisaEquipeTorneio(String nomeTorneio) {
        connectToDb(); //Conecta ao banco de dados
        ArrayList<Integer> listaIdsEquipe = new ArrayList<>();
        ArrayList<String> listaNomesEquipe = new ArrayList<>();

        int idTorneio = 0;
        String sqltorneio = "SELECT * FROM torneio WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqltorneio);
            pst.setString(1, nomeTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idTorneio = rs.getInt("idtorneio");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        int idEquipe = 0;
        String sqltorneiohasequipe = "SELECT equipe_idequipe FROM torneio_has_equipe WHERE torneio_idtorneio = ? ";
        try {
            pst = con.prepareStatement(sqltorneiohasequipe);
            pst.setInt(1, idTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idEquipe = rs.getInt("equipe_idequipe");
                listaIdsEquipe.add(idEquipe);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        String nomeEquipe;

        for (int i : listaIdsEquipe) {

            String sqlcapturafinal = "SELECT * FROM equipe WHERE idequipe = ? ";
            try {
                pst = con.prepareStatement(sqlcapturafinal);
                pst.setInt(1, i);
                rs = pst.executeQuery();

                while (rs.next()) {
                    nomeEquipe = rs.getString("nome");
                    listaNomesEquipe.add(nomeEquipe);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            }

        }

        try {
            con.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
        }

        return listaNomesEquipe;

    }

    public ArrayList<String> pesquisaCapitaoEquipe(String nomeTorneio) {
        connectToDb(); //Conecta ao banco de dados
        ArrayList<Integer> listaIdsEquipe = new ArrayList<>();
        ArrayList<String> listaCapitaes = new ArrayList<>();

        int idTorneio = 0;
        String sqltorneio = "SELECT * FROM torneio WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqltorneio);
            pst.setString(1, nomeTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idTorneio = rs.getInt("idtorneio");
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }

        int idEquipe = 0;
        String sqltorneiohasequipe = "SELECT equipe_idequipe FROM torneio_has_equipe WHERE torneio_idtorneio = ? ";
        try {
            pst = con.prepareStatement(sqltorneiohasequipe);
            pst.setInt(1, idTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idEquipe = rs.getInt("equipe_idequipe");
                listaIdsEquipe.add(idEquipe);
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }

        String nomeCapitao;

        for (int i : listaIdsEquipe) {

            String sqlcapturaidcapitao = "SELECT * FROM equipe WHERE idequipe = ? ";
            try {
                pst = con.prepareStatement(sqlcapturaidcapitao);
                pst.setInt(1, i);
                rs = pst.executeQuery();

                while (rs.next()) {

                    int idCapitao = rs.getInt("competidor_idcompetidor");

                    if (idCapitao != 0) {

                        String sqlcapturanomecapitao = "SELECT * FROM competidor WHERE idcompetidor = ? ";

                        try {
                            pst = con.prepareStatement(sqlcapturanomecapitao);
                            pst.setInt(1, idCapitao);
                            rs = pst.executeQuery();

                            while (rs.next()) {

                                nomeCapitao = rs.getString("nome");
                                listaCapitaes.add(nomeCapitao);
                            }
                        } catch (SQLException ex) {
                            System.out.println("Erro = " + ex.getMessage());
                        }

                    } else {
                        listaCapitaes.add("Sem Capitão");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Erro = " + ex.getMessage());
            }
        }
        try {
            con.close();
            pst.close();
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }

        return listaCapitaes;

    }
}
