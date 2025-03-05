package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.users;
import generator.service.UsersService;
import generator.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-03-03 14:02:14
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, users>
    implements UsersService{

}




