package com.example.hwhong.searchsuggestions;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener{

    private EditText searchBox;
    private Button button;
    private TextView suggestions;

    private SpellCheckerSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = (EditText) findViewById(R.id.searchEditText);
        button = (Button) findViewById(R.id.searchButton);
        suggestions = (TextView) findViewById(R.id.suggestions);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //param 1:  text metadata for a spell checker
                //param 2: number of suggestions
                session.getSuggestions(new TextInfo(searchBox.getText().toString()), 5);
            }
        });
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] suggestionsInfos) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < suggestionsInfos.length; ++i) {

            //get number of suggestions
            final int numSuggests = suggestionsInfos[i].getSuggestionsCount();
            builder.append('\n');

            //retrieve the individual suggestions
            for (int j = 0; j < numSuggests; ++j) {
                builder.append("," + suggestionsInfos[i].getSuggestionAt(j));
            }

        }
        runOnUiThread(new Runnable() {
            public void run() {
                //let the suggestions appear on the UI, via the textview object
                suggestions.append("Did you mean:" + builder.toString() + " ?");
            }
        });

    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfos) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        final TextServicesManager manager = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        session = manager.newSpellCheckerSession(null, null, this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(session != null) {
            session.close();
        }
    }
}
