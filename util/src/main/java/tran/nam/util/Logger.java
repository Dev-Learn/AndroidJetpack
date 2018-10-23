package tran.nam.util;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings("unused")
public final class Logger {

    private static final String TAG = "Nam Tran";
    private Logger() {
    }

    private static StackTraceElement getMethodName() {
        StackTraceElement[] tracks = Thread.currentThread().getStackTrace();
        int STACK_TRACE_LEVELS_UP = 4;
        if (tracks != null && tracks.length > STACK_TRACE_LEVELS_UP) {
            return tracks[STACK_TRACE_LEVELS_UP];
        }
        return null;
    }

    private static void e(String message, Object... objects) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(message, objects) + " /" + getMethodName());
        }

    }

    public static void w(String message, Object... objects) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, String.format(message, objects) + " /" + getMethodName());
        }
    }

    public static void error(String message, Object... objects) {
        Log.e(TAG, String.format(message, objects) + " /" + getMethodName());
    }

    public static void dump(Intent intent) {
        if (intent == null) {
            e("Intent is null");
            return;
        }

        e("Intent: action: %s", intent.getAction());

        if (intent.getPackage() != null) {
            e("  pkg: %s", intent.getPackage());
        }

        if (intent.getType() != null) {
            e("  type: %s", intent.getType());
        }

        if (intent.getComponent() != null) {
            e("  comp: %s", intent.getComponent().flattenToString());
        }

        if (intent.getDataString() != null) {
            e("  data: %s", intent.getDataString());
        }

        if (intent.getCategories() != null) {
            for (String cat : intent.getCategories()) {
                e("  cat: %s", cat);
            }
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                e("  extra: %s->%s", key, value);
            }
        }

    }

    public static void dump(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.moveToFirst();

            do {
                int cc = cursor.getColumnCount();
                Logger.e(" cursor: %s/%s", cc, cursor.toString());
                for (int i = 0; i < cc; i++) {
                    Logger.e("  %s: %s", cursor.getColumnName(i),
                            cursor.getString(i));
                }
            } while (cursor.moveToNext());

            cursor.moveToFirst();
        }

    }

    public static void enter(Object... args) {
        if (BuildConfig.DEBUG) {
            try {
                StringBuilder b = new StringBuilder();
                for (Object arg : args) {
                    b.append(arg).append(", ");
                }
                Log.d(TAG, String.format("Enter %s (%s)", getMethodName(), b.toString()));
            } catch (Throwable ignored) {
            }
        }
    }

    public static void debug(Object... args) {
        if (BuildConfig.DEBUG) {
            try {
                StringBuilder b = new StringBuilder();
                for (Object arg : args) {
                    b.append(arg).append(", ");
                }
                Log.d(TAG, String.format("Debug %s (%s)", getMethodName(), b.toString()));
            } catch (Throwable ignored) {
            }
        }
    }

    public static void exit(Object... args) {
        if (BuildConfig.DEBUG) {
            try {
                StringBuilder b = new StringBuilder();
                for (Object arg : args) {
                    b.append(arg).append(", ");
                }
                Log.d(TAG, String.format("Exit %s (%s)", getMethodName(), b.toString()));
            } catch (Throwable ignored) {

            }
        }
    }
}
