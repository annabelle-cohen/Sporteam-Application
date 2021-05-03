package com.example.sporteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.sporteam.Style.LoadingDialog;

public class MainActivity extends AppCompatActivity {
private VideoView videoBG;
MediaPlayer mMediaPlayer;
int mCurrentVideoPosition;
Button btnSignIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hook up the VideoView to out UI.
        videoBG = (VideoView)findViewById(R.id.videoView);

        //Buttons
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);

        //Dialog
        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

        //Build video uri
        Uri uri = Uri.parse(getString(R.string.resources)+getPackageName()+"/"+R.raw.game);

        //Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        videoBG.requestFocus();

        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                 mMediaPlayer=mp;
                 mMediaPlayer.setLooping(true);

                 if(mCurrentVideoPosition != 0 ){
                     mMediaPlayer.seekTo(mCurrentVideoPosition);
                     mMediaPlayer.start();
                 }
            }
        });

      //Sometimes the app send weird message like "can't play this video and it still playing so here we fix it
        videoBG.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(getString(R.string.video), getString(R.string.setOnErrorListener));
                return true;
            }
        });

        //Start the VideoView
        videoBG.start();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingMessageDialog(loadingDialog);
                Intent signIn = new Intent(MainActivity.this,SignIn_Activity.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingMessageDialog(loadingDialog);
                Intent signUp = new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        //Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer=null;
    }
    private void LoadingMessageDialog(final LoadingDialog loadingDialog){
        loadingDialog.startLoadingDialog();
        Handler handler= new Handler();
        //If the user press back then the loading message dialog disappear
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },3000);
    }
}
