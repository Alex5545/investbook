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

package ru.investbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.investbook.entity.PortfolioPropertyEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PortfolioPropertyRepository extends JpaRepository<PortfolioPropertyEntity, Integer> {

    Optional<PortfolioPropertyEntity> findFirstByPortfolioIdAndPropertyOrderByTimestampDesc(String portfolio,
                                                                                            String property);

    Optional<PortfolioPropertyEntity> findFirstByPropertyOrderByTimestampDesc(String property);

    List<PortfolioPropertyEntity> findByPropertyAndTimestampBetweenOrderByTimestampDesc(String property,
                                                                                        Instant startDate,
                                                                                        Instant endDate);
}
