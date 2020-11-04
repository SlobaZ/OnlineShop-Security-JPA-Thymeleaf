package paket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import paket.model.Kupovina;
import paket.model.Proizvod;
import paket.model.Stavka;
import paket.repository.ProizvodRepository;
import paket.repository.StavkaRepository;
import paket.service.StavkaService;

@Service
public class JpaStavkaService implements StavkaService {
	
	@Autowired
	private StavkaRepository stavkaRepository;
	
	@Autowired
	private ProizvodRepository proizvodRepository;

	@Override
	public Stavka getOne(Integer id) {
		return stavkaRepository.getOne(id);
	}

	@Override
	public List<Stavka> findAll() {
		return stavkaRepository.findAll();
	}

	@Override
	public Page<Stavka> findAll(int pageNum) {
		PageRequest pageable = PageRequest.of(pageNum, 5);
		return stavkaRepository.findAll(pageable);
	}

	@Override
	public Stavka save(Stavka stavka) {
		return stavkaRepository.save(stavka);
	}

	@Override
	public Stavka delete(Integer id) {
		Stavka stavka = stavkaRepository.getOne(id);
		if(stavka != null) {
			stavkaRepository.delete(stavka);
		}
		return stavka;
	}


	@Override
	public List<Stavka> findByIdKupovine(Integer kupovinaId) {
		return stavkaRepository.findByIdKupovine(kupovinaId);
	}

	@Override
	public Stavka kupiStavku(Integer id, int kolicinastavke) {
		Stavka stavka = stavkaRepository.getOne(id);
		Proizvod proizvod = stavka.getProizvod();
		Kupovina kupovina = stavka.getKupovina();
		if( proizvod.getKolicina()- kolicinastavke >= 0   &&  proizvod.getKolicina() >= kolicinastavke 
			&& kupovina.getUser().getStanje() >= (stavka.getCenastavke() + (proizvod.getCena()*kolicinastavke))  ) {
			proizvod.setKolicina( proizvod.getKolicina() - kolicinastavke ); 
			stavka.setKolicinastavke(stavka.getKolicinastavke() + kolicinastavke);
			stavka.setCenastavke(stavka.getCenastavke() + (proizvod.getCena()*kolicinastavke) );
			}
		else {
			return null;
		}
		proizvodRepository.save(proizvod);
		stavkaRepository.save(stavka);
		
		return stavka;
	}

	@Override
	public Stavka resetujStavku(Integer id) {
		Stavka stavka = stavkaRepository.getOne(id);
		Proizvod proizvod = stavka.getProizvod();
		proizvod.setKolicina( proizvod.getKolicina() + stavka.getKolicinastavke() ); 
		stavka.setCenastavke(0.0);
		stavka.setKolicinastavke(0);
		proizvodRepository.save(proizvod);
		stavkaRepository.save(stavka);
		return stavka;
	}
	

}
