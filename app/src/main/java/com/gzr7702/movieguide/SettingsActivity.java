
package com.gzr7702.movieguide;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    private String mSortOrder;
    public static final String KEY_PREF_SORT_ORDER = "sort";
    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_order_key)));
        this.findPreference("sort").setOnPreferenceChangeListener(this);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        mSortOrder = value.toString();

        // Handle list preferences and others
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(mSortOrder);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(mSortOrder);
        }

        String message = "in onPreferenceChange(), sort order is " + mSortOrder;
        Log.v(LOG_TAG, message);

        return true;
    }

}
