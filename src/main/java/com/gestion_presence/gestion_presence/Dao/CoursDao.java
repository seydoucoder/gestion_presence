package com.gestion_presence.gestion_presence.Dao;

import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class CoursDao {


    public void addCours(Cours cours) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(cours);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static boolean existeCours(String nom) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {

            String query = "SELECT COUNT(s) FROM Cours s WHERE s.nom = :nom";
            long count = (long) em.createQuery(query)
                    .setParameter("nom", nom)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }


    public boolean deleteCours(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Cours cours = em.find(Cours.class, id);
            if (cours != null) {
                em.remove(cours);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }


    public boolean updateCours(Cours cours) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Cours existingCours = em.find(Cours.class, cours.getId());
            if (existingCours != null) {
                existingCours.setNom(cours.getNom());
                existingCours.setDescription(cours.getDescription());
                existingCours.setHeureDebut(cours.getHeureDebut());
                existingCours.setHeureFin(cours.getHeureFin());
                existingCours.setSalle(cours.getSalle());
                existingCours.setJour(cours.getJour());
                em.merge(existingCours);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    public List<Cours> getCoursByProfesseur(int profId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cours c WHERE c.professeur.id = :profId", Cours.class)
                    .setParameter("profId", profId)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    public List<Cours> getAllCours() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cours c", Cours.class).getResultList();
        } finally {
            em.close();
        }
    }
}
