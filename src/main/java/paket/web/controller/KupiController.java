package paket.web.controller;


import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import paket.utils.PomocnaKlasa;
import paket.model.Kupovina;
import paket.model.Proizvod;
import paket.model.Stavka;
import paket.model.User;
import paket.security.UserService;
import paket.service.KupovinaService;
import paket.service.ProizvodService;
import paket.service.StavkaService;


@Controller
public class KupiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StavkaService stavkaService;
	
	@Autowired
	private KupovinaService kupovinaService;
	
	@Autowired
	private ProizvodService proizvodService;
	
	
		
	@GetMapping("/user/napravikupovinu")
	public String kreirajKupovinu(Model model,Principal principal) {

		User user =  userService.pronadjiPoEmailu(principal.getName());
	    model.addAttribute("pozdravnaPorukaUseru", "Welcome " + user.getUsername() + " / " + user.getFirstname() + " " + user.getLastname());

		Kupovina kupovina = new Kupovina();
		kupovina.setSifra(PomocnaKlasa.DodeliSifru());
		kupovina.setDatumvreme(PomocnaKlasa.UpisiSadasnjiDatumIVremeSql());
		kupovina.setDatetime(PomocnaKlasa.PrikaziTekstualnoDatumIVreme(PomocnaKlasa.UpisiSadasnjiDatumIVremeSql()));
		kupovina.setUkupnacena(0.0);
		kupovina.setUser(user);
		kupovinaService.save(kupovina);
		
		List<Proizvod> proizvodi = proizvodService.findAll();
		for(Proizvod proizvod:proizvodi) {
			Stavka stavka = new Stavka();
			stavka.setKolicinastavke(0);
			stavka.setCenastavke(0.0);
			stavka.setProizvod(proizvod);
			stavkaService.save(stavka);
			kupovina.addStavka(stavka);
		}
		kupovinaService.save(kupovina);
		model.addAttribute("kupovina", kupovina);
		
		List<Stavka> stavke = stavkaService.findByIdKupovine(kupovina.getId());
		model.addAttribute("stavke", stavke);
		return "/user/kreirajkupovinu";
	}

	
	
	@GetMapping("/user/kupovinastavki/{id}")
	public String zapocniKupovinu(@PathVariable("id") Integer id, Model model,Principal principal) {

		User user =  userService.pronadjiPoEmailu(principal.getName());
	    model.addAttribute("pozdravnaPorukaUseru",  user.getUsername());

	    Kupovina kupovina = kupovinaService.getOne(id);
		model.addAttribute("kupovina", kupovina);
		
		List<Stavka> stavke = stavkaService.findByIdKupovine(kupovina.getId());
		model.addAttribute("stavke", stavke);
		return "/user/kupistavku";
	}

	
	@RequestMapping(value = "/user/{id}/kupitistavku/{ids}", method = RequestMethod.GET)
	public String kupiStavku(@PathVariable("id") Integer id,
							 @PathVariable("ids") Integer ids,
							@RequestParam (required = false, defaultValue = "0") int kolicinastavke,
							HttpServletRequest request, Model model) {
		
		  stavkaService.kupiStavku(ids,kolicinastavke);
		  return "redirect:/user/kupovinastavki/{id}";
	}
	


    @GetMapping("/user/{id}/resetujstavku/{ids}")
    private String resetujstavku(@PathVariable("id") Integer id, @PathVariable("ids") Integer ids){
    	stavkaService.resetujStavku(ids);
        return "redirect:/user/kupovinastavki/{id}";
    }

    

    
    @GetMapping("/user/kupi/{id}")
    private String kupi(@PathVariable("id") Integer id, Model model,Principal principal){
    	Kupovina kupovina = kupovinaService.getOne(id);
    	kupovina = kupovinaService.kupi(id);
		if(kupovina==null) {
			return "redirect:/user/kupovinastavki";
		}
		model.addAttribute("kupovina", kupovina);
		List<Stavka> stavke = stavkaService.findByIdKupovine(kupovina.getId());
		model.addAttribute("stavke", stavke);
		User user =  userService.pronadjiPoEmailu(principal.getName());
	    model.addAttribute("pozdravnaPorukaUseru",  user.getUsername()  + " / " + " " + user.getFirstname() + " " + user.getLastname()   );
        return "/user/rezultatkupovine";
    }
    
    
    
    
    
}
