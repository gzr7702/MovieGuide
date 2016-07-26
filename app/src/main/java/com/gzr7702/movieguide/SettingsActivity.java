
package com.gzr7702.movieguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

// TODO: CHANGE TO SETTINGS FRAGMENT ==========================================================
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    private String mSortOrder;
    public static final String KEY_PREF_SORT_ORDER = "sort";
    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_order_key)));
        this.findPreference("sort").setOnPreferenceChangeListener(this);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        /*
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
                        */
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        mSortOrder = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(mSortOrder);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(mSortOrder);
        }

        String message = "in onPreferenceChange(), sort order is " + mSortOrder;
        Log.v(LOG_TAG, message);

        return true;
    }

}
