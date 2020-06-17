/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.CompetidorDAO;
import model.TorneioDAO;
import model.TorneioHasEquipeDAO;

/**
 *
 * @author gustavo
 */
public class GeradorPdf {

    public boolean salvarPdf(String nomeTorneio) throws FileNotFoundException, DocumentException, IOException {

        boolean sucesso = false;

        TorneioDAO daoTorneio = new TorneioDAO();
        
        Document document = new Document();
        String fileDirectory = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar a Pasta...");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(new JPanel());

        if (result == JFileChooser.APPROVE_OPTION) {
            fileDirectory = fileChooser.getSelectedFile().getPath();

            try {

                PdfWriter.getInstance(document, new FileOutputStream(fileDirectory + "/documento.pdf"));
                document.open();
                Image img = Image.getInstance("./src/images/iconPdf.png");
                img.setAlignment(50);
                img.setIndentationLeft(20);
                document.add(img);
                Paragraph pgTorneio = new Paragraph(-20, "Nome do torneio: " + nomeTorneio);
                Paragraph pgLocal = new Paragraph(-20, "Localização: " + daoTorneio.getLocalTorneio(nomeTorneio));
                document.add(pgLocal);
                document.add(pgTorneio);
                

                TorneioHasEquipeDAO daoTorneioHasEquipe = new TorneioHasEquipeDAO();
                document.add(new Paragraph(50, "Equipes"));
                ArrayList<String> equipes = daoTorneioHasEquipe.pesquisaEquipeTorneio(nomeTorneio);

                for (int i = 0; i < equipes.size(); i++) {

                    Paragraph pgEquipe = new Paragraph(equipes.get(i) + "\n");
                    pgEquipe.setIndentationLeft(25);
                    document.add(pgEquipe);

                    CompetidorDAO daoCompetidor = new CompetidorDAO();
                    ArrayList<Competidor> competidoresEquipe = daoCompetidor.listaCompetidores(equipes.get(i));

                    //CRIANDO CABEÇALHO DA TABELA
                    if (!competidoresEquipe.isEmpty()) {
                        document.add(new Paragraph(15," "));
                        PdfPTable table = this.inserirTabela(competidoresEquipe);  //FUNÇÃO DE INSERIR TABELA
                        document.add(table); 
                    } else {
                        Paragraph pgEquipeSemComp = new Paragraph("Equipe sem competidores.\n");
                        pgEquipeSemComp.setIndentationLeft(50);
                        document.add(pgEquipeSemComp);
                    }
                }
                sucesso = true;
            } catch (DocumentException | FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Erro = " + ex.getMessage());
            } finally {
                document.close();
            }
            Desktop.getDesktop().open(new File(fileDirectory + "/documento.pdf"));
        }

        return sucesso;
    }

    /**
     *
     * @param competidoresEquipe
     * @return table
     * @throws DocumentException
     */
    public PdfPTable inserirTabela(ArrayList<Competidor> competidoresEquipe) throws DocumentException {

        PdfPTable table = new PdfPTable(new float[]{10f, 3f, 3f});
        PdfPCell celulaNome = new PdfPCell(new Phrase("Nome"));
        celulaNome.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celulaDataNasc = new PdfPCell(new Phrase("Idade"));
        celulaDataNasc.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celulaSexo = new PdfPCell(new Phrase("Sexo"));
        celulaSexo.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(celulaNome);
        table.addCell(celulaDataNasc);
        table.addCell(celulaSexo);

        for (Competidor competidor : competidoresEquipe) {  //ADICIONANDO OS COMPETIDORES NA TABELA

            PdfPCell celula1 = new PdfPCell(new Phrase(competidor.getNome()));
            PdfPCell celula2 = new PdfPCell(new Phrase(Integer.toString(competidor.getIdade())));
            PdfPCell celula3 = new PdfPCell(new Phrase(competidor.getSexo()));

            celula1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celula2.setHorizontalAlignment(Element.ALIGN_CENTER);
            celula3.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (competidor.isCapitao()) {                     // Aplica a cor Ciano ao competidor se o mesmo for o capitão.
                celula1.setBackgroundColor(BaseColor.CYAN);
                celula2.setBackgroundColor(BaseColor.CYAN);
                celula3.setBackgroundColor(BaseColor.CYAN);
            }

            table.addCell(celula1);
            table.addCell(celula2);
            table.addCell(celula3);

        }

        return table;

    }
}
