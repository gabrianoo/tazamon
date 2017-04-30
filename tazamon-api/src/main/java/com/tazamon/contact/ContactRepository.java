package com.tazamon.contact;

import com.tazamon.common.User;
import com.tazamon.contact.Contact;

import java.util.List;

public interface ContactRepository {

    List<Contact> findAllContacts(User user);
}
