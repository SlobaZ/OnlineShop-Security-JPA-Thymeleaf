package paket.repository;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import paket.model.Kupovina;


@Repository
public interface KupovinaRepository extends JpaRepository<Kupovina, Integer>{

//	Page<Kupovina> findByUserId(Integer userId, Pageable page);
	
	@Query("SELECT k FROM Kupovina k WHERE "
			+ "(:userid IS NULL or k.user.id = :userid ) AND "
			+ "(:sifra IS NULL OR k.sifra like :sifra) AND "
			+ "(:ukupnacena IS NULL or k.ukupnacena = :ukupnacena ) AND "
			+ "(:datumVremePocetak IS NULL or k.datumvreme >= :datumVremePocetak ) AND "
			+ "(:datumVremeKraj IS NULL or k.datumvreme <= :datumVremeKraj ) "
			)
	Page<Kupovina> search(
			@Param("userid") Integer userid, 
			@Param("sifra") String sifra, 
			@Param("ukupnacena") Double ukupnacena,
			@Param("datumVremePocetak") Timestamp datumVremePocetak,
			@Param("datumVremeKraj") Timestamp datumVremeKraj,
			Pageable pageRequest);
	
}
