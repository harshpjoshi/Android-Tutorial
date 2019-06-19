
package com.example.developer.audioplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    MediaPlayer mediaController;
    String[] items;
    int currentpos;
    Uri uri;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView)findViewById(R.id.list);

        final Button play=(Button)findViewById(R.id.play);
        final Button pause=(Button)findViewById(R.id.pause);
        final Button stop=(Button)findViewById(R.id.stop);
        final Button next=(Button)findViewById(R.id.next);
        final Button prev=(Button)findViewById(R.id.prev);
        final ArrayList<File> mysong=findSongs(Environment.getExternalStorageDirectory().getAbsolutePath());

        items=new String[mysong.size()];

        for(int i=0;i<mysong.size();i++)
        {
            items[i]=mysong.get(i).getName().toString();
        }

       CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),items);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                uri=Uri.parse(String.valueOf(mysong.get(position).toURI()));
                if(mediaController!=null)
                {
                    mediaController.stop();
                    mediaController.release();
                    mediaController=null;
                }
                mediaController=MediaPlayer.create(getApplicationContext(),uri);
                mediaController.start();

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaController==null)
                        {
                            mediaController.start();
                        }
                        else if (!mediaController.isPlaying())
                        {
                            mediaController.seekTo(currentpos);
                            mediaController.start();
                        }
                        else
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mediaController.selectTrack(0);
                            }
                        }
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaController!=null)
                        {
                            mediaController.pause();
                            currentpos=mediaController.getCurrentPosition();
                        }
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaController!=null)
                        {
                            mediaController.stop();
                            mediaController=null;
                        }
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try
                        {
                            count++;
                            uri=Uri.parse(String.valueOf(mysong.get(position+count).toURI()));

                        if(mediaController!=null)
                        {
                            mediaController.stop();
                            mediaController.release();
                            mediaController=null;
                        }
                        mediaController=MediaPlayer.create(getApplicationContext(),uri);
                        mediaController.start();
                        }catch (Exception e)
                        {
                            count=0;
                        }
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try
                        {
                            count--;
                            uri=Uri.parse(String.valueOf(mysong.get(position+count).toURI()));

                            if(mediaController!=null)
                            {
                                mediaController.stop();
                                mediaController.release();
                                mediaController=null;
                            }
                            mediaController=MediaPlayer.create(getApplicationContext(),uri);
                            mediaController.start();
                        }catch (Exception e)
                        {
                            mediaController.release();
                        }
                    }
                });

            }
        });
    }

    private ArrayList<File> findSongs(String absolutePath) {

        ArrayList<File> arrayList=new ArrayList<>();

        File file=new File(absolutePath);
        File files[]=file.listFiles();

        for(File singlefile:files)
        {
            if(singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findSongs(singlefile.getAbsolutePath()));
            }
            else
            {
                if(singlefile.getName().endsWith(".mp3"))
                {
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

}
