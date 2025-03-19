package com.gestion_presence.gestion_presence.Dao;
import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.Models.Emargements;

import com.gestion_presence.gestion_presence.Models.User;
import com.gestion_presence.gestion_presence.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.List;

public class EmargementsDao {

    public void addEmargement(Emargements emargement) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(emargement);
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

    public void updateEmargement(Emargements emargement) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(emargement);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    public void deleteEmargement(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Emargements emargement = em.find(Emargements.class, id);
            if (emargement != null) {
                em.remove(emargement);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Emargements> getAllEmargements() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Emargements e", Emargements.class).getResultList();
        } finally {
            em.close();
        }
    }
    public Emargements getEmargementProf(Cours cours, User professeur, Date date) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {

            String query = "SELECT e FROM Emargements e WHERE e.cours = :cours AND e.professeur = :professeur AND e.date = :date";
            TypedQuery<Emargements> typedQuery = em.createQuery(query, Emargements.class);
            typedQuery.setParameter("cours", cours);
            typedQuery.setParameter("professeur", professeur);
            typedQuery.setParameter("date", date);


            List<Emargements> resultList = typedQuery.getResultList();


            if (resultList.isEmpty()) {
                return null;
            } else {
                return resultList.get(0);
            }
        } finally {
            em.close();
        }
    }

    public List<Emargements> getEmargementsBetweenDates(Date dateDebut, Date dateFin) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT e FROM Emargements e WHERE e.date BETWEEN :dateDebut AND :dateFin ORDER BY e.date ASC";
            TypedQuery<Emargements> typedQuery = em.createQuery(query, Emargements.class);
            typedQuery.setParameter("dateDebut", dateDebut);
            typedQuery.setParameter("dateFin", dateFin);

            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }


}
