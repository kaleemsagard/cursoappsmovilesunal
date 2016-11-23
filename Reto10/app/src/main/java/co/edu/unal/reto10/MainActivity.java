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
    @ViewById(R.id.rangoDesde)
    TextView rangoDesde;
    @ViewById(R.id.rangoHasta)
    TextView rangoHasta;

    private String anioSelected = null;
    private String departamentoSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anioSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.anio_array, android.R.layout.simple_spinner_item));
        departamentoSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.departamento_array, android.R.layout.simple_spinner_item));
    }

    @Click(R.id.fire_query_button)
    @Background
    protected void dispararConsulta() {
        try {
            ArrayList<UnidadEstadisticaAgregada> listado = servicioDatosAbiertos.
                    listarRegistroAgregadoVictimas(
                            Integer.parseInt(rangoDesde.getText().toString()),
                            Integer.parseInt(rangoHasta.getText().toString()));
            mostrarResultados(listado);
        } catch (ConnectException e) {
            Toast.makeText(this, "Hubo un error de conexión consultando la plataforma de datos abiertos.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @ItemSelect(R.id.anio_spinner)
    protected void onAnioSpinnerItemSelected(boolean selected, String itemSelected) {
        anioSelected = itemSelected;
    }

    @ItemSelect(R.id.departamento_spinner)
    protected void onDepartamentoSpinnerItemSelected(boolean selected, String itemSelected) {
        departamentoSelected = itemSelected;
    }

    @UiThread
    protected void mostrarResultados(ArrayList<UnidadEstadisticaAgregada> listado) {
        UnidadEstadisticaAgregadaFragment uadFragment = UnidadEstadisticaAgregadaFragment.newInstance(1, listado);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.activity_main, uadFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
