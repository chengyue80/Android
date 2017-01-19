package com.yue.demo.photoupload;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yue.demo.R;

public class Upload extends Activity implements OnClickListener {

    private static String requestURL = "http://192.168.1.212:8011/pd/upload/fileUpload.do";
    private Button selectImage, uploadImage;
    private ImageView imageView;

    private String picPath = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoupload);

        selectImage = (Button) this.findViewById(R.id.selectImage);
        uploadImage = (Button) this.findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);

        imageView = (ImageView) this.findViewById(R.id.imageView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.selectImage:
            /***
             * ����ǵ���android���õ�intent��������ͼƬ�ļ� ��ͬʱҲ���Թ��������
             */
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
            break;
        case R.id.uploadImage:
            if (picPath == null) {

                Toast.makeText(Upload.this, "��ѡ��ͼƬ��", 1000).show();
            } else {
                final File file = new File(picPath);

                if (file != null) {
                    int request = UploadUtil.uploadFile(file, requestURL);
                    uploadImage.setText(request);
                }
            }
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��
             */
            Uri uri = data.getData();
            Log.e("TAG", "uri = " + uri);
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * ���������һ���ж���Ҫ��Ϊ�˵�������ѡ�񣬱��磺ʹ�õ�����ļ��������Ļ��
                     * ���ѡ����ļ��Ͳ�һ����ͼƬ�ˣ� ����Ļ��������ж��ļ��ĺ�׺��
                     * �����ͼƬ��ʽ�Ļ�����ô�ſ���
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("��ʾ")
                .setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
                .setPositiveButton("ȷ��",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                picPath = null;
                            }
                        }).create();
        dialog.show();
    }

}
