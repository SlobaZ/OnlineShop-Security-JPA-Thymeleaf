package paket.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import paket.model.Kupovina;

public interface KupovinaService {
	
	Kupovina getOne(Integer id);
	List<Kupovina> findAll();
	Page<Kupovina> findAll(int pageNum);
	Kupovina save(Kupovina kupovina);
	Kupovina delete(Integer id);
		
	Page<Kupovina> search(
			@Param("userid") Integer userid, 
			@Param("sifra") String sifra, 
			@Param("ukupnacena") Double ukupnacena,
			@Param("datumvremepocetak") String datumvremepocetak,
			@Param("datumvremekraj") String datumvremekraj,
			 int pageNum);

	
	Kupovina kupi(Integer id);


}
