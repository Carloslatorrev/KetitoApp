package cl.ckelar.android.ketito.helpers.generator;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class SatisfaccionNumeroGenerator {
    private Context contextT;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton;
    private TextView txtMinimo;
    private TextView txtMaximo;
    private TextView txtDescription;
    private TextView title;

    private KetitoDatabase ketitoDatabase;
    private Preguntas pregunta;
    private PreguntasAlternativas preguntasAlternativas;
    private List<PreguntasAlternativas> preguntasAlternativasList;
    private Respuesta respuesta;
    private int idPreg;
    private View view;
    private ViewGroup auxRoot;
    private String idRespuesta;

    /**
     * Inicializa los elementos de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     */
    public void init(Context context, ViewGroup root){
        contextT = context;
        auxRoot = root;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.partial_encuesta_satisfaccionnumero, root, true);
        title = view.findViewById(R.id.satisfaccionNumeroTitle);
        txtDescription = view.findViewById(R.id.satisfaccionNumeroDescription);
        radioGroup = view.findViewById(R.id.satisfaccionNumeroRadioGroup);
        radioButton1 = view.findViewById(R.id.satisfaccionNumeroRadio1);
        radioButton2 = view.findViewById(R.id.satisfaccionNumeroRadio2);
        radioButton3 = view.findViewById(R.id.satisfaccionNumeroRadio3);
        radioButton4 = view.findViewById(R.id.satisfaccionNumeroRadio4);
        radioButton5 = view.findViewById(R.id.satisfaccionNumeroRadio5);
        txtMinimo = view.findViewById(R.id.txtSatisfaccionNumeroMinimo);
        txtMaximo = view.findViewById(R.id.txtSatisfaccionNumeroMaximo);
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
        String etiquetaMaximo = pregunta.getEtiquetaMaximo();
        String maxText= txtMaximo.getText().toString();
        if(etiquetaMaximo != null && etiquetaMaximo != ""){
            maxText = maxText.replace("%fin", pregunta.getEtiquetaMaximo());
            txtMaximo.setText(maxText);
        }else{
            maxText = "";
            txtMaximo.setText(maxText);
        }

        String etiquetaMinimo = pregunta.getEtiquetaMinimo();
        String minimoText = txtMinimo.getText().toString();
        if(etiquetaMinimo != null && etiquetaMinimo != ""){
            minimoText = minimoText.replace("%init", pregunta.getEtiquetaMinimo());
            txtMinimo.setText(minimoText);
        }else{
            minimoText = "";
            txtMinimo.setText(minimoText);
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
        int idx = radioGroup.indexOfChild(radioButton) + 1;
        int random = (int) (Math.random() * 1000) + 1;
        idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
        respuesta = new Respuesta();
        respuesta.setIdRespuesta(idRespuesta);
        respuesta.setIdAlternativa("null");
        respuesta.setIdPregunta(idPreg);
        respuesta.setIdToma(idToma);
        if(radioButton != null){
            String textRespuesta = null;
            respuesta.setRespuesta(textRespuesta);
            respuesta.setNivel(String.valueOf(idx));
        }else{
            respuesta.setRespuesta(null);
        }
        return respuesta;
    }

    public void RemoveView() {
        auxRoot.removeViewInLayout(radioGroup);
        auxRoot.removeViewInLayout(title);
        auxRoot.removeViewInLayout(txtDescription);
        auxRoot.removeViewInLayout(txtMinimo);
        auxRoot.removeViewInLayout(txtMaximo);
        LayoutInflater inflater = (LayoutInflater) contextT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_encuesta_presentation, auxRoot, true);
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
        listaView.add(txtMaximo);
        listaView.add(txtMinimo);
        return listaView;
    }

    /**
     * @return El objeto View en relación a la vista actual
     */
    public View getView(){
        return view;
    }

    /** Retorna el Id de la respuesta
     * @return IdRespuesta
     */
    public String GetIdRespuesta(){
        return idRespuesta;
    }

}
