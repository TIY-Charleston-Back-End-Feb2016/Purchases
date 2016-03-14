package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zach on 3/10/16.
 */
@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;

    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init() throws FileNotFoundException {
        if (customers.count() == 0) {
            Scanner customerScanner = new Scanner(new File("customers.csv"));
            customerScanner.nextLine();
            while (customerScanner.hasNext()) {
                String line = customerScanner.nextLine();
                String[] columns = line.split(",");
                String name = columns[0];
                String email = columns[1];
                Customer c = new Customer(name, email);
                customers.save(c);
            }
        }

        if (purchases.count() == 0) {
            Scanner purchaseScanner = new Scanner(new File("purchases.csv"));
            purchaseScanner.nextLine();
            while (purchaseScanner.hasNext()) {
                String line = purchaseScanner.nextLine();
                String[] columns = line.split(",");
                String date = columns[1];
                String creditCard = columns[2];
                String cvv = columns[3];
                String category = columns[4];
                Customer customer = customers.findOne(Integer.valueOf(columns[0]));
                Purchase p = new Purchase(date, creditCard, cvv, category, customer);
                purchases.save(p);
            }
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, String category, Integer page) {
        page = (page == null) ? 0 : page;
        PageRequest pr = new PageRequest(page, 10);
        Page<Purchase> p;
        if (category != null) {
            p = purchases.findByCategory(pr, category);
        }
        else {
            p = purchases.findAll(pr);
        }
        model.addAttribute("purchases", p);
        model.addAttribute("nextPage", page+1);
        model.addAttribute("showNext", p.hasNext());
        model.addAttribute("category", category);

        return "home";
    }
}
