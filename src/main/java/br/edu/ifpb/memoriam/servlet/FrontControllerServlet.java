package br.edu.ifpb.memoriam.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpb.memoriam.entity.Contato;
import br.edu.ifpb.memoriam.entity.Operadora;
import br.edu.ifpb.memoriam.facade.ContatoController;
import br.edu.ifpb.memoriam.facade.OperadoraController;
import br.edu.ifpb.memoriam.facade.Resultado;

/**
 * Servlet implementation class FrontControllerServlet
 */
@WebServlet("/controller.do")
public class FrontControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	ContatoController contatoCtrl = new ContatoController();
    	OperadoraController operadoraCtrl = new OperadoraController();
		String proxPagina = null;
		
		this.getServletContext().removeAttribute("msgs");
		
		String operacao = request.getParameter("op");
		if(operacao == null) {
			this.getServletContext().setAttribute("msgs", "Operação (op) não especificada na requisição!");
			response.sendRedirect(request.getHeader("Referer"));
			return;
		}
		
		int id;
		
		switch (operacao) {
		case "conctt":
			List<Contato> contatos = contatoCtrl.consultar();
			request.setAttribute("contatos", contatos);
			proxPagina = "contato/consulta.jsp";
			break;
			
		case "edtctt":
			id = Integer.parseInt(request.getParameter("id"));
			Contato contato = contatoCtrl.buscar(id);
			request.setAttribute("contato", contato);
			proxPagina = "contato/edita.jsp";
//			resultado = contatoCtrl.cadastrar(request.getParameterMap());
//			if (!resultado.isErro()){
//				proxPagina = paginaSucesso;
//				request.setAttribute("msgs", resultado.getMensagensSucesso());
//			} else {
//				request.setAttribute("contato", (Contato) resultado.getEntitade());
//				request.setAttribute("msgs", resultado.getMensagensErro());
//				proxPagina = paginaErro;
//			}
			break;
			
		case "conop":
			List<Operadora> operadoras = operadoraCtrl.consultar();
			request.setAttribute("operadoras", operadoras);
			proxPagina = "operadora/consulta.jsp";
			break;
			
		case "edtop":
			id = Integer.parseInt(request.getParameter("id"));
			Operadora operadora = operadoraCtrl.buscar(id);
			request.setAttribute("operadora", operadora);
			proxPagina = "operadora/edita.jsp";
			break;
		}
		
		
		RequestDispatcher dispatcher  =request.getRequestDispatcher(proxPagina);
		dispatcher.forward(request, response);
	}

    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		this.getServletContext().removeAttribute("msgs");
		
		String operacao = request.getParameter("op");
		if (operacao == null){
			this.getServletContext().setAttribute("msgs", new String[] {"Operação (op) não especificada na requisição!"});
			response.sendRedirect(request.getHeader("Referer"));
			return;
		}
		
		ContatoController contatoCtrl = new ContatoController();
		OperadoraController operadoraCtrl = new OperadoraController();
		
		Resultado resultado = null;
		String paginaSucesso = "controller.do?op=conctt";
		String paginaSucessoOp = "controller.do?op=conop";
		String paginaErro	 = "contato/cadastro.jsp";
		String proxPagina 	 = null;
		
		int prefixo;
		
		//String paginaEdita = "controller.do?op=edtctt&id=";
		
		switch(operacao){
		case "cadctt":
			resultado = contatoCtrl.cadastrar(request.getParameterMap());
			if (!resultado.isErro()){
				//Contato contAtual = (Contato) resultado.getEntitade();
				proxPagina = paginaSucesso;
				request.setAttribute("msgs", resultado.getMensagensSucesso());
			} else {
				request.setAttribute("contato", (Contato) resultado.getEntitade());
				request.setAttribute("msgs", resultado.getMensagensErro());
				proxPagina = paginaErro;
			}
			break;
		
		case "edtctt":
			resultado = contatoCtrl.cadastrar(request.getParameterMap());
			if (!resultado.isErro()){
				proxPagina = paginaSucesso;
				request.setAttribute("msgs", resultado.getMensagensSucesso());
			} else {
				request.setAttribute("contato", (Contato) resultado.getEntitade());
				request.setAttribute("msgs", resultado.getMensagensErro());
				proxPagina = paginaErro;
			}
			break;
			
		case "cadop":
			prefixo = Integer.parseInt(request.getParameter("prefixo"));
			resultado = operadoraCtrl.cadastrar(request.getParameterMap(), prefixo);
			if (!resultado.isErro()){
				proxPagina = paginaSucessoOp;
				request.setAttribute("msgs", resultado.getMensagensSucesso());
			} else {
				request.setAttribute("operadora", (Operadora) resultado.getEntitade());
				request.setAttribute("msgs", resultado.getMensagensErro());
				proxPagina = paginaErro;
			}
			break;
			
		case "edtop":
			prefixo = Integer.parseInt(request.getParameter("prefixo"));
			resultado = operadoraCtrl.cadastrar(request.getParameterMap(), prefixo);
			if (!resultado.isErro()){
				proxPagina = paginaSucessoOp;
				request.setAttribute("msgs", resultado.getMensagensSucesso());
			} else {
				request.setAttribute("contato", (Operadora) resultado.getEntitade());
				request.setAttribute("msgs", resultado.getMensagensErro());
				proxPagina = paginaErro;
			}
			break;	
			
			
			default:
				request.setAttribute("erro", "Operação não especificada np servlet");
				proxPagina = "../erro/erro.jsp";
		}
		
		if(resultado.isErro()){
			RequestDispatcher dispatcher = request.getRequestDispatcher(proxPagina);
			dispatcher.forward(request, response);
		}else{
			response.sendRedirect(proxPagina);
		}
	}
		
}
