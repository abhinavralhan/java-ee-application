/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.dao;


import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.NoResultException;
import xray.evs.dto.Item;
import xray.evs.entities.ItemEntity;
import xray.evs.entities.QuestionEntity;

/**
 *
 * @author abhinavralhan
 */
@LocalBean
@Stateless
public class ItemAccess {
    
    @PersistenceContext(unitName = "XrayEVS-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        System.out.println("Construct ItemBean " + this);
    }
    
   public ItemEntity createItem(Item item, QuestionEntity question) {
        ItemEntity ie = new ItemEntity(true);
        ie.setShortName(item.getShortName());

        String itemDescription = item.getDescription();
        if (itemDescription != null && !itemDescription.equals("")) {
            ie.setDescription(itemDescription);
        }

        ie.setVotesCounter(0);
        ie.setQuestion(question);
        em.persist(ie);
        return ie;
    }
    
   
   public List<ItemEntity> createItem(List<Item> items, QuestionEntity question) {
        return items.stream()
                .map(item -> createItem(item, question))
                .collect(Collectors.toList());
    }
   
   
    public ItemEntity getItemByUuid(String uuid) {
        try {
            ItemEntity ie = em.createNamedQuery("getItemByUuid", ItemEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
            return ie;
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
