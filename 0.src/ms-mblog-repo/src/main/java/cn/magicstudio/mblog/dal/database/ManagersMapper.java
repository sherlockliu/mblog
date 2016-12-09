package cn.magicstudio.mblog.dal.database;

import java.util.List;
import cn.magicstudio.mblog.model.Managers;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author user
 * @date  2016-12-09 10:45:03
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface ManagersMapper extends BaseCrudMapper {
	
	List<Managers> getManagers();
}