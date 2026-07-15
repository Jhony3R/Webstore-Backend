package jhony.ruiz.sigevi.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponse(
        @JsonProperty(value = "access_token") String accessToken,
        String rol
) {}