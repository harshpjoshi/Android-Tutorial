package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewAddMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_add_main);

        Intent intent=getIntent();
        String image=intent.getStringExtra("advertise_image");

        ImageView imageView=(ImageView)findViewById(R.id.advertise_full_image);

        Picasso.get().load(image).into(imageView);

    }
}
