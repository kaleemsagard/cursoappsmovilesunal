package co.edu.unal.reto10.services.restful;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;

/**
 * Created by christian on 23/11/16.
 */
@Rest(rootUrl = "https://www.datos.gov.co/resource",
        converters = { MappingJackson2HttpMessageConverter.class })
public interface ClienteRestDatosAbiertos {

    @Get("/qqcu-iapb.json?$offset={offset}&$limit={limit}")
    public ArrayList<UnidadEstadisticaAgregada> listarRegistroUnicoVictimasAgregado(
            @Path int offset,
            @Path int limit);
}
