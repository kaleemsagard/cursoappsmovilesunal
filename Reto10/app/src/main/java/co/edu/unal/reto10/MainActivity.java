package co.edu.unal.reto10;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;
import co.edu.unal.reto10.services.ServicioDatosAbiertos;

@EActivity
public class MainActivity extends AppCompatActivity {

    @Bean
    ServicioDatosAbiertos servicioDatosAbiertos;
    @ViewById(R.id.anio_spinner)
    Spinner anioSpinner;
    @ViewById(R.id.departamento_spinner)
    Spinner departamentoSpinner;
    @ViewById(R.id.ciclo_vital_spinner)
    Spinner ciclovitalSpinner;
    @ViewById(R.id.genero_spinner)
    Spinner generoSpinner;
    @ViewById(R.id.etnia_spinner)
    Spinner etniaSpinner;
    @ViewById(R.id.rangoDesde)
    TextView rangoDesde;
    @ViewById(R.id.rangoHasta)
    TextView rangoHasta;

    private String anioSelected = null;
    private String departamentoSelected = null;
    private String ciclovitalSelected = null;
    private String generoSelected = null;
    private String etniaSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.anioSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.anio_array, android.R.layout.simple_spinner_item));
        this.departamentoSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.departamento_array, android.R.layout.simple_spinner_item));
        this.ciclovitalSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.ciclo_vital_array, android.R.layout.simple_spinner_item));
        this.generoSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item));
        this.etniaSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.etnia_array, android.R.layout.simple_spinner_item));
    }

    @Click(R.id.fire_query_button)
    @Background
    protected void dispararConsulta() {
        try {
            ArrayList<UnidadEstadisticaAgregada> listado = servicioDatosAbiertos.
                    listarRegistroAgregadoVictimas(
                            Integer.parseInt(this.rangoDesde.getText().toString()),
                            Integer.parseInt(this.rangoHasta.getText().toString()),
                            this.anioSelected, this.departamentoSelected, this.ciclovitalSelected,
                            this.generoSelected, this.etniaSelected);
            if(listado.isEmpty()) {
                notificarMensaje("No hay registros para la consulta realizada.");
            } else {
                mostrarResultados(listado);
            }
        } catch (ConnectException e) {
            notificarMensaje("Hubo un error de conexión consultando la plataforma de datos abiertos.");
        }
    }

    @ItemSelect(R.id.anio_spinner)
    protected void onAnioSpinnerItemSelected(boolean selected, String itemSelected) {
        if(itemSelected.equals("Todos")) {
            anioSelected = null;
        } else {
            anioSelected = itemSelected;
        }
    }

    @ItemSelect(R.id.departamento_spinner)
    protected void onDepartamentoSpinnerItemSelected(boolean selected, String itemSelected) {
        if(itemSelected.equals("Todos")) {
            departamentoSelected = null;
        } else {
            departamentoSelected = itemSelected.toUpperCase();
        }
    }

    @ItemSelect(R.id.ciclo_vital_spinner)
    protected void onCicloVitalSpinnerItemSelected(boolean selected, String itemSelected) {
        if(itemSelected.equals("Todos")) {
            ciclovitalSelected = null;
        } else {
            ciclovitalSelected = itemSelected;
        }
    }

    @ItemSelect(R.id.genero_spinner)
    protected void onGeneroSpinnerItemSelected(boolean selected, String itemSelected) {
        if(itemSelected.equals("Todos")) {
            generoSelected = null;
        } else {
            generoSelected = itemSelected;
        }
    }

    @ItemSelect(R.id.etnia_spinner)
    protected void onEtniaSpinnerItemSelected(boolean selected, String itemSelected) {
        if(itemSelected.equals("Todos")) {
            etniaSelected = null;
        } else {
            etniaSelected = itemSelected;
        }
    }

    @UiThread
    protected void mostrarResultados(ArrayList<UnidadEstadisticaAgregada> listado) {
        UnidadEstadisticaAgregadaFragment uadFragment = UnidadEstadisticaAgregadaFragment.newInstance(1, listado);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.activity_main, uadFragment);
        fragmentTransaction.commit();
    }

    @UiThread
    protected void notificarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
