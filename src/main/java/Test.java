import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Point {
        int x, y;
    }

    public static void main(String[] args) {
        generatePDFV2();
    }

    public static void generatePDFV2() {
        float contentBorder = 20f;
        try (PDDocument doc = new PDDocument()) {
            float pageHalfWidth = PDRectangle.A4.getWidth() / 2.0f - contentBorder;

            // 汉字字体目录
            PDFont font = PDType0Font.load(doc, new File(""));
            // pdf 内容，左右两列，利用 table 排版
            Table table = Table.builder()
                    .addColumnsOfWidth(pageHalfWidth, pageHalfWidth)
                    .addRow(addRowText(font, "标题1", 0))
                    .addRow(addRowText(font, "次级标题", 0))
                    .addRow(addRowTwoText(font, "左边1", "右边1 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边2", "右边2 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边3", "右边3 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边4", "右边4 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边5", "右边5 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边6", "右边6 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边7", "右边7 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .addRow(addRowTwoText(font, "左边8", "右边8 apple 超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长超级超级超级长"))
                    .build();

            // 分页
            float positionY = 0f;
            List<Table.TableBuilder> tableList = new ArrayList<>();
            Table.TableBuilder spTable = Table.builder().addColumnsOfWidth(pageHalfWidth, pageHalfWidth);
            tableList.add(spTable);
            for (Row row : table.getRows()) {
                if (positionY + row.getHeight() > PDRectangle.A4.getHeight() - contentBorder) {
                    positionY = 0f;
                    spTable = Table.builder().addColumnsOfWidth(pageHalfWidth, pageHalfWidth);
                    tableList.add(spTable);
                }
                spTable.addRow(row);
                positionY += row.getHeight();
            }

            // 绘制
            for (Table.TableBuilder tableBuilder : tableList) {
                Table t = tableBuilder.build();
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                    TableDrawer tableDrawer = TableDrawer.builder()
                            .contentStream(contentStream)
                            .startX(contentBorder)
                            .startY(page.getMediaBox().getUpperRightY() - contentBorder)
                            .table(t)
                            .build();
                    tableDrawer.draw();
                }
            }

            // 增加水印
            generateWaterMark(doc, font, "张三 Ka12032035923469");

            // 保存
            doc.save("");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 水印
     */
    public static void generateWaterMark(PDDocument doc, PDFont font, String text) throws IOException {
        for (PDPage page : doc.getPages()) {
            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
                // 设置透明度
                r0.setNonStrokingAlphaConstant(0.2f);
                r0.setAlphaSourceFlag(true);
                contentStream.setGraphicsStateParameters(r0);
                // 设置颜色
                contentStream.setNonStrokingColor(Color.GRAY);
                // 绘制文本
                float width = page.getMediaBox().getWidth(), height = page.getMediaBox().getHeight();
                for (float x = 0.0f; x < width; x += width / 10) {
                    for (float y = 0.0f; y < height; y += height / 10) {
                        contentStream.beginText();
                        contentStream.setFont(font, 14.0f);
                        contentStream.setTextMatrix(Matrix.getRotateInstance(0.5236, x, y));
                        contentStream.showText(text);
                        contentStream.endText();
                    }
                }
            }
        }
    }

    /**
     * 加入单列文本
     *
     * @param font
     * @param text
     * @param border
     * @return
     */
    public static Row addRowText(PDFont font, String text, float border) {
        TextCell textCell = TextCell.builder().font(font).text(text).textColor(null).borderWidth(border).colSpan(2).horizontalAlignment(HorizontalAlignment.CENTER).build();

        return Row.builder().add(textCell).build();
    }

    /**
     * 加入双列文本
     *
     * @param font
     * @param text1
     * @param text2
     * @return
     */
    public static Row addRowTwoText(PDFont font, String text1, String text2) {
        TextCell textCell1 = TextCell.builder().font(font).text(text1).borderWidth(1).horizontalAlignment(HorizontalAlignment.CENTER).build();
        TextCell textCell2 = TextCell.builder().font(font).text(text2).borderWidth(1).horizontalAlignment(HorizontalAlignment.CENTER).build();
        return Row.builder().add(textCell1).add(textCell2).build();
    }
}
