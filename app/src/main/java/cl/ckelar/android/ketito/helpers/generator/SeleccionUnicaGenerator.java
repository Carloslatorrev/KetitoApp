package cl.ckelar.android.ketito.helpers.generator;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class SeleccionUnicaGenerator {

     private Context contextT;
     private RadioGroup radioGroup;
     private RadioButton radioButton;
     private TextView title;
     private TextView txtDescription;

     private KetitoDatabase ketitoDatabase;
     private Preguntas pregunta;
     private PreguntasAlternativas preguntasAlternativas;
     private List<PreguntasAlternativas> preguntasAlternativasList;
     private Respuesta respuesta;
     private int idPreg;
     private String idRespuesta;

    /**
     * Inicializa los elementos y componentes de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     */
    public void init(Context context, ViewGroup root){
        contextT = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_encuesta_seleccionunica, root, true);
        title = view.findViewById(R.id.seleccionUnicaTitle);
        txtDescription = view.findViewById(R.id.seleccionUnicaDescription);
        radioGroup = view.findViewById(R.id.seleccionUnicaRadioGroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        ketitoDatabase = ketitoDatabase.getInstance(context);
    }

    /**
     * Realiza el set de las variables y elementos de la vista asociada a la pregunta
     * @param idPregunta Id del objeto pregunta
     */
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
        preguntasAlternativasList = ketitoDatabase.preguntasAlternativasDao().getListPreguntasAlternativasByIdPregunta(idPregunta);
        for (PreguntasAlternativas pregAlt: preguntasAlternativasList) {
            int id= pregAlt.getIdAlternativa();
            radioButton = new RadioButton(contextT);
            radioButton.setButtonDrawable(R.drawable.radiobutton_seleccionunica);
            radioButton.setText(pregAlt.getAlternativa());
            int paddingDp = 15;
            float density = contextT.getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            radioButton.setPadding(0,0,paddingDp,0);
            radioButton.setId(id);
            radioButton.setTextColor(ContextCompat.getColor(contextT, R.color.baseKetito));
            radioButton.setTextSize(24);
            radioButton.setTypeface(null, Typeface.BOLD);
            RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton, rprms);
        }

    }

    /**
     * Obtiene la respuesta de los elementos de la vista en relación a la selección del usuario.
     * @param idToma Id de la Toma correspondiente a la encuesta
     * @return Retorna el objeto respuesta de la clase Respuesta, con los parametros de la selección del usuario
     */
    public Respuesta getRespuesta(int idToma){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = radioGroup.findViewById(id);
        int idx = radioGroup.indexOfChild(radioButton);
        String textRespuesta = "";
        if(radioButton != null){
            if(radioButton.getText().toString() != null){
                textRespuesta = "NULL";
            }else{
                textRespuesta = "";
            }
        }else{
            textRespuesta = "";
        }
        respuesta = new Respuesta();
        int random = (int) (Math.random() * 1000) + 1;
        idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
        respuesta.setIdRespuesta(idRespuesta);
        respuesta.setIdPregunta(idPreg);
        respuesta.setIdAlternativa(String.valueOf(id));
        respuesta.setIdToma(idToma);
        respuesta.setRespuesta(null);
        respuesta.setNivel("null");
        return respuesta;
    }

    /**
     * Crea el listado de elementos de la vista
     * @return listado o list View de elementos de la vista
     */
    public List<View> getListView(){
        List<View> listaView = new ArrayList<>();
        listaView.add(radioGroup);
        listaView.add(title);
        listaView.add(txtDescription);
        return listaView;
    }

    /** Retorna el Id de la respuesta
     * @return IdRespuesta
     */
    public String GetIdRespuesta(){
        return idRespuesta;
    }

}
