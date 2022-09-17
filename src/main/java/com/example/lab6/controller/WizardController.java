package com.example.lab6.controller;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;


    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public List<Wizard> getWizard(){
        List<Wizard> wizard = wizardService.wizard();
        return wizard;
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> createWizard(@RequestBody Wizard wizard){
        Wizard n = wizardService.addWizard(wizard);
        return ResponseEntity.ok(n);
    }
    @RequestMapping(value ="/updateWizard", method = RequestMethod.POST)
    public boolean updateWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard wizard = wizardService.wizardName(d.get("oldname"));
        int num = Integer.parseInt(d.get("money"));
        System.out.println(num);
        if(wizard != null) {
            wizardService.updateWizard(new Wizard(wizard.get_id(), d.get("sex"), d.get("newname"), d.get("school")
            , d.get("house"), num, d.get("position")));
            return true;
        }else {
            return false;
        }
    }
    @RequestMapping(value ="/deleteWizard", method = RequestMethod.POST)
    public boolean deleteWizard(@RequestBody MultiValueMap<String, String> n) {
        Map<String, String> d = n.toSingleValueMap();
        Wizard wizard = wizardService.wizardName(d.get("name"));
        if(wizard != null) {
            wizardService.deleteWizard(wizard);
            return true;
        }else {
            return false;
        }
    }
}
