package com;

import com.google.common.collect.HashMultiset;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
Готовое тестовое задание. Местами немного некрасиво, но работает.
 */

public class Main {
    
    public static void main(String[] args) throws FileNotFoundException {

        //парсинг json
        JsonReader reader = new JsonReader(new FileReader("/com/customers.json"));
        Type CustType = new TypeToken<List<Customer>>(){}.getType();
        Gson gson = new Gson();
        List<Customer> custDataDes = gson.fromJson(reader, CustType);

        //список для расчета среднего баланса
        List<Double> acctList = new ArrayList<Double>();

        //удобная коллекция из google guava, реализующая счетчик
        HashMultiset<Object> company = HashMultiset.create();

        //приведение даты в нужный формат
        String pattern = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate currentDay = LocalDate.now();
        MonthDay curMonthDay = MonthDay.from(currentDay);

        //список др и имен в Map для удобства
        Map<LocalDate, String> birthMap = new HashMap<>();
        String names = " ";

        for (Customer customer : custDataDes) {

            //расчет среднего баланса
            double acctBal = Double.parseDouble(customer.accountBalance);
            acctList.add(acctBal);

            //составление счетчика работников в компаниях
            company.add(customer.companyName);

            //расчет дат рождения
            LocalDate birthday = LocalDate.parse(customer.birthDate, formatter);
            MonthDay monthDayBirth = MonthDay.from(birthday);
            LocalDate cur14 = currentDay.plusDays(14);
            MonthDay mon14 = MonthDay.from(cur14);
            names = customer.firstName + " " + customer.lastName;

            if (monthDayBirth.isAfter(curMonthDay) || curMonthDay.equals(monthDayBirth)) {
                if (monthDayBirth.isBefore(mon14))
                    birthMap.put(birthday, names);
            }
        }

        double sum = 0;
        for (int i = 0; i < acctList.size(); i++) {
            sum += acctList.get(i);
        }
        System.out.println("Ближайшие именинники: " + "\n" + birthMap);
        System.out.println("Средний баланс на счетах клиентов" + "\n" +
                sum/acctList.size());
        System.out.println("Количество сотрудников в компаниях" + "\n" + company);

    }
}

