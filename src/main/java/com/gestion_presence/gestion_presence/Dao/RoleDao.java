package com.gestion_presence.gestion_presence.Dao;

import com.gestion_presence.gestion_presence.Models.Role;

import com.gestion_presence.gestion_presence.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RoleDao {

    public static List<Role> getAllRole() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Role s", Role.class).getResultList();
        } finally {
            em.close();
        }
    }
}
