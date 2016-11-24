package co.edu.unal.reto10.services.restful;

import android.util.Log;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;
import co.edu.unal.reto10.services.restful.interceptors.ClienteRestDatosAbiertosInterceptor;

/**
 * Created by christian on 23/11/16.
 */
@Rest(rootUrl = "https://www.datos.gov.co/resource",
        converters = { MappingJackson2HttpMessageConverter.class },
        interceptors = {ClienteRestDatosAbiertosInterceptor.class})
public interface ClienteRestDatosAbiertos {

    @Get("/qqcu-iapb.json?$offset={offset}&$limit={limit}{filtros}")
    public ArrayList<UnidadEstadisticaAgregada> listarRegistroUnicoVictimasAgregado(
            @Path int offset,
            @Path int limit,
            @Path String filtros);
}

