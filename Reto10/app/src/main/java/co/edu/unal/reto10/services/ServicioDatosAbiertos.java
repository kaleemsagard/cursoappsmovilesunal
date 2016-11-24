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

    public ArrayList<UnidadEstadisticaAgregada> listarRegistroAgregadoVictimas(int offset, int limit,
                                                                               String anio,
                                                                               String departamento,
                                                                               String ciclovital,
                                                                               String genero,
                                                                               String etnia) throws ConnectException {
        ArrayList<UnidadEstadisticaAgregada> registros;
        try {
            registros = clienteRestDatosAbiertos.listarRegistroUnicoVictimasAgregado(offset, limit,
                    construirFiltros(anio, departamento, ciclovital, genero, etnia));
        } catch (HttpClientErrorException ex) {
            throw new ConnectException(ex.getMessage());
        }

        return registros;
    }

    private String construirFiltros(String anio, String departamento, String ciclovital, String genero, String etnia) {
        String filtros = null;
        StringBuilder filtrosBuilder = new StringBuilder();

        if(anio != null) {
            filtrosBuilder.append("&anio_ocurrencia=");
            filtrosBuilder.append(anio);
        }
        if(departamento != null) {
            filtrosBuilder.append("&departamento_ocurrencia=");
            filtrosBuilder.append(departamento);
        }
        if(ciclovital != null) {
            filtrosBuilder.append("&ciclo_vital=");
            filtrosBuilder.append(ciclovital);
        }
        if(genero != null) {
            filtrosBuilder.append("&genero=");
            filtrosBuilder.append(genero);
        }
        if(etnia != null) {
            filtrosBuilder.append("&pertenencia_etnica=");
            filtrosBuilder.append(etnia);
        }

        if(filtrosBuilder.length() > 0) {
            filtros = filtrosBuilder.toString();
        }

        return filtros;
    }
}
