/*
 * InvestBook
 * Copyright (C) 2020  Vitalii Ananev <an-vitek@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.investbook.parser.uralsib;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.investbook.parser.BrokerReport;
import ru.investbook.parser.table.ReportPage;
import ru.investbook.parser.table.TableCell;
import ru.investbook.parser.table.TableCellAddress;
import ru.investbook.parser.table.excel.ExcelSheet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RequiredArgsConstructor()
@EqualsAndHashCode(of = "path")
public class UralsibBrokerReport implements BrokerReport {
    public static final ZoneId zoneId = ZoneId.of("Europe/Moscow");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String PORTFOLIO_MARKER = "Номер счета Клиента:";
    private static final String REPORT_DATE_MARKER = "за период";

    private final Workbook book;
    @Getter
    private final ReportPage reportPage;
    @Getter
    private final String portfolio;
    @Getter
    private final Path path;
    @Getter
    private final Instant reportDate;

    public UralsibBrokerReport(ZipInputStream zis) throws IOException {
        ZipEntry zipEntry = zis.getNextEntry();
        this.path = Paths.get(zipEntry.getName());
        this.book = getWorkBook(this.path.getFileName().toString(), zis);
        this.reportPage = new ExcelSheet(book.getSheetAt(0));
        this.portfolio = getPortfolio(this.reportPage);
        this.reportDate = getReportDate(this.reportPage);
    }

    public UralsibBrokerReport(String excelFileName, InputStream is) throws IOException {
        this.path = Paths.get(excelFileName);
        this.book = getWorkBook(excelFileName, is);
        this.reportPage = new ExcelSheet(book.getSheetAt(0));
        this.portfolio = getPortfolio(this.reportPage);
        this.reportDate = getReportDate(this.reportPage);
    }

    private Workbook getWorkBook(String excelFileName, InputStream is) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is); // constructor close zis
        } else {
            return new XSSFWorkbook(is);
        }
    }

    private static String getPortfolio(ReportPage reportPage) {
        try {
            TableCellAddress address = reportPage.find(PORTFOLIO_MARKER);
            for (TableCell cell : reportPage.getRow(address.getRow())) {
                if (cell != null && cell.getColumnIndex() > address.getColumn()) {
                    Object value = cell.getValue();
                    if (value instanceof String) {
                        return value.toString()
                                .replace("_invest", "")
                                .replace("SP", "");
                    } else if (value instanceof Number) {
                        return String.valueOf(((Number) value).longValue());
                    }
                }
            }
            throw new IllegalArgumentException(
                    "В отчете не найден номер договора по заданному шаблону '" + PORTFOLIO_MARKER + " XXX'");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка поиска номера Брокерского счета в отчете", e);
        }
    }

    private Instant getReportDate(ReportPage reportPage) {
        try {
            TableCellAddress address = reportPage.find(REPORT_DATE_MARKER, 0, Integer.MAX_VALUE,
                    (cell, value) -> cell.toLowerCase().contains(value.toString()));
            return convertToInstant(
                    Lists.reverse(
                            Arrays.asList(
                                    reportPage.getRow(address.getRow())
                                            .getCell(address.getColumn())
                                            .getStringCellValue()
                                            .split(" ")))
                            .get(0));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка поиска даты отчета");
        }
    }

    public Instant convertToInstant(String value) {
        value = value.trim();
        if (value.contains(":")) {
            return LocalDateTime.parse(value, UralsibBrokerReport.dateTimeFormatter).atZone(UralsibBrokerReport.zoneId).toInstant();
        } else {
            return LocalDate.parse(value, UralsibBrokerReport.dateFormatter).atStartOfDay(UralsibBrokerReport.zoneId).toInstant();
        }
    }

    public static String convertToCurrency(String value) {
        return value.replace("RUR", "RUB"); // uralsib uses RUR (used till 1998) code in reports
    }

    @Override
    public void close() throws IOException {
        this.book.close();
    }
}
