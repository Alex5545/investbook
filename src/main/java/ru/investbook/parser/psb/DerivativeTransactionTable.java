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

package ru.investbook.parser.psb;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.investbook.parser.*;
import ru.investbook.parser.table.Table;
import ru.investbook.parser.table.TableRow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.investbook.parser.psb.DerivativeTransactionTable.FortsTableHeader.*;

@Slf4j
public class DerivativeTransactionTable extends AbstractReportTable<DerivativeTransaction> {
    private static final String TABLE_NAME = "Информация о заключенных сделках";
    private static final String TABLE_END_TEXT = "Итого";

    public DerivativeTransactionTable(PsbBrokerReport report) {
        super(report, TABLE_NAME, TABLE_END_TEXT, FortsTableHeader.class);
    }

    @Override
    protected Collection<DerivativeTransaction> parseTable(Table table) {
        Collection<DerivativeTransaction> data = new ArrayList<>();
        data.addAll(super.parseTable(table));
        data.addAll(new DerivativeExpirationTable((PsbBrokerReport) getReport()).getData());
        return data;
    }

    @Override
    protected Collection<DerivativeTransaction> getRow(Table table, TableRow row) {
        boolean isBuy = table.getStringCellValue(row, DIRECTION).equalsIgnoreCase("покупка");
        int count = table.getIntCellValue(row, COUNT);
        String type = table.getStringCellValue(row, TYPE).toLowerCase();
        BigDecimal value;
        BigDecimal valueInPoints;
        switch (type) {
            case "опцион":
                value = table.getCurrencyCellValue(row, OPTION_PRICE).multiply(BigDecimal.valueOf(count));
                valueInPoints = table.getCurrencyCellValue(row, OPTION_QUOTE).multiply(BigDecimal.valueOf(count));
                break;
            case "фьючерс":
                value = table.getCurrencyCellValue(row, VALUE);
                valueInPoints = table.getCurrencyCellValue(row, QUOTE).multiply(BigDecimal.valueOf(count));
                break;
            default:
                throw new IllegalArgumentException("Не известный контракт " + type);
        }
        if (isBuy) {
            value = value.negate();
            valueInPoints = valueInPoints.negate();
        }
        BigDecimal commission = table.getCurrencyCellValue(row, MARKET_COMMISSION)
                .add(table.getCurrencyCellValue(row, BROKER_COMMISSION))
                .negate();
        List<DerivativeTransaction> transactionInfo = new ArrayList<>(2);
        DerivativeTransaction.DerivativeTransactionBuilder builder = DerivativeTransaction.builder()
                .timestamp(convertToInstant(table.getStringCellValue(row, DATE_TIME)))
                .transactionId(Long.parseLong(table.getStringCellValue(row, TRANSACTION)))
                .portfolio(getReport().getPortfolio())
                .contract(table.getStringCellValue(row, CONTRACT))
                .count((isBuy ? 1 : -1) * count);
        transactionInfo.add(builder
                .value(value)
                .commission(commission)
                .valueCurrency("RUB") // FORTS, only RUB
                .commissionCurrency("RUB") // FORTS, only RUB
                .build());
        transactionInfo.add(builder
                .value(valueInPoints)
                .commission(BigDecimal.ZERO)
                .valueCurrency(DerivativeTransaction.QUOTE_CURRENCY)
                .commissionCurrency("RUB") // FORTS, only RUB
                .build());
        return transactionInfo;
    }

    enum FortsTableHeader implements TableColumnDescription {
        DATE_TIME("дата включения в клиринг"),
        TRANSACTION("№"),
        TYPE("вид контракта"),
        CONTRACT("контракт"),
        DIRECTION("покупка", "продажа"),
        COUNT("кол-во"),
        QUOTE("цена фьючерсного контракта", "цена исполнения опциона", "пункты"),
        VALUE("сумма срочной сделки"),
        OPTION_QUOTE("цена опциона", "пункты"),
        OPTION_PRICE("цена опциона", "руб"),
        MARKET_COMMISSION("комиссия торговой системы"),
        BROKER_COMMISSION("комиссия брокера");

        @Getter
        private final TableColumn column;
        FortsTableHeader(String ... words) {
            this.column = TableColumnImpl.of(words);
        }
    }

}
