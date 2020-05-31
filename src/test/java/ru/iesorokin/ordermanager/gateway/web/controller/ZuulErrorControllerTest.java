package ru.iesorokin.ordermanager.gateway.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iesorokin.ordermanager.gateway.core.exception.BaseClientException;
import ru.iesorokin.ordermanager.gateway.web.dto.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ZuulErrorControllerTest {
    private ZuulErrorController zuulErrorController;
    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        zuulErrorController = new ZuulErrorController();
    }

    @Test
    public void shouldHandleError() {
        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(404);
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(null);
        ResponseEntity responseEntity = ResponseEntity.status(404).body(new ErrorResponse("Unexpected error occurred"));
        ResponseEntity methodResponse = zuulErrorController.error(request);
        assertThat(responseEntity).isEqualTo(methodResponse);
        assertThat(zuulErrorController.getErrorPath()).isNullOrEmpty();
    }

    @Test
    public void shouldHandleError2() {
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new BaseClientException("BaseClientException"));
        ResponseEntity responseEntity = ResponseEntity
                                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                .body(new ErrorResponse("BaseClientException"));
        ResponseEntity methodResponse = zuulErrorController.error(request);
        assertThat(responseEntity).isEqualTo(methodResponse);
        assertThat(zuulErrorController.getErrorPath()).isNullOrEmpty();
    }

}
