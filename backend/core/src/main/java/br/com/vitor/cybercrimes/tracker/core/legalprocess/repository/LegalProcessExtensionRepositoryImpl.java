package br.com.vitor.cybercrimes.tracker.core.legalprocess.repository;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessDTO;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFilter;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.mapper.LegalProcessRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static br.com.vitor.cybercrimes.tracker.core.utils.SqlUtils.buildOrderByClause;
import static br.com.vitor.cybercrimes.tracker.core.utils.SqlUtils.buildPagination;

@Repository
@RequiredArgsConstructor
public class LegalProcessExtensionRepositoryImpl implements LegalProcessExtensionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<LegalProcessDTO> findAllPaged(LegalProcessFilter legalProcessFilter) {
        String query = "SELECT *" +
                " FROM legal_process";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", legalProcessFilter.getPageSize())
                .addValue("pageNumber", legalProcessFilter.getPageNumber())
                .addValue("orderBy", legalProcessFilter.getOrderBy());

        query += buildWhereClause(legalProcessFilter, params);
        query += buildOrderByClause(legalProcessFilter.getOrderBy());
        query += buildPagination(legalProcessFilter);

        return namedParameterJdbcTemplate.query(query, params, new LegalProcessRowMapper());
    }

    private String buildWhereClause(LegalProcessFilter legalProcessFilter, MapSqlParameterSource params) {
        String whereClause = " WHERE 1=1";
        final String search = legalProcessFilter.getSearch();

        if (search != null && !search.isEmpty()) {
            whereClause += " AND (process_code ilike '%" + search + "%'" +
                    " OR acordao_code::text ilike '%" + search + "%'" +
                    " OR class ilike '%" + search + "%'" +
                    " OR subject ilike '%" + search + "%'" +
                    " OR rapporteur ilike '%" + search + "%'" +
                    ")";
        }

        if (legalProcessFilter.getIsCybercrime() != null) {
            whereClause += " AND is_cybercrime = :isCybercrime";
            params.addValue("isCybercrime", legalProcessFilter.getIsCybercrime());
        }

        return whereClause;
    }

    @Override
    public long countAll(LegalProcessFilter legalProcessFilter) {
        String query = "SELECT COUNT(1)" +
                " FROM legal_process";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", legalProcessFilter.getPageSize())
                .addValue("pageNumber", legalProcessFilter.getPageNumber())
                .addValue("orderBy", legalProcessFilter.getOrderBy());

        query += buildWhereClause(legalProcessFilter, params);

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}
