package com.gestion_presence.gestion_presence.Dao;
import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.Models.Emargements;

import com.gestion_presence.gestion_presence.Models.User;
import com.gestion_presence.gestion_presence.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 import java.time.LocalDate;
    import java.util.ArrayList;
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

    //Graph
    public Map<String, Long> getEmargementsCountByProfesseur() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT e.professeur.nom, COUNT(e) FROM Emargements e GROUP BY e.professeur.nom";
            List<Object[]> results = em.createQuery(query, Object[].class).getResultList();

            Map<String, Long> stats = new HashMap<>();
            for (Object[] result : results) {
                stats.put((String) result[0], (Long) result[1]);
            }
            return stats;
        } finally {
            em.close();
        }
    }
    public Map<String, Long> getEmargementsEvolution() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT TO_CHAR(e.date, 'YYYY-MM') AS mois, COUNT(e) FROM Emargements e GROUP BY mois ORDER BY mois ASC";
            List<Object[]> results = em.createQuery(query, Object[].class).getResultList();

            Map<String, Long> evolutionStats = new LinkedHashMap<>();
            for (Object[] result : results) {
                evolutionStats.put((String) result[0], (Long) result[1]);
            }
            return evolutionStats;
        } finally {
            em.close();
        }
    }

    public Map<String, Double> getPresenceParCours() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT e.cours.nom, COUNT(e) * 100.0 / (SELECT COUNT(e2) FROM Emargements e2) FROM Emargements e GROUP BY e.cours.nom";
            List<Object[]> results = em.createQuery(query, Object[].class).getResultList();

            Map<String, Double> presenceParCours = new HashMap<>();
            for (Object[] result : results) {
                presenceParCours.put((String) result[0], (Double) result[1]);
            }
            return presenceParCours;
        } finally {
            em.close();
        }
    }
    public int getTotalProfesseurs() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(u) FROM User u WHERE u.role.id = 4";
            return em.createQuery(query, Long.class).getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

 

    public int getTotalCours() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(c) FROM Cours c";
            return em.createQuery(query, Long.class).getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

  



}
