package com.ef.dao;

import java.util.List;

import com.ef.domain.BlockedIP;

public interface BlockedIPDAO {

	public List<BlockedIP> findAllBlocked();
	public BlockedIP findById(Long id);
	public List<BlockedIP> findById();
	public void insertBlockedIP(BlockedIP blockedIP);
	public void update();
	public BlockedIP delete(Long id);
}
