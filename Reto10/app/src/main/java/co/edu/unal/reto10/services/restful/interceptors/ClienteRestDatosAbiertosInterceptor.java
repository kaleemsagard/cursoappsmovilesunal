package co.edu.unal.reto10.services.restful.interceptors;

import android.util.Log;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Intercpetor para las invocaciones a través del cliente de datos abiertos. Se utiliza para recodificar
 * los símbolos "&" y "=" que se envían en las consultas que tienen filtros preconstruidos.
 */
public class ClienteRestDatosAbiertosInterceptor implements ClientHttpRequestInterceptor {

    private static final String TAG = ClienteRestDatosAbiertosInterceptor.class.getSimpleName();

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final QueryMultiParamsHttpRequest modifiedRequest = new QueryMultiParamsHttpRequest(request);
        return execution.execute(modifiedRequest, body);
    }
}

/**
 * Clase encargada de procesar el request y recodificar la URI consultada para cambiar los símbolos
 * codificados de "&" y "=" por su valor original.
 */
class QueryMultiParamsHttpRequest implements HttpRequest {

    private static final String TAG = QueryMultiParamsHttpRequest.class.getSimpleName();
    private HttpRequest httpRequest;

    public QueryMultiParamsHttpRequest(final HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public HttpMethod getMethod() {
        return httpRequest.getMethod();
    }

    @Override
    public URI getURI() {
        final URI originalURI = httpRequest.getURI();
        final String query = originalURI.getQuery() != null ? originalURI.getQuery().replace("%3D", "=").replace("%26", "&") : null;
        URI newURI = null;
        try {
            newURI = new URI(originalURI.getScheme(), originalURI.getUserInfo(), originalURI.getHost(), originalURI.getPort(), originalURI.getPath(),
                    query, originalURI.getFragment());
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error while creating URI of QueryMultiParamsHttpRequest", e);
        }
        return newURI;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpRequest.getHeaders();
    }
}
