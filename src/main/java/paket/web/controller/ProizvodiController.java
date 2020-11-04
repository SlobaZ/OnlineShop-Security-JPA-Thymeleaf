package paket.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import paket.model.Kategorija;
import paket.model.Proizvod;
import paket.repository.ProizvodRepository;
import paket.service.ProizvodService;

@Controller
public class ProizvodiController {

	@Autowired
	private ProizvodService proizvodService;

	@Autowired
	private ProizvodRepository proizvodRepository;

	@GetMapping("/proizvodiSve")
	public String getAll(Model model) {
		model.addAttribute("proizvodi", proizvodService.findAll());
		return "proizvod";
	}

		
	@RequestMapping(value = "/proizvodi", method = RequestMethod.GET)
	public String searchProizvodi(@RequestParam (required = false) String naziv,
										@RequestParam (required = false) String marka,
										@RequestParam (required = false) Kategorija kategorija,
										@RequestParam (required = false) Double cena,
										HttpServletRequest request, Model model) {
	    
		
		model.addAttribute("kategorije", paket.model.Kategorija.values());

		int page = 0;
		int size = 5;
	    
	    Page<Proizvod> proizvodPage = null;
	
			if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
				page = Integer.parseInt(request.getParameter("page")) - 1;
			}

			if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
				size = Integer.parseInt(request.getParameter("size"));
			}
		    if (naziv != null || marka != null || kategorija!=null || cena != null) {
		    	proizvodPage = proizvodService.search(naziv, marka, kategorija, cena, page);
		    }
		    else {
			proizvodPage = proizvodRepository.findAll(PageRequest.of(page, size));
		    }
		
		    
	    model.addAttribute("proizvodi", proizvodPage);

	    return "proizvod";
	}
	


	
	
	
	

}
