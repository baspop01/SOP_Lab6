package com.example.lab6.repository;

import com.example.lab6.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository wizardRepository;

    public List<Wizard> wizard() {
        return wizardRepository.findAll();
    }

    public Wizard addWizard(Wizard wizard){
        return wizardRepository.insert(wizard);
    }

    public Wizard updateWizard(Wizard wizard){
        return wizardRepository.save(wizard);
    }
    public Wizard wizardName(String name){
        return wizardRepository.findByName(name);
    }
    public String deleteWizard(Wizard wizard){
        wizardRepository.delete(wizard);
        return "delete Success";
    }
}
