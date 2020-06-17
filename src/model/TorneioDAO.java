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
            //JOptionPane.showMessageDialog(null, "Conexão feita com sucesso!");
            //System.out.println("Conexão feita com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Mensagem de Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * ********************** INSERIR DADOS ********************************
     */
    public boolean inserirTorneio(Torneio novoTorneio) {
        connectToDb(); //Conecta ao banco de dados
        //Comando em SQL:
        String sql = "INSERT INTO torneio (nome,localizacao) values (?,?)";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, novoTorneio.getNomeTorneio());
            pst.setString(2, novoTorneio.getLocalizacao());

            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
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
                System.out.println("Erro = " + ex.getMessage());
            }
        }
        return sucesso;
    }

    /**
     * ********************* CRIANDO ARRAYLIST COM OS NOMES DAS EQUIPES
     * **************
     */
    public ArrayList<String> listaTorneios() {
        ArrayList<String> listaTorneios = new ArrayList<>();
        connectToDb();
        //Comando em SQL:
        String sql = "SELECT * FROM torneio";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql); //ref. a tabela resultante da busca
            while (rs.next()) {
                //System.out.println(rs.getString("nome"));

                listaTorneios.add(rs.getString("nome"));
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
        return listaTorneios;
    }

    public boolean atualizaNomeTorneio(String nomeTorneio, String novoNomeTorneio) {

        connectToDb();
        int idTorneio = this.getIdTorneio(nomeTorneio);

        //Comando em SQL:
        String sql = "UPDATE torneio SET nome = ? WHERE idtorneio = ?";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
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
                System.out.println("Erro = " + ex.getMessage());
            }
        }

        return sucesso;
    }

    public boolean atualizaLocalizacaoTorneio(String nomeTorneio, String novaLocalizacao) {

        connectToDb();
        int idTorneio = this.getIdTorneio(nomeTorneio);

        //Comando em SQL:
        String sql = "UPDATE torneio SET localizacao = ? WHERE idtorneio = ?";
        //O comando recebe paramêtros -> consulta dinâmica (pst)
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
                System.out.println("Erro = " + ex.getMessage());
            }
        }

        return sucesso;
    }

    public boolean deletarTorneio(String nomeTorneio) {
        connectToDb();
        //Comando em SQL:

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
    
    public Integer getIdTorneio(String nomeTorneio){
        
        connectToDb();
        
        int idTorneio = 0;
        
        String sqlequipe = "SELECT * FROM torneio WHERE nome = ?";
        //O comando NÃO recebe parâmetros -> consulta estática (st)
        try {
            pst = con.prepareStatement(sqlequipe);
            pst.setString(1, nomeTorneio);
            rs = pst.executeQuery();

            while (rs.next()) {
                idTorneio = rs.getInt("idtorneio");
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
        }
        
        return idTorneio;
    }
}
