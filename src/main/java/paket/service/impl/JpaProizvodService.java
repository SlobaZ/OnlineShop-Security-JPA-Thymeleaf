package paket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import paket.model.Kategorija;
import paket.model.Proizvod;
import paket.repository.ProizvodRepository;
import paket.service.ProizvodService;

@Service 
public class JpaProizvodService implements ProizvodService{
	
	@Autowired
	private ProizvodRepository proizvodRepository;

	@Override
	public Proizvod getOne(Integer id) {
		return proizvodRepository.getOne(id);
	}

	@Override
	public List<Proizvod> findAll() {
		return proizvodRepository.findAll();
	}

	@Override
	public Page<Proizvod> findAll(int pageNum) {
		PageRequest pageable = PageRequest.of(pageNum, 5);
		return proizvodRepository.findAll(pageable);
	}

	@Override
	public Proizvod save(Proizvod proizvod) {
		return proizvodRepository.save(proizvod);
	}
	
	@Override
	public List<Proizvod> saveAll(List<Proizvod> proizvodi) {
		return proizvodRepository.saveAll(proizvodi); 
	}


	@Override
	public Proizvod delete(Integer id) {
		Proizvod proizvod = proizvodRepository.getOne(id);
		if(proizvod != null) {
			proizvodRepository.delete(proizvod);
		}
		return proizvod;
	}

	@Override
	public Page<Proizvod> search(String naziv, String marka, Kategorija kategorija, Double cena, int pageNum) {
		if( naziv != null) {
			naziv = '%' + naziv + '%';
		}
		if( marka != null) {
			marka = '%' + marka + '%';
		}
		PageRequest pageable = PageRequest.of(pageNum, 5);
		return proizvodRepository.search(naziv, marka, kategorija, cena, pageable);
	}

	@Override
	public Proizvod findByNazivAndMarka(String naziv, String marka) {
		return proizvodRepository.findByNazivAndMarka(naziv, marka);
	}



}
