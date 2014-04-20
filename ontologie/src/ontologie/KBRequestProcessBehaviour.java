package ontologie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class KBRequestProcessBehaviour extends OneShotBehaviour {

	public KBRequestProcessBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
		m_agent = (KnowledgeBaseAgent) a;
	}

	@Override
	public void action() {
		String content = m_message.getContent();
		
		ExtendedIterator<Statement> statements = m_agent.getModel().listStatements();
		
		//convert JSON string to Map
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root, contentNode = null, dropNode = null, keepNode = null;
		try {
			root = mapper.readValue(content, JsonNode.class);
			
			contentNode = root.path("content");
			keepNode = contentNode.path("keep");
			dropNode = contentNode.path("drop");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		if (dropNode != null && keepNode != null){
			//Filtrage par exclusion d'information
			for (Iterator<String> it = dropNode.fieldNames(); it.hasNext();){
				final String key = it.next();
				
				if (dropNode.get(key).asText().equals("subject")){
					statements = statements.filterDrop(new Filter<Statement>(){
						@Override
						public boolean accept(Statement arg){
							return arg.getSubject().getURI().equals(key);
						}
					});
				} else if (dropNode.get(key).asText().equals("object")){
					statements = statements.filterDrop(new Filter<Statement>(){
						@Override
						public boolean accept(Statement arg){
							if (arg.getObject().isResource())
								return arg.getObject().asResource().getURI().equals(key);
							else
								return false;
						}
					});
				}
			}
	
		
		
		//Filtrage par inclusion d'information
			for (Iterator<String> it = keepNode.fieldNames(); it.hasNext();){
				
				final String key = it.next();
				
				if (keepNode.get(key).asText().equals("subject")){
					statements = statements.filterKeep(new Filter<Statement>(){
						@Override
						public boolean accept(Statement arg){
							return arg.getSubject().getURI().equals(key);
						}
					});
				} else if (keepNode.get(key).asText().equals("object")){
					statements = statements.filterKeep(new Filter<Statement>(){
						@Override
						public boolean accept(Statement arg){
							if (arg.getObject().isResource())
								return arg.getObject().asResource().getURI().equals(key);
							else
								return false;
						}
					});
				} else if (keepNode.get(key).asText().equals("property")){
					Property prop = m_agent.getModel().getProperty(key);
					SimpleSelector selector = new SimpleSelector(null, prop, key);
					for (Statement s : statements.toList()) {
						if (!selector.test(s)) {
							s.remove();
						}
					}
//					statements = statements.filterKeep(new Filter<Statement>(){
//						@Override
//						public boolean accept(Statement arg){
//							if (arg.getObject().isResource())
//								return arg.getProperty(arg0)
//							else
//								return false;
//						}
//					});
				}
			}
		}else
			System.out.println("Error in JSON parsing");
		
		String newContent;
		ArrayList<AID> receivers;
		
		while (statements.hasNext()){
			System.out.println(statements.next());
		}
		
		//myAgent.addBehaviour(new KBSendResultBehaviour(myAgent, newContent, receivers));
	}

	
	//Members
	private ACLMessage m_message;
	private KnowledgeBaseAgent m_agent;
}
