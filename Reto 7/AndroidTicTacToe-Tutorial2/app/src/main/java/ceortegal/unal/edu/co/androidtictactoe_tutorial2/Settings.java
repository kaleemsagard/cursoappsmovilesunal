package ceortegal.unal.edu.co.androidtictactoe_tutorial2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        setSummaryMessageToPreference("difficulty_level", getResources().getString(R.string.difficulty_expert));
        setSummaryMessageToPreference("victory_message", getResources().getString(R.string.estado_humano_gana));
    }


    // PRIVATE METHODS ////

    private void setSummaryMessageToPreference(final String preferenceKey, String defaultMessage) {
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final Preference preference = findPreference(preferenceKey);
        String message = prefs.getString(preferenceKey, defaultMessage);

        preference.setSummary(message);
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((CharSequence) newValue);

                // Since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString(preferenceKey, newValue.toString());
                ed.commit();
                return true;
            }
        });
    }
}
