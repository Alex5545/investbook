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

package ru.investbook.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.investbook.converter.EntityConverter;
import ru.investbook.entity.TransactionEntity;
import ru.investbook.entity.TransactionEntityPK;
import ru.investbook.pojo.Transaction;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionRestController extends AbstractRestController<TransactionEntityPK, Transaction, TransactionEntity> {

    public TransactionRestController(JpaRepository<TransactionEntity, TransactionEntityPK> repository,
                                     EntityConverter<TransactionEntity, Transaction> converter) {
        super(repository, converter);
    }

    @Override
    @GetMapping("/transactions")
    protected List<TransactionEntity> get() {
        return super.get();
    }

    /**
     * see {@link AbstractRestController#get(Object)}
     */
    @GetMapping("/transactions/portfolio/{portfolio}/id/{id}")
    public ResponseEntity<TransactionEntity> get(@PathVariable("portfolio") String portfolio,
                                                 @PathVariable("id") Long id) {
        return super.get(getId(portfolio, id));
    }

    @Override
    @PostMapping("/transactions")
    public ResponseEntity<TransactionEntity> post(@Valid @RequestBody Transaction object) {
        return super.post(object);
    }

    /**
     * see {@link AbstractRestController#put(Object, Object)}
     */
    @PutMapping("/transactions/portfolio/{portfolio}/id/{id}")
    public ResponseEntity<TransactionEntity> put(@PathVariable("portfolio") String portfolio,
                                                 @PathVariable("id") Long id,
                                                 @Valid @RequestBody Transaction object) {
        return super.put(getId(portfolio, id), object);
    }

    /**
     * see {@link AbstractRestController#delete(Object)}
     */
    @DeleteMapping("/transactions/portfolio/{portfolio}/id/{id}")
    public void delete(@PathVariable("portfolio") String portfolio,
                       @PathVariable("id") Long id) {
        super.delete(getId(portfolio, id));
    }

    @Override
    protected Optional<TransactionEntity> getById(TransactionEntityPK id) {
        return repository.findById(id);
    }

    @Override
    protected TransactionEntityPK getId(Transaction object) {
        return getId(object.getPortfolio(), object.getId());
    }

    private TransactionEntityPK getId(String portfolio, long transactionId) {
        TransactionEntityPK pk = new TransactionEntityPK();
        pk.setId(transactionId);
        pk.setPortfolio(portfolio);
        return pk;
    }

    @Override
    protected Transaction updateId(TransactionEntityPK id, Transaction object) {
        return object.toBuilder()
                .id(id.getId())
                .portfolio(id.getPortfolio())
                .build();
    }

    @Override
    protected URI getLocationURI(Transaction object) throws URISyntaxException {
        return new URI(getLocation() + "/portfolio/" + object.getPortfolio() + "/id/" + object.getId());
    }

    @Override
    protected String getLocation() {
        return "/transactions";
    }
}
