package doit.study.droid.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class QuizProvider extends ContentProvider {
    public static final String AUTHORITY = "doit.study.droid";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TAG = "tag";
    public static final Uri TAG_URI = BASE_URI.buildUpon().appendPath(PATH_TAG).build();
    public static final String TAG_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TAG;
    public static final String TAG_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_TAG;

    public static final String PATH_QUESTION = "question";
    public static final Uri QUESTION_URI = BASE_URI.buildUpon().appendPath(PATH_QUESTION).build();
    public static final String QUESTION_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_QUESTION;
    public static final String QUESTION_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_QUESTION;

    public static final String PATH_STATISTICS = "statistics";
    public static final Uri STATISTICS_URI = BASE_URI.buildUpon().appendPath(PATH_STATISTICS).build();
    public static final String STATISTICS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_STATISTICS;
    public static final String STATISTICS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_STATISTICS;


    private QuizDBHelper mQuizDBHelper;

    static final int QUESTION_ITEM = 100;
    static final int QUESTION_DIR = 101;
    static final int RAND_QUESTION_DIR = 150;
    static final int TAG_ITEM = 200;
    static final int TAG_DIR = 201;
    static final int STATISTICS_ITEM = 300;
    static final int STATISTICS_DIR = 301;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AUTHORITY, PATH_QUESTION, QUESTION_DIR);
        sUriMatcher.addURI(AUTHORITY, PATH_QUESTION + "/#", QUESTION_ITEM);
        sUriMatcher.addURI(AUTHORITY, PATH_QUESTION + "/rand/#", RAND_QUESTION_DIR);

        sUriMatcher.addURI(AUTHORITY, PATH_TAG, TAG_DIR);
        sUriMatcher.addURI(AUTHORITY, PATH_TAG + "/#", TAG_ITEM);

        sUriMatcher.addURI(AUTHORITY, PATH_STATISTICS, STATISTICS_DIR);
        sUriMatcher.addURI(AUTHORITY, PATH_STATISTICS + "/#", STATISTICS_ITEM);
    }

    private static final SQLiteQueryBuilder sQuizQueryBuilder;

    static {
        sQuizQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sQuizQueryBuilder.setTables(
                Question.Table.NAME +
                        " INNER JOIN " + RelationTables.QuestionTag.NAME +
                        " ON " + Question.Table.FQ_ID + " = " + RelationTables.QuestionTag.FQ_QUESTION_ID +
                        " INNER JOIN " + Tag.Table.NAME +
                        " ON " + RelationTables.QuestionTag.FQ_TAG_ID + " = " + Tag.Table.FQ_ID
        );
    }

    @Override
    public boolean onCreate() {
        mQuizDBHelper = new QuizDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case (RAND_QUESTION_DIR):
                return QUESTION_TYPE;
            case QUESTION_DIR:
                return QUESTION_TYPE;
            case (TAG_DIR):
                return TAG_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case (RAND_QUESTION_DIR): {
                cursor = getRandSelectedQuestions(uri, projection);
                break;
            }
            case (TAG_DIR): {
                cursor = getTags();
                break;
            }
            case (QUESTION_DIR): {
                cursor = getQuestions(uri, projection);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mQuizDBHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case (TAG_DIR):{
                return db.update(Tag.Table.NAME, values, selection, selectionArgs);
            }
        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }


    private Cursor getRandSelectedQuestions(Uri uri, String[] projection) {
        String limit = uri.getPathSegments().get(2);
        SQLiteDatabase db = mQuizDBHelper.getReadableDatabase();
        String selection = Tag.Table.NAME + "." + Tag.Table.SELECTED + " = 1";
        String sortOrder = "RANDOM()";
        return sQuizQueryBuilder.query(db, projection, selection, null, null, null, sortOrder, limit);
    }


    private Cursor getTags() {
        // authority/tag/
        SQLiteDatabase db = mQuizDBHelper.getReadableDatabase();
        String tagTotalCounter = "COUNT(" + Tag.Table.FQ_TEXT + ") as " + Tag.Table.TOTAL_COUNTER;
        String tagStudiedCounter = "SUM(" + Question.Table.FQ_STATUS + "=" + Tag.Table.QTY_WHEN_STUDIED + ") as " +
                Tag.Table.STUDIED_COUNTER;
        String[] projection = {Tag.Table.FQ_ID, Tag.Table.FQ_TEXT, tagTotalCounter, tagStudiedCounter, Tag.Table.FQ_SELECTION};
        return sQuizQueryBuilder.query(db, projection, null, null, Tag.Table.FQ_TEXT, null, null);
    }

    private Cursor getQuestions(Uri uri, String [] projection){
        SQLiteDatabase db = mQuizDBHelper.getReadableDatabase();
        return db.query(Question.Table.NAME, projection, null, null, null, null, null);
    }
}