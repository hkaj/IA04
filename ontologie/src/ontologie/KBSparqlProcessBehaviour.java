package ontologie;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class KBSparqlProcessBehaviour extends OneShotBehaviour {

	public KBSparqlProcessBehaviour(Agent a, ACLMessage message) {
		super(a);
		m_message = message;
		m_agent = (KnowledgeBaseAgent) a;
	}

	@Override
	public void action() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String request = mapper.readValue(m_message.getContent(), JsonNode.class).path("request").asText();
			Model model = ModelFactory.createDefaultModel();
			try {
				model.read(new FileInputStream("ressources/ABox"), null, "TURTLE");
				String results = runExecQuery(request, model);
				System.out.println("Results: " + results);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String runExecQuery(String queryString, Model model) {
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet r = queryExecution.execSelect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outStream, r);
		queryExecution.close();
		return new String(outStream.toByteArray());
	}
	
	private ACLMessage m_message;
	private KnowledgeBaseAgent m_agent;
}
