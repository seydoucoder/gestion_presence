package com.gestion_presence.gestion_presence.Dao;


import com.gestion_presence.gestion_presence.Models.Salle;
import com.gestion_presence.gestion_presence.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class SalleDao {


    public static void addSalle(Salle salle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(salle);
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
    public static boolean existeSalleParLibelle(String libelle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {

            String query = "SELECT COUNT(s) FROM Salle s WHERE s.libelle = :libelle";
            long count = (long) em.createQuery(query)
                    .setParameter("libelle", libelle)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }


    public static boolean deleteSalle(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Salle salle = em.find(Salle.class, id);
            if (salle != null) {
                em.remove(salle);
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


    public static boolean updateSalle(Salle salle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Salle existingSalle = em.find(Salle.class, salle.getId());
            if (existingSalle != null) {
                existingSalle.setLibelle(salle.getLibelle());
                em.merge(existingSalle);
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


    public static List<Salle> getAllSalles() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();
        } finally {
            em.close();
        }
    }


    public Salle findSalleById(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Salle.class, id);
        } finally {
            em.close();
        }
    }
}
