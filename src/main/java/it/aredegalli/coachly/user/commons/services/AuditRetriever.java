package it.aredegalli.coachly.user.commons.services;

import it.aredegalli.coachly.user.commons.dto.AuditDto;
import it.aredegalli.coachly.user.commons.utils.constants.AuditConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditRetriever {

    private final ObjectProvider<HttpServletRequest> requestProvider;

    public AuditDto retrieve() {
        HttpServletRequest request = requestProvider.getIfAvailable();
        return AuditDto.builder()
            .userId(parseUuid(header(request, AuditConstants.USER_ID_HEADER)))
            .username(header(request, AuditConstants.USERNAME_HEADER))
            .email(header(request, AuditConstants.EMAIL_HEADER))
            .givenName(header(request, AuditConstants.GIVEN_NAME_HEADER))
            .familyName(header(request, AuditConstants.FAMILY_NAME_HEADER))
            .realmRoles(header(request, AuditConstants.REALM_ROLES_HEADER))
            .internalGateway(Boolean.parseBoolean(header(request, AuditConstants.INTERNAL_GATEWAY_HEADER)))
            .build();
    }

    private String header(HttpServletRequest request, String headerName) {
        if (request == null) {
            return null;
        }
        String value = request.getHeader(headerName);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private UUID parseUuid(String value) {
        if (value == null) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
