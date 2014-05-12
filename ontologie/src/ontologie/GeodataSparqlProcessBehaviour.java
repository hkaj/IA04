package ontologie;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class GeodataSparqlProcessBehaviour extends OneShotBehaviour {

	
	
	public GeodataSparqlProcessBehaviour(Agent agent, ACLMessage message) {
		super(agent);
		this.m_message = message;
	}

	@Override
	public void action() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<AID> receivers = new ArrayList<>();
		receivers.add(m_message.getSender());
		try {
			String request = mapper.readValue(m_message.getContent(), JsonNode.class).path("request").asText();
			String results = runExecQuery(request);
//			System.out.println("Results: " + results);
			myAgent.addBehaviour(new KBSendResultBehaviour(myAgent, results, receivers));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String runExecQuery(String request) {
		Query query = QueryFactory.create(request);
		
		//System.setProperty("http.proxyHost","proxyweb.utc.fr");
		//System.setProperty("http.proxyPort","3128");
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService( "http://linkedgeodata.org/sparql", query );
		ResultSet r = qexec.execSelect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outStream, r);
		qexec.close();
		return new String(outStream.toByteArray());
	}
	
	
	//Members
	private ACLMessage m_message;

}
