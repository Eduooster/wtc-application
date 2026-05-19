package org.wtc.application.user.dto;

import java.util.Set;

public record UpdateUserTagRequestDto (

        Set<Long> tagsId

) {
}
