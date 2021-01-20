package com.etshomework.personlist.DataAccess;

import com.etshomework.personlist.Model.Person;

import java.util.List;

public interface IPersonDal {
    public List<Person> getPersons();
    public Person getPersonById(int id);
    public boolean addPerson(Person person);
    public boolean updatePerson(Person person);
    public boolean deletePerson(Person Person);
}
