package com.ef.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.ef.domain.BlockedIP;

public class BlockedIPDAOImpl implements BlockedIPDAO {

	private EntityManager entityManager;

	public BlockedIPDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<BlockedIP> findAllBlocked() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockedIP findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BlockedIP> findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertBlockedIP(BlockedIP blockedIP) {
		entityManager.persist(blockedIP);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public BlockedIP delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
