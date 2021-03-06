package mobiledev.unb.ca.trydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBHelper mDBHelper;
    private Button mAddButton;
    private EditText mSearchEditText;
    private EditText mItemEditText;
    private EditText mNumberEditText;
    private TextView mResultsTextView;

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);

        mAddButton = (Button) findViewById(R.id.add_button);
        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        mItemEditText = (EditText) findViewById(R.id.item_edit_text);
        mNumberEditText = (EditText) findViewById(R.id.number_edit_text);
        mResultsTextView = (TextView) findViewById(R.id.results_text_view);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Check if some text has been entered in both the item and number
                // EditTexts. If so, create and execute an AddTask, passing its
                // doInBackground method the text from these EditTetxs. If not,
                // display a toast indicating that the data entered was incomplete.
                if (mItemEditText.getText().toString().isEmpty() || mNumberEditText.getText().toString().isEmpty()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid input";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    new AddTask().execute(mItemEditText.getText().toString(), mNumberEditText.getText().toString());
                }



            }
        });

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    // TODO v is the search EditText. (EditText is a subclass of TextView.)
                    // Get the text from this view. Create and execute a QueryTask passing
                    // its doInBackground method this text.
                    String query = mSearchEditText.getText().toString();
                    new QueryTask().execute(query);


                }
                return false;
            }
        });
    }


    private class AddTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            // TODO Get the item and number that were passed to this method
            // as params. Add a corresponding row to the the database.
            String item = params[0];
            String number = params[1];
            SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.ITEM, item);
            values.put(DBHelper.NUM, number);
            sqLiteDatabase.insert(
                    DBHelper.TABLE_NAME,
                    null,
                    values);

            return null;
        }

        protected void onPostExecute(Void result) {

            // TODO You will need to write a bit of extra code to get the
            // UI to behave nicely, e.g., showing and hiding the keyboard
            // at the right time, clearing text fields appropriately. Some
            // of that code will likely go here, but you might also make
            // changes elsewhere in the app. Exactly how you make the
            // UI behave is up to you, but you should make reasonable
            // choices.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mItemEditText.setText("");
            mNumberEditText.setText("");


        }
    }


    private class QueryTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // TODO Get the query String from params. Query the database to
            // retrieve all rows that have an item that matches this query.
            // Iterate the Cursor returned from querying the database,
            // and return a String representing the results of this query.
            // HINT: See the link in README.md for documentation on Cursor,
            // in particular the methods moveToFirst(), isAfterLast(), and
            // moveToNext().
            String query = params[0];
            SQLiteDatabase sqLiteDatabase = mDBHelper.getReadableDatabase();
            Cursor c = sqLiteDatabase.rawQuery("select * from mytable where item like ? order by item,number", new String[]{"%" + query + "%"});
            String result = "";
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                result += c.getString(c.getColumnIndex(DBHelper.ITEM)) + ": " + c.getString(c.getColumnIndex(DBHelper.NUM)) + "\n";
                c.moveToNext();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Set the results TextView to display result, as
            // shown in the sample screenshot in README.md. If result is
            // empty, display a message indicating so. Again, you might
            // need to write a bit of extra code here, or elsewhere, to get
            // the UI to behave nicely.

            mResultsTextView.setText(result);
            mSearchEditText.setText("");
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        }
    }


}
