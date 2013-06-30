import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

public class TextMethods {
	public static final Font UNDERLINED = new Font(FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE);
	public final int FIELD_TYPE_CHECKBOX = 3;
	private static String FILE = "/Users/mhamm/Desktop/fields.pdf";

	public TextMethods() throws DocumentException, IOException {
		Document document = new Document(PageSize.A4, 20, 20, 55, 35);

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));

		document.open();
		String[] rows = { "Smoke Detectors", "Pull Stations", "Waterflows", "Tampers", "Duct Detectors",
				"Heat Detectors", "Others" };
		String[] columns = { "TYPE", "VISUAL", "FUNCTIONAL", "LOCATION/COMMENT" };
		createFourColumnChecklist(document, writer, columns, rows);
		document.close();
	}

	private void createFourColumnChecklist(Document document, PdfWriter writer, String[] columns, String[] rows)
			throws DocumentException, IOException {
		PdfPTable table = createTable(new int[] { 2, 1, 1, 4 }, 100);
		createTableHeaders(columns, table);
		createFourColumnBody(rows, table, writer, document);
	}

	private void createFourColumnBody(String[] rowLabels, PdfPTable table, PdfWriter writer, Document document)
			throws DocumentException {
		PdfFormField checkboxGroupField = PdfFormField.createCheckBox(writer);
		for (String row : rowLabels) {
			PdfPCell defaultContentCell = table.getDefaultCell();
			PdfPCell cell = new PdfPCell(defaultContentCell);
			cell.setCellEvent(new CellField(writer, checkboxGroupField, true));
			table.addCell(row);
			table.addCell(cell);
			table.addCell(cell);
			table.addCell("                                ");
		}
		document.add(table);
		writer.addAnnotation(checkboxGroupField);

	}

	private void createTableHeaders(String[] columns, PdfPTable table) {
		PdfPCell cell = null;
		for (String column : columns) {
			cell = new PdfPCell(new Paragraph(column));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		}
	}

	private PdfPTable createTable(int[] columnWidths, int percentageSize) throws DocumentException {
		PdfPTable table = new PdfPTable(columnWidths.length);
		table.setWidthPercentage(percentageSize);
		table.setWidths(columnWidths);
		table.setKeepTogether(true);
		table.getDefaultCell().setBorderWidth(0f);
		return table;
	}

	protected class CellField implements PdfPCellEvent {
		private PdfFormField parent;

		private String partialFieldName;

		private PdfWriter writer;

		private boolean checked;

		public CellField(PdfWriter writer, PdfFormField parent, boolean checked) {
			this.writer = writer;
			this.parent = parent;
			this.checked = checked;
		}

		public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {

			try {
				createCheckboxField(rect);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		private void createCheckboxField(Rectangle rect) throws IOException, DocumentException {
			RadioCheckField rf = new RadioCheckField(writer, new Rectangle(rect.getLeft(3), rect.getBottom(),
					rect.getRight(3), rect.getTop()), partialFieldName, "");
			rf.setChecked(checked);
			rf.setBorderColor(GrayColor.GRAYBLACK);
			rf.setBackgroundColor(GrayColor.GRAYWHITE);
			rf.setCheckType(RadioCheckField.TYPE_CHECK);
			parent.addKid(rf.getCheckField());
		}
	}

	public static void main(String[] args) throws IOException, DocumentException {
		new TextMethods();
	}

}