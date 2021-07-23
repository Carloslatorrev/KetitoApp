package cl.ckelar.android.ketito.helpers.generator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class NotaCalificacionGenerator {

    private Context context;
    private TextView title;
    private TextView txtMinimo;
    private TextView txtMaximo;
    private TextView nota;
    private TextView txtDescription;
    private SeekBar seekBar;

    private KetitoDatabase ketitoDatabase;
    private Preguntas pregunta;
    private UserSesion userSesion;
    private int idPreg;
    private Respuesta respuesta;
    private Context contextT;
    private String idRespuesta;

    /**
     * Inicializa los elementos de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     */
    public void init(Context context, ViewGroup root){
        contextT = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_encuesta_notacalificacion, root, true);
        title = view.findViewById(R.id.notaCalificacionTitle);
        txtMinimo= view.findViewById(R.id.txtNotaCalifiacionInit);
        txtMaximo= view.findViewById(R.id.txtNotaCalifiacionFin);
        txtDescription = view.findViewById(R.id.txtNotaCalificacionDescription);
        nota= view.findViewById(R.id.txtNotaCalifiacionNota);
        seekBar = view.findViewById(R.id.notaCalificacionSeekBar);
        ketitoDatabase = ketitoDatabase.getInstance(context);

    }

    /**
     * Realiza el set de las variables y elementos de la vista asociada a la pregunta
     * @param idPregunta Id del objeto pregunta
     */
    //Creamos la vista de acuerdo al Id de la pregunta (guardada en la base de datos local)
    public void setLayout(int idPregunta){
        idPreg = idPregunta;
        pregunta = ketitoDatabase.preguntasDao().getPreguntasById(idPregunta);
        String titleText = title.getText().toString();
        titleText = titleText.replace("%title", pregunta.getPregunta());
        title.setText(titleText);
        String subtitle = pregunta.getSubtitulo();
        String descriptionText = txtDescription.getText().toString();
        if(subtitle.length()>0 && subtitle != null){
            descriptionText = descriptionText.replace("%description", subtitle);
            txtDescription.setText(descriptionText);
        }else{
            descriptionText = descriptionText.replace("%description", "");
            txtDescription.setText(descriptionText);
        }

        seekBar.setMax(pregunta.getEscala());
        String etiquetaMinimo = pregunta.getEtiquetaMinimo();
        String minimoText = txtMinimo.getText().toString();
        if(etiquetaMinimo != null && etiquetaMinimo != ""){
            minimoText = minimoText.replace("%init", pregunta.getEtiquetaMinimo());
            txtMinimo.setText(minimoText);
        }else{
            minimoText = "1";
            txtMinimo.setText(minimoText);
        }
        String etiquetaMaximo = pregunta.getEtiquetaMaximo();
        String maxText= txtMaximo.getText().toString();
        if(etiquetaMaximo != null && etiquetaMaximo != ""){
            maxText = maxText.replace("%fin", pregunta.getEtiquetaMaximo());
            txtMaximo.setText(maxText);
        }else{
            maxText = String.valueOf(pregunta.getEscala());
            txtMaximo.setText(maxText);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nota.setTextColor(ContextCompat.getColor(contextT, R.color.baseKetito));
                nota.setText(String.valueOf(progress));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Obtiene la respuesta de los elementos de la vista en relación a la selección del usuario.
     * @param idToma Id de la Toma correspondiente a la encuesta
     * @return Retorna el objeto respuesta de la clase Respuesta, con los parametros de la selección del usuario
     */
    //Se obtiene la respuesta seleccionada
    public Respuesta getRespuesta(int idToma){
        String textRespuesta;
        if(nota.getText().toString() != null){
            textRespuesta = nota.getText().toString();
        }else{
            textRespuesta = "null";
        }
        respuesta = new Respuesta();
        int random = (int) (Math.random() * 1000) + 1;
        idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
        respuesta.setIdRespuesta(idRespuesta);
        respuesta.setIdPregunta(idPreg);
        respuesta.setIdAlternativa("null");
        respuesta.setIdToma(idToma);
        respuesta.setNivel("null");
        respuesta.setRespuesta(textRespuesta);
        return respuesta;
    }

    /**
     * Crea el listado de elementos de la vista
     * @return listado o list View de elementos de la vista
     */
    public List<View> getListView(){
        List<View> listaView = new ArrayList<>();
        listaView.add(seekBar);
        listaView.add(title);
        listaView.add(txtDescription);
        listaView.add(txtMaximo);
        listaView.add(txtMinimo);
        listaView.add(nota);
        return listaView;
    }


}
