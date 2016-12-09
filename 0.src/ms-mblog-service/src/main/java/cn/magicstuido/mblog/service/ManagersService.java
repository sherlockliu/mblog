package cn.magicstuido.mblog.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.magicstudio.mblog.dal.database.ManagersMapper;
import cn.magicstudio.mblog.framework.exception.ServiceException;
import cn.magicstudio.mblog.model.Managers;
import cn.magicstuido.mblog.service.interfaces.IManagers;

@Service("managersService")
public class ManagersService implements IManagers {

	@Resource
	private ManagersMapper managerMapper;

	@Override
	public List<Managers> getManagers() throws ServiceException {
		try{
			return this.managerMapper.selectByParams(null, null);
		}catch(Exception ex){
			throw new ServiceException(ex);
		}
	}	
}
