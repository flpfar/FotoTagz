package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CardView cvFototag = (CardView) findViewById(R.id.cv_fototag);
        final CardView cvGaleria = (CardView) findViewById(R.id.cv_galeria);
        final CardView cvCamera = (CardView) findViewById(R.id.cv_camera);

        cvFototag.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                
            }
        });

        cvGaleria.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        cvCamera.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }
}
