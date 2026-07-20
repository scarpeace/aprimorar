package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AlunoRelatorioResponse;
import java.awt.Color;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlunoRelatorioPdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Color HEADER_BACKGROUND = new Color(230, 230, 230);
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9);

    public byte[] gerar(AlunoRelatorioResponse relatorio) {
        try {
            var output = new ByteArrayOutputStream();
            var document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, output);

            document.open();
            addHeader(document, relatorio);
            addResumo(document, relatorio.resumo());
            addAtendimentos(document, relatorio);
            document.close();

            return output.toByteArray();
        } catch (DocumentException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível gerar o PDF", ex);
        }
    }

    private void addHeader(Document document, AlunoRelatorioResponse relatorio) throws DocumentException {
        document.add(new Paragraph("Relatório de atendimentos", TITLE_FONT));
        document.add(new Paragraph("Aluno: " + relatorio.aluno().nome(), BODY_FONT));
        document.add(new Paragraph("Escola: " + valueOrDash(relatorio.aluno().escola()), BODY_FONT));
        document.add(new Paragraph("Responsável: " + relatorio.responsavel().nome(), BODY_FONT));
        document.add(new Paragraph("Telefone: " + relatorio.responsavel().telefone(), BODY_FONT));
        document.add(new Paragraph("E-mail: " + relatorio.responsavel().email(), BODY_FONT));
        document.add(new Paragraph(
            "Período: " + DATE_FORMAT.format(relatorio.periodo().dataInicio()) + " a " + DATE_FORMAT.format(relatorio.periodo().dataFim()),
            BODY_FONT
        ));
        document.add(new Paragraph(" "));
    }

    private void addResumo(Document document, AlunoRelatorioResponse.Resumo resumo) throws DocumentException {
        document.add(new Paragraph("Resumo", SECTION_FONT));

        var table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        table.setSpacingAfter(12);
        table.addCell(headerCell("Atendimentos"));
        table.addCell(headerCell("Total"));
        table.addCell(headerCell("Pago"));
        table.addCell(headerCell("Pendente"));
        table.addCell(bodyCell(String.valueOf(resumo.totalAtendimentos())));
        table.addCell(bodyCell(formatMoney(resumo.valorTotal())));
        table.addCell(bodyCell(formatMoney(resumo.valorPago())));
        table.addCell(bodyCell(formatMoney(resumo.valorPendente())));

        document.add(table);
    }

    private void addAtendimentos(Document document, AlunoRelatorioResponse relatorio) throws DocumentException {
        document.add(new Paragraph("Atendimentos", SECTION_FONT));

        var table = new PdfPTable(new float[] {1.1f, 1.3f, 1.5f, 2.4f, 1.2f, 1.2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        table.addCell(headerCell("Data"));
        table.addCell(headerCell("Horário"));
        table.addCell(headerCell("Tipo"));
        table.addCell(headerCell("Colaborador"));
        table.addCell(headerCell("Valor"));
        table.addCell(headerCell("Pagamento"));

        for (var atendimento : relatorio.atendimentos()) {
            table.addCell(bodyCell(DATE_FORMAT.format(atendimento.dataHoraInicio())));
            table.addCell(bodyCell(TIME_FORMAT.format(atendimento.dataHoraInicio()) + " - " + TIME_FORMAT.format(atendimento.dataHoraFim())));
            table.addCell(bodyCell(atendimento.tipo().name()));
            table.addCell(bodyCell(atendimento.nomeColaborador()));
            table.addCell(bodyCell(formatMoney(atendimento.pagamentoAluno())));
            table.addCell(bodyCell(atendimento.dataPagamentoAluno() == null ? "Pendente" : "Pago"));
        }

        document.add(table);
    }

    private PdfPCell headerCell(String text) {
        var cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(HEADER_BACKGROUND);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private PdfPCell bodyCell(String text) {
        var cell = new PdfPCell(new Phrase(valueOrDash(text), BODY_FONT));
        cell.setPadding(6);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

    private String formatMoney(BigDecimal value) {
        return NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(value);
    }
}
