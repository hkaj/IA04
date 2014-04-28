package ontologie;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
		JsonNode root;
		final JsonNode contentNode;
		try {
			root = mapper.readValue(content, JsonNode.class);
			contentNode = root.path("content");
		
			//Filtrage par inclusion d'information
				for (Iterator<String> it = contentNode.fieldNames(); it.hasNext();){
					
					final String key = it.next();					
					
					//Séparation des deux cas (on connait ID / on connait prop-value)
					if (key.equals("id")){
							statements = statements.filterKeep(new Filter<Statement>(){
								@Override
								public boolean accept(Statement arg){
									return arg.getSubject().getURI().equals(contentNode.get(key).asText());
								}
							});
					} else if (key.equals("prop-name")){
						Property prop = m_agent.getModel().getProperty(contentNode.get("prop-value").asText());
						String value = contentNode.get("prop-value").asText();
						SimpleSelector selector = new SimpleSelector(null, prop, value);
						List<Statement> statementList = statements.toList(); 
						for (Statement s : statementList) {
							if (!selector.test(s)) {
								s.remove();
							}
						}
					}
					
					
				}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Format the result
		ObjectMapper writerMapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		
		HashMap<String, Object> newContent = new HashMap<>();
		ArrayList<String> statementsToSend = new ArrayList<>();
		
		while (statements.hasNext()){
			String assertion = statements.next().toString();
			statementsToSend.add(assertion);
		}
		
		newContent.put("assert",statementsToSend);
		
		HashMap<String, Object> jsonContent = new HashMap<>();
		jsonContent.put("content", newContent);
		
		try {
			writerMapper.writeValue(sw, jsonContent);
			String messageContent = sw.toString();
			
			   ArrayList<AID> receivers = new ArrayList<>();
			   receivers.add(m_message.getSender());		
				
				myAgent.addBehaviour(new KBSendResultBehaviour(myAgent, messageContent, receivers));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	//Members
	private ACLMessage m_message;
	private KnowledgeBaseAgent m_agent;
}
