package com.testdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 向SD卡写入一个XML文件
     *
     * @param v
     */
    public void savexml(View v) {

        try {
            File file = new File(Environment.getExternalStorageDirectory()+"/aaaa/",
                    "persons.xml");
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "persons");
            for (int i = 0; i < 10; i++) {
                serializer.startTag(null, "person");
                serializer.attribute(null, "id", String.valueOf(i));
                // 写姓名
                serializer.startTag(null, "name");
                serializer.text("张三" + i);
                serializer.endTag(null, "name");
                // 写性别
                serializer.startTag(null, "gender");
                serializer.text("男" + i);
                serializer.endTag(null, "gender");
                // 写年龄
                serializer.startTag(null, "age");
                serializer.text("1" + i);
                serializer.endTag(null, "age");

                serializer.endTag(null, "person");
            }
            serializer.endTag(null, "persons");
            serializer.endDocument();
            fos.close();
            Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 读取SD卡中的XML文件,使用pull解析
     *
     * @param v
     */
    public void readxml(View v) {

        try {
            File path = new File(Environment.getExternalStorageDirectory()+"/aaaa/",
                    "persons.xml");
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型

            String id = null;
            String name = null;
            String gender = null;
            String age = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("persons".equals(tagName)) { // <persons>
                        } else if ("person".equals(tagName)) { // <person id="1">
                            id = parser.getAttributeValue(null, "id");
                        } else if ("name".equals(tagName)) { // <name>
                            name = parser.nextText();
                        } else if ("gender".equals(tagName)) { // <age>
                            gender = parser.nextText();
                        } else if ("age".equals(tagName)) { // <age>
                            age = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if ("person".equals(tagName)) {
                            Log.i(TAG, "id---" + id);
                            Log.i(TAG, "name---" + name);
                            Log.i(TAG, "gender---" + gender);
                            Log.i(TAG, "age---" + age);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
