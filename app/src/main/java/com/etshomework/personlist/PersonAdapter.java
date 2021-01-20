package com.etshomework.personlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.etshomework.personlist.DataAccess.IPersonDal;
import com.etshomework.personlist.DataAccess.SQLitePersonDal;
import com.etshomework.personlist.Model.Person;
import com.example.recyclerdemo.R;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    ArrayList<Person> list;
    LayoutInflater layoutInflater;
    IPersonDal personDal;
    public PersonAdapter(Context context, ArrayList<Person> personList){
        layoutInflater = LayoutInflater.from(context);
        this.list = personList;
        personDal = new SQLitePersonDal(context);
    }


    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_card,parent,false);
        PersonViewHolder holder = new PersonViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person currentPerson = list.get(position);
        holder.setData(currentPerson,position);
    }

    @Override
    public int getItemCount() {
        if(list!=null) {
            return list.size();
        }else{
            return 0;
        }
    }



    class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtBirthday, txtPhone, txtEmail, txtDescription;
        CardView cvPersonRoot;
        Person currentPerson;


        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtBirthday = itemView.findViewById(R.id.txtBirthday);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            txtDescription = itemView.findViewById(R.id.txt_comment);
            cvPersonRoot = itemView.findViewById(R.id.cv_person_root);
            cvPersonRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),AddPersonActivity.class);
                    intent.putExtra("type","update");
                    intent.putExtra("person",currentPerson);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setData(Person currentPerson, int position) {
            this.currentPerson = currentPerson;
            this.txtName.setText(currentPerson.getName() + " " + currentPerson.getSurname());
            this.txtPhone.setText(currentPerson.getPhoneNumber());
            this.txtBirthday.setText(currentPerson.getBirthday());
            this.txtDescription.setText(currentPerson.getNotes());
            this.txtEmail.setText(currentPerson.getEmail());

            if(currentPerson.getNotes().trim().equals("")|| currentPerson.getNotes().isEmpty()){
                txtDescription.setVisibility(View.GONE);
            }
        }
    }

    public void removeItem(int position) {
        Person person = list.get(position);
        if(personDal.deletePerson(person)){
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        }
    }

}
