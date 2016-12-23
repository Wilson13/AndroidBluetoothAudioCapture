package ow.wilson.bluetoothaudiomanager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private String fileName;
    private boolean isRecording;

    private Button recordButton;
    private VideoView mVideoView;

    private AudioManager mAudioManager;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.start_button:
                if ( isRecording ) {
                    // Stop recording if audio is recording.
                    isRecording = false;
                    recordButton.setText("Start Recording");
                    stopRecording();
                } else {
                    isRecording = true;
                    recordButton.setText("Stop Recording");
                    startRecording();
                }
            break;
        }
    }

    private void init() {

        isRecording = false;
        recordButton = (Button) findViewById(R.id.start_button);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.3gp";

        // Request use of Bluetooth SCO headset for communications.
        mAudioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        recordButton.setOnClickListener(this);
    }

    private void startRecording() {

        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.startBluetoothSco();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        mediaRecorder.start();
        //Log.i(TAG, "isBluetoothScoOn(): " + mAudioManager.isBluetoothScoOn());
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        // Stop audio I/O operation
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.stopBluetoothSco();

        //Log.i(TAG, "2 isBluetoothScoOn(): " + mAudioManager.isBluetoothScoOn());
    }
}
