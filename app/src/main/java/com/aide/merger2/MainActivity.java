package com.aide.merger2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.aide.merger.AndroidManifestMerger;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String dir = Environment.getExternalStorageDirectory().getPath();
        AndroidManifestMerger.merge(dir + "/ManifestOut.xml", dir + "/Manifest1.xml", new String[0], new String[]{dir + "/Manifest2.xml"});
    }
}
