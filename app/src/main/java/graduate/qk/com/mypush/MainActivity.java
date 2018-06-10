package graduate.qk.com.mypush;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;

public class MainActivity extends Activity {
private ImageView img;
    private boolean imgTag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView) findViewById(R.id.speaker);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
       // 请勿在“=”与appid之间添加任何空字符或者转义符SpeechUtility
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59f56d27");
        img.setVisibility(View.GONE);
    }
    public void startListen(View view){
          /* RecognizerDialog dialog=new RecognizerDialog(this,null);
        //这是科大讯飞开发文档中一种写法，dialog弹窗方式，这里会报java.lang.reflect.InvocationTargetException以及.findViewWithTag(java.lang.Object)' on a null object reference错误
        dialog.setParameter(SpeechConstant.DOMAIN, "iat");
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin ");

        dialog.setListener(mRecognizerDialogListener);
        dialog.show();*/

        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
         final StringBuffer finallyStringBufer=new StringBuffer();
        //听写监听器
        RecognizerListener mRecoListener = new RecognizerListener(){
            //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
            //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
            //关于解析Json的代码可参见MscDemo中JsonParser类；
            //isLast等于true时会话结束。
            public void onResult(RecognizerResult results, boolean isLast) {
                String result=results.getResultString();
                if (result.isEmpty()){
                    Toast.makeText(MainActivity.this,"没有听清你说了什么",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"我说了："+result,Toast.LENGTH_SHORT).show();
                }
                Log.e("101:","状态"+result);
                Log.e("121:","状态"+isLast);
                String resultString=ResultData(result);
                finallyStringBufer.append(resultString);
                if (isLast){
                    String finallyString=finallyStringBufer.toString();

                    ChatBean chatBean=new ChatBean(finallyString,true,-1);//这里是把说的话专成字符串后添加到一个bean集合中去，方便list布局
                    //这里的步骤是把chatBean加入到数据集合中去

                }
            }
            //会话发生错误回调接口
            public void onError(SpeechError error) {
                error.getPlainDescription(true);
                //获取错误码描述
            }
            //开始录音

            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
            }
            public void onBeginOfSpeech() {}
            //音量值0~30
            public void onVolumeChanged(int volume){}
            //结束录音
            public void onEndOfSpeech() {}
            //扩展用接口
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
        };
        //3.开始听写
        mIat.startListening(mRecoListener);
    }
    public void speech(View view){
        SpeechSynthesizer speechSynthesizer=SpeechSynthesizer.createSynthesizer(MainActivity.this,null);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        speechSynthesizer.setParameter(SpeechConstant.SPEED,"50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME,"80");
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);

        speechSynthesizer.startSpeaking("妈的智障，快出来啊",null);
    }

    public String ResultData(String results){
        StringBuffer sb=new StringBuffer();
        Gson gson =new Gson();
       VoiceBean bean= gson.fromJson(results,VoiceBean.class);
       ArrayList<VoiceBean.Salls> words= bean.alls;
        for (VoiceBean.Salls sallsbean:words) {
            String word=sallsbean.mallsMsg.get(0).wd;
            sb.append(word);
        }
        return sb.toString();
    }
}
