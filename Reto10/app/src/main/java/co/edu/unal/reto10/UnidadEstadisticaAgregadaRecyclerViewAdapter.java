package co.edu.unal.reto10;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.unal.reto10.models.UnidadEstadisticaAgregada;

import java.util.List;

/**
 *
 */
public class UnidadEstadisticaAgregadaRecyclerViewAdapter extends
        RecyclerView.Adapter<UnidadEstadisticaAgregadaRecyclerViewAdapter.ViewHolder> {

    private final List<UnidadEstadisticaAgregada> mValues;

    public UnidadEstadisticaAgregadaRecyclerViewAdapter(List<UnidadEstadisticaAgregada> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_unidadestadisticaagregada, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        StringBuilder anioLugarBuilder = new StringBuilder();
        anioLugarBuilder.append(mValues.get(position).anio_ocurrencia);
        anioLugarBuilder.append(" - ");
        if(!mValues.get(position).municipio_ocurrencia.equals("Sin Información")) {
            anioLugarBuilder.append(mValues.get(position).municipio_ocurrencia);
            anioLugarBuilder.append(" (");
            if(!mValues.get(position).departamento_ocurrencia.equals("Sin Información")) {
                anioLugarBuilder.append(mValues.get(position).departamento_ocurrencia);
            } else {
                anioLugarBuilder.append("No departamento");
            }
            anioLugarBuilder.append(" )");
        } else {
            if(!mValues.get(position).departamento_ocurrencia.equals("Sin Información")) {
                anioLugarBuilder.append(mValues.get(position).departamento_ocurrencia);
            } else {
                anioLugarBuilder.append("No informa lugar");
            }
        }
        holder.mAnioOcurrenciaDepartamentoMunicipioDane.setText(anioLugarBuilder.toString());
        holder.mCicloVital.setText(mValues.get(position).ciclo_vital);
        //holder.mDiscapacidad.setText(mValues.get(position).discapacidad);
        holder.mGenero.setText(mValues.get(position).genero);
        holder.mPertenenciaEtnica.setText(mValues.get(position).pertenencia_etnica);
        //holder.mTipoVictima.setText(mValues.get(position).tipo_de_victima);
        holder.mTotal.setText(mValues.get(position).total);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public UnidadEstadisticaAgregada mItem;
        public final TextView mAnioOcurrenciaDepartamentoMunicipioDane;
        public final TextView mCicloVital;
        //public final TextView mDiscapacidad;
        public final TextView mGenero;
        public final TextView mPertenenciaEtnica;
        //public final TextView mTipoVictima;
        public final TextView mTotal;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAnioOcurrenciaDepartamentoMunicipioDane = (TextView) view.findViewById(R.id.anio_lugar_ocurrencia);
            mCicloVital = (TextView) view.findViewById(R.id.ciclo_vital);
            //mDiscapacidad = (TextView) view.findViewById(R.id.discapacidad);
            mGenero = (TextView) view.findViewById(R.id.genero);
            mPertenenciaEtnica = (TextView) view.findViewById(R.id.etnia);
            //mTipoVictima = (TextView) view.findViewById(R.id.tipo);
            mTotal = (TextView) view.findViewById(R.id.total);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAnioOcurrenciaDepartamentoMunicipioDane.getText() + "'";
        }
    }
}
