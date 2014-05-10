package ontologie;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class KBRequestProcessBehaviour extends OneShotBehaviour {

	public KBRequestProcessBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
		m_agent = (KnowledgeBaseAgent) a;
	}

	@Override
	public void action() {
		String content = m_message.getContent();
		List<Statement> resultList = new ArrayList<Statement>();
		ExtendedIterator<Statement> statements = m_agent.getModel().listStatements();
		List<String> fields = new ArrayList<String>();
		//convert JSON string to Map
		ObjectMapper mapper = new ObjectMapper();
		final JsonNode contentNode;
		try {
			contentNode = mapper.readValue(content, JsonNode.class).path("content");
			for (Iterator<String> it = contentNode.fieldNames(); it.hasNext();){
				fields.add(it.next());
			}
			// We have the subject and the property, let's seek the objects 
			if (fields.contains("subject") && fields.contains("property")) {
				String propName = contentNode.get("property").asText();
				Property prop = m_agent.getModel().getProperty(propName);
				String subjectStr = contentNode.get("subject").asText();
				Resource subject = m_agent.getModel().getResource(subjectStr);
				SimpleSelector selector = new SimpleSelector(subject, prop, (Resource)null);
				List<Statement> statementList = statements.toList();
				for (Statement s : statementList) {
					if (selector.test(s)) {
						resultList.add(s);
					}
				}
			// We have the property and the object, let's seek the subjects
			} else if (fields.contains("property") && fields.contains("object")) {
				String propName = contentNode.get("property").asText();
				Property prop = m_agent.getModel().getProperty(propName);
				String objectStr = contentNode.get("object").asText();
				SimpleSelector selector;
				if (objectStr.contains("http://")) {
					Resource object = m_agent.getModel().getResource(objectStr);
					selector = new SimpleSelector(null, prop, object);
				} else {
					selector = new SimpleSelector(null, prop, objectStr);
				}
				List<Statement> statementList = statements.toList();
				for (Statement s : statementList) {
					if (selector.test(s)) {
						resultList.add(s);
					}
				}
			// We have a resource, let's seek all the relation for which the subject is this resource 
			} else if (fields.size() == 1 && fields.contains("subject")) {
				statements = statements.filterKeep(new Filter<Statement>(){
					@Override
					public boolean accept(Statement arg){
						return arg.getSubject().getURI().equals(contentNode.get("subject").asText());
					}
				});
				
				for (Iterator<Statement> s = statements; statements.hasNext();){
					resultList.add(s.next());
				}
			} else {
				System.out.println("La requete n'a pas été comprise, réessayez.");
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Format the result
		ObjectMapper writerMapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		Iterator<Statement> resultStatements = resultList.iterator();
		HashMap<String, Object> newContent = new HashMap<>();
		ArrayList<String> statementsToSend = new ArrayList<>();
		String assertion = "";
		while (resultStatements.hasNext()){
			if (fields.size() == 1) {
				assertion = resultStatements.next().toString();
			} else if (fields.contains("object")) {
				assertion = resultStatements.next().getSubject().toString();
			} else {
				assertion = resultStatements.next().getObject().toString();
			}
			statementsToSend.add(assertion);
		}
		
		newContent.put("assert", statementsToSend);
		
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
