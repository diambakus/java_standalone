package com.ef.dao;

import java.util.List;

import com.ef.domain.Access;

public interface AccessDAO {

	public List<Access> findAllAccesses();
	public Access findById(Long id);
	public List<Access> findById();
	public void insertAccess(Access access);
	public void updateAccess();
	public Access deleteAccess(Long id);

}
