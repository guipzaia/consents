package com.raidiam.consents.adapters.rest.port;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentErrorResponse {

    private String message;
    private List<String> errors;
}
