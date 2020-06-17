/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Equipe;
import controller.Torneio;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.EquipeDAO;
import model.TorneioDAO;

/**
 *
 * @author gustavo
 */
public class TelaInicial extends javax.swing.JFrame {

    private static String selectedValueNomeEquipe;
    private static String selectedValueNomeTorneio;

    public static String getSelectedValueNomeEquipe() {
        return selectedValueNomeEquipe;
    }

    public static void setSelectedValueNomeEquipe(String selectedValueNomeEquipe) {
        TelaInicial.selectedValueNomeEquipe = selectedValueNomeEquipe;
    }

    public static String getSelectedValueNomeTorneio() {
        return selectedValueNomeTorneio;
    }

    public static void setSelectedValueNomeTorneio(String selectedValueNomeTorneio) {
        TelaInicial.selectedValueNomeTorneio = selectedValueNomeTorneio;
    }

 

    /**
     * Creates new form TelaInicial
     */
    public TelaInicial() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btn_cadastroNT = new javax.swing.JButton();
        btn_editarT = new javax.swing.JButton();
        btn_editarE = new javax.swing.JButton();
        btn_verEquipesTorneio = new javax.swing.JButton();
        btn_cadastroE = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gerenciador de Competições");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(153, 255, 102));

        btn_cadastroNT.setBackground(new java.awt.Color(234, 91, 12));
        btn_cadastroNT.setText("Cadastrar Novo Torneio");
        btn_cadastroNT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastroNTActionPerformed(evt);
            }
        });

        btn_editarT.setBackground(new java.awt.Color(234, 91, 12));
        btn_editarT.setText("Editar Torneio");
        btn_editarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarTActionPerformed(evt);
            }
        });

        btn_editarE.setBackground(new java.awt.Color(234, 91, 12));
        btn_editarE.setText("Editar Equipe");
        btn_editarE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarEActionPerformed(evt);
            }
        });

        btn_verEquipesTorneio.setBackground(new java.awt.Color(234, 91, 12));
        btn_verEquipesTorneio.setText("Ver Equipes por Torneio");
        btn_verEquipesTorneio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_verEquipesTorneioActionPerformed(evt);
            }
        });

        btn_cadastroE.setBackground(new java.awt.Color(234, 91, 12));
        btn_cadastroE.setText("Cadastrar Equipe");
        btn_cadastroE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastroEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_verEquipesTorneio, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editarE, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editarT, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cadastroE, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cadastroNT, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btn_cadastroNT)
                .addGap(18, 18, 18)
                .addComponent(btn_cadastroE)
                .addGap(18, 18, 18)
                .addComponent(btn_editarT)
                .addGap(18, 18, 18)
                .addComponent(btn_editarE)
                .addGap(18, 18, 18)
                .addComponent(btn_verEquipesTorneio)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cadastroNTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastroNTActionPerformed

        CadastroTorneio cadastro = new CadastroTorneio();
        cadastro.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cadastro.setVisible(true);
    }//GEN-LAST:event_btn_cadastroNTActionPerformed

    private void btn_cadastroEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastroEActionPerformed
        // TODO add your handling code here:

        CadastroEquipe cadastroEquipe = new CadastroEquipe();

        cadastroEquipe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cadastroEquipe.setVisible(true);
    }//GEN-LAST:event_btn_cadastroEActionPerformed

    private void btn_editarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarTActionPerformed
        // TODO add your handling code here:

        TorneioDAO daoTorneio = new TorneioDAO();
        if (!daoTorneio.listaTorneios().isEmpty()) {
            Object[] itens = daoTorneio.listaTorneios().toArray();
            selectedValueNomeTorneio = (String) JOptionPane.showInputDialog(null,
                    "Escolha a operação", "Opçao", JOptionPane.INFORMATION_MESSAGE, null,
                    itens, itens[0]);
            if(TelaInicial.selectedValueNomeTorneio != null){
                EditarTorneio editarTorneio = new EditarTorneio();
                editarTorneio.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                editarTorneio.setVisible(true);
            } 
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum Torneio Cadastrado");
        }

    }//GEN-LAST:event_btn_editarTActionPerformed

    private void btn_editarEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarEActionPerformed
        // TODO add your handling code here:
        EquipeDAO daoEquipe = new EquipeDAO();

        if (!daoEquipe.listaEquipes().isEmpty()) {
            Object[] itens = daoEquipe.listaEquipes().toArray();
            selectedValueNomeEquipe = (String) JOptionPane.showInputDialog(null,
                    "Selecione a Equipe", "Opçao", JOptionPane.INFORMATION_MESSAGE, null,
                    itens, itens[0]);

            if (selectedValueNomeEquipe != null) {
                EditarEquipe editarEquipe = new EditarEquipe();
                editarEquipe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                editarEquipe.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma Equipe Cadastrada");
        }


    }//GEN-LAST:event_btn_editarEActionPerformed

    private void btn_verEquipesTorneioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_verEquipesTorneioActionPerformed
        // TODO add your handling code here:
        TorneioDAO daoTorneio = new TorneioDAO();
        if (!daoTorneio.listaTorneios().isEmpty()) {
            VizualizaEquipeTorneio vizualizaEquipeTorneio = new VizualizaEquipeTorneio();
            vizualizaEquipeTorneio.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            vizualizaEquipeTorneio.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum Torneio Cadastrado");
        }
    }//GEN-LAST:event_btn_verEquipesTorneioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cadastroE;
    private javax.swing.JButton btn_cadastroNT;
    private javax.swing.JButton btn_editarE;
    private javax.swing.JButton btn_editarT;
    private javax.swing.JButton btn_verEquipesTorneio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}