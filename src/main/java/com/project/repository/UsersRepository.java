package  com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.dto.EnquiryRequest;
import com.project.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{
	
	Boolean existsByEmail(String email);
	
	Boolean existsByAccountNumber(String accountNumber);
	
	User findByAccountNumber(String accountNumber);

}
