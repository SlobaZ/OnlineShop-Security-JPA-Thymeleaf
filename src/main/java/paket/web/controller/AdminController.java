package paket.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import paket.utils.DodeliKategoriju;
import paket.utils.PomocnaKlasa;
import paket.service.KupovinaService;
import paket.service.StavkaService;
import paket.model.Kupovina;
import paket.model.Stavka;
import paket.model.Proizvod;
import paket.model.User;
import paket.repository.KupovinaRepository;
import paket.repository.UserRepository;
import paket.security.UserService;
import paket.service.ProizvodService;

@Controller
public class AdminController {  
	
	@Autowired
	private ProizvodService proizvodService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KupovinaService kupovinaService;
	
	@Autowired
	private KupovinaRepository kupovinaRepository;
	
	@Autowired
	private StavkaService stavkaService;
	
	DodeliKategoriju dk = new DodeliKategoriju();
	

	@GetMapping(value="/admin/addproizvod")
    public ModelAndView createNewProizvod(){
        ModelAndView modelAndView = new ModelAndView();
        Proizvod proizvod = new Proizvod();
        modelAndView.addObject("proizvod", proizvod);
        modelAndView.setViewName("/admin/proizvod-add");
        return modelAndView;
    }
	
    @PostMapping(value = "/admin/addproizvod")
    public ModelAndView addNewProizvod(@Valid Proizvod proizvod, @RequestParam(value="file") MultipartFile file, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Proizvod proizvodExists = proizvodService.findByNazivAndMarka(proizvod.getNaziv(),proizvod.getMarka()); 
        if (proizvodExists != null) {
            bindingResult.rejectValue("naziv", "error.naziv",  "There is already a proizvod added with the naziv and marka provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/admin/proizvod-add");
        } 
        else {
        	proizvod.setPhoto(file.getOriginalFilename());
        	dk.dodeli(proizvod);
        	proizvodService.save(proizvod);
            modelAndView.addObject("successMessage", "Proizvod has been add successfully");
            modelAndView.addObject("proizvod", new Proizvod());
            List<Kupovina> kupovine = kupovinaService.findAll();
    		for( Kupovina kupovina : kupovine ) {
    			Stavka stavka = new Stavka();
    			stavka.setProizvod(proizvod);
    			stavkaService.save(stavka);
    			kupovina.addStavka(stavka);
    			kupovinaService.save(kupovina);
    		}
            modelAndView.setViewName("/admin/proizvod-add");

        }
        return modelAndView;
    }

    
    @GetMapping("/admin/deleteproizvod/{id}")
    private String deleteProizvod(@PathVariable("id") Integer id){
    	Proizvod  proizvod = proizvodService.getOne(id);
        if(proizvod != null){
        	proizvodService.delete(id);
        	
        }else{
            System.err.println("not found");
        }
        return "redirect:/proizvodi";
    }
    

    @GetMapping(path = {"/admin/editproizvod/{id}"})
    private String addFormProizvod(@PathVariable("id") Integer id, Model model){
        if(id != null){
            model.addAttribute("proizvod", proizvodService.getOne(id) );
        }
        else{
        	return null;
        }
        return "/admin/proizvod-edit";
    }
    
    
    @PostMapping("/admin/editProizvod")
    private String insertOrUpdateProizvod(@Valid Proizvod proizvod, @RequestParam(value="file") MultipartFile file, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
        	return "/admin/proizvod-edit";
        	} 
        else{
        	Proizvod editProizvod = proizvodService.getOne(proizvod.getId());
            if(editProizvod !=null){
            	editProizvod.setNaziv(proizvod.getNaziv());
            	editProizvod.setMarka(proizvod.getMarka());
            	editProizvod.setPhoto(file.getOriginalFilename());
            	editProizvod.setKolicina(proizvod.getKolicina());
            	editProizvod.setCena(proizvod.getCena());
            	dk.dodeli(editProizvod);
            	proizvodService.save(editProizvod);
            }
        }
        return "redirect:/proizvodi";
    }
    
        
	@GetMapping("/admin/usersSve")
	public String listaUsers(Model model) {
		model.addAttribute("users", userService.findAll());		
		return "/admin/user";
	}
	
	
	
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String sviItrazeniUsers (@RequestParam (required = false) String username,
					@RequestParam (required = false) String mesto,
					HttpServletRequest request, Model model) {
	    
		int page = 0;
		int size = 5;
	    
	    Page<User> userPage = null;
	
			if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
				page = Integer.parseInt(request.getParameter("page")) - 1;
			}

			if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
				size = Integer.parseInt(request.getParameter("size"));
			}
		    if (username != null || mesto != null) {
		    	userPage = userService.search(username, mesto, page);
		    }
		    else {
		    	userPage = userRepository.findAll(PageRequest.of(page, size));
		    }
		    
	    model.addAttribute("users", userPage);
	    return "/admin/user";
	}
	

	
	 @GetMapping("/admin/deleteuser/{id}")
	    private String deleteUser(@PathVariable("id") Integer id){
	    	User  user = userService.getOne(id);
	        if(user != null){
	        	userService.delete(id);
	        }else{
	            System.err.println("not found");
	        }
	        return "redirect:/admin/users";
	    }
	    

	    @GetMapping(path = {"/admin/edituser/{id}"})
	    private String addFormUser(@PathVariable("id") Integer id, Model model){
	        if(id != null){
	            model.addAttribute("user", userService.getOne(id) );
	        }
	        else{
	        	return null;
	        }
	        return "/admin/user-edit";
	    }
	    
	    
	    @PostMapping("/admin/editUser")
	    private String insertOrUpdateUser(@Valid User user, BindingResult bindingResult){
	        if (bindingResult.hasErrors()) {
	        	return "/admin/user-edit";
	        	} 
	        else{
	        	User editUser = userService.getOne(user.getId());
	            if(editUser !=null){
	            	editUser.setUsername(user.getUsername());
	            	editUser.setFirstname(user.getFirstname());
	            	editUser.setLastname(user.getLastname());
	            	editUser.setEmail(user.getEmail());
	            	editUser.setPassword(user.getPassword());
	            	editUser.setMesto(user.getMesto());
	            	editUser.setAdresa(user.getAdresa());
	            	editUser.setJmbg(user.getJmbg());
	            	editUser.setTelefon(user.getTelefon());
	            	editUser.setBrojracuna(user.getBrojracuna());
	            	editUser.setStanje(user.getStanje());
	            	userService.saveUser(editUser);
	            }
	        }
	        return "redirect:/admin/users";
	    }
	
	
	
	
    
	    @RequestMapping(value = "/admin/kupovine", method = RequestMethod.GET)
		public String sveItrazeneKupovine(@RequestParam(value = "userid",required=false) Integer userid, 
							@RequestParam(value = "sifra",required=false) String sifra, 
							@RequestParam(value = "ukupnacena",required=false) Double ukupnacena, 
							@RequestParam(value = "datumvremepocetak",required=false) String datumvremepocetak,
							@RequestParam(value = "datumvremekraj",required=false) String datumvremekraj, 
							HttpServletRequest request, Model model) {
		    
	    	    	
			int page = 0;
			int size = 5;

			 model.addAttribute("users", userService.findAll());
			
		    Page<Kupovina> kupovinaPage = null;
		
				if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
					page = Integer.parseInt(request.getParameter("page")) - 1;
				}

				if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
					size = Integer.parseInt(request.getParameter("size"));
				}
				if(userid!=null || sifra!=null || ukupnacena!=null || datumvremepocetak!=null || datumvremekraj!=null) {
					
			    	kupovinaPage = kupovinaService.search(userid,sifra,ukupnacena,datumvremepocetak,datumvremekraj, page);
			    }
			    else {
			    	kupovinaPage = kupovinaRepository.findAll(PageRequest.of(page, size));
			    }
			    
		    model.addAttribute("kupovine", kupovinaPage);
		    return "/admin/kupovine-sve";
		}

	    
	    
	    
	    
	    
	    @GetMapping("/admin/deletekupovina/{id}")
	    private String deleteKupovina(@PathVariable("id") Integer id){
	    	Kupovina  kupovina = kupovinaService.getOne(id);
	        if(kupovina != null){
	        	kupovinaService.delete(id);
	        	List<Stavka> stavke = stavkaService.findByIdKupovine(kupovina.getId());
	        	for(Stavka stavka: stavke) {
	        		stavkaService.delete(stavka.getId());
	        	}
	        }else{
	            System.err.println("not found");
	        }
	        return "redirect:/admin/kupovine";
	    }
	    
	
	    @GetMapping(path = {"/admin/editkupovina/{id}"})
	    private String addFormKupovina(@PathVariable("id") Integer id, Model model){
	        if(id != null){
	            model.addAttribute("kupovina", kupovinaService.getOne(id) );
	        }
	        else{
	        	return null;
	        }
	        return "/admin/kupovina-edit";
	    }
	    
	    
	    @PostMapping("/admin/editKupovina")
	    private String insertOrUpdateKupovina(@Valid Kupovina kupovina, BindingResult bindingResult){
	        if (bindingResult.hasErrors()) {
	        	return "/admin/kupovina-edit";
	        	} 
	        else{
	        	Kupovina editKupovina = kupovinaService.getOne(kupovina.getId());
	            if(editKupovina !=null){
	            	editKupovina.setSifra(kupovina.getSifra());
	            	editKupovina.setUkupnacena(kupovina.getUkupnacena());
	            	editKupovina.setDatetime(kupovina.getDatetime());
	            	editKupovina.setDatumvreme(PomocnaKlasa.KonvertujStringUSqlDatumIVreme(kupovina.getDatetime()));
	            	editKupovina.setUser(kupovina.getUser());
	            	kupovinaService.save(editKupovina);
	            }
	        }
	        return "redirect:/admin/kupovine";
	    }
	
	    
	    
	    
	    
	
    
}
