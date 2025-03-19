package com.gestion_presence.gestion_presence.utils;

import com.gestion_presence.gestion_presence.Models.Emargements;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import jxl.write.*;
import jxl.write.Number;
import jxl.Workbook;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


import java.io.FileOutputStream;


public class ExportService {

    public void exportToPDF(List<Emargements> emargements, String filePath) {
        try {
            // Création du document PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titre
            document.add(new Paragraph("Liste des Émargements").setBold().setFontSize(16));

            // Création du tableau
            float[] columnWidths = {50, 150, 100, 200, 200}; // Largeur des colonnes
            Table table = new Table(columnWidths);

            // Ajout des en-têtes
            table.addCell(new Cell().add(new Paragraph("ID")));
            table.addCell(new Cell().add(new Paragraph("Date")));
            table.addCell(new Cell().add(new Paragraph("Statut")));
            table.addCell(new Cell().add(new Paragraph("Professeur")));
            table.addCell(new Cell().add(new Paragraph("Cours")));

            // Remplissage des données
            for (Emargements e : emargements) {
                table.addCell(String.valueOf(e.getId()));
                table.addCell(e.getDate().toString());
                table.addCell(e.getStatut());
                table.addCell(e.getProfesseur().getNom());
                table.addCell(e.getCours().getNom());
            }

            // Ajout du tableau au document
            document.add(table);

            // Fermeture du document
            document.close();
            System.out.println("📄 PDF exporté avec succès : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportToExcel(List<Emargements> emargements, String filePath) {
        try {
            // Création du fichier Excel
            WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));
            WritableSheet sheet = workbook.createSheet("Émargements", 0);

            // Définition des entêtes
            sheet.addCell(new Label(0, 0, "ID"));
            sheet.addCell(new Label(1, 0, "Date"));
            sheet.addCell(new Label(2, 0, "Statut"));
            sheet.addCell(new Label(3, 0, "Professeur"));
            sheet.addCell(new Label(4, 0, "Cours"));

            // Format de la date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Remplir les données
            int rowNum = 1;
            for (Emargements emargement : emargements) {
                sheet.addCell(new Number(0, rowNum, emargement.getId()));
                // Formatage de la date avant de l'ajouter
                String formattedDate = dateFormat.format(emargement.getDate());
                sheet.addCell(new Label(1, rowNum, formattedDate));
                sheet.addCell(new Label(2, rowNum, emargement.getStatut()));
                sheet.addCell(new Label(3, rowNum, emargement.getProfesseur().getNom()));
                sheet.addCell(new Label(4, rowNum, emargement.getCours().getNom()));
                rowNum++;
            }

            // Écriture et fermeture du fichier Excel
            workbook.write();
            workbook.close();

            System.out.println("Excel exporté avec succès : " + filePath);
        } catch (IOException | WriteException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'exportation d'Excel.");
        }
    }
}
