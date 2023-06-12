package org.example;

import org.example.pages.LoginPages.AuthPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AuthPage().setVisible(true);
            }
        });
        // Сразу получаем готовый SessionFactory и сразу создаем готовую сессию

//        session.getTransaction().begin();
//
//        User user = new User();
//        user.setEmail("app4d-official@yandex.ru");
//        user.setPassport("4004300002");
//        user.setFirstName("Vlad");
//        user.setLastName("Mitin");
//        user.setUPassword("1234567");
//        user.setRegistrationDate(OffsetDateTime.now(ZoneOffset.UTC));
//
//        session.save(user);
//
//        session.getTransaction().commit(); //save transaction

    }
}