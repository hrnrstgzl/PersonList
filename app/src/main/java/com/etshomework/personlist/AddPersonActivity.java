package com.etshomework.personlist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.etshomework.personlist.DataAccess.IPersonDal;
import com.etshomework.personlist.DataAccess.SQLitePersonDal;
import com.etshomework.personlist.Model.Person;
import com.example.recyclerdemo.R;

import java.util.Calendar;

public class AddPersonActivity extends AppCompatActivity {
    Button btnAddOrUpdatePerson;
    Spinner spnCountryCode;
    EditText txtName, txtSurname, txtPhoneNumber, txtBirthday, txtEmail, txtNotes;
    TextView lblAddTitle, lblValidateName, lblValidateSurname, lblValidateEmail, lblValidatePhone, lblValidateBirthday;
    LinearLayout lytAddName, lytAddSurname, lytAddPhone, lytAddEmail, lytAddBirthday;
    IPersonDal personDal;
    NestedScrollView scrl_add_person;
    Person person = null;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        Intent intent = getIntent();

        personDal = new SQLitePersonDal(this);
        btnAddOrUpdatePerson = findViewById(R.id.btn_add_or_update_person);

        scrl_add_person = findViewById(R.id.scrl_add_person);

        txtNotes = findViewById(R.id.txt_add_note);
        txtEmail = findViewById(R.id.txt_add_email);
        txtName = findViewById(R.id.txt_add_name);
        txtSurname = findViewById(R.id.txt_add_surname);
        txtPhoneNumber = findViewById(R.id.txt_add_phone_number);
        txtBirthday = findViewById(R.id.txt_add_birthday);

        spnCountryCode = findViewById(R.id.spn_add_country_code);

        lblAddTitle = findViewById(R.id.lbl_add_title);
        lblValidateName = findViewById(R.id.lbl_validate_name);
        lblValidateSurname = findViewById(R.id.lbl_validate_surname);
        lblValidateEmail = findViewById(R.id.lbl_validate_email);
        lblValidatePhone = findViewById(R.id.lbl_validate_phone);
        lblValidateBirthday = findViewById(R.id.lbl_validate_birthday);

        lytAddName = findViewById(R.id.lyt_add_name);
        lytAddSurname = findViewById(R.id.lyt_add_surname);
        lytAddPhone = findViewById(R.id.lyt_add_phone);
        lytAddEmail = findViewById(R.id.lyt_add_email);
        lytAddBirthday = findViewById(R.id.lyt_add_birthday);

        type = intent.getStringExtra("type");
        if (type.equals("update")) {
            person = (Person) intent.getSerializableExtra("person");
            lblAddTitle.setText("Güncelle");
            btnAddOrUpdatePerson.setText("Güncelle");
            txtName.setText(person.getName());
            txtSurname.setText(person.getSurname());
            txtBirthday.setText(person.getBirthday());
            txtPhoneNumber.setText(person.getPhoneNumber().substring(4));
            txtNotes.setText(person.getNotes());
            txtEmail.setText(person.getEmail());
        }

        initValidation();

        txtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });


        btnAddOrUpdatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lblValidateName.getVisibility() == View.GONE
                        && lblValidateSurname.getVisibility() == View.GONE
                        && lblValidateEmail.getVisibility() == View.GONE
                        && lblValidatePhone.getVisibility() == View.GONE
                        && !txtName.getText().toString().trim().equals("")
                        && !txtSurname.getText().toString().trim().equals("")
                        && !txtBirthday.getText().toString().trim().equals("")
                        && !txtEmail.getText().toString().trim().equals("")
                        && !txtPhoneNumber.getText().toString().trim().equals("")) {
                    if (type.equals("update")) {
                        updateUser();
                    } else {
                        addUser();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Lütfen Verilerinizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
                    scrl_add_person.scrollTo(0, 0);
                }

            }
        });
    }


    private void initValidation() {
        //Ad Alanı Validasyon
        txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && (txtName.getText().toString().length() <= 2 || txtName.getText().toString().length() > 20)) {
                    lblValidateName.setVisibility(View.VISIBLE);
                    lytAddName.setBackgroundResource(R.drawable.border_error);
                } else {
                    lblValidateName.setVisibility(View.GONE);
                    lytAddName.setBackgroundResource(R.drawable.border_edittext);
                }
            }
        });
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtName.getText().toString().length() <= 2 || txtName.getText().toString().length() > 20) {
                    //lblValidateName.setVisibility(View.VISIBLE);
                    //lytAddName.setBackgroundResource(R.drawable.border_error);
                } else {
                    lblValidateName.setVisibility(View.GONE);
                    lytAddName.setBackgroundResource(R.drawable.border_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Soyad Alanı Validasyon
        txtSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && (txtSurname.getText().toString().length() <= 2 || txtSurname.getText().toString().length() > 20)) {
                    lblValidateSurname.setVisibility(View.VISIBLE);
                    lytAddSurname.setBackgroundResource(R.drawable.border_error);
                } else {
                    lblValidateSurname.setVisibility(View.GONE);
                    lytAddSurname.setBackgroundResource(R.drawable.border_edittext);
                }
            }
        });
        txtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtSurname.getText().toString().length() <= 2 || txtSurname.getText().toString().length() > 20) {
                    //lblValidateSurname.setVisibility(View.VISIBLE);
                    //lytAddSurname.setBackgroundResource(R.drawable.border_error);
                } else {
                    lblValidateSurname.setVisibility(View.GONE);
                    lytAddSurname.setBackgroundResource(R.drawable.border_edittext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Email Alanı Validasyon
        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lblValidateEmail.setVisibility(View.GONE);
                    lytAddEmail.setBackgroundResource(R.drawable.border_edittext);
                } else if (txtEmail.getText().toString().contains("@")
                        && (txtEmail.getText().toString().contains(".com")
                        || txtEmail.getText().toString().contains(".net")
                        || txtEmail.getText().toString().contains(".co")
                        || txtEmail.getText().toString().contains(".edu"))
                        && txtEmail.getText().toString().length() <= 60) {
                    lblValidateEmail.setVisibility(View.GONE);
                    lytAddEmail.setBackgroundResource(R.drawable.border_edittext);
                } else {
                    lblValidateEmail.setVisibility(View.VISIBLE);
                    lytAddEmail.setBackgroundResource(R.drawable.border_error);

                }
            }
        });
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtEmail.getText().toString().contains("@")
                        && (txtEmail.getText().toString().contains(".com")
                        || txtEmail.getText().toString().contains(".net")
                        || txtEmail.getText().toString().contains(".edu")
                        || txtEmail.getText().toString().contains(".org"))
                        && txtEmail.getText().toString().length() <= 60) {
                    lblValidateEmail.setVisibility(View.GONE);
                    lytAddEmail.setBackgroundResource(R.drawable.border_edittext);
                } else {
                    //lblValidateEmail.setVisibility(View.VISIBLE);
                    //lytAddEmail.setBackgroundResource(R.drawable.border_error);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Telefon Validasyon Alanı

        txtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (spnCountryCode.getSelectedItem().equals("+90")) {
                    if (!hasFocus && txtPhoneNumber.getText().toString().length() != 13) {
                        lblValidatePhone.setVisibility(View.VISIBLE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_error);
                    } else {
                        lblValidatePhone.setVisibility(View.GONE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_edittext);
                    }
                }else {
                    if(!hasFocus && txtPhoneNumber.getText().toString().length()>20){
                        lblValidatePhone.setVisibility(View.VISIBLE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_error);
                    }else{
                        lblValidatePhone.setVisibility(View.GONE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_edittext);
                    }
                }
            }
        });


        txtPhoneNumber.addTextChangedListener(new TextWatcher() {
            int precount = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spnCountryCode.getSelectedItem().equals("+90")){
                    if (precount < txtPhoneNumber.getText().toString().length() && (txtPhoneNumber.getText().toString().length() == 3
                            || txtPhoneNumber.getText().toString().length() == 7
                            || txtPhoneNumber.getText().toString().length() == 10)) {
                        txtPhoneNumber.setText(txtPhoneNumber.getText().toString() + " ");
                        txtPhoneNumber.setSelection(txtPhoneNumber.getText().toString().length());
                    }

                    if (txtPhoneNumber.getText().toString().length() < 13) {
                        //lblValidatePhone.setVisibility(View.VISIBLE);
                        //lytAddPhone.setBackgroundResource(R.drawable.border_error);
                    } else if (txtPhoneNumber.getText().toString().length() > 13) {
                        txtPhoneNumber.setText(txtPhoneNumber.getText().toString().substring(0, 13));
                        txtPhoneNumber.setSelection(txtPhoneNumber.getText().toString().length());
                        lblValidatePhone.setVisibility(View.GONE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_edittext);
                    } else {
                        lblValidatePhone.setVisibility(View.GONE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_edittext);
                    }
                    precount = txtPhoneNumber.getText().toString().length();
                }else{
                    if(txtPhoneNumber.getText().toString().length()>20){
                        lblValidatePhone.setVisibility(View.VISIBLE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_error);
                    }else{
                        lblValidatePhone.setVisibility(View.GONE);
                        lytAddPhone.setBackgroundResource(R.drawable.border_edittext);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Doğum Tarihi Validasyonu

        txtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(v);

                } else {
                    if (txtBirthday.getText().toString().equals("") || txtBirthday.getText().toString().isEmpty()) {
                        lblValidateBirthday.setVisibility(View.VISIBLE);
                        lytAddBirthday.setBackgroundResource(R.drawable.border_error);
                    }
                }
            }
        });


        //Note Alanı Validasyon

        txtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtNotes.getText().toString().length()>100){
                    txtNotes.setText(txtNotes.getText().toString().substring(0,100));
                    txtNotes.setSelection(txtNotes.getText().toString().length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*-----------------------------------------------------------------------------------*/


    }


    boolean addUser() {
        person = new Person();
        person.setName(txtName.getText().toString());
        person.setSurname(txtSurname.getText().toString());
        person.setEmail(txtEmail.getText().toString());
        person.setNotes(txtNotes.getText().toString());
        person.setBirthday(txtBirthday.getText().toString());
        person.setPhoneNumber(spnCountryCode.getSelectedItem().toString() + " " + txtPhoneNumber.getText().toString());
        personDal.addPerson(person);
        showSuccessDialog();
        clearPage();
        return true;

    }

    boolean updateUser() {
        person.setName(txtName.getText().toString());
        person.setSurname(txtSurname.getText().toString());
        person.setEmail(txtEmail.getText().toString());
        person.setNotes(txtNotes.getText().toString());
        person.setBirthday(txtBirthday.getText().toString());
        person.setPhoneNumber(spnCountryCode.getSelectedItem().toString() + " " + txtPhoneNumber.getText().toString());
        personDal.updatePerson(person);
        showSuccessDialog();
        clearPage();
        return true;
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        View view = LayoutInflater.from(this).inflate(R.layout.success_dialog, findViewById(R.id.lyt_save_ok));
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.lbl_save_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.success_background);
        }
        alertDialog.show();
    }

    void clearPage() {
        txtName.setText("");
        txtSurname.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtBirthday.setText("");
        txtNotes.setText("");
        spnCountryCode.setSelection(0);
    }

    void showDatePicker(View v) {
        final Calendar clndr = Calendar.getInstance();
        int day = clndr.get(Calendar.DAY_OF_MONTH);
        int month = clndr.get(Calendar.MONTH);
        int year = clndr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                lblValidateBirthday.setVisibility(View.GONE);
                lytAddBirthday.setBackgroundResource(R.drawable.border_edittext);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}