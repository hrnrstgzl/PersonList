package com.etshomework.personlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etshomework.personlist.DataAccess.IPersonDal;
import com.etshomework.personlist.DataAccess.SQLitePersonDal;
import com.etshomework.personlist.Model.Person;
import com.example.recyclerdemo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton btnAddPerson;
    EditText txtSearchPerson;
    private Paint p = new Paint();
    PersonAdapter personAdapter;
    IPersonDal personDal;
    List<Person> personList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        personDal = new SQLitePersonDal(this);
        recyclerView = findViewById(R.id.rcv_users);
        btnAddPerson = findViewById(R.id.btn_add_person);
        personList = personDal.getPersons();
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddPersonActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });

        txtSearchPerson = findViewById(R.id.txt_search_person);
        txtSearchPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Person> filteredList = new ArrayList<Person>();
                for(Person person : personList){
                    if((person.getName().toLowerCase() + " " + person.getSurname().toLowerCase()).contains(txtSearchPerson.getText().toString().toLowerCase())){
                        filteredList.add(person);
                    }
                }
                personAdapter = new PersonAdapter(context, (ArrayList<Person>) filteredList);
                recyclerView.setAdapter(personAdapter);
            }
        });
        personAdapter = new PersonAdapter(this, (ArrayList<Person>) personList);
        recyclerView.setAdapter(personAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initSwipe();


    }

    @Override
    protected void onResume() {
        super.onResume();
        personAdapter = new PersonAdapter(this, (ArrayList<Person>) personDal.getPersons());
        recyclerView.setAdapter(personAdapter);
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                personAdapter.removeItem(position);
                personAdapter.notifyItemRemoved(position);
                personAdapter.notifyItemRangeChanged(position, personAdapter.list.size());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, p);
                    p.setColor(Color.WHITE);
                    p.setAntiAlias(true);
                    p.setTextSize(40);
                    c.drawText("Sil",background.centerX(), background.centerY(), p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}