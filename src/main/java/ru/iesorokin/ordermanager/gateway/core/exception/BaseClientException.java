package ru.iesorokin.ordermanager.gateway.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import ru.iesorokin.ordermanager.gateway.web.dto.ErrorResponse;

import java.io.IOException;

@Slf4j
public class BaseClientException extends RuntimeException {
    private final ObjectMapper mapper = new ObjectMapper();
    @Getter
    private ErrorResponse errors;

    public BaseClientException(String message) {
        super(message);
    }

    public BaseClientException(String message, RestClientException e) {
        super(message, e);
        checkBody(e);
    }

    @Override
    public synchronized RestClientException getCause() {
        return (RestClientException) super.getCause();
    }

    private void checkBody(RestClientException e) {
        if (e instanceof RestClientResponseException) {
            String bodyAsString = ((RestClientResponseException) e).getResponseBodyAsString();
            if (StringUtils.isNotEmpty(bodyAsString)) {
                try {
                    errors = mapper.readValue(bodyAsString, ErrorResponse.class);
                } catch (IOException ioException) {
                    log.error("Error when communicated with client", e, ioException);
                }
            }
        }
    }
}
