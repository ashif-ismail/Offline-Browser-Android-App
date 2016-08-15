package as.hif.offlinebrowser;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public  class fileListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

        private ListView mListView;
        private List<String> fileNameList;
        private FlAdapter mAdapter;
        private File directory;
        private File sdcard;
        public String fileDir;
        String thisWikiKeyword;
        String thisWebKeyword;
        String thisGoogleKeyWord;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_file_list);
            mListView = (ListView) findViewById(R.id.lv);
            sdcard = Environment.getExternalStorageDirectory();
            directory = new File(sdcard.getAbsolutePath() + "/offlinebrowser");
            fileNameList = getFileListfromSDCard();
            mAdapter = new FlAdapter(this, R.layout.fl_list_item, fileNameList);
            Collections.reverse(fileNameList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    fileDir = directory + "/" + fileNameList.get(i).trim();
                    Intent intent;
                    intent = new Intent(fileListActivity.this, openSavedPagesActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("filePath", fileDir);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }

    private List<String> getFileListfromSDCard() {
            String state = Environment.getExternalStorageState();
            List<String> flLst = new ArrayList<String>();
            if (Environment.MEDIA_MOUNTED.equals(state) && directory.isDirectory()) {
                File[] fileArr = directory.listFiles();
                int length = fileArr.length;
                for (int i = 0; i < length; i++) {
                    File f = fileArr[i];
                    flLst.add(f.getName());
                }
            }

            return flLst;
        }

 @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
    }


    public class FlAdapter extends ArrayAdapter<String> {

        private List<String> fLst;
        private Context adapContext;

        public FlAdapter(Context context, int textViewResourceId,
                         List<String> fLst) {
            super(context, textViewResourceId, fLst);
            this.fLst = fLst;
            adapContext = context;
        }
        @Override
          public View getView(int position, View convertView, ViewGroup parent) {
            thisGoogleKeyWord = GoogleActivity.keyWord;
            thisWebKeyword = MainActivity.keyWord;
            thisWikiKeyword = WikiActivity.keyWord;
            View view = convertView;
            FHolder fHolder = null;
            if (convertView == null) {
                    view = View.inflate(adapContext, R.layout.fl_list_item, null);
                    fHolder = new FHolder();
                    fHolder.fNameView = (TextView) view.findViewById(R.id.filename);
                    view.setTag(fHolder);
                } else {
                    fHolder = (FHolder) view.getTag();
                }
                String fileName = fLst.get(position);
                fHolder.fNameView.setText(fileName);
                return view;
            }
    }

        static class FHolder {
            public TextView fNameView;

        }

    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,MainActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
}

