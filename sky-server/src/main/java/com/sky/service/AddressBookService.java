package com.sky.service;

import com.sky.entity.AddressBook;
import java.util.List;

public interface AddressBookService {

    List<AddressBook> list();

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);
    AddressBook getDefault();

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

}
