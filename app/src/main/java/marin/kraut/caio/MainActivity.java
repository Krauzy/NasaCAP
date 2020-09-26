package marin.kraut.caio;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import marin.kraut.caio.API.AcessaWSTask;
import marin.kraut.caio.API.ImageCatch;

public class MainActivity extends AppCompatActivity {
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtDesc;
    private ImageView Nasa;
    private Button btnAnt;
    private Button btnProx;
    private Calendar date;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTitle = findViewById(R.id.txtTitle);
        txtDate = findViewById(R.id.txtDate);
        txtDesc = findViewById(R.id.txtDescr);
        Nasa = findViewById(R.id.imageNASA);
        btnAnt = findViewById(R.id.btnAnterior);
        btnProx = findViewById(R.id.btnProx);
        scroll = findViewById(R.id.scrollview);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = Calendar.getInstance();
        runAPI();
        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.add(Calendar.DATE, 1);
                runAPIWithValues();
                scroll.scrollTo(0, 0);
            }
        });
        btnAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.add(Calendar.DATE, -1);
                runAPIWithValues();
                scroll.scrollTo(0, 0);
            }
        });
    }

    private void runAPI() {
        AcessaWSTask task = new AcessaWSTask();
        String http = "https://api.nasa.gov/planetary/apod?api_key=GsnuiW4FRF0NPzBr1asuYdyXj2TkE91Z6nTWEsth";
        try {
            String JSON = task.execute(http).get();
            JSONObject obj = new JSONObject(JSON);
            txtTitle.setText(obj.getString("title"));
            String dt = obj.getString("date");
            int year = Integer.parseInt(dt.substring(0, 4));
            int mounth = Integer.parseInt(dt.substring(6, 7));
            int day = Integer.parseInt(dt.substring(8, 10));
            date.set(year, mounth, day);
            txtDate.setText(date.get(Calendar.YEAR)+ "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
            txtDesc.setText(obj.getString("explanation"));
            ImageCatch catcher = new ImageCatch();
            Bitmap img  = catcher.execute(obj.getString("url")).get();
            Nasa.setImageBitmap(img);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            txtDesc.setText(e.getMessage());
        }
    }

    private boolean runAPIWithValues() {
        AcessaWSTask task = new AcessaWSTask();
        String http = "https://api.nasa.gov/planetary/apod?api_key=GsnuiW4FRF0NPzBr1asuYdyXj2TkE91Z6nTWEsth&date=" + date.get(Calendar.YEAR)+ "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH);
        try {
            String JSON = task.execute(http).get();
            JSONObject obj = new JSONObject(JSON);
            txtTitle.setText(obj.getString("title"));
            txtDate.setText(date.get(Calendar.YEAR)+ "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
            txtDesc.setText(obj.getString("explanation"));
            ImageCatch catcher = new ImageCatch();
            Bitmap img  = catcher.execute(obj.getString("url")).get();
            Nasa.setImageBitmap(img);
            return true;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            txtTitle.setText(":)");
            txtDate.setText(date.get(Calendar.YEAR)+ "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
            txtDesc.setText("Imagem ainda não existe: O app não viaja no tempo :)");
            return false;
        }
    }
}