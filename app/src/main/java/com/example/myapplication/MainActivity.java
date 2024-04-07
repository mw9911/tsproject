package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    Intent intent;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    private TextView tvState,tvIpState;
    private EditText etip1,etip2,etip3,etip4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvState=findViewById(R.id.tvState);
        tvIpState=findViewById(R.id.tvIp_State);
        etip1=findViewById(R.id.et_ip1);
        etip2=findViewById(R.id.et_ip2);
        etip3=findViewById(R.id.et_ip3);
        etip4=findViewById(R.id.et_ip4);
        Context context=this;
        etip1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=3) {
                    etip2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEtRange(etip2,context);
            }
        });
        etip2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=3) {
                    etip3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEtRange(etip3,context);
            }
        });
        etip3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=3) {
                    etip4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEtRange(etip4,context);
            }
        });
        etip4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=3) {
                    String res="";
                    res=etip1.getText().toString()+"."+etip2.getText().toString()+"."+etip3.getText().toString()+"."+etip4.getText().toString();
                    tvIpState.setText(res);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEtRange(etip4,context);
            }
        });
        // 안드로이드 6.0버전 이상인지 체크해서 퍼미션 체크
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        // RecognizerIntent 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName()); // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); // 언어 설정

        // 버튼 클릭 시 객체에 Context와 listener를 할당
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this); // 새 SpeechRecognizer 를 만드는 팩토리 메서드
                mRecognizer.setRecognitionListener(listener); // 리스너 설정
                mRecognizer.startListening(intent); // 듣기 시작
            }
        });
    }
    public void checkEtRange(EditText et,Context context){
        String etText=et.getText().toString();
        if (etText.isEmpty()) {
            if(Integer.parseInt(etText)>255){
                et.setText(etText.substring(0,et.length()-1));
                et.setSelection(et.length());
                Toast.makeText(context, "0~255까지의 숫자를 입력해주세요.", Toast.LENGTH_LONG).show();
            }
        }
        return;


    }
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // 말하기 시작할 준비가되면 호출
            Toast.makeText(getApplicationContext(),"음성인식 시작",Toast.LENGTH_SHORT).show();
            tvState.setText("이제 말씀하세요!");
        }

        @Override
        public void onBeginningOfSpeech() {
            // 말하기 시작했을 때 호출
            tvState.setText("잘 듣고 있어요.");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 입력받는 소리의 크기를 알려줌
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 말을 시작하고 인식이 된 단어를 buffer에 담음
        }

        @Override
        public void onEndOfSpeech() {
            // 말하기를 중지하면 호출
            tvState.setText("끝!");
        }

        @Override
        public void onError(int error) {
            // 네트워크 또는 인식 오류가 발생했을 때 호출
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER 가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러 발생 : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 인식 결과가 준비되면 호출
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String res="";
            for(int i = 0; i < matches.size() ; i++){
                res=matches.get(i);
            }
            textView.setText(res);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/Download/files") ;
            if(!file.exists()){ // 폴더 없을 경우
                file.mkdir(); // 폴더 생성
            }
            try{
                BufferedWriter buf=new BufferedWriter(new FileWriter(file+"/files.txt",true));
                buf.append(res);
                buf.newLine();
                buf.close();
            }catch (Exception e) {
                e.printStackTrace() ;
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 부분 인식 결과를 사용할 수 있을 때 호출
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // 향후 이벤트를 추가하기 위해 예약
        }
    };
}