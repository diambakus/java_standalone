package com.ef.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ef.domain.Access;


public class AccessDAOImpl implements AccessDAO {

    private EntityManager entityManager;
	
    public AccessDAOImpl(EntityManager entityManager) {
       this.entityManager = entityManager;
	}
    
	@Override
	public List<Access> findAllAccesses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Access findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Access> findById() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * That function persists access object into database
	 * @param An object of type access. In others words, a line
	 * of web server access log.
	 */
	@Override
	public void insertAccess(Access access) {
		entityManager.persist(access);
	}

	@Override
	public void updateAccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Access deleteAccess(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
