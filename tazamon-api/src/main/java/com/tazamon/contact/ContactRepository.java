package com.tazamon.contact;

import com.tazamon.user.User;

import java.util.List;

public interface ContactRepository {

    List<Contact> findAllContacts(User user);
}
