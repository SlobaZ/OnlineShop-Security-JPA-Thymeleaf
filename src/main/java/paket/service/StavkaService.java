package paket.service;

import java.util.List;

import org.springframework.data.domain.Page;

import paket.model.Stavka;

public interface StavkaService {
	
	Stavka getOne(Integer id);
	List<Stavka> findAll();
	Page<Stavka> findAll(int pageNum);
	Stavka save(Stavka Stavka);
	Stavka delete(Integer id);
			
	List<Stavka> findByIdKupovine(Integer kupovinaId);
	
	Stavka kupiStavku (Integer id , int kolicinaStavke);
	
	Stavka resetujStavku (Integer id);
}
