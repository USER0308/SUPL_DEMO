package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.formssi.dao.UserDao;
//import com.formssi.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={ "classpath:spring/spring-*.xml"})
public class JunitDemo {
	
//	@Autowired
//	private UserDao userDao;
	
	@Before
	public void beforTest(){
		
	}

	@Test
	public void test() {
		String id = "zj111";
//		User user = userDao.queryById(id);
//		System.out.println(user.getUserName());
	}

}
