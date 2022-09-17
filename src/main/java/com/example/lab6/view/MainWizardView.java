package com.example.lab6.view;


import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Route(value = "mainPage.it")
public class MainWizardView extends FormLayout {

    public int number = -1;
    public String oldname = "";

    public MainWizardView(){
        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout();

        TextField fullname = new TextField();
        fullname.setPlaceholder("Fullname");

        RadioButtonGroup<String> gender = new RadioButtonGroup<>();
        gender.setLabel("Gender : ");
        gender.setItems("Male", "Female");

        ComboBox<String> position = new ComboBox<>();
        position.setItems("student", "teacher");
        position.setPlaceholder("Position");

        NumberField dollar = new NumberField("Dollars");
        dollar.setPrefixComponent(new Span("$"));

        ComboBox<String> school = new ComboBox<>();
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        school.setPlaceholder("School");

        ComboBox<String> house = new ComboBox<>();
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");
        house.setPlaceholder("House");

        Button left = new Button("<<");
        Button right = new Button(">>");
        Button create = new Button("Create");
        Button update = new Button("Update");
        Button delete = new Button("Delete");

        hl.add(left, create, update, delete, right);
        vl.add(fullname, gender, position, dollar, school, house, hl);
        add(vl);

        left.addClickListener(buttonClickEvent -> {
            List<Wizard> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            this.number--;
            if (this.number < 0){
                this.number = out.size()-1;
            }
            Wizard wizard = mapper.convertValue(out.get(this.number), Wizard.class);
            fullname.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            }else{
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            dollar.setValue((double) wizard.getMoney());
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
            this.oldname = wizard.getName();
        });
        right.addClickListener(buttonClickEvent -> {
            List<Wizard> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            if (this.number >= out.size()-1){
                this.number = -1;
            }
            this.number++;
            Wizard wizard = mapper.convertValue(out.get(this.number), Wizard.class);
            fullname.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            }else{
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            dollar.setValue((double) wizard.getMoney());
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
            this.oldname = wizard.getName();
        });

        create.addClickListener(buttonClickEvent -> {
            String name = fullname.getValue();
            String sex = "";
            if (gender.getValue().equals("Male")){
                sex = "m";
            }else{
                sex = "f";
            }
            String posi = position.getValue();
            int money = dollar.getValue().intValue();
            String sch = school.getValue();
            String hou = house.getValue();

            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(new Wizard(null, sex, name, sch, hou, money, posi)), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            new Notification("Wizard has been created", 5000).open();
            this.oldname = fullname.getValue();
        });

        update.addClickListener(buttonClickEvent -> {
            MultiValueMap<String, String> formdata = new LinkedMultiValueMap<>();
            formdata.add("oldname", this.oldname);
            formdata.add("newname", fullname.getValue());
            String sex = "";
            if (gender.getValue().equals("Male")){
                sex = "m";
            }else{
                sex = "f";
            }
            formdata.add("sex", sex);
            formdata.add("position", position.getValue());
            formdata.add("school", school.getValue());
            formdata.add("money", String.valueOf(dollar.getValue().intValue()));
            formdata.add("house", house.getValue());
           String out = WebClient.create()
                   .post()
                   .uri("http://localhost:8080/updateWizard")
                   .body(BodyInserters.fromFormData(formdata))
                   .retrieve()
                   .bodyToMono(String.class)
                   .block();
            new Notification("Wizard has been updated", 5000).open();
        });

        delete.addClickListener(buttonClickEvent -> {
            MultiValueMap<String, String> formdata = new LinkedMultiValueMap<>();
            formdata.add("name", fullname.getValue());
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(BodyInserters.fromFormData(formdata))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            new Notification("Wizard has benn removed", 5000).open();
        });
    }
}
