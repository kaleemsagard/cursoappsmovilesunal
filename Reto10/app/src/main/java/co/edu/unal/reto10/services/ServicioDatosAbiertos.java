package co.edu.unal.reto10.services;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpClientErrorException;

import java.net.ConnectException;
import java.util.ArrayList;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;
import co.edu.unal.reto10.services.restful.ClienteRestDatosAbiertos;

/**
 * Created by christian on 23/11/16.
 */
@EBean
public class ServicioDatosAbiertos {

    @RestService
    ClienteRestDatosAbiertos clienteRestDatosAbiertos;

    public ArrayList<UnidadEstadisticaAgregada> listarRegistroAgregadoVictimas(int offset, int limit) throws ConnectException {
        ArrayList<UnidadEstadisticaAgregada> registros;
        try {
            registros = clienteRestDatosAbiertos.listarRegistroUnicoVictimasAgregado(offset, limit);
        } catch (HttpClientErrorException ex) {
            throw new ConnectException(ex.getMessage());
        }

        return registros;
    }
}
