package com.springboot.rendezvousapp.services.impl;


import com.springboot.rendezvousapp.entities.*;
import com.springboot.rendezvousapp.repository.*;
import com.springboot.rendezvousapp.services.Services;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesImpl implements Services {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MedecinRepo medecinRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private RDVRepo rdvRepo;
    @Autowired
    private CliniqueRepo cliniqueRepo;

    @Override
    public User addUser(User user) {
      return userRepo.save(user);
    }
    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }
    @Override
    public void deleteUser(Integer idUser) {
        userRepo.deleteById(idUser);
    }
    @Override
    public User affichUser(Integer idUser) {
        return userRepo.findById(idUser).orElse(null);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    @Override
    public Medecin addMedecin(Medecin medecin) {
        return medecinRepo.save(medecin);
    }

    @Override
    public Medecin getOneMedecin(Integer ID) {
        return medecinRepo.findById(ID).orElse(null);
    }

    @Override
    public void deleteMedecin(Integer idMedecin) {
        medecinRepo.deleteById(idMedecin);
    }


    @Override
    public List<Medecin> getAllMedecins() {
        return medecinRepo.findAll();
    }

    @Override
    public Medecin updateMedecin(Medecin medecin) {
        return medecinRepo.save(medecin);
    }

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepo.save(patient);
    }


    @Override
    public Patient updatePatient(Patient patient) {
        return patientRepo.save(patient);
    }
    @Override
    public void deletePatient(Integer idPatient) {
        patientRepo.deleteById(idPatient);
    }
    @Override
    public Patient affichPatient(Integer idPatient) {
        return patientRepo.findById(idPatient).orElse(null);
    }

    @Override
    public List<Patient> allpatient() {
        return patientRepo.findAll();
    }

    //choisir mode de paiement
    @Override
    public void choisirModePaiement(Integer cinPat, ModePaiement modePaiementChoisi) {
        Patient patient = patientRepo.findByCinPat(cinPat);

        if (patient != null) {

            patient.setModePaiement(modePaiementChoisi);
            patientRepo.save(patient);
        }
    }
    @Override
    public RDV addRDV(RDV rdv) {

        Medecin medecin = medecinRepo.findByNomMed(rdv.getNomDuMedecin());
        Patient patient = patientRepo.findByNomPat(rdv.getNomDuPatient());


        rdv.setMedecin(medecin);
        rdv.setPatient(patient);
        rdv.setPaiementRDV(PaiementRDV.NonPayes);
        return rdvRepo.save(rdv);
    }

    @Override
    public void marquerEtatRDV(Integer idRDV, EtatRDV nouvelEtat, Integer cinMedecin) {
        RDV rdv = rdvRepo.findById(idRDV).orElse(null);

        if (rdv != null) {
            Medecin medecin = medecinRepo.findByCinMed(cinMedecin);

            // Vérifiez si le médecin est associé au RDV
            if (medecin != null && rdv.getMedecin().equals(medecin)) {
                rdv.setEtatRDV(nouvelEtat);
                rdvRepo.save(rdv);
            }
        }
    }
    @Override
    public List<RDV> getRDVsForPatient(Integer cinPatient) {
        Patient patient = patientRepo.findByCinPat(cinPatient);

        if (patient == null) {
            throw new EntityNotFoundException("Patient not found for CIN: " + cinPatient);
        }
        List<RDV> rdvsForPatient = rdvRepo.findByPatient_IdPat(patient.getIdPat());

        return rdvsForPatient;
    }
    @Override
    public List<RDV> getRDVsForMedecin(Integer cinMedecin) {
        Medecin medecin = medecinRepo.findByCinMed(cinMedecin);

        if (medecin == null) {
            throw new EntityNotFoundException("Medecin not found for CIN: " + cinMedecin);
        }
        List<RDV> rdvsForMedecin = rdvRepo.findByMedecin_IdMed(medecin.getIdMed());
        return rdvsForMedecin;
    }

    @Override
    public List<Medecin> getAllMedecin(Specialite specialite) {
        List<Medecin> medecins = medecinRepo.getMedecinBySpecialite(specialite);
        return medecins;
    }
    //Clinique
    public List<Clinique> allClinique() {
        return cliniqueRepo.findAll();
    }
    @Override
    public Clinique addClinique(Clinique clinique) {
        return cliniqueRepo.save(clinique);
    }
    @Override
    public Clinique updateClinique(Clinique clinique) {
        return cliniqueRepo.save(clinique);
    }
    @Override
    public void deleteClinique(Integer codeClinique) {

      cliniqueRepo.deleteById(codeClinique);
      cliniqueRepo.findByCodeClinique(codeClinique);

    }
    @Override
    public Clinique affichClinique(Integer codeClinique) {
        return cliniqueRepo.findByCodeClinique(codeClinique).orElse(null);
    }


    @Override
    public Optional<RDV> findRDVCommun(Integer cinPatient, Integer cinMedecin) {

        Optional<RDV> rdvCommun = rdvRepo.findRDVCommun(cinPatient, cinMedecin);
        return rdvCommun;
    }

    @Override
    public void affectMedecinClinique(Integer idMed, Integer idClinique) {
        Medecin m = medecinRepo.findById(idMed).get();
        Clinique c = cliniqueRepo.findById(idClinique).get();

        m.setClinique(c);
        medecinRepo.save(m);


    }

    @Override
    public List<RDV> getAllRDVs() {
        return rdvRepo.findAll();
    }

























}
