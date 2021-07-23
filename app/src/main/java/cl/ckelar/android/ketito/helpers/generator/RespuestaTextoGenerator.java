package cl.ckelar.android.ketito.helpers.generator;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class RespuestaTextoGenerator {

    private Context context;
    private TextView title;
    private TextView txtDescription;
    private EditText editTextShort;
    private EditText editTextLarge;
    private ImageButton btnSiguiente;

    private KetitoDatabase ketitoDatabase;
    private Preguntas pregunta;
    private UserSesion userSesion;
    private int idPreg;
    private Respuesta respuesta;
    private String idRespuesta;


    /**
     * Inicializa los elementos de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     * @param type tipo de pregunta, larga o corta
     */
    public void init(Context context, ViewGroup root, Boolean type){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_encuesta_respuestatexto, root, true);
        title = view.findViewById(R.id.respuestaTextoTitle);
        txtDescription = view.findViewById(R.id.respuestaTextoDescription);
        editTextShort = view.findViewById(R.id.respuestaTextoEditTextShort);
        editTextLarge = view.findViewById(R.id.respuestaTextoEditTextLarge);
        btnSiguiente = view.findViewById(R.id.btnEncuestaMainSiguiente);

        if(type == false){
            editTextLarge.setVisibility(View.GONE);
        }else{
            editTextShort.setVisibility(View.GONE);
        }
        ketitoDatabase = ketitoDatabase.getInstance(context);
    }

    /**
     * Realiza el set de las variables y elementos de la vista asociada a la pregunta
     * @param idPregunta Id del objeto pregunta
     */
    public void setLayout(int idPregunta, String hint){
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
        editTextLarge.setHint(hint);
        editTextShort.setHint(hint);

        //Si la respuesta es obligatoria, se evalúa si el usuario escribió una respuesta o no.
        //de no ser así se oculta el boton siguiente
        editTextLarge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editTextLarge.getText() == null || editTextLarge.getText().toString().equals("") || editTextLarge.getText().toString() == ""){
                    btnSiguiente.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSiguiente.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextLarge.getText() == null || editTextLarge.getText().toString().equals("") || editTextLarge.getText().toString() == ("")){
                    btnSiguiente.setVisibility(View.GONE);
                }
            }
        });

        editTextShort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editTextShort.getText() == null || editTextShort.getText().toString().equals("") || editTextShort.getText().toString() == ""){
                    btnSiguiente.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSiguiente.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextShort.getText() == null || editTextShort.getText().toString().equals("") || editTextShort.getText().toString() == ""){
                    btnSiguiente.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Obtiene la respuesta de los elementos de la vista en relación a la selección del usuario.
     * @param idToma Id de la Toma correspondiente a la encuesta
     * @return Retorna el objeto respuesta de la clase Respuesta, con los parametros de la selección del usuario
     */
    public Respuesta getRespuesta(int idToma){

        String textRespuesta= "";
        String largeRespuesta= "";
        if(editTextShort.getVisibility() == View.GONE){
            if(editTextLarge.getText().toString() != null && editTextLarge.getText().toString().length() >0){
                largeRespuesta = editTextLarge.getText().toString();
            }else{
                largeRespuesta = "";
            }
        }else if(editTextLarge.getVisibility() == View.GONE){
            if(editTextShort.getText().toString() != null && editTextShort.getText().toString().length() >0){
                textRespuesta = editTextShort.getText().toString();
            }else{
                textRespuesta = "";
            }
        }

        respuesta = new Respuesta();
        int random = (int) (Math.random() * 1000) + 1;
        idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
        respuesta.setIdRespuesta(idRespuesta);
        respuesta.setIdPregunta(idPreg);
        respuesta.setIdAlternativa("null");
        respuesta.setIdToma(idToma);
        respuesta.setNivel("null");
        if(largeRespuesta != null && largeRespuesta != ""){
            respuesta.setRespuesta(largeRespuesta);
        }else if(textRespuesta != null && textRespuesta != ""){
            respuesta.setRespuesta(textRespuesta);
        }else{
            respuesta.setRespuesta("NULL");
        }
        return respuesta;
    }

    /**
     * Crea el listado de elementos de la vista
     * @return listado o list View de elementos de la vista
     */
    public List<View> getListView(){
        List<View> listaView = new ArrayList<>();
        listaView.add(editTextLarge);
        listaView.add(title);
        listaView.add(txtDescription);
        listaView.add(editTextShort);
        return listaView;
    }

    /** Retorna el Id de la respuesta
     * @return IdRespuesta
     */
    public String GetIdRespuesta(){
        return idRespuesta;
    }
}
