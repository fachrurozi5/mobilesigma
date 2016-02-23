package com.fachru.sigmamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fachru.sigmamobile.utils.CommonUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test);
    }
    private void actionPrint(String title) {

        Document document = new Document();

        File file = null;

        try {

            PdfPTable table = createTable();

            createHeaderSOTable(table);

            createColumnsTableTitle(table);

            fillTable(table);

            document = new Document(new Rectangle(612, table.getTotalHeight() + 250));

            file = CommonUtil.getOutputMediaFile(title);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            PdfWriter.getInstance(document, fileOutputStream);

            document.open();

            document.add(table);

            document.add(createFooter());

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            document.close();
            if (file != null)
                openPDf(file);
        }
    }

    private PdfPTable createTable() {
        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(400);
        table.setLockedWidth(true);

        return table;
    }

    public void createHeaderSOTable(PdfPTable table) {

        table.addCell(createCell("No. Order:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("SO102992008002709", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));
        table.addCell(createCell("Tgl. Order:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("20/10/2015", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));

        table.addCell(createCell("No. PO:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));
        table.addCell(createCell("Tgl. Kirim:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("21/10/2015", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));

        table.addCell(createCell("Tgl. Cetak:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("20/10/2015 16:50:01", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));

        table.addCell(createCell("NPWP", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell("Alamat Pembeli", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell("B2720050 ELLY", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));

    }

    public void createColumnsTableTitle( PdfPTable table) {

        table.addCell(createCell("Kode", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("Nama Produk", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 4));
        table.addCell(createCell("Satuan", Element.ALIGN_CENTER, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Qty", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Harga/Unit", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Disc", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Jml Bersih", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
    }

    public void fillTable(PdfPTable table) {

        for (int i=0; i < 15; i++) {
            table.addCell(createCell("105893", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("UHT FF DISNEY CHOCO CP 36X115ML", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell("PCS", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(String.valueOf(12),Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.priceFormat(1873), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.percentFormat(/*item.discount_principal + item.discount_nusantara*/0), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.priceFormat(24724), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        }


        table.addCell(createCell("Total Sebelum PPN :", Element.ALIGN_RIGHT, Rectangle.TOP, 4));
        table.addCell(createCell(CommonUtil.priceFormat(24724), Element.ALIGN_RIGHT, Rectangle.TOP, 1));
        table.addCell(createCell("Bonus :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(0), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("Dasar Pengenaan Pajak :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(24724), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("PPN(10%) :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(2472.4), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("Grand Total :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(24724 + 2472.4), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));

    }

    public PdfPTable createFooter() {
        float[] columnWidths = {2f, 0.8f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setTotalWidth(400);
        table.setLockedWidth(true);
        table.setSpacingBefore(100f);

        table.addCell(createCell("102992008\nWENDY(TO)", Element.ALIGN_LEFT, Rectangle.TOP, 0));
        table.addCell(createCell("", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("Ttd. Pelanggan", Element.ALIGN_LEFT, Rectangle.TOP, 0));

        return table;
    }

    private PdfPCell createCell(String text,int align, int border, int colspan) {
        PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
        pdfPCel.setHorizontalAlignment(align);
        pdfPCel.setBorder(border);
        pdfPCel.setColspan(colspan);
        pdfPCel.setPaddingBottom(5f);
        return pdfPCel;
    }

    private void openPDf(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(intent);
    }

    public void onShowPdf(View view) {
        actionPrint("Sample PDF");
    }

}
