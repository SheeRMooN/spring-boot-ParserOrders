package com.example.springboot03.service;

import com.example.springboot03.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope("prototype")
public class ParserFiles {

    @Autowired
    private Order order;
    private String regex;
    private String fileName;
    

    public void readFileInStdout(String fileName){
        this.fileName = fileName;
        regex = getRegex(fileName);
        List<String> strings = readFile(fileName) ;
        List<Order> orders = serchOrder(strings);
        validatorOrders(orders);
        stdout(orders);
    }

    @Async
    private void stdout(List<Order> orders) {
        Iterator iterator = orders.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }

    @Async
    private List<Order> serchOrder(List<String> strings) {
        List<Order> orderList = new LinkedList<>();

        int count = 1;
        String result = "OK";
        Iterator iterator = strings.iterator();
        while (iterator.hasNext()){
            String str = (String) iterator.next();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                Order order = getOrder();
                order.setId(String.valueOf(matcher.group(1)));
                order.setAmount(String.valueOf(matcher.group(2)));
                order.setCurrency(String.valueOf(matcher.group(3)));
                order.setComment(String.valueOf(matcher.group(4)));
                order.setFilename(fileName);
                order.setLine(count);
                order.setResult(result);
                orderList.add(order);
            }
            count++;
        }
        return orderList;
    }
    @Async
    private  void validatorOrders(List<Order> allOrder) {
        String report = "OK";

        Iterator<Order> iterator = allOrder.iterator();
        while (iterator.hasNext()){
            Order order = iterator.next();
            try {
                int id = Integer.valueOf(order.getId());


            }catch (NumberFormatException e){
                report = "Error format id";
            }
            try {
                double amount = Double.valueOf(order.getAmount());

            }catch (NumberFormatException e){
                report = "Error format amount";
            }
            if (!order.getCurrency().equals("USD") & !order.getCurrency().equals("EUR")){
                System.out.println(order.getCurrency());
                report = "Error format currency";
            }
            order.setResult(report);
        }
    }

    private String getRegex(String string) {
        String type = string.substring(string.indexOf('.'), string.length());
        switch (type){
            case ".csv":
                regex = "(\\d+)\\,(\\d+(?:[\\.,]\\d+)?)\\,(\\w+)\\,(\\W+)";
                break;
            case ".json":
                regex = "\\{\"orderId\":(\\d)\\,\\\"amount\":(\\d+(?:[\\.,]\\d+)?)\\,\\\"currency\":\"(\\w+)\\\",\"comment\":\"(\\W+)\\\"}";
                break;
            case ".xlsx":
                regex = "";
                break;
        }

        return regex;
    }

    @Async
    private List<String> readFile(String string) {
        List<String> strings = new LinkedList();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            while (reader.ready()){
                strings.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }
    @Lookup
    public Order getOrder(){
        return null;
    }
}
