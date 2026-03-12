package it.aredegalli.coachly.user.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public final class AuditDto {

    private final UUID userId;
    private final String username;
    private final String email;
    private final String givenName;
    private final String familyName;
    private final String realmRoles;
    private final boolean internalGateway;
}
