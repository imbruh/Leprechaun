package br.edu.ifpb.pweb2.leprechaun.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.leprechaun.Dto.DezenasDTO;
import br.edu.ifpb.pweb2.leprechaun.Dto.UsuarioLoginDTO;
import br.edu.ifpb.pweb2.leprechaun.Model.Sorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.TipoSorteio;
import br.edu.ifpb.pweb2.leprechaun.Model.Usuario;
import br.edu.ifpb.pweb2.leprechaun.Repository.ApostaRepository;
import br.edu.ifpb.pweb2.leprechaun.Repository.UsuarioRepository;
import br.edu.ifpb.pweb2.leprechaun.Service.SorteioService;
import br.edu.ifpb.pweb2.leprechaun.Service.UsuarioService;

@Controller
public class UsuarioController {
    
   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private UsuarioService usuarioService;
   
   @Autowired
   SorteioService sorteioService;
   
   @Autowired
   ApostaRepository apostaRepository;
    
    @GetMapping("/")
    public List<Usuario> getUsuarios() {
        return this.usuarioRepository.findAll();
    }
    
    @GetMapping("/clientes")
    public List<Usuario> getClientes() {
        return this.usuarioService.getClientes();
    }
   
    @RequestMapping("/login")
    public String login(Model model, @ModelAttribute("mensagem") String mensagem) {
    	model.addAttribute("usuario", new Usuario());
    	
    	if(mensagem.isEmpty()) {
    		mensagem = null;
    	}
    	 	
    	model.addAttribute("mensagem", mensagem);
    	
    	return "login";
    }
    
    @RequestMapping("/cadastro")
    public String cadastro(Model model, @ModelAttribute("mensagem") String mensagem) {
    	model.addAttribute("usuario", new Usuario());
    	
    	if(mensagem.isEmpty()) {
    		mensagem = null;
    	}
    	 	
    	model.addAttribute("mensagem", mensagem);
    	
    	return "Cadastro";
    }
    
    @RequestMapping("/cadastro/usuario")
    public String cadastrarUsuario(ModelAndView mav, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes, HttpSession session) {
    	String mensagem = "";
    	
    	if(usuario.getTipoUsuario()==null) {
    		mensagem = "Escolha um tipo de usuario.";
    		redirectAttributes.addFlashAttribute("mensagem", mensagem);
    		return "redirect:/cadastro";
    	}
    	
    	mensagem = this.usuarioService.cadastrarUsuario(usuario);
   	
    	if(mensagem.equals("Você precisa ter 18 anos ou mais para se cadastrar.")) {
    		redirectAttributes.addFlashAttribute("mensagem", mensagem);
    		return "redirect:/cadastro";
    	}
    	
    	redirectAttributes.addFlashAttribute("usuario", usuario);
    	
    	return "redirect:/login/usuario";
       
    }
  
    @RequestMapping("/login/usuario")
    public String logarUsuario(ModelAndView mav, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes, HttpSession session) {
    	Usuario usuarioExistente = this.usuarioService.logarUsuario(usuario);
  
    	if(usuarioExistente == null) {
    		redirectAttributes.addFlashAttribute("mensagem", "Login ou senha inválidos");
    		mav.setViewName("login");
    		return "redirect:/login";
    	}
    	session.setAttribute("usuario", usuarioExistente);
    	redirectAttributes.addFlashAttribute("usuario", usuarioExistente);
    	if(usuarioExistente.getTipoUsuario().toString().equals("CLIENTE")) {
    		return "redirect:/sorteio/cliente";    		
    	}
    	
    	session.setAttribute("msgSucesso", false);
    	session.setAttribute("aleatorio", true);
    	return "redirect:/sorteio/controlador";
    	
    }
    
    @RequestMapping("/sair")
    public String sair(HttpSession session) {
    	session.invalidate();
    	return "redirect:/login";
    }
    
    @RequestMapping("/sorteio/cliente")
    public ModelAndView getTelaSorteioCliente(ModelAndView mav, @ModelAttribute("usuario") Usuario usuario) {

    	mav.addObject("usuario", usuario);
    	mav.setViewName("Telas/TelaSorteioCliente"); 
    	Sorteio sorteio = this.sorteioService.getSorteioAberto();
    	if(sorteio == null) {
    		return mav;
    	}
    	
    	if(sorteio.getDataHora().isBefore(LocalDate.now())) {
    		this.sorteioService.realizarSorteio(null, TipoSorteio.ALEATORIO, usuario.getId());
    		sorteio = null;
    	}
    	
    	mav.addObject("sorteio", sorteio);
    	   	
    	if(sorteio != null) {
    		mav.addObject("dataFormatada", sorteio.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
    	}
    	
    	return mav;
    }
    
    @RequestMapping("/sorteio/controlador")
    public ModelAndView getTelaSorteioControlador(ModelAndView mav, @ModelAttribute("usuario") Usuario usuario, @ModelAttribute("dezenasDTO") DezenasDTO dezenasDTO, HttpSession session) {
    	
    	mav.addObject("usuario", usuario);
    	mav.setViewName("Telas/TelaSorteioControlador");
    	Sorteio sorteio = this.sorteioService.getSorteioAberto();
    	if(sorteio == null) {
    		mav.addObject("novoSorteio", new Sorteio());
    		return mav;
    	}
    	
    	if(sorteio.getDataHora().isBefore(LocalDate.now())) {
    		this.sorteioService.realizarSorteio(null, TipoSorteio.ALEATORIO, usuario.getId());
    		sorteio = null;
    	}
    	
    	mav.addObject("sorteio", sorteio);
    	mav.addObject("data", LocalDate.now());
    	
    	if(sorteio != null) {
    		mav.addObject("dataFormatada", sorteio.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
    		mav.addObject("quantApostas", this.apostaRepository.getQuantidadeApostasSorteio(sorteio.getId()));
    	}
    	
    	String dataProxSorteio = this.sorteioService.getDataProxSorteio();
    	mav.addObject("dataProxSorteio", dataProxSorteio);
    	  	
    	return mav;
    }
    
}
