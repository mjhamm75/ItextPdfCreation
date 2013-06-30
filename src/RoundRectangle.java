import java.io.FileOutputStream;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RoundRectangle implements PdfPCellEvent {

	@Override
	public void cellLayout(PdfPCell cell, com.itextpdf.text.Rectangle rect, PdfContentByte[] canvas) {
		PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
		cb.setColorStroke(new GrayColor(0.8f));
		cb.roundRectangle(1f, 1f, 1f, 1f, 1f);
		// cb.roundRectangle(rect.left() + 4, rect.bottom(), rect.width() - 8,
		// rect.height() - 4, 4);
		cb.stroke();
	}

	public static void main(String[] args) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("2.pdf"));
		document.open();
		RoundRectangle border = new RoundRectangle();

		PdfPTable table = new PdfPTable(6);
		PdfPCell cell;
		for (int i = 1; i <= 30; i++) {
			cell = new PdfPCell(new Phrase("day " + i));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.OUT_BOTTOM);
			cell.setPadding(4);
			cell.setCellEvent(border);
			table.addCell(cell);
		}
		document.add(table);
		document.close();

	}
}
