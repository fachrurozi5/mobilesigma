package com.fachru.sigmamobile.utils;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * Created by fachru on 11/02/16.
 */
public class PdfHelper {


	public static PdfPCell createCell(String text) {
		PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
		pdfPCel.setBorder(Rectangle.NO_BORDER);
		return pdfPCel;
	}

	public static PdfPCell createCell(String text, int align) {
		PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
		pdfPCel.setHorizontalAlignment(align);
		pdfPCel.setBorder(Rectangle.NO_BORDER);
		return pdfPCel;
	}

	public static PdfPCell createCell(String text, int align, int border) {
		PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
		pdfPCel.setHorizontalAlignment(align);
		pdfPCel.setBorder(border);
		return pdfPCel;
	}

	public static PdfPCell createCell(String text, int align, int border, int colspan) {
		PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
		pdfPCel.setHorizontalAlignment(align);
		pdfPCel.setBorder(border);
		pdfPCel.setColspan(colspan);
		return pdfPCel;
	}
}
