package ontologie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SparqlMain {

	public SparqlMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String[] queries = {"ressources/q11.sparql", "ressources/q12.sparql", "ressources/q13.sparql"};
		for (String q: queries) {
			System.out.println("Executing the query in '" + q + "'...");
			test(q);
		}
		
		String[] queriesDistant = {"ressources/q21.sparql"};
		for (String q: queriesDistant) {
			System.out.println("Executing the query in '" + q + "'...");
			testDistant(q);
		}

	}
	
	public static void test(String queryFile) {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(new FileInputStream("ressources/foaf.n3"), null, "TURTLE");
			runExecQuery(queryFile, model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void runExecQuery(String qfilename, Model model) {
		Query query = QueryFactory.read(qfilename);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet r = queryExecution.execSelect();
		ResultSetFormatter.out(System.out, r);
		queryExecution.close();
	}
	
	public static void testDistant(String queryFile) {
		Query query = QueryFactory.read(queryFile);
		
		//System.setProperty("http.proxyHost","proxyweb.utc.fr");
		//System.setProperty("http.proxyPort","3128");
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService( "http://linkedgeodata.org/sparql", query );
		ResultSet concepts = qexec.execSelect();
		ResultSetFormatter.out(System.out, concepts);
		qexec.close();
	}
}
