package com.example.schoolmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
    public class DatabaseHelper extends SQLiteOpenHelper {
            private static final String DATABASE_NAME = "SchoolApp.db";
            private static final int DATABASE_VERSION = 2;

            // Table names and columns
        private static final String TABLE_NAME = "allusers";
            private static final String COLUMN_EMAIL = "email";
            private static final String COLUMN_NAME = "name";
            private static final String COLUMN_PASSWORD = "password";
            private static final String COLUMN_ROLE = "role";

            private static final String TABLE_RESULTS = "results";
            private static final String COLUMN_ID = "id";
            private static final String COLUMN_STUDENT_NAME = "student_name";
            private static final String COLUMN_SUBJECT = "subject";
            private static final String COLUMN_MARKS = "marks";
            private static final String COLUMN_GRADE = "grade";
        private static final String COLUMN_CLASS_NAME = "class_name";

            private static final String TABLE_STUDENTS = "students";

            // SQL create table statements
            private static final String CREATE_TABLE_SQL =
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                            COLUMN_NAME + " TEXT, " +
                            COLUMN_PASSWORD + " TEXT, " +
                            COLUMN_ROLE + " TEXT);";

            private static final String CREATE_TABLE_RESULTS =     //farhana
                    "CREATE TABLE " + TABLE_RESULTS + " (" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_STUDENT_NAME + " TEXT, " +
                            COLUMN_CLASS_NAME + " TEXT, " +
                            COLUMN_SUBJECT + " TEXT, " +
                            COLUMN_MARKS + " INTEGER, " +
                            COLUMN_GRADE + " TEXT);";

            private static final String CREATE_TABLE_STUDENTS =       //far
                    "CREATE TABLE " + TABLE_STUDENTS + " (" +
                            COLUMN_STUDENT_NAME + " TEXT PRIMARY KEY);";


            public DatabaseHelper(@Nullable Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    db.execSQL(CREATE_TABLE_SQL);
                    db.execSQL(CREATE_TABLE_RESULTS);

                    // Insert initial data
                    insertInitialData(db);
                } catch (SQLException e) {
                    Log.e("DatabaseHelper", "Error creating tables: " + e.getMessage());
                }
            }

            private void insertInitialData(SQLiteDatabase db) {   //far
                ContentValues studentValues = new ContentValues();
                studentValues.put(COLUMN_STUDENT_NAME, "SAIDATUL ATHIRAH");
                db.insert(TABLE_STUDENTS, null, studentValues);

                ContentValues subjectValues = new ContentValues();
                subjectValues.put(COLUMN_SUBJECT, "MATHEMATICS");
                db.insert(COLUMN_SUBJECT, null, subjectValues);
                subjectValues.put(COLUMN_SUBJECT, "SCIENCE");
                db.insert(COLUMN_SUBJECT, null, subjectValues);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
                    db.execSQL("DROP TABLE IF EXISTS " + COLUMN_SUBJECT);
                    onCreate(db);
                } catch (SQLException e) {
                    Log.e("UnifiedDatabaseHelper", "Error upgrading tables: " + e.getMessage());
                }
            }

            // User operations
            public boolean isEmailRegistered(String email) {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
                boolean exists = cursor.getCount() > 0;
                cursor.close();
                return exists;
            }

            public boolean insertUser(String name, String email, String password, String role) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_EMAIL, email);
                values.put(COLUMN_PASSWORD, hashPassword(password));
                values.put(COLUMN_ROLE, role);

                long result = db.insert(TABLE_NAME, null, values);
                return result != -1;
            }

            public boolean checkLogin(String email, String password) {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                        new String[]{email, hashPassword(password)});
                boolean valid = cursor.getCount() > 0;
                cursor.close();
                return valid;
            }

            private String hashPassword(String password) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(password.getBytes());
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hash) {
                        hexString.append(Integer.toHexString(0xff & b));
                    }
                    return hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    Log.e("UnifiedDatabaseHelper", "Error hashing password: " + e.getMessage());
                    return null;
                }
            }

            // Student result operations
            public boolean insertResult(StudentResult result) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_STUDENT_NAME, result.getStudentName());
                values.put(COLUMN_SUBJECT, result.getSubject());
                values.put(COLUMN_MARKS, result.getMarks());
                values.put(COLUMN_GRADE, result.getGrade());

                long resultId = db.insert(TABLE_RESULTS, null, values);
                db.close();
                return resultId != -1;
            }

            public List<StudentResult> getAllResults() {
                List<StudentResult> results = new ArrayList<>();
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_RESULTS, null, null, null, null, null, null);


                if (cursor.moveToFirst()) {
                    do {
                        StudentResult result = new StudentResult(
                                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME)),
                                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT)),
                                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_NAME)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARKS)),
                                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE))
                        );
                        results.add(result);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return results;
            }

            public void deleteResult(String studentName) {
                SQLiteDatabase db = this.getWritableDatabase();
                db.delete(TABLE_RESULTS, COLUMN_STUDENT_NAME + "=?", new String[]{studentName});
                db.close();
            }

            public void updateResult(StudentResult result) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_SUBJECT, result.getSubject());
                values.put(COLUMN_MARKS, result.getMarks());
                values.put(COLUMN_GRADE, result.getGrade());

                db.update(TABLE_RESULTS, values, COLUMN_STUDENT_NAME + "=?", new String[]{result.getStudentName()});
                db.close();
            }

            // Student and subject operations
            public List<String> getAllStudents() {
                List<String> students = new ArrayList<>();
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_STUDENTS, new String[]{COLUMN_STUDENT_NAME}, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        students.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME)));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return students;
            }

        public void insertOrUpdateResult(StudentResult result) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_STUDENT_NAME, result.getStudentName());
            values.put(COLUMN_CLASS_NAME, result.getClassName());
            values.put(COLUMN_SUBJECT, result.getSubject());
            values.put(COLUMN_MARKS, result.getMarks());
            values.put(COLUMN_GRADE, result.getGrade());

            // Check if record exists
            String whereClause = COLUMN_STUDENT_NAME + " = ? AND " + COLUMN_SUBJECT + " = ?";
            String[] whereArgs = {result.getStudentName(), result.getSubject()};

            int rowsAffected = db.update(TABLE_RESULTS, values, whereClause, whereArgs);

            // If no record updated, insert a new record
            if (rowsAffected == 0) {
                db.insert(TABLE_RESULTS, null, values);
            }

            db.close();
        }


        public List<StudentResult> getResultsByClass(String className) {
            List<StudentResult> results = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.query(TABLE_RESULTS, null, COLUMN_CLASS_NAME + " = ?", new String[]{className}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String studentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
                        String subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
                        int marks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARKS));
                        String grade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE));

                        results.add(new StudentResult(studentName, className, subject, marks, grade));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error retrieving results by class", e);
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }
            return results;
        }

        public List<String> getAllClasses() {
            List<String> classNames = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                String query = "SELECT DISTINCT " + COLUMN_CLASS_NAME + " FROM " + TABLE_RESULTS;
                cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        classNames.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error retrieving classes", e);
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }
            return classNames;
        }
        public List<StudentResult> getResultsByStudentAndClass(String studentName, String className) {
            List<StudentResult> results = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.query(TABLE_RESULTS,
                        null,
                        COLUMN_STUDENT_NAME + " = ? AND " + COLUMN_CLASS_NAME + " = ?",
                        new String[]{studentName, className},
                        null,
                        null,
                        null);

                if (cursor.moveToFirst()) {
                    do {
                        String subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT));
                        int marks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARKS));
                        String grade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE));

                        results.add(new StudentResult(studentName, className, subject, marks, grade));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error retrieving results by student and class", e);
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }

            return results;
        }

    }


