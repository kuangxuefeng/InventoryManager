package com.kxf.inventorymanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.utils.FileUtils;
import com.kxf.inventorymanager.utils.LogUtil;
import com.kxf.inventorymanager.view.CustomView;

import java.util.ArrayList;
import java.util.List;

import exocr.engine.EngineManager;
import exocr.engine.ViewEvent;
import exocr.exocrengine.EXIDCardResult;
import exocr.idcard.IDCardManager;

public class IdScanActivity extends BaseMenuActivity implements BaseMenuActivity.OnItemClickListener, ViewEvent {
    private ArrayList<String> itemTitles;

    private EditText name_edt_id,sex_edt_id,nation_edt_id,birth_edt_id,address_edt_id,num_edt_id,sign_edt_id,data_edt_id;
    private ImageView head_img_id,font_img_id,back_img_id;
    private boolean font = true;
    private EXIDCardResult result;
    private Button btn_ok;
    private TextView isCompleteFront;
    private TextView isCompleteBack;
    private View customView;
    private CheckBox cb_flash_id;
    private CustomView viewFrame;
    private Button back;
    private Button btn_photo_id;

    private boolean isFront = false;
    private String name = "";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_id_scan);
//    }

    @Override
    protected List<BaseItem> getBaseItem() {
        menuType = MENU_TYPE.SINGLE_LINE;
        itemTitles = new ArrayList<>();
        itemTitles.add("身份证正面识别");
        itemTitles.add("身份证反面识别");
        List<BaseItem> bis = new ArrayList<>();
        for (String title : itemTitles){
            BaseItem bi = new BaseItem().setId(itemTitles.indexOf(title)).setTitle(title).setListener(this);
            bis.add(bi);
        }
        return bis;
    }

    @Override
    protected String getMenuTitle() {
        return null;
    }

    @Override
    protected void afterInitMenu() {
        EngineManager.getInstance().initEngine(this);

        customView = getLayoutInflater().inflate(R.layout.customview,null);
        back = ((Button) customView.findViewById(R.id.btn_back_id));
        cb_flash_id = ((CheckBox) customView.findViewById(R.id.cb_flash_id));
        viewFrame = ((CustomView) customView.findViewById(R.id.customFrame));
        btn_photo_id = ((Button) customView.findViewById(R.id.btn_photo_id));
        btn_photo_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IDCardManager.getInstance().openPhoto();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IDCardManager.getInstance().stopRecognize();
            }
        });
        cb_flash_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                IDCardManager.getInstance().setFlash(b);
            }
        });
    }

    @Override
    public void onClick(View v, BaseItem bi) {
        switch (bi.getId()){
            case 0:
                isFront = true;
                startRecognize();
                break;
            case 1:
                isFront = false;
                startRecognize();
                break;
            case 2:
                break;
        }
    }

    public void startRecognize(){
        LogUtil.i(getPackageName());
        IDCardManager.getInstance().setView(customView);
        IDCardManager.getInstance().setScanMode(IDCardManager.ID_IMAGEMODE_MEDIUM,20);
        IDCardManager.getInstance().setPackageName(getPackageName());
        IDCardManager.getInstance().recognizeWithSide(this,this,isFront);
    }

    @Override
    public void onCardDetected(Parcelable parcelable) {
        LogUtil.i("");
        IDCardManager.getInstance().pauseRecognizeWithStopStream(true);
        result = ((EXIDCardResult) parcelable);
        AlertDialog.Builder builder = new AlertDialog.Builder(IDCardManager.getInstance().getActivity());
        builder.setTitle("超时\n");
        builder.setMessage("识别成功，点击退出 " + result.name);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IDCardManager.getInstance().stopRecognize();
            }
        });
        builder.create().show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(result.name)){
                   name = result.name;
                }
                String fn = name;
                if (isFront){
                    fn = fn + "_正面_";
                }else {
                    fn = fn + "_反面_";
                }
                FileUtils.saveBitmap(result.stdCardIm, fn + "stdCardIm.png");
                FileUtils.saveBitmap(result.previewImg, fn + "previewImg.png");
            }
        }).start();
    }

    @Override
    public void onCameraDenied() {
        LogUtil.i("");
    }

    @Override
    public Rect getRectByOrientation(int i) {
        LogUtil.i("" + i);
        return null;
    }

    @Override
    public void invalideView(int i) {
        LogUtil.i("");
    }

    @Override
    public void onTimeOut() {
        LogUtil.i("");
    }

    @Override
    public void refreshScanViewByRecoContnueWithSide(boolean b) {
        LogUtil.i("");
    }

    @Override
    public void onBack() {
        LogUtil.i("");
    }

    @Override
    public void onLightChanged(float v) {
        LogUtil.i("");
    }

    @Override
    public void onPauseRecognize() {
        LogUtil.i("");
    }

    @Override
    public void onRecoErrorWithWrongSide() {
        LogUtil.i("");
    }
}
