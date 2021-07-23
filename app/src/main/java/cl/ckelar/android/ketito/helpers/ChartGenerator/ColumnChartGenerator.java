package cl.ckelar.android.ketito.helpers.ChartGenerator;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;

public class ColumnChartGenerator {

    /**
     * Genera el Chart/Gráfica
     * @param respuestasList Corresponde al Listado de Respuestas
     * @param preguntas Corresponde al objeto de la Clase Pregunta en relación a la selección del usuario
     * @return Retorna el objeto cartesian de la clase AnyChart.column()
     */
        public Cartesian GeneratorColumn(List<Respuesta> respuestasList, Preguntas preguntas){
            Cartesian cartesian = AnyChart.column();
            List<DataEntry> data = new ArrayList<>();

            if(preguntas.getIdTipo() == 6){
                ArrayList<Integer> escala = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    escala.add(i+1);
                }

                int count = 0;
                for (Integer integer: escala) {
                    for (Respuesta respuesta: respuestasList) {
                        if(respuesta.getNivel().equals(integer.toString())){
                            count++;
                        }
                    }
                    data.add(new ValueDataEntry(integer.toString(),count));
                    count = 0;
                }


                cartesian.xAxis(0).title("Nota");
                cartesian.yAxis(0).title("Total Respuestas");

            }else{

                ArrayList<Integer> escala = new ArrayList<Integer>();
                for (int i = 0; i < preguntas.getEscala(); i++) {
                    escala.add(i+1);
                }

                int count = 0;
                for (Integer integer: escala) {
                    for (Respuesta respuesta: respuestasList) {
                        if(respuesta.getRespuesta().equals(integer.toString())){
                            count++;
                        }
                    }
                    data.add(new ValueDataEntry(integer.toString(),count));
                    count = 0;
                }



                cartesian.xAxis(0).title("Escala");
                cartesian.yAxis(0).title("Total Respuestas");

            }

            Column column = cartesian.column(data);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title(preguntas.getPregunta());

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%value}{type:number, decimalsCount:0}");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);



            return  cartesian;
        }

}
