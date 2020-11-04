package paket.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import paket.model.Kategorija;
import paket.model.Proizvod;


public interface ProizvodService {

	Proizvod getOne(Integer id);
	List<Proizvod> findAll();
	Page<Proizvod> findAll(int pageNum);
	Proizvod save(Proizvod Proizvod);
	List<Proizvod> saveAll(List<Proizvod> proizvodi);
	Proizvod delete(Integer id);
	
	Proizvod findByNazivAndMarka(String naziv, String marka);
	
	Page<Proizvod> search(
			@Param("naziv") String naziv, 
			@Param("marka") String marka,
			@Param("kategorija") Kategorija kategorija,
			@Param("cena") Double cena,
			int pageNum );
	
	

}
