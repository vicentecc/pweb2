package br.edu.ifpb.memoriam.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.edu.ifpb.memoriam.dao.OperadoraDAO;
import br.edu.ifpb.memoriam.dao.PersistenceUtil;
import br.edu.ifpb.memoriam.entity.Operadora;

public class OperadoraController {
	private Operadora operadora;
	private List<String> mensagensErro;
	
	public List<Operadora> consultar(){
		OperadoraDAO dao = new OperadoraDAO(PersistenceUtil.getCurrentEntityManager());
		List<Operadora> operadoras = dao.findAll();
		return operadoras;
	}
	
	public Operadora buscar(int id){
		OperadoraDAO dao = new OperadoraDAO(PersistenceUtil.getCurrentEntityManager());
		Operadora operadora = dao.find(id);
		return operadora;
	}
	
	public Resultado cadastrar(Map<String, String[]> parametros, int prefix) {
		Resultado resultado= new Resultado();
		
		if(isParametrosValidos(parametros)) {
				OperadoraDAO dao= new OperadoraDAO(PersistenceUtil.getCurrentEntityManager());
				dao.beginTransaction();
				if(this.operadora.getId() == null) {
					this.operadora.setPrefixo(prefix);
					dao.insert(this.operadora);
				} else{
					this.operadora.setPrefixo(prefix);
					dao.update(this.operadora);
				}
				dao.commit();
				
				resultado.setErro(false);
				resultado.setMensagensErro(Collections.singletonList("operadora criado com sucesso"));
			} else{
				resultado.setEntitade(this.operadora);
				resultado.setErro(true);
				resultado.setMensagensErro(this.mensagensErro);
				}
			return resultado;
	}
	
	private boolean isParametrosValidos(Map<String, String[]> parametros) {
		// nomes dos parâmetros vêm dos atributos 'name' das tags HTML do formulário
		String[] id= parametros.get("id");
		String[] nome= parametros.get("nome");
		
		this.operadora= new Operadora();
		this.mensagensErro= new ArrayList<String>();
		
		if(id!= null && id.length>0 && !id[0].isEmpty()) {
			operadora.setId(Integer.parseInt(id[0]));
		}
		if(nome== null|| nome.length== 0 || nome[0].isEmpty()) {
			this.mensagensErro.add("Nome é campo obrigatório!");
		} else{
			operadora.setNome(nome[0]);
		}
	
		return this.mensagensErro.isEmpty();
	}
	
}