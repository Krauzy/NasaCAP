package marin.kraut.caio.API;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AcessaWSTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... url){
        String dados = AcessaWS.consumir(url[0]);
        return dados;
    }
}
