package co.edu.unal.reto10;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;

import java.util.ArrayList;

/**
 *
 */
@EFragment
public class UnidadEstadisticaAgregadaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LISTADO = "listado";
    private int mColumnCount = 1;
    private ArrayList<UnidadEstadisticaAgregada> mListado = new ArrayList<UnidadEstadisticaAgregada>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UnidadEstadisticaAgregadaFragment() {
    }

    public static UnidadEstadisticaAgregadaFragment newInstance(int columnCount, ArrayList<UnidadEstadisticaAgregada> listado) {
        UnidadEstadisticaAgregadaFragment fragment = new UnidadEstadisticaAgregadaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelableArrayList(ARG_LISTADO, listado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mListado = getArguments().getParcelableArrayList(ARG_LISTADO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unidadestadisticaagregada_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new UnidadEstadisticaAgregadaRecyclerViewAdapter(mListado));
        }
        return view;
    }
}
