package com.tazamon.contact;

import com.tazamon.common.User;

import java.util.List;

public interface ContactRepository {

    List<Contact> findAllContacts(User user);
}
