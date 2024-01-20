package com.example.partner;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        mButtonSpeak=findViewById(R.id.speak_button);
        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result=mTTS.setLanguage(Locale.GERMAN);
                    if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(SpeechActivity.this, "語言沒支援", Toast.LENGTH_SHORT).show();
                    }else{
                        mButtonSpeak.setEnabled(true);
                    }
                }else{
                    Toast.makeText(SpeechActivity.this, "初始化失敗", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mEditText=findViewById(R.id.wantspeek_text);
        mSeekBarPitch=findViewById(R.id.seek_bar_pitch);
        mSeekBarPitch=findViewById(R.id.seek_bar_speed);
        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    private void speak() {
        String text=mEditText.getText().toString();
        float pitch =(float) mSeekBarPitch.getProgress()/50;
        if(pitch<0.1){
            pitch=0.1f;
        }
        float speed =(float) mSeekBarPitch.getProgress()/50;
        if(speed<0.1){
            speed=0.1f;
        }
        mTTS.setSpeechRate(speed);
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}