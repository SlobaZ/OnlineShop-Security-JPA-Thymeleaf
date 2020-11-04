package paket.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import paket.model.Kupovina;
import paket.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User pronadjiPoEmailu(String email);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(:username IS NULL or u.username like :username ) AND "
			+ "(:mesto IS NULL OR u.mesto like :mesto) "
			)
	Page<User> search(
			@Param("username") String username, 
			@Param("mesto") String mesto,
			Pageable pageRequest);
	
	
	
	@Query("SELECT k FROM Kupovina k WHERE k.user.id = :idK")
	Kupovina podatakKorisnika( @Param("idK") Integer idK);
	
	

}
