package com.etshomework.personlist.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.etshomework.personlist.Model.Person;
import java.util.ArrayList;
import java.util.List;

public class SQLitePersonDal implements IPersonDal {

    private  static  final String ID = "id";
    private  static  final String NAME = "name";
    private  static  final String SURNAME = "surname";
    private  static  final String EMAIL = "email";
    private  static  final String PHONE_NUMBER = "phoneNumber";
    private  static  final String NOTES = "notes";
    private  static  final String BIRTHDAY = "birthday";
    Context context;
    SQLiteDatabase database;

    public SQLitePersonDal(Context context) {
        this.context = context;
        this.database = context.openOrCreateDatabase("dbPerson",Context.MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS person(id INTEGER PRIMARY KEY, name VARCHAR, surname VARCHAR, phoneNumber VARCHAR, birthday VARCHAR, email VARCHAR, notes VARCHAR)");
    }

    @Override
    public List<Person> getPersons() {
        try {
            List<Person> personList = new ArrayList<>();
            Cursor cursor = database.rawQuery("SELECT * FROM person ORDER BY name", null);
            int nameIx = cursor.getColumnIndex(NAME);
            int surnameIx = cursor.getColumnIndex(SURNAME);
            int phoneNumberIx = cursor.getColumnIndex(PHONE_NUMBER);
            int idIx = cursor.getColumnIndex(ID);
            int emailIx = cursor.getColumnIndex(EMAIL);
            int notesIx = cursor.getColumnIndex(NOTES);
            int birthdayIx = cursor.getColumnIndex(BIRTHDAY);
            while (cursor.moveToNext()) {
                Person person = new Person();
                person.setId(cursor.getInt(idIx));
                person.setName(cursor.getString(nameIx));
                person.setSurname(cursor.getString(surnameIx));
                person.setEmail(cursor.getString(emailIx));
                person.setPhoneNumber(cursor.getString(phoneNumberIx));
                person.setNotes(cursor.getString(notesIx));
                person.setBirthday(cursor.getString(birthdayIx));
                personList.add(person);
            }
            return personList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Person getPersonById(int id) {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM person where id=" + id, null);
            Person person = new Person();
            person.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            person.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            person.setSurname(cursor.getString(cursor.getColumnIndex(SURNAME)));
            person.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            person.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            person.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
            person.setBirthday(cursor.getString(cursor.getColumnIndex(BIRTHDAY)));
            return person;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean addPerson(Person person) {
        try {
            String sqlString = "INSERT INTO person(name,surname,phoneNumber,birthday,email,notes) VALUES (?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,person.getName());
            sqLiteStatement.bindString(2,person.getSurname());
            sqLiteStatement.bindString(3,person.getPhoneNumber());
            sqLiteStatement.bindString(4,person.getBirthday());
            sqLiteStatement.bindString(5,person.getEmail());
            sqLiteStatement.bindString(6,person.getNotes());
            sqLiteStatement.executeInsert();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updatePerson(Person person) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID,person.getId());
            contentValues.put(NAME,person.getName());
            contentValues.put(SURNAME,person.getSurname());
            contentValues.put(PHONE_NUMBER,person.getPhoneNumber());
            contentValues.put(EMAIL,person.getEmail());
            contentValues.put(NOTES,person.getNotes());
            contentValues.put(BIRTHDAY,person.getBirthday());
            database.update("person",contentValues,"id=?",new String[]{String.valueOf(person.getId())});
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePerson(Person person) {
        try {
            database.execSQL("DELETE FROM person where id=" + person.getId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
