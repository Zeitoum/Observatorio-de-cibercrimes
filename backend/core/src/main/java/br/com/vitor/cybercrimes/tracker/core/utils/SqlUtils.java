package br.com.vitor.cybercrimes.tracker.core.utils;

import br.com.vitor.cybercrimes.tracker.core.commons.BaseRequestFilter;

public class SqlUtils {

    public static String buildOrderByClause(String orderBy) {
        if (orderBy == null || orderBy.isBlank()) {
            return "";
        }

        String[] parts = orderBy.split(",");
        StringBuilder orderByClause = new StringBuilder(" ORDER BY ");
        StringBuilder fields = new StringBuilder();

        for (String part : parts) {
            String trimmedPart =  part.trim();
            trimmedPart = trimmedPart.replace('+', ' ');

            if (!fields.isEmpty()) {
                fields.append(", ");
            }

            fields.append(trimmedPart);
        }

        return orderByClause.append(fields).toString();
    }

    public static String buildPagination(BaseRequestFilter filter) {
        int pageSize = filter.getPageSize();
        int pageNumber = filter.getPageNumber();

        if (pageSize <= 0 || pageNumber < 0) {
            return "";
        }

        int offset = calculateOffset(pageSize, pageNumber);
        return String.format(" LIMIT %d OFFSET %d", pageSize, offset);
    }

    public static int calculateOffset(int pageSize, int pageNumber) {
        return pageSize * (pageNumber - 1);
    }
}
