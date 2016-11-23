package co.edu.unal.reto10.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christian on 23/11/16.
 */
public class UnidadEstadisticaAgregada implements Parcelable {

    public String anio_ocurrencia;
    public String ciclo_vital;
    public String dane_ocurrencia;
    public String departamento_ocurrencia;
    public String discapacidad;
    public String genero;
    public String municipio_ocurrencia;
    public String pertenencia_etnica;
    public String tipo_de_victima;
    public String total;

    public UnidadEstadisticaAgregada() {

    }

    public UnidadEstadisticaAgregada(String anio_ocurrencia, String ciclo_vital, String dane_ocurrencia,
                                     String departamento_ocurrencia, String discapacidad, String genero,
                                     String municipio_ocurrencia, String pertenencia_etnica,
                                     String tipo_de_victima, String total) {
        this.anio_ocurrencia = anio_ocurrencia;
        this.ciclo_vital = ciclo_vital;
        this.dane_ocurrencia = dane_ocurrencia;
        this.departamento_ocurrencia = departamento_ocurrencia;
        this.discapacidad = discapacidad;
        this.genero = genero;
        this.municipio_ocurrencia = municipio_ocurrencia;
        this.pertenencia_etnica = pertenencia_etnica;
        this.tipo_de_victima = tipo_de_victima;
        this.total = total;
    }

    protected UnidadEstadisticaAgregada(Parcel in) {
        anio_ocurrencia = in.readString();
        ciclo_vital = in.readString();
        dane_ocurrencia = in.readString();
        departamento_ocurrencia = in.readString();
        discapacidad = in.readString();
        genero = in.readString();
        municipio_ocurrencia = in.readString();
        pertenencia_etnica = in.readString();
        tipo_de_victima = in.readString();
        total = in.readString();
    }

    public static final Creator<UnidadEstadisticaAgregada> CREATOR = new Creator<UnidadEstadisticaAgregada>() {
        @Override
        public UnidadEstadisticaAgregada createFromParcel(Parcel in) {
            return new UnidadEstadisticaAgregada(in);
        }

        @Override
        public UnidadEstadisticaAgregada[] newArray(int size) {
            return new UnidadEstadisticaAgregada[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.anio_ocurrencia);
        parcel.writeString(this.ciclo_vital);
        parcel.writeString(this.dane_ocurrencia);
        parcel.writeString(this.departamento_ocurrencia);
        parcel.writeString(this.discapacidad);
        parcel.writeString(this.genero);
        parcel.writeString(this.municipio_ocurrencia);
        parcel.writeString(this.pertenencia_etnica);
        parcel.writeString(this.tipo_de_victima);
        parcel.writeString(this.total);
    }
}
