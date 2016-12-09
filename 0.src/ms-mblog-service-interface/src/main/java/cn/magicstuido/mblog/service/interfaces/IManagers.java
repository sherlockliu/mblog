package cn.magicstuido.mblog.service.interfaces;

import java.util.List;
import cn.magicstudio.mblog.framework.exception.ServiceException;
import cn.magicstudio.mblog.model.Managers;

public interface IManagers {
	
	List<Managers> getManagers() throws ServiceException;

}
